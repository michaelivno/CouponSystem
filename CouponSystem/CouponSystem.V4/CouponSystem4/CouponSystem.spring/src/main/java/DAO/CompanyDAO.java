package DAO;

import java.util.Collection;

import CouponSystemBeans.Company;
import CouponSystemBeans.Coupon;
import CouponSystemException.GeneralCouponSystemException;
/**
 * This CompanyDAO class represent Interface class that the specific CompanyDBDAO class must implement all the abstract method in this class.
 * @author michael
 *
 */
public interface CompanyDAO {
	
	  void createCompany(Company com) throws GeneralCouponSystemException;
	  void removeCompany(Company com) throws GeneralCouponSystemException;
	  void updateCompany(Company com) throws GeneralCouponSystemException;
	  Company getCompany(long id) throws GeneralCouponSystemException;
	  Collection<Company> getAllCompanies() throws GeneralCouponSystemException;
	  Collection<Coupon> getCoupons() throws GeneralCouponSystemException;
	  Boolean login(String compName,String password) throws GeneralCouponSystemException;
	  
	 
		
	

}
