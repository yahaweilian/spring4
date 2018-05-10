package spittr.util;

public class ErrorAuto {

	private int code;
	private String message;
	
	public ErrorAuto(int code, String message) {
		this.code = code;
		this.message = message;
	}
	public int getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}
	
	
}
