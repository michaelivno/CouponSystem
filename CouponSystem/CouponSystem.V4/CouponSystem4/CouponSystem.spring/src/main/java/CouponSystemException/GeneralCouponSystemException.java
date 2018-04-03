package CouponSystemException;
/**
 * This Class represent the EXEPTION class of the couponSystem Program.
 * @author michael
 * 
 */
public class GeneralCouponSystemException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String message = "The Coupon System Had An Error.";

	public GeneralCouponSystemException() {
		super(message);
	}

	/**
	 * New massage for each method exception.
	 * @param message
	 */
	public GeneralCouponSystemException(String message) {
		super(message);
	}
	
	
}
