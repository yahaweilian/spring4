package spittr.exception;

//@ResponseStatus(value=HttpStatus.NOT_FOUND,reason="Spittle Not Found")//将异常映射为HTTP状态404
public class SpittleNotFoundException extends RuntimeException {

	private long spittleId;

	public SpittleNotFoundException() {
		super();
	}

	public SpittleNotFoundException(long spittleId) {
		super();
		this.spittleId = spittleId;
	}

	public long getSpittleId() {
		return spittleId;
	}

}
