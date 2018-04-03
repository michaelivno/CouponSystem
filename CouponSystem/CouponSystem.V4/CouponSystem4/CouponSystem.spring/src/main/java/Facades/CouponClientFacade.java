package Facades;

import CouponSystemException.GeneralCouponSystemException;

/**
 * This class represent ENUM class Of Client Types that could use the
 * couponSystem.
 * 
 * @author michael
 *
 */
public interface CouponClientFacade {

	public enum ClientType {
		ADMIN, COMPANY, CUSTOMER,

	}

	/**
	 * This login method need to be implements for all the facades to get access to
	 * the Data base end the system at all if the parameters is right you will get
	 * access.
	 * 
	 * @param name
	 * @param password
	 * @param clientType
	 * @return CouponClientFacade
	 * @throws GeneralCouponSystemException
	 */
	CouponClientFacade login(String name, String password, ClientType clientType) throws GeneralCouponSystemException;

}
