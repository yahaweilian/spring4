package spittr.web;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/*import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.Permission;*/

import spittr.data.SpitterRepository;
import spittr.entity.Spitter;
import spittr.exception.ImageUploadException;

@Controller
@RequestMapping("/spitter")
public class SpitterController {

	private SpitterRepository spitterRepository;
	
	public SpitterController() {
	}

	@Autowired
	public SpitterController(SpitterRepository spitterRepository) {
		this.spitterRepository = spitterRepository;
	}

	@RequestMapping(value="register",method=RequestMethod.GET)
	public String showRegistrationForm(Model model){
		model.addAttribute(new Spitter());//对应jsp表单 属性commandName 需要的key为“spitter”的模型对象
		return "registerForm";
	}
	
	@RequestMapping(value="/register",method=RequestMethod.POST)
	public String processRegistration(//在multipart中， 每个输入域都会对应一个part
			@RequestPart("profilePicture") MultipartFile profilePicture,//或者byte[] profilePicture
			@Valid Spitter spitter,
			Errors errors,
			RedirectAttributes model,
			HttpSession session) throws ImageUploadException  {
		if(errors.hasErrors()){//表单验证错误,返回注册页面
			return "registerForm";
		}
		//保存图片到文件系统
		try {
			String uploadDir = session.getServletContext().getRealPath("/resources/uploads");
			File dir = new File(uploadDir);
	        if (!dir.exists() && !dir.isDirectory()) {//创建保存文件地址
	            dir.mkdirs();
	        }
	        if(profilePicture.getSize() != 0){ // 文件不为空
	        	File file = new File(dir + File.separator + profilePicture.getOriginalFilename());
	        	profilePicture.transferTo(file);
	        }
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
			throw new ImageUploadException("Unable to save image",e);
		} 
//		savaImage(profilePicture);//保存图片到Amazon s3中
		spitterRepository.save(spitter);
		
//		return "redirect:/spitter/" + spitter.getUsername();//重定向，后面连接字符串，不安全
		model.addAttribute("username",spitter.getUsername());
		//Spring提供了将数据发送为flash属性（flash attribute） 的功能。 按照定义，
		//flash属性会一直携带这些数据直到下一次请求， 然后才会消失
		model.addFlashAttribute("spitter", spitter);//RedirectAttributes的addFlashAttribute方法,重定向model，可以传递对象
		return "redirect:/spitter/{username}";
	}
	
	@RequestMapping(value="/{username}",method=RequestMethod.GET)
	public String showSpitterProfile(
			@PathVariable String username, Model model){
		if(!model.containsAttribute("spitter")){
			model.addAttribute(spitterRepository.findByUsername(username));
		}
		return "profile";
	}
	
	/**
	 * 上传图片到Amazon s3
	 */
	/*private void savaImage(MultipartFile image) throws ImageUploadException{
		try {
			// 创建凭证
			AWSCredentials awsCredentials = new BasicAWSCredentials("accessKey", "secretKey");//TODO
			// 创建s3 Client
			AmazonS3 s3client = new AmazonS3Client(awsCredentials);
			
			// 创建 Bucket
//			String bucketName = "javatutorial-net-example-bucket";
			String bucketName = "spittrImages";
			Bucket bucket = s3client.createBucket(bucketName);
			
			// 设置图片数据
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentLength(image.getSize());
			metadata.setContentType(image.getContentType());
			
			//设置权限
			AccessControlList acl = new AccessControlList();
			acl.setOwner(bucket.getOwner());
			acl.grantPermission(GroupGrantee.AllUsers, Permission.Read);
			
			// 保存图片
			String key = image.getOriginalFilename();//文件名
			s3client.putObject(bucketName, key, image.getInputStream(), metadata);
		} catch (Exception e) {
			throw new ImageUploadException("Unable to save image",e);
		}
		
	}*/
}
