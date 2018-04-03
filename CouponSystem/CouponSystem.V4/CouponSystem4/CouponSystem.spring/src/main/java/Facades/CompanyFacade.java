package Facades;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

import CouponSystemBeans.Company;
import CouponSystemBeans.Coupon;
import CouponSystemBeans.CouponType;
import CouponSystemException.GeneralCouponSystemException;
import DBDAO.CompanyCouponDBDAO;
import DBDAO.CompanyDBDAO;
import DBDAO.CouponDBDAO;
import DBDAO.CustomerCouponDBDAO;

/**
 * This class represent The COMPANY FACADE, the actions(methods) that can use
 * only Company of couponSystem.
 * 
 * @author michael
 *
 */
public class CompanyFacade implements CouponClientFacade {

	private CouponDBDAO couponDBDAO;
	private CompanyCouponDBDAO companyCouponDBDAO;
	private Company usingFacafeCompany;
	private CustomerCouponDBDAO customerCouponDBDAO;
	private CompanyDBDAO companyDBDAO;

	/**
	 * Facade constructor
	 * 
	 * @throws GeneralCouponSystemException
	 */
	public CompanyFacade(CouponDBDAO couponDBDAO, CompanyCouponDBDAO companyCouponDBDAO, CustomerCouponDBDAO customerCouponDBDAO,CompanyDBDAO companyDBDAO ) throws GeneralCouponSystemException {
		this.couponDBDAO = couponDBDAO;
		this.companyCouponDBDAO = companyCouponDBDAO;
		this.customerCouponDBDAO = customerCouponDBDAO;
		this.companyDBDAO = companyDBDAO;
	}

	public Company getUsingFacafeCompany() {
		return usingFacafeCompany;
	}


	/**
	 * This "create" method check that the parameter you inserted is legal and is in
	 * the correct limits and if it is, the method send the object you insert to
	 * DBDAO classes.
	 * 
	 * @param coupon
	 * @throws GeneralCouponSystemException
	 */
	public void createCoupon(Coupon coupon) throws GeneralCouponSystemException {
		try {
			Collection<Coupon> allCoup = couponDBDAO.getAllCoupon();
			Iterator<Coupon> coupIter = allCoup.iterator();
			if (!allCoup.isEmpty()) {
				while (coupIter.hasNext()) {
					if (coupIter.next().getTitle().equals(coupon.getTitle())) {
						GeneralCouponSystemException Message = new GeneralCouponSystemException(
								"Couldn't Create, The Object With Same Title Might Be In Your DataBase Already,\nIf Not Try Again Later . ");
						throw Message;
					}
				}
			}
			if (coupon.getAmount() < 1) {
				GeneralCouponSystemException Message = new GeneralCouponSystemException(
						"Couldn't Create, The Object Amount Might Be More Then 0 ,\nIf Not Try Again Later . ");
				throw Message;
			} else if (coupon.getEndDate().before(new Date())) {
				GeneralCouponSystemException Message = new GeneralCouponSystemException(
						"Couldn't Create, The Object End Date Might Be After Today Date,\nIf Not Try Again Later . ");
				throw Message;
			} else if (coupon.getPrice() < 0) {
				GeneralCouponSystemException Message = new GeneralCouponSystemException(
						"Couldn't Create, The Object Minimum Price is 0 ,\nIf Not Try Again Later . ");
				throw Message;
			} else {
				couponDBDAO.createCoupon(coupon);
				Collection<Coupon> compCoup = companyDBDAO.getCoupons();
				Iterator<Coupon> Iter =compCoup.iterator();
				while (Iter.hasNext()) {
					Coupon coup = Iter.next();
                      if (coupon.getTitle().equals(coup.getTitle())) {
                    	  companyCouponDBDAO.createCompanyCoupon(coup.getId(), usingFacafeCompany.getId());
					}
					
				}
			}
		} catch (Exception e) {
			GeneralCouponSystemException Message = new GeneralCouponSystemException(
					"Couldn't Create Or Purchase, The Object Might Be In Your DataBase Already, If Not Try Again Later . ");
			throw Message;
		}
	}


