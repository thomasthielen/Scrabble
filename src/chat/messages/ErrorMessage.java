package chat.messages;

/**
 * @author lsteltma
 *
 */

public class ErrorMessage extends Message {

	/**
	 * Default serialization UID
	 */
	private static final long serialVersionUID = 1L;
	private String errorType;
	/**
	 * @param from
	 * @param errorType
	 */
	public ErrorMessage(String name, String errorType) {
		super(MessageType.ERROR, name);
		this.errorType = errorType;
	}
	
	public String getErrorType() {
		return this.errorType;
	}

}
