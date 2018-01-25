package spittr.web;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import spittr.exception.DuplicateSpittleException;
import spittr.exception.ImageUploadException;

/**
 * 如果任意的控制器方法抛出了DuplicateSpittleException， 不管这个方法位于哪个控制器中， 都会调用这
 * 个duplicateSpittleHandler()方法来处理异常,ImageUploadException...同理
 */
@ControllerAdvice
public class AppWideExceptionHandler {

	@ExceptionHandler(DuplicateSpittleException.class)
	public String duplicateSpittleHandler(){
		return "error/duplicate";
	}
	
	@ExceptionHandler(ImageUploadException.class)
	public String imageUploadHandler(){
		return "error/imageUpload";
	}
	
	
}
