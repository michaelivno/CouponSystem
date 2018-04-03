package CouponSystem;

import CouponSystemException.GeneralCouponSystemException;
import DBDAO.CompanyCouponDBDAO;
import DBDAO.CompanyDBDAO;
import DBDAO.CouponDBDAO;
import DBDAO.CustomerCouponDBDAO;
import DBDAO.CustomerDBDAO;
import DailyCouponExpirationTask.DailyCouponExpirationTask;
import Facades.AdminFacade;
import Facades.CompanyFacade;
import Facades.CouponClientFacade;
import Facades.CustomerFacade;
import ConnectionPool.ConnectionPool;
import Facades.CouponClientFacade.ClientType;

/**
 * This class represent instance of all program, you need to run the program
 * from this class and its single ton class...
 * 
 * @author michael
 *
 */
public final class CouponSystem {
	private DailyCouponExpirationTask dailyCouponExpirationTask = new DailyCouponExpirationTask();
	private Thread t1;
	private static CouponSystem instance;
	private CouponDBDAO couponDBDAO;
	private CustomerDBDAO customerDBDAO;
	private CompanyDBDAO companyDBDAO;
	private CompanyCouponDBDAO companyCouponDBDAO;
	private CustomerCouponDBDAO customerCouponDBDAO;

	/**
	 * This constructor is single ton constructor and can build only from
	 * getInstance/ in this constructor you run the daily thread.
	 * 
	 * @throws GeneralCouponSystemException
	 */
	private CouponSystem() throws GeneralCouponSystemException {
		super();
		try {
			ConnectionPool.getInstance();
			this.couponDBDAO = new CouponDBDAO();
			this.customerDBDAO = new CustomerDBDAO();
			this.companyDBDAO = new CompanyDBDAO();
			this.companyCouponDBDAO = new CompanyCouponDBDAO();
			this.customerCouponDBDAO = new CustomerCouponDBDAO();

			// t1 = new Thread(dailyCouponExpirationTask);
			// t1.start();
		} catch (Exception e) {
			GeneralCouponSystemException message = new GeneralCouponSystemException(
					"The Coupon System Had An Error in Daily Thread.");
			throw message;
		}

	}

	/**
	 * This get instance start all the program and the threads.
	 * 
	 * @return CouponSystem
	 * @throws GeneralCouponSystemException
	 */
	public static CouponSystem getInstance() throws GeneralCouponSystemException {
		if (instance == null) {
			try {
				instance = new CouponSystem();
			} catch (Exception e) {
				GeneralCouponSystemException message = new GeneralCouponSystemException();
				throw message;
			}
		}
		return instance;

	}

	/**
	 * This login check the client type and send it forward to the relevant FACADE.
	 * 
	 * @param name
	 * @param password
	 * @param clientType
	 * @return CouponClientFacade
	 * @throws GeneralCouponSystemException
	 */
	public CouponClientFacade login(String name, String password, ClientType clientType)
			throws GeneralCouponSystemException {

		if (clientType.equals(ClientType.ADMIN)) {
			AdminFacade admin = (AdminFacade) new AdminFacade(this.companyDBDAO, this.customerDBDAO, this.couponDBDAO,
					this.companyCouponDBDAO, this.customerCouponDBDAO).login(name, password, ClientType.ADMIN);
			return admin;

		} else if (clientType.equals(ClientType.COMPANY)) {
			CompanyFacade company = (CompanyFacade) new CompanyFacade(this.couponDBDAO, this.companyCouponDBDAO,
					this.customerCouponDBDAO, this.companyDBDAO).login(name, password, ClientType.COMPANY);
			return company;

		} else if (clientType.equals(ClientType.CUSTOMER)) {
			CustomerFacade customer = (CustomerFacade) new CustomerFacade(this.couponDBDAO, this.customerCouponDBDAO,
					this.customerDBDAO).login(name, password, ClientType.CUSTOMER);
			return customer;

		}
		return null;
	}

	/**
	 * This method SHUTDOWN all the program, the daily thread and the connectionPool
	 * to the data base.
	 * 
	 * @throws GeneralCouponSystemException
	 */
	public void shutDown() throws GeneralCouponSystemException {
		t1.interrupt();
		dailyCouponExpirationTask.stopTask();
		ConnectionPool.getInstance().closeAllConnection();
	}

}
