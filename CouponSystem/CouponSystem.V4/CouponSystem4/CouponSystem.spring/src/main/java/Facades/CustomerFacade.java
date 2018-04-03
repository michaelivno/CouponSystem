package Facades;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import CouponSystemBeans.Coupon;
import CouponSystemBeans.CouponType;
import CouponSystemBeans.Customer;
import CouponSystemException.GeneralCouponSystemException;
import DBDAO.CouponDBDAO;
import DBDAO.CustomerCouponDBDAO;
import DBDAO.CustomerDBDAO;

/**
 * This class represent The CUSTOMER FACADE the actions(methods) that can use
 * only customer of couponSystem.
 * 
 * @author michael
 *
 */
public class CustomerFacade implements CouponClientFacade {

	private CouponDBDAO couponDBDAO;
	private CustomerCouponDBDAO customerCouponDBDAO;
	private Customer usingFacadeCustomer;
	private CustomerDBDAO customerDBDAO;

	/**
	 * this method return the company that use the facade.
	 * 
	 * @return Customer
	 */
	public Customer getUsingFacadeCustomer() {
		return usingFacadeCustomer;
	}

	/**
	 * Facade constructor
	 * 
	 * @throws GeneralCouponSystemException
	 */
	public CustomerFacade(CouponDBDAO couponDBDAO, CustomerCouponDBDAO customerCouponDBDAO, CustomerDBDAO customerDBDAO) throws GeneralCouponSystemException {
		this.couponDBDAO = couponDBDAO;
		this.customerCouponDBDAO = customerCouponDBDAO;
		this.customerDBDAO = customerDBDAO;

	}

	/**
	 * This method check the parameters you sent and if the parameters legal you
	 * will you will purchase the object you ask.
	 * 
	 * @param couponId
	 *            - coupon id.
	 * @throws GeneralCouponSystemException
	 */
	public void purchaseCouponById(long couponId) throws GeneralCouponSystemException {
		try {
			Coupon coup = couponDBDAO.getCoupon(couponId);
			if (coup != null && coup.getId() > 0 && coup.getAmount() > 0) {
				Collection<Coupon> usingFacadeCoupons = customerCouponDBDAO
						.getAllCouponsByCustomer(usingFacadeCustomer.getId());
				if (usingFacadeCoupons.isEmpty()) {
					coup.setAmount(coup.getAmount() - 1);
					couponDBDAO.updateCoupon(coup);
					customerCouponDBDAO.createCustomerCoupon(coup, usingFacadeCustomer.getId());
					return;
				} else {
					Iterator<Coupon> coupIter = usingFacadeCoupons.iterator();
					while (coupIter.hasNext()) {
						Coupon copyIter = coupIter.next();
						if (copyIter.getId() == coup.getId()) {
							GeneralCouponSystemException message = new GeneralCouponSystemException(
									"Purchase Couldn't Execute You Already Purchase This Coupon, Please Try Again Later");
							throw message;
						}
					}
					coup.setAmount(coup.getAmount() - 1);
					couponDBDAO.updateCoupon(coup);
					customerCouponDBDAO.createCustomerCoupon(coup, usingFacadeCustomer.getId());
				}
			} else {
				GeneralCouponSystemException message = new GeneralCouponSystemException(
						"Purchase Couldn't Execute Coupon With This ID Doesn't Exist / Coupon Amount Is 0, Please Try Again Later");
				throw message;
			}
		} catch (Exception e) {
			GeneralCouponSystemException message = new GeneralCouponSystemException(
					"Purchase Couldn't Execute, Please Try Again Later");
			throw message;
		}
	}

	
	/**
	 * This "GET" method checked that the object you ask for exist it the data base
	 * and if it is, the method return collection of objects from same "family" .
	 * 
	 * @return collection.
	 * @throws GeneralCouponSystemException
	 */
	public Collection<Coupon> getAllPurchaseCoupons() throws GeneralCouponSystemException {
		Collection<Coupon> allCoup = customerCouponDBDAO.getAllCouponsByCustomer(usingFacadeCustomer.getId());
		if (allCoup.isEmpty()) {
			GeneralCouponSystemException message = new GeneralCouponSystemException(
					"Couldn't Get Data From DataBase, Data Not Exist Or Invaild Empty");
			throw message;
		}
		return allCoup;
	}

	
	public Collection<Coupon> getAllCoupons() throws GeneralCouponSystemException{
		Collection<Coupon> allCoup = couponDBDAO.getAllCoupon();
		return allCoup;
	}
	
	
	/**
	 * This "GET" method checked that the object you ask for exist it the data base
	 * and if it is, the method return collection of objects from same "family" .
	 * 
	 * @param type
	 *            - coupon type.
	 * @return collection.
	 * @throws GeneralCouponSystemException
	 */
	public Collection<Coupon> getAllPurchaseCouponsByType(CouponType type) throws GeneralCouponSystemException {
		Collection<Coupon> coupByType = new HashSet<>();
		try {
			Collection<Coupon> allCoup = this.getAllPurchaseCoupons();
			if (allCoup.isEmpty()|| allCoup==null) {
				GeneralCouponSystemException message = new GeneralCouponSystemException(
						"Couldn't Get Data From DataBase, Data Not Exist Or Invaild Empty");
				throw message;
			}
			Iterator<Coupon> coupIter = allCoup.iterator();
			while (coupIter.hasNext()) {
				Coupon coup = coupIter.next();
				if (coup.getType().equals(type)) {
					coupByType.add(coup);
				}
			}
			if (coupByType.isEmpty()) {
				GeneralCouponSystemException message = new GeneralCouponSystemException(
						"Couldn't Get Data From DataBase, Data Not Exist Or Invaild Type");
				throw message;
			}
		} catch (Exception e) {
			GeneralCouponSystemException message = new GeneralCouponSystemException(
					"Couldn't Get Data From DataBase, Data Not Exist Or Invaild ");
			throw message;
		}
		return coupByType;
	}

