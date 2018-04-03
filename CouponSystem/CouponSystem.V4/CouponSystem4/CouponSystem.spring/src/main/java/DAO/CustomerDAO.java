package DAO;

import java.util.Collection;

import CouponSystemBeans.Coupon;
import CouponSystemBeans.Customer;
import CouponSystemException.GeneralCouponSystemException;
/**
 * This CustomerDAO class represent Interface class that the specific CustomerDBDAO class must implement all the abstract method in this class.
 * @author michael
 *
 */
public interface CustomerDAO {
	
	void createCustomer(Customer customer) throws GeneralCouponSystemException;
	void removeCustomer(Customer customer) throws GeneralCouponSystemException;
	void updateCustomer(Customer customer) throws GeneralCouponSystemException;
	Customer getCustomer(long id) throws GeneralCouponSystemException;
	Collection<Customer> getAllCustomer() throws GeneralCouponSystemException;
	Collection<Coupon> getCoupons() throws GeneralCouponSystemException;
	boolean login (String custName,String password) throws GeneralCouponSystemException;
	
	
	
	
	

}
