package spittr.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import spittr.exception.DuplicateSpittleException;
import spittr.exception.ImageUploadException;
import spittr.exception.SpittleNotFoundException;
import spittr.util.ErrorAuto;

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
	
	@ExceptionHandler(SpittleNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public @ResponseBody ErrorAuto spittleNotFound(SpittleNotFoundException e){
		long SpittleId = e.getSpittleId();
		return new ErrorAuto(4,"Spittle [" +SpittleId + "] not found");
	}
	
}
