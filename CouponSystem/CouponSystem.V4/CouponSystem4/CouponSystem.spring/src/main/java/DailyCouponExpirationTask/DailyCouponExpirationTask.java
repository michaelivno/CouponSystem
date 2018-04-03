package DailyCouponExpirationTask;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import CouponSystemBeans.Coupon;
import CouponSystemException.GeneralCouponSystemException;
import DBDAO.CompanyCouponDBDAO;
import DBDAO.CouponDBDAO;
import DBDAO.CustomerCouponDBDAO;

/**
 * This class represents class that implements Runnable class whit run() method
 * that has command for the daily thread.
 * 
 * @author michael
 *
 */
public class DailyCouponExpirationTask implements Runnable {
	private CompanyCouponDBDAO companyCouponDBDAO;
	private CustomerCouponDBDAO customerCouponDBDAO;
	private CouponDBDAO couponDBDAO;
	private boolean quit = false;

	/**
	 * DailyCouponExpirationTask Constructor.
	 * 
	 * @throws GeneralCouponSystemException
	 */
	public DailyCouponExpirationTask() throws GeneralCouponSystemException {
		super();
		this.companyCouponDBDAO = new CompanyCouponDBDAO();
		this.customerCouponDBDAO = new CustomerCouponDBDAO();
		this.couponDBDAO = new CouponDBDAO();
	}

	/**
	 * This the implemented method from Runnable class, every coupon that the
	 * entdate hes expired will be deleted.
	 */
	@Override
	public void run() {
		try {
			while (quit != true) {
				Collection<Coupon> allCoup = couponDBDAO.getAllCoupon();
				Iterator<Coupon> coupIter = allCoup.iterator();
				while (coupIter.hasNext()) {
					Coupon coup = (Coupon) coupIter.next();
					if (coup.getEndDate().before(new Date())) {
						customerCouponDBDAO.removeCustomerCoupon(coup);
						companyCouponDBDAO.removeCompanyCoupon(coup);
						couponDBDAO.removeCoupon(coup);
					}
				}
				while (!Thread.currentThread().isInterrupted()) {
					TimeUnit.HOURS.sleep(24);
					
				}
			}

		} catch (Exception e) {
			GeneralCouponSystemException message = new GeneralCouponSystemException(
					"Problem whit daily TASK meneger Thread...");
			try {
				throw message;
			} catch (GeneralCouponSystemException e1) {
				e1.printStackTrace();
			}
		}

	}

	/**
	 * This method stop daily task Thread.
	 */
	public void stopTask() {
		this.quit = true;
	}

}
