package DBDAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import CouponSystemBeans.Coupon;
import CouponSystemBeans.CouponType;
import CouponSystemException.GeneralCouponSystemException;
import DAO.CouponDAO;
import ConnectionPool.ConnectionPool;

/**
 * This specify class Override the DAO methods and Performs actions between The
 * program and the Data Base Create/remove/update/get methods from and into data
 * base.
 * 
 * @author michael
 *
 */
public class CouponDBDAO implements CouponDAO {
	private ConnectionPool pool;

	private Connection con = null;

	/**
	 * This constructor Creating access to the connectionPoll and open the access to
	 * the data base.
	 * 
	 * @throws GeneralCouponSystemException
	 */
	public CouponDBDAO() throws GeneralCouponSystemException {
		super();
		pool = ConnectionPool.getInstance();
	}

	/**
	 * This "create" method take object from java and change it to sql, after the
	 * change it sends sql to the data base and create the object.
	 * 
	 * @param coupon.
	 * @throws GeneralCouponSystemException
	 */
	@Override
	public void createCoupon(Coupon coupon) throws GeneralCouponSystemException {

		try {

			con = pool.getConnections();
			String sql = "INSERT INTO coupon (TITLE, START_DATE, END_DATE, AMOUNT, TYPE, DESCRIPTION, PRICE, IMAGE) VALUES (?,?,?,?,?,?,?,?)";
			PreparedStatement preparedStatement = con.prepareStatement(sql);

			preparedStatement.setString(1, coupon.getTitle());
			preparedStatement.setDate(2, (Date) initStartDate());
			preparedStatement.setDate(3, new Date(coupon.getEndDate().getTime()));
			preparedStatement.setInt(4, coupon.getAmount());
			preparedStatement.setString(5, coupon.getType().name());
			preparedStatement.setString(6, coupon.getMessage());
			preparedStatement.setDouble(7, coupon.getPrice());
			preparedStatement.setString(8, coupon.getImage());
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			GeneralCouponSystemException message = new GeneralCouponSystemException(
					"Couldn't Create Or Purchase, The Object Might Be In Your DataBase Already,\nIf Not Try Again Later . ");
			throw message;
		} finally {
			if (con != null) {
				pool.returnConnection(con);

			}
		}

	}

	public java.util.Date initStartDate() {
		LocalDate localDate = LocalDate.now();
		DateTimeFormatter.ofPattern("yyyy/MM/DD").format(localDate);
		java.util.Date date = java.sql.Date.valueOf(localDate);
		return date;
	}

	public java.util.Date initEndDate(int year, int month, int day) {
		month = month - 1;
		GregorianCalendar gcalender = new GregorianCalendar(year, month, day);
		return gcalender.getTime();
	}

