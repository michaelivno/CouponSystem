package Facades;

import java.util.Collection;
import java.util.Iterator;
import CouponSystemBeans.Company;
import CouponSystemBeans.Coupon;
import CouponSystemBeans.Customer;
import CouponSystemException.GeneralCouponSystemException;
import DBDAO.CompanyCouponDBDAO;
import DBDAO.CompanyDBDAO;
import DBDAO.CouponDBDAO;
import DBDAO.CustomerCouponDBDAO;
import DBDAO.CustomerDBDAO;

/**
 * This class represent The ADMIN FACADE the actions(methods) that can use only
 * administrator of couponSystem.
 * 
 * @author michael
 *
 */
public class AdminFacade implements CouponClientFacade {
	private CompanyDBDAO companyDBDAO;
	private CustomerDBDAO customerDBDAO;
	private CouponDBDAO couponDBDAO;
	private CustomerCouponDBDAO customerCouponDBDAO;
	private CompanyCouponDBDAO companyCouponDBDAO;

	/**
	 * Facade constructor
	 * 
	 * @throws GeneralCouponSystemException
	 */
	public AdminFacade(CompanyDBDAO companyDBDAO, CustomerDBDAO customerDBDAO, CouponDBDAO couponDBDAO, CompanyCouponDBDAO companyCouponDBDAO, CustomerCouponDBDAO customerCouponDBDAO ) throws GeneralCouponSystemException {
		this.customerDBDAO =  customerDBDAO;
		this.couponDBDAO = couponDBDAO;
		this.companyDBDAO = companyDBDAO;
		this.companyCouponDBDAO = companyCouponDBDAO;
		this.customerCouponDBDAO = customerCouponDBDAO;

	}

	/**
	 * This "create" method check that the parameter you inserted is legal and is in
	 * the correct limits and if it is, the method send the object you insert to
	 * DBDAO classes.
	 * 
	 * @param company
	 * @throws GeneralCouponSystemException
	 */
	public void createCompany(Company company) throws GeneralCouponSystemException {
		try {
			
			if(company.getCompName().equals("")||company.getPassword().equals("")||company.getPassword().equals("")) {
				GeneralCouponSystemException message = new GeneralCouponSystemException("Couldn't Create Company, some of the feelds empty");
			throw message;
			}
			if(!validEmailAddress(company.getEmail())){ 
				throw new GeneralCouponSystemException("The email you have entered is illegal");
			}
			Collection<Company> allComp = companyDBDAO.getAllCompanies();
			Iterator<Company> iter = allComp.iterator();
			while (iter.hasNext()) {
				if (iter.next().getCompName().equals(company.getCompName())) {
					GeneralCouponSystemException message = new GeneralCouponSystemException(
							"Couldn't Create Or Purchase, The Object With Same Name Might Be In Your DataBase Already,\nIf Not Try Again Later . ");
					throw message;
				}
			}
			companyDBDAO.createCompany(company);

		} catch (Exception e) {
			GeneralCouponSystemException message = new GeneralCouponSystemException(
					"Couldn't Create Or Purchase, The Object Might Be In Your DataBase Already,\nIf Not Try Again Later . ");
			throw message;

		}

	}
	
