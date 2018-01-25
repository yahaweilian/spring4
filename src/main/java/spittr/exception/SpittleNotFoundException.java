package spittr.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND,reason="Spittle Not Found")//将异常映射为HTTP状态404
public class SpittleNotFoundException extends RuntimeException {

}
