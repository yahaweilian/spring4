package spittr.controller;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import spittr.data.SpittleRepository;
import spittr.entity.Spitter;
import spittr.entity.Spittle;
import spittr.exception.DuplicateSpittleException;
import spittr.exception.NotModifiedException;
import spittr.exception.SpitterException;
import spittr.exception.SpittleNotFoundException;

//@RestController//为每个方法都添加了@ResponseBody
@Controller
@RequestMapping("/spittles")
public class SpittleController {

	private static final String MAX_LONG_AS_STRING = Long.toString(Long.MAX_VALUE);
	private SpittleRepository spittleRepository;

	@Autowired
	public SpittleController(SpittleRepository spittleRepository) {
		this.spittleRepository = spittleRepository;
	}

	/*
	 * @RequestMapping(method=RequestMethod.GET) public String spittles(Model
	 * model){ model.addAttribute(spittleRepository.findSpittles(Long.MAX_VALUE,
	 * 20)); return "spittles"; }
	 */

	/**
	 * @ResponseBody注解会告知Spring， 我们要将返回的对象作为资源发送给客户端， 并将其转换为客户端可接受的表述形式。 更具体地 讲，
	 * DispatcherServlet将会考虑到请求中Accept头部信息， 并查找能够为客户端提供所需表述形式的消息转换器
	 * 
	 * @param max
	 * @param count
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<Spittle> spittles(@RequestParam(value = "max", defaultValue = "1000000000") long max,
			@RequestParam(value = "count", defaultValue = "20") int count) {
		return spittleRepository.findSpittles(max, count);// 返回页面“spittles"与方法名相同，如上
	}

	@RequestMapping(value = "/{spittleId}", method = RequestMethod.GET)
	public @ResponseBody Spittle showSpittle(@PathVariable("spittleId") long spittleId) {
		Spittle spittle = spittleRepository.findOne(spittleId);
		if (spittle == null) {
			throw new SpittleNotFoundException(spittleId);
		}
		return spittle;
	}

	/**
	 * consumes属性的工作方式类似 于produces， 不过它会关注请求的Content-Type头部信息。
	 * 它会告诉Spring这个方法只会处理对“/spittles”的POST请求， 并且要求请
	 * 求的Content-Type头部信息为“application/json”。 如果无法满足这些条件的话， 会由其他方法（如果存在合适的方法的话）
	 * 来处理请 求。
	 * 
	 * @param spittle
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<Spittle> saveSpittle(@RequestBody Spittle spittlePram, UriComponentsBuilder ucb) {
		Spittle spittle = spittleRepository.save(spittlePram);
		HttpHeaders headers = new HttpHeaders();
		//计算Location URI
		URI locationUri = ucb.path("/spittles/")
				.path(String.valueOf(spittle.getId()))
				.build().toUri();
		headers.setLocation(locationUri);
		//在响应中设置头部信息
		ResponseEntity<Spittle> responseEntity = 
				new ResponseEntity<>(spittle, headers, HttpStatus.CREATED);
		return responseEntity;
	}

	/*public String savaSpittle(Spittle spittle, Model model) throws DuplicateSpittleException {
		spittleRepository.saveSpittle(spittle);
		model.addAttribute("max", 100000);
		model.addAttribute("count", 20);
		return "redirect:/spittles";
	}*/
	
	/**
	 * REST 客户端
	 * 获取某人的Facebook基本信息
	 * @param id
	 * @return
	 */
	public Profile fetchFacebookProfile(String id){
		Map<String,String> urlVaribles = new HashMap<>();
		urlVaribles.put("id", id);
		RestTemplate rest = new RestTemplate();
		return rest.getForObject("http://graph.facebook.com/{spittle}", //GET资源
				Profile.class,urlVaribles);
	}
	
	/**
	 * getForEntity()会在ResponseEntity中返回相同的对象，
	 *  而且ResponseEntity还带有关于响应的额外信息， 如HTTP状态码和响应头。
	 * @param id
	 * @return
	 * @throws NotModifiedException 
	 */
	public Spittle fetchSpittle(long id) throws NotModifiedException{
		RestTemplate rest = new RestTemplate();
		ResponseEntity<Spittle> response = rest.getForEntity(
				"http://localhost:8080/spring4/spittles/{id}", 
				Spittle.class,id);
		if(response.getStatusCode() == HttpStatus.NOT_MODIFIED){
			throw new NotModifiedException();
		}
		return response.getBody();
	}
	
	/**
	 * REST PUT
	 * @param spittle
	 */
	public void updateSpittle(Spittle spittle) throws SpitterException{
		RestTemplate rest = new RestTemplate();
//		String url = "http://localhost:8080/spring4/spittles/" + spittle.getId();
//		rest.put(URI.create(url), spittle);
		rest.put("http://localhost:8080/spring4/spittles/{id}",
				spittle,spittle.getId());
	}
	
	public void deleteSpittle(long id){//DELETE
		RestTemplate rest = new RestTemplate();
		rest.delete("http://localhost:8080/spring4/spittles/{id}",id);
	}
	
	public Spitter postSpitterForEntity(Spitter spitter){//POST资源
		RestTemplate rest = new RestTemplate();
		ResponseEntity<Spitter> response = rest.postForEntity(
				"http://localhost:8080/spring4/spitters",
				spitter, Spitter.class);
		URI url = response.getHeaders().getLocation();
		Spitter s = response.getBody();
		return s;
	}
}