	/**
	 * This "REMOVE" method take the object you send and Send it to DBDAO, if it
	 * exist it will be DELETE from data base.
	 * 
	 * @param Id
	 *            - coupon id.
	 * @throws GeneralCouponSystemException
	 */
	public void removeCouponById(long Id) throws GeneralCouponSystemException {
		try {
			Coupon coupon = couponDBDAO.getCoupon(Id);
			if (coupon.getId() == 0) {
				GeneralCouponSystemException message = new GeneralCouponSystemException(
						"Delete Couldn't Execute Coupon With This ID Doesn't Exist, Please Try Again Later");
				throw message;
			}
			if (coupon != null) {
				Collection<Coupon> allcoup = getAllCompanyCoupons();
				Iterator<Coupon> coupIter = allcoup.iterator();
				while (coupIter.hasNext()) {
					Coupon nextCoup = coupIter.next();
					if (coupon.getId() == nextCoup.getId()) {
						customerCouponDBDAO.removeCustomerCoupon(coupon);
						companyCouponDBDAO.removeCompanyCoupon(coupon);
						couponDBDAO.removeCoupon(coupon);
						return;
					}
				}
			}
		} catch (Exception e) {
			GeneralCouponSystemException message = new GeneralCouponSystemException(
					"Delete Couldn't Execute, Please Try Again Later");
			throw message;
		}
	}

	/**
	 * This "UPDATE" method check that the parameter you inserted is legal and is in
	 * the correct limits and if it is, the method send the object you insert to
	 * DBDAO classes.
	 * 
	 * @param updateCoupon
	 *            - coupon.
	 * @throws GeneralCouponSystemException
	 */
	public void updateCoupon(Coupon updateCoupon) throws GeneralCouponSystemException {
		try {
			Coupon newCoup = couponDBDAO.getCoupon(updateCoupon.getId());
			if (newCoup.getId() == 0) {
				GeneralCouponSystemException message = new GeneralCouponSystemException(
						"Update Couldn't Execute Coupon With This ID Doesn't Exist, Please Try Again Later");
				throw message;
			}
			newCoup.setEndDate(updateCoupon.getEndDate());
			newCoup.setPrice(updateCoupon.getPrice());
			couponDBDAO.updateCoupon(newCoup);
		} catch (Exception e) {
			GeneralCouponSystemException message = new GeneralCouponSystemException(
					"Update Couldn't Execute, Please Try Again Later");
			throw message;
		}
	}


	/**
	 * This "GET" method checked that the object you ask for exist it the data base
	 * and if it is, the method return the object.
	 * 
	 * @param company.
	 * @return company.
	 * @throws GeneralCouponSystemException
	 */
	public Company getCompany(Company company) throws GeneralCouponSystemException {
		Company comp = companyDBDAO.getCompany(company.getId());
		if (comp.getId() > 0) {
			return comp;
		} else {
			GeneralCouponSystemException message = new GeneralCouponSystemException(
					"Couldn't Get Data From DataBase, Data Not Exist Or Invaild ID");
			throw message;
		}
	}

	/**
	 * This "GET" method checked that the object you ask for exist it the data base
	 * and if it is, the method return the object.
	 * 
	 * @param id
	 *            - company id.
	 * @return company.
	 * @throws GeneralCouponSystemException
	 */
	public Company getThisCompany() throws GeneralCouponSystemException {
			return usingFacafeCompany;
	}

