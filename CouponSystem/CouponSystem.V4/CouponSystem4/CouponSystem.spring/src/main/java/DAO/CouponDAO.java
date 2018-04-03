package DAO;

import java.util.Collection;

import CouponSystemBeans.CouponType;
import CouponSystemException.GeneralCouponSystemException;
import CouponSystemBeans.Coupon;
/**
 * This CouponDAO class represent Interface class that the specific CouponDBDAO class must implement all the abstract method in this class.
 * @author michael
 *
 */
public interface CouponDAO {
	
	void createCoupon(Coupon coupon) throws GeneralCouponSystemException;
	void removeCoupon(Coupon coupon) throws GeneralCouponSystemException;
	void updateCoupon(Coupon coupon) throws GeneralCouponSystemException;
	Coupon getCoupon(long id) throws GeneralCouponSystemException;
	Collection<Coupon> getAllCoupon() throws GeneralCouponSystemException;
	Collection<Coupon> getCouponByType(CouponType type) throws GeneralCouponSystemException;
	

}
