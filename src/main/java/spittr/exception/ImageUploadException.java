package spittr.exception;


public class ImageUploadException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String message ;
	Exception e;
	public ImageUploadException(String message, Exception e) {
		this.message = message;
		this.e = e;
	}

}