	/**
	 * This "GET" method checked that the object you ask for exist it the data base
	 * and if it is, the method return collection of objects from same "family" .
	 * 
	 * @param price
	 *            - coupon price.
	 * @return collection.
	 * @throws GeneralCouponSystemException
	 */
	public Collection<Coupon> getAllPurchaseCouponsByPrice(double price) throws GeneralCouponSystemException {
		Collection<Coupon> CoupByPrice = new HashSet<>();
		try {
			Collection<Coupon> allCoup = getAllPurchaseCoupons();
			if (allCoup.isEmpty()) {
				GeneralCouponSystemException message = new GeneralCouponSystemException(
						"Couldn't Get Data From DataBase, Data Not Exist Or Invaild Empty");
				throw message;
			}
			Iterator<Coupon> coupIter = allCoup.iterator();
			Coupon coup = new Coupon();
			while (coupIter.hasNext()) {
				coup = coupIter.next();
				if (coup.getPrice() <= price) {
					CoupByPrice.add(coup);
				}
			}
			if (CoupByPrice.isEmpty()) {
				GeneralCouponSystemException message = new GeneralCouponSystemException(
						"Couldn't Get Data From DataBase, Data Not Exist Or Invaild Type");
				throw message;
			}
		} catch (Exception e) {
			GeneralCouponSystemException message = new GeneralCouponSystemException(
					"Couldn't Get Data From DataBase, Data Not Exist Or Invaild ");
			throw message;
		}
		return CoupByPrice;
	}

	/**
	 * 
	 */
	@Override
	public CouponClientFacade login(String name, String password, ClientType clientType)
			throws GeneralCouponSystemException {
		if (clientType.equals(ClientType.CUSTOMER)) {
			if (customerDBDAO != null && customerDBDAO.login(name, password)) {
				this.usingFacadeCustomer = customerDBDAO.getCustomerByName(name);
				return this;
			} else {
				GeneralCouponSystemException message = new GeneralCouponSystemException(
						"Wrong Password or Username. Try again later!");
				throw message;
			}
		}
		return null;
	}
}
