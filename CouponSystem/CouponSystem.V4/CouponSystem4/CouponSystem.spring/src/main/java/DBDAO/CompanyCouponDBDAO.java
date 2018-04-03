package DBDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import CouponSystemBeans.Company;
import CouponSystemBeans.Coupon;
import CouponSystemBeans.CouponType;
import CouponSystemException.GeneralCouponSystemException;
import ConnectionPool.ConnectionPool;
/**
 * This specify class Performs actions between The program and the Data Base Create/remove/update/get methods from and into data base. 
 * @author michael
 *
 */
public class CompanyCouponDBDAO {

	private ConnectionPool pool;
	
/**
 * This constructor Creating access to the connectionPoll and open the access to the data base.
 * @throws GeneralCouponSystemException
 */
	public CompanyCouponDBDAO() throws GeneralCouponSystemException {
		pool = ConnectionPool.getInstance();
	}
	
	/**
	 * This "create" method take object from java and change it to sql, after the change it sends sql to the data base and create the object.
	 * @param comp - company.
	 * @throws GeneralCouponSystemException
	 */
	public void createCompanyCoupon(Company comp) throws GeneralCouponSystemException {
		Connection con = pool.getConnections();
		PreparedStatement preparedStatement;
		try {
			Collection<Coupon> ccCoupon = comp.getCoupons();
			if (ccCoupon != null) {
				Iterator<Coupon> CoupoIter = ccCoupon.iterator();
				while (CoupoIter.hasNext()) {
					Coupon coup = CoupoIter.next();

					preparedStatement = con.prepareStatement("INSETR INTO company_coupon VALUES(?,?)");
					preparedStatement.setLong(1, comp.getId());
					preparedStatement.setLong(2, coup.getId());
					preparedStatement.execute();
				}
			}
		} catch (SQLException e) {
			GeneralCouponSystemException message = new GeneralCouponSystemException(
					"Couldn't Create Or Purchase, The Object Might Be In Your DataBase Already,\nIf Not Try Again Later . ");
			throw message;
		} finally {
			if (con != null) {
				pool.returnConnection(con);

			}
		}
	}
	/**
	 * This "create" method take object from java and change it to sql, after the change it sends the sql to the data base and create the object.
	 * @param coup - coupon.
	 * @param id - company id.
	 * @throws GeneralCouponSystemException
	 */
	public void createCompanyCoupon(long coupId, long id) throws GeneralCouponSystemException {

		Connection con = pool.getConnections();
		PreparedStatement preStatement;

		try {
			preStatement = con.prepareStatement("INSERT INTO company_coupon (COMPANY_ID, COUPON_ID) VALUES (?,?)");
			preStatement.setLong(1, id);
			preStatement.setLong(2, coupId);
			preStatement.execute();

		} catch (SQLException e) {
			e.printStackTrace();
			GeneralCouponSystemException message = new GeneralCouponSystemException(
					"Couldn't Create Or Purchase, The Object Might Be In Your DataBase Already,\nIf Not Try Again Later . ");
			throw message;
		} finally {
			if (con != null)
				pool.returnConnection(con);
		}

	}

	/**
	 * This "remove" method take object from java and change it to sql, after the change it sends sql to the data base and DELETE the object.
	 * @param coupon
	 * @throws GeneralCouponSystemException
	 */
	public void removeCompanyCoupon(Coupon coupon) throws GeneralCouponSystemException {
		Connection con = pool.getConnections();
		Statement stm;
		try {
			stm = con.createStatement();
			String sql = "DELETE FROM company_coupon WHERE coupon_id =" + coupon.getId();
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

	/**
	 * This "remove" method take object from java and change it to sql, after the change it sends sql to the data base and DELETE the object.
	 * @param company
	 * @throws GeneralCouponSystemException
	 */
	public void removeCompanyCoupon(Company company) throws GeneralCouponSystemException {
		Connection con = pool.getConnections();
		Statement stm;
		try {
			stm = con.createStatement();
			String sql = "DELETE FROM company_coupon WHERE company_id =" + company.getId();
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

	/**
	 * This "GET" method take ID from java and change it to sql, after the change it sends sql to the data base and search for the object whit the same ID, if it find it will return the object from the data base.
	 * @param id - company ID
	 * @return Collection<Coupon>.
	 * @throws GeneralCouponSystemException
	 */
	public Collection<Coupon> getAllCouponsByCompany(long id) throws GeneralCouponSystemException {

		Collection<Coupon> coupCollection = new HashSet<>();

		Connection con = pool.getConnections();

		if (con != null) {

			try {
				Statement stm = con.createStatement();
				ResultSet result;
				result = stm.executeQuery("SELECT * FROM coupon INNER JOIN company_coupon ON coupon.id ="
						+ " company_coupon.coupon_id WHERE company_id = " + id);

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
				e.printStackTrace();
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