	/**
	 * This "remove" method take object from java and change it to sql, after the
	 * change it sends sql to the data base and DELETE the object.
	 * 
	 * @param coupon
	 * @throws GeneralCouponSystemException
	 */
	@Override
	public void removeCoupon(Coupon coupon) throws GeneralCouponSystemException {
		String sql = "DELETE FROM coupon WHERE ID =" + coupon.getId();

		try {
			con = pool.getConnections();
			Statement stmt = con.createStatement();
			stmt.executeUpdate(sql);

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
	 * This "update" method take object from java and change it to sql, after the
	 * change it sends sql to the data base and UPDATE the old object.
	 * 
	 * @param coupon
	 * @throws GeneralCouponSystemException
	 */
	@Override
	public void updateCoupon(Coupon coupon) throws GeneralCouponSystemException {
		try {
			con = pool.getConnections();
			String sql = "UPDATE coupon set TITLE=?, START_DATE=?, END_DATE=?, AMOUNT=?, TYPE=?, DESCRIPTION=?, PRICE=?, IMAGE=? WHERE ID=?";
			PreparedStatement preparedStatement = con.prepareStatement(sql);

			java.util.Date startDate = new java.util.Date(coupon.getStartDate().getTime());
			java.sql.Date SqlStartDate = new java.sql.Date(startDate.getTime());
			java.util.Date endDate = new java.util.Date(coupon.getEndDate().getTime());
			java.sql.Date SqlEndDate = new java.sql.Date(endDate.getTime());

			preparedStatement.setString(1, coupon.getTitle());
			preparedStatement.setDate(2, SqlStartDate);
			preparedStatement.setDate(3, SqlEndDate);
			preparedStatement.setInt(4, coupon.getAmount());
			preparedStatement.setString(5, coupon.getType().name());
			preparedStatement.setString(6, coupon.getMessage());
			preparedStatement.setDouble(7, coupon.getPrice());
			preparedStatement.setString(8, coupon.getImage());
			preparedStatement.setLong(9, coupon.getId());
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			GeneralCouponSystemException message = new GeneralCouponSystemException(
					"Update Couldn't Execute, Please Try Again Later");
			throw message;
		} finally {
			if (con != null) {
				pool.returnConnection(con);

			}
		}

	}

	/**
	 * This "GET" method take ID from java and change it to sql, after the change it
	 * sends sql to the data base and search for the object whit the same ID, if it
	 * find it will return the object from the data base.
	 * 
	 * @param id
	 *            - Coupon ID.
	 * @return Coupon.
	 * @throws GeneralCouponSystemException
	 */
	@Override
	public Coupon getCoupon(long id) throws GeneralCouponSystemException {

		Coupon coup = new Coupon();

		try {
			con = pool.getConnections();
			if (con != null) {

				Statement stmt = con.createStatement();
				ResultSet result = stmt.executeQuery("SELECT * FROM coupon");

				if (result != null) {
					while (result.next() == true) {
						if (id == result.getLong("ID")) {

							coup.setId(id);
							coup.setTitle(result.getString("TITLE"));
							coup.setStartDate(result.getDate("START_DATE"));
							coup.setEndDate(result.getDate("END_DATE"));
							coup.setAmount(result.getInt("AMOUNT"));
							coup.setType(CouponType.valueOf(result.getString("TYPE")));
							coup.setMessage(result.getString("DESCRIPTION"));
							coup.setPrice(result.getDouble("PRICE"));
							coup.setImage(result.getString("IMAGE"));
						}
					}
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
		return coup;
	}

	/**
	 * This "GET" method search for all objects from same "family" and return a
	 * collection of objects.
	 * 
	 * @return Collection.
	 * @throws GeneralCouponSystemException
	 */
	@Override
	public Collection<Coupon> getAllCoupon() throws GeneralCouponSystemException {
		Connection con = null;
		Set<Coupon> coupons = new HashSet<>();

		try {
			con = pool.getConnections();
			if (con != null) {

				Statement stmt = con.createStatement();
				ResultSet result = stmt.executeQuery("SELECT * FROM coupon");

				while (result.next() == true) {
					Coupon coup = new Coupon();
					coup.setId(result.getLong("ID"));
					coup.setTitle(result.getString("TITLE"));
					coup.setStartDate(result.getDate("START_DATE"));
					coup.setEndDate(result.getDate("END_DATE"));
					coup.setAmount(result.getInt("AMOUNT"));
					coup.setType(CouponType.valueOf(result.getString("TYPE")));
					coup.setMessage(result.getString("DESCRIPTION"));
					coup.setPrice(result.getDouble("PRICE"));
					coup.setImage(result.getString("IMAGE"));
					coupons.add(coup);
				}
			}
		} catch (SQLException e) {
			GeneralCouponSystemException message = new GeneralCouponSystemException(
					"Couldn't Get Data From DataBase, Data Not Exist Or Invaild");
			throw message;
		} finally {
			if (con != null) {
				pool.returnConnection(con);

			}
		}
		return coupons;
	}

	/**
	 * This "GET" method take TYPE from java and change it to sql, after the change
	 * it sends sql to the data base and search for the object whit the same TYPE,
	 * if it find it will return the object from the data base.
	 * 
	 * @param type
	 *            - coupon Type.
	 * @return Collection<Coupon>.
	 * @throws GeneralCouponSystemException
	 */
	@Override
	public Collection<Coupon> getCouponByType(CouponType type) throws GeneralCouponSystemException {
		Connection con = null;
		Collection<Coupon> coupByType = new LinkedList<>();
		try {

			con = pool.getConnections();
			if (con != null) {

				Statement stmt = con.createStatement();
				ResultSet result = stmt.executeQuery("SELECT * FROM coupon WHERE TYPE=" + "'" + type.name() + "'");
				while (result.next()) {
					Coupon coup = new Coupon();
					coup.setId(result.getLong("ID"));
					coup.setTitle(result.getString("TITLE"));
					coup.setStartDate(result.getDate("START_DATE"));
					coup.setEndDate(result.getDate("END_DATE"));
					coup.setAmount(result.getInt("AMOUNT"));
					coup.setType(CouponType.valueOf(result.getString("TYPE")));
					coup.setMessage(result.getString("DESCRIPTION"));
					coup.setPrice(result.getDouble("PRICE"));
					coup.setImage(result.getString("IMAGE"));
					coupByType.add(coup);
					System.out.println(coup.getId());
				}
			}
		} catch (SQLException e) {
			GeneralCouponSystemException message = new GeneralCouponSystemException(
					"Couldn't Get Data From DataBase, Data Not Exist Or Invaild Type");
			throw message;
		} finally {
			if (con != null) {
				pool.returnConnection(con);
			}
		}

		return coupByType;
	}

}
