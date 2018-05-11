package dreckssau.exceptions;

public class DreckssauNullPointerException extends DreckssauException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DreckssauNullPointerException(String e){
		//build string
		super("nullPointer bei variable: "+e +".");
	}

}