	private boolean validEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
 }


	/**
	 * This "create" method check that the parameter you inserted is legal and is in
	 * the correct limits and if it is, the method send the object you insert to
	 * DBDAO classes.
	 * 
	 * @param customer
	 * @throws GeneralCouponSystemException
	 */
	public void createCustomer(Customer customer) throws GeneralCouponSystemException {
		if (customer.getCustName().equals("")||customer.getPassword().equals("")) {
			GeneralCouponSystemException message =  new GeneralCouponSystemException("coldn't create customer one or more of the feelds is empty");
			throw message;
		}
		Collection<Customer> custColl = customerDBDAO.getAllCustomer();
		if (custColl.size() > 0) {
			Iterator<Customer> CusIter = custColl.iterator();
			while (CusIter.hasNext()) {
				if (customer.getCustName().equals(CusIter.next().getCustName())) {
					GeneralCouponSystemException message = new GeneralCouponSystemException(
							"Couldn't Create Or Purchase, The Object With Same Name Might Be In Your DataBase Already,\nIf Not Try Again Later . ");
					throw message;
				}
			}
			customerDBDAO.createCustomer(customer);
		} else {
			customerDBDAO.createCustomer(customer);
		}
	}


	/**
	 * This "UPDATE" method check that the parameter you inserted is legal and is in
	 * the correct limits and if it is, the method send the object you insert to
	 * DBDAO classes.
	 * 
	 * @param comp
	 *            - company.
	 * @throws GeneralCouponSystemException
	 */
	public void updateCompany(Company comp) throws GeneralCouponSystemException {
		Company oldComp = companyDBDAO.getCompany(comp.getId());
		if (oldComp.getId() != 0) {
			if (oldComp.getCompName().equals(comp.getCompName())) {
				companyDBDAO.updateCompany(comp);
			} else {
				GeneralCouponSystemException message = new GeneralCouponSystemException(
						"Update Couldn't Execute You Can't Change The NAME, Please Try Again Later");
				throw message;
			}
		} else {
			GeneralCouponSystemException message = new GeneralCouponSystemException(
					"Update Couldn't Execute Company With This ID Doesn't Exist, Please Try Again Later");
			throw message;
		}
	}


	/**
	 * This "UPDATE" method check that the parameter you inserted is legal and is in
	 * the correct limits and if it is, the method send the object you insert to
	 * DBDAO classes.
	 * 
	 * @param customer
	 * @throws GeneralCouponSystemException
	 */
	public void updateCustomer(Customer customer) throws GeneralCouponSystemException {
		Customer oldCust = customerDBDAO.getCustomer(customer.getId());
		if (oldCust.getId() != 0) {
			if (oldCust.getCustName().equals(customer.getCustName())) {
				customerDBDAO.updateCustomer(customer);
			} else {
				GeneralCouponSystemException message = new GeneralCouponSystemException(
						"Update Couldn't Execute You Can't Change The NAME, Please Try Again Later");
				throw message;
			}
		} else {
			GeneralCouponSystemException message = new GeneralCouponSystemException(
					"Update Couldn't Execute Customer With This ID Doesn't Exist, Please Try Again Later");
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
	public Collection<Company> getaAllCompanys() throws GeneralCouponSystemException {
		Collection<Company> allComp = companyDBDAO.getAllCompanies();
		if (allComp.isEmpty()) {
			GeneralCouponSystemException message = new GeneralCouponSystemException(
					"Couldn't Get Data From DataBase, Data Not Exist Or Invaild ");
			throw message;
		}
		return allComp;
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
	public Company getCompany(long id) throws GeneralCouponSystemException {
		Company comp = companyDBDAO.getCompany(id);
		if (comp.getId() == 0) {
			GeneralCouponSystemException message = new GeneralCouponSystemException(
					"Couldn't Get Data From DataBase, Data Not Exist Or Invaild ID");
			throw message;
		}
		return comp;
	}

	/**
	 * This "GET" method checked that the object you ask for exist it the data base
	 * and if it is, the method return collection of objects from same "family" .
	 * 
	 * @return collection.
	 * @throws GeneralCouponSystemException
	 */
	public Collection<Customer> getAllCustomers() throws GeneralCouponSystemException {
		Collection<Customer> allCust = customerDBDAO.getAllCustomer();
		if (allCust.isEmpty()) {
			GeneralCouponSystemException message = new GeneralCouponSystemException(
					"Couldn't Get Data From DataBase, Data Not Exist Or Invaild ");
			throw message;
		}
		return allCust;
	}

	/**
	 * This "GET" method checked that the object you ask for exist it the data base
	 * and if it is, the method return the object.
	 * 
	 * @param id
	 *            - customer id.
	 * @return customer.
	 * @throws GeneralCouponSystemException
	 */
	public Customer getCustomer(long id) throws GeneralCouponSystemException {
		Customer cust = customerDBDAO.getCustomer(id);
		if (cust.getId() == 0) {
			GeneralCouponSystemException message = new GeneralCouponSystemException(
					"Couldn't Get Data From DataBase, Data Not Exist Or Invaild ID");
			throw message;
		}
		return cust;
	}


	/**
	 * This "REMOVE" method take the object you send and Send it to DBDAO, if it
	 * exist it will be DELETE from data base.
	 * 
	 * @param id
	 *            - company id.
	 * @throws GeneralCouponSystemException
	 */
	public void removeCompany(long id) throws GeneralCouponSystemException {
		try {
			Company company = companyDBDAO.getCompany(id);
			if (company.getId() == 0) {
				GeneralCouponSystemException message = new GeneralCouponSystemException(
						"Delete Couldn't Execute Company With This ID Doesn't Exist, Please Try Again Later");
				throw message;
			}
			Collection<Coupon> companyCoup = companyCouponDBDAO.getAllCouponsByCompany(company.getId());
			if (companyCoup != null) {
				Iterator<Coupon> compIter = companyCoup.iterator();
				while (compIter.hasNext()) {
					Coupon coup = compIter.next();
					customerCouponDBDAO.removeCustomerCoupon(coup);
					companyCouponDBDAO.removeCompanyCoupon(company);
					couponDBDAO.removeCoupon(coup);
				}
			}
			companyDBDAO.removeCompany(company);
		} catch (Exception e) {
			GeneralCouponSystemException message = new GeneralCouponSystemException(
					"Delete Couldn't Execute, Please Try Again Later");
			throw message;
		}
	}


	/**
	 * This "REMOVE" method take the object you send and Send it to DBDAO, if it
	 * exist it will be DELETE from data base.
	 * 
	 * @param id
	 *            - customer id.
	 * @throws GeneralCouponSystemException
	 */
	public void removeCustomer(long id) throws GeneralCouponSystemException {
		try {
			Customer customer = customerDBDAO.getCustomer(id);
			if (customer.getId() == 0) {
				GeneralCouponSystemException message = new GeneralCouponSystemException(
						"Delete Couldn't Execute Customer With This ID Doesn't Exist, Please Try Again Later");
				throw message;
			}
			Collection<Coupon> customerCoupon = customerCouponDBDAO.getAllCouponsByCustomer(customer.getId());
			if (customerCoupon != null) {
				Iterator<Coupon> coup = customerCoupon.iterator();
				while (coup.hasNext()) {
					customerCouponDBDAO.removeCustomerCoupon(coup.next());
				}
			}
			customerDBDAO.removeCustomer(customer);
		} catch (Exception e) {
			GeneralCouponSystemException message = new GeneralCouponSystemException(
					"Delete Couldn't Execute, Please Try Again Later");
			throw message;
		}
	}

	/**
	 * 
	 */
	@Override
	public CouponClientFacade login(String name, String password, ClientType clientType)
			throws GeneralCouponSystemException {
		if (clientType.equals(ClientType.ADMIN)) {
			if (name.equals("admin") && password.equals("1234")) {
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
