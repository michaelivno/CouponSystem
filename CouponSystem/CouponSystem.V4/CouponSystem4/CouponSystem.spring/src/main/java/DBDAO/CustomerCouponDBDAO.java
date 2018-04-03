package DBDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import CouponSystemBeans.Coupon;
import CouponSystemBeans.CouponType;
import CouponSystemBeans.Customer;
import CouponSystemException.GeneralCouponSystemException;
import ConnectionPool.ConnectionPool;
/**
 * This specify class Performs actions between The program and the Data Base Create/remove/update/get methods from and into data base.
 * @author michael
 *
 */
public class CustomerCouponDBDAO {

	private ConnectionPool pool;
/**
 * This constructor Creating access to the connectionPoll and open the access to the data base.
 * @throws GeneralCouponSystemException
 */
	public CustomerCouponDBDAO() throws GeneralCouponSystemException {
		super();
		pool = ConnectionPool.getInstance();
	}

	/**
	 * This "create" method take object from java and change it to sql, after the change it sends sql to the data base and create the object.
	 * @param cust - customer.
	 * @throws GeneralCouponSystemException
	 */
	public void createCustomerCoupon(Customer cust) throws GeneralCouponSystemException {

		Connection con = pool.getConnections();

		PreparedStatement preStatement;

		try {
			preStatement = con.prepareStatement("INSERT INTO customer_coupon VALUES (?,?)");
			Collection<Coupon> couponCol = cust.getCoupons();

			if (couponCol != null) {
				Iterator<Coupon> it = couponCol.iterator();
				while (it.hasNext()) {
					Coupon currentCoupon = it.next();
					preStatement.setLong(1, cust.getId());
					preStatement.setLong(2, currentCoupon.getId());
					preStatement.execute();
				}

			}
		} catch (SQLException e) {
			GeneralCouponSystemException message = new GeneralCouponSystemException(
					"Couldn't Create Or Purchase, The Object Might Be In Your DataBase Already,\nIf Not Try Again Later . ");
			throw message;
		} finally {
			if (con != null)
				pool.returnConnection(con);
		}
	}

	/**
	 * This "create" method take object from java and change it to sql, after the change it sends sql to the data base and create the object.
	 * @param coup - coupon.
	 * @param id - customer id.
	 * @throws GeneralCouponSystemException
	 */
	public void createCustomerCoupon(Coupon coup, long id) throws GeneralCouponSystemException {

		Connection con = pool.getConnections();
		PreparedStatement preStatement;
		if (con != null) {

			try {
				preStatement = con.prepareStatement("INSERT INTO customer_coupon VALUES (?,?)");
				preStatement.setLong(1, id);
				preStatement.setLong(2, coup.getId());
				preStatement.execute();

			} catch (SQLException e) {
				GeneralCouponSystemException message = new GeneralCouponSystemException(
						"Couldn't Create Or Purchase, The Object Might Be In Your DataBase Already,\nIf Not Try Again Later . ");
				throw message;
			} finally {
				if (con != null)
					pool.returnConnection(con);
			}
		}
	}

	/**
	 * This "remove" method take object from java and change it to sql, after the change it sends sql to the data base and DELETE the object.
	 * @param coupon
	 * @throws GeneralCouponSystemException
	 */
	public void removeCustomerCoupon(Coupon coupon) throws GeneralCouponSystemException {
		CouponDBDAO dbCoup = new CouponDBDAO();
		Coupon coup = dbCoup.getCoupon(coupon.getId());

		if (coup != null) {

			Connection con = pool.getConnections();

			if (con != null) {
				Statement stm;
				try {

					stm = con.createStatement();
					String sql = "DELETE FROM customer_coupon WHERE coupon_id =" + coupon.getId();
					stm.executeUpdate(sql);

				} catch (SQLException e) {
					GeneralCouponSystemException message = new GeneralCouponSystemException(
							"Delete Couldn't Execute, Please Try Again Later");
					throw message;
				} finally {
					if (con != null) {
						pool.returnConnection(con);
					}
				}
			}
		}
	}

	/**
	 * This "GET" method take ID from java and change it to sql, after the change it sends sql to the data base and search for the object whit the same ID, if it find it will return the object from the data base.
	 * @param id - Customer ID.
	 * @return Collection<Coupon>.
	 * @throws GeneralCouponSystemException
	 */
	public Collection<Coupon> getAllCouponsByCustomer(long id) throws GeneralCouponSystemException {

		Collection<Coupon> coupCollection = new HashSet<>();

		Connection con = pool.getConnections();

		if (con != null) {

			try {

				Statement stm = con.createStatement();
				ResultSet result;
				result = stm.executeQuery("SELECT * FROM coupon INNER JOIN customer_coupon ON coupon.id ="
						+ " customer_coupon.coupon_id WHERE customer_ID = " + id);

				while (result.next() == true) {
					if (result != null) {

						Coupon coupon = new Coupon();

						coupon.setAmount(result.getInt("amount"));
						coupon.setTitle(result.getString("title"));
						coupon.setStartDate(result.getDate("start_date"));
						coupon.setEndDate(result.getDate("end_date"));
						coupon.setType(CouponType.valueOf(result.getString("type")));
						coupon.setMessage(result.getString("description"));
						coupon.setPrice(result.getDouble("price"));
						coupon.setImage(result.getString("image"));
						coupon.setId(result.getLong("id"));

						coupCollection.add(coupon);
					}
				}

			} catch (SQLException e) {
				GeneralCouponSystemException message = new GeneralCouponSystemException(
						"Couldn't Get Data From DataBase, Data Not Exist Or Invaild ID");
				throw message;
			} finally {
				if (con != null) {
					pool.returnConnection(con);
				}
			}
		}

		return coupCollection;

	}

}