	/**
	 * This "GET" method checked that the object you ask for exist it the data base
	 * and if it is, the method return the object.
	 * 
	 * @param id
	 *            - Coupon id.
	 * @return coupon.
	 * @throws GeneralCouponSystemException
	 */
	public Coupon getCoupon(long id) throws GeneralCouponSystemException {
		Coupon coup = couponDBDAO.getCoupon(id);
		if (coup.getId() > 0) {
			return coup;
		} else {
			GeneralCouponSystemException message = new GeneralCouponSystemException(
					"Couldn't Get Data From DataBase, Data Not Exist Or Invaild ID");
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
	public Collection<Coupon> getAllCompanyCoupons() throws GeneralCouponSystemException {
		Collection<Coupon> allCoup = companyCouponDBDAO.getAllCouponsByCompany(usingFacafeCompany.getId());
		if (allCoup.isEmpty()) {
			GeneralCouponSystemException message = new GeneralCouponSystemException(
					"Couldn't Get Data From DataBase, Data Not Exist Or Invaild Empty");
			throw message;
		} else {
			return allCoup;
		}
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
	public Collection<Coupon> getCouponByType(CouponType couponType) throws GeneralCouponSystemException {
		Collection<Coupon> coupByType = new HashSet<>();
		try {
			Collection<Coupon> compCoup = companyCouponDBDAO.getAllCouponsByCompany(usingFacafeCompany.getId());
			if (compCoup.isEmpty()) {
				GeneralCouponSystemException message = new GeneralCouponSystemException(
						"Couldn't Get Data From DataBase, Data Not Exist Or Invaild Empty");
				throw message;
			} else {
				Iterator<Coupon> CoupIter = compCoup.iterator();
				Coupon coup = new Coupon();
				while (CoupIter.hasNext()) {
					coup = CoupIter.next();
					if (coup.getType().equals(couponType)) {
						coupByType.add(coup);
					}
				}
				if (coupByType.isEmpty()) {
					GeneralCouponSystemException message = new GeneralCouponSystemException(
							"Couldn't Get Data From DataBase, Data Not Exist Or Invaild Type");
					throw message;
				} else {
					return coupByType;
				}
			}
		} catch (Exception e) {
			GeneralCouponSystemException message = new GeneralCouponSystemException(
					"Couldn't Get Data From DataBase, Data Not Exist Or Invaild ");
			throw message;
		}
	}

	/**
	 * This "GET" method checked that the object you ask for exist it the data base
	 * and if it is, the method return collection of objects from same "family" .
	 * 
	 * @param date
	 *            - coupon date.
	 * @return collection.
	 * @throws GeneralCouponSystemException
	 */
	public Collection<Coupon> getCouponByDate(Date date) throws GeneralCouponSystemException {
		Collection<Coupon> allCoup = new HashSet<>();
		Collection<Coupon> newAllCoup = new HashSet<>();
		try {
			allCoup = companyCouponDBDAO.getAllCouponsByCompany(usingFacafeCompany.getId());
			if (allCoup.isEmpty()) {
				GeneralCouponSystemException message = new GeneralCouponSystemException(
						"Couldn't Get Data From DataBase, Data Not Exist Or Invaild Empty");
				throw message;
			} else {
				Iterator<Coupon> CoupIter = allCoup.iterator();
				Coupon coup = new Coupon();
				while (CoupIter.hasNext()) {
					coup = CoupIter.next();
					if (coup.getEndDate().before(date)|| coup.getEndDate().equals(date)) {
						newAllCoup.add(coup);
					}
				}
				if (newAllCoup.isEmpty()) {
					GeneralCouponSystemException message = new GeneralCouponSystemException(
							"Couldn't Get Data From DataBase, Data Not Exist Or Invaild Empty");
					throw message;
				}else {
					return newAllCoup;
				}
			}
		} catch (Exception e) {
			GeneralCouponSystemException message = new GeneralCouponSystemException(
					"Couldn't Get Data From DataBase, Data Not Exist Or Invaild ");
			throw message;
		}
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
	public Collection<Coupon> getCouponByPrice(double price) throws GeneralCouponSystemException {
		try {
			Collection<Coupon> allCoupByPrice = companyCouponDBDAO.getAllCouponsByCompany(usingFacafeCompany.getId());
             Collection<Coupon> newAllCoupByPrice = new HashSet<>();			
			Iterator<Coupon> CoupIter = allCoupByPrice.iterator();
			while (CoupIter.hasNext()) {
				Coupon coup = CoupIter.next();
				if (coup.getPrice() <= price) {
					newAllCoupByPrice.add(coup);
				}
			}
			if (newAllCoupByPrice.isEmpty()) {
				GeneralCouponSystemException message = new GeneralCouponSystemException(
						"Couldn't Get Data From DataBase, Data Not Exist Or Invaild Price");
				throw message;
			} else {
				return newAllCoupByPrice;
			}
		} catch (Exception e) {
			GeneralCouponSystemException message = new GeneralCouponSystemException(
					"Couldn't Get Data From DataBase, Data Not Exist Or Invaild ");
			throw message;
		}
	}

	/**
	 * 
	 */
	@Override
	public CouponClientFacade login(String name, String password, ClientType clientType)
			throws GeneralCouponSystemException {
		try {
			if (clientType.equals(ClientType.COMPANY)) {
				if (companyDBDAO != null && companyDBDAO.login(name, password)) {
					this.usingFacafeCompany = companyDBDAO.getCompanyByName(name);
					return this;
				}
			}
		} catch (Exception e) {
			GeneralCouponSystemException message = new GeneralCouponSystemException(
					"Wrong Password or Username. Try again later!");
			throw message;
		}
		return null;
	}
}
