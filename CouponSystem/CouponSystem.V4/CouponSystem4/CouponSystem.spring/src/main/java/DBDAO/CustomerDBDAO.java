package DBDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import CouponSystemBeans.Coupon;
import CouponSystemBeans.Customer;
import CouponSystemException.GeneralCouponSystemException;
import DAO.CustomerDAO;
import ConnectionPool.ConnectionPool;

/**
 * This specify class Override the DAO methods and Performs actions between The
 * program and the Data Base Create/remove/update/get methods from and into data
 * base.
 * 
 * @author michael
 *
 */
public class CustomerDBDAO implements CustomerDAO {

	private ConnectionPool pool;
	Connection con = null;

	/**
	 * This constructor Creating access to the connectionPoll and open the access to
	 * the data base.
	 * 
	 * @throws GeneralCouponSystemException
	 */
	public CustomerDBDAO() throws GeneralCouponSystemException {
		super();
		pool = ConnectionPool.getInstance();
	}

	@Override
	public void createCustomer(Customer customer) throws GeneralCouponSystemException {

		try {
			con = pool.getConnections();
			if (con != null) {

				String sql = "INSERT INTO customer (CUST_NAME, PASSWORD) VALUES (?,?)";
				PreparedStatement preparedStatement = con.prepareStatement(sql);

				preparedStatement.setString(1, customer.getCustName());
				preparedStatement.setString(2, customer.getPassword());
				preparedStatement.executeUpdate();

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
	 * This "remove" method take object from java and change it to sql, after the
	 * change it sends sql to the data base and DELETE the object.
	 * 
	 * @param customer
	 * @throws GeneralCouponSystemException
	 */
	@Override
	public void removeCustomer(Customer customer) throws GeneralCouponSystemException {
		String sql = "DELETE FROM customer WHERE ID =" + customer.getId();

		try {
			con = pool.getConnections();
			if (con != null) {

				Statement stmt = con.createStatement();
				stmt.executeUpdate(sql);

			}
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
	 * @param customer
	 * @throws GeneralCouponSystemException
	 */
	@Override
	public void updateCustomer(Customer customer) throws GeneralCouponSystemException {
		PreparedStatement preparedStatement;
		String sql = "UPDATE customer set CUST_NAME = ?, PASSWORD = ? WHERE ID=?";
		try {
			con = pool.getConnections();
			if (con != null) {

				preparedStatement = con.prepareStatement(sql);
				preparedStatement.setString(1, customer.getCustName());
				preparedStatement.setString(2, customer.getPassword());
				preparedStatement.setLong(3, customer.getId());
				preparedStatement.executeUpdate();

			}
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
	 * @return Customer
	 * @param id
	 *            - customer id.
	 * @throws GeneralCouponSystemException
	 */
	@Override
	public Customer getCustomer(long id) throws GeneralCouponSystemException {
		Customer cust = new Customer();

		try {
			con = pool.getConnections();
			if (con != null) {

				Statement stmt = con.createStatement();
				ResultSet result = stmt.executeQuery("SELECT * FROM customer");

				if (result != null) {
					while (result.next() == true) {
						if (id == result.getLong("ID")) {

							cust.setId(id);
							cust.setCustName(result.getString("CUST_NAME"));
							cust.setPassword(result.getString("PASSWORD"));
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
		return cust;

	}

	/**
	 * This "GET" method take NAME from java and change it to sql, after the change
	 * it sends sql to the data base and search for the object whit the same NAME,
	 * if it find it will return the object from the data base.
	 * 
	 * @param name
	 *            - Customer Name.
	 * @return Customer.
	 * @throws GeneralCouponSystemException
	 */
	public Customer getCustomerByName(String name) throws GeneralCouponSystemException {
		Customer cust = new Customer();

		try {
			con = pool.getConnections();
			if (con != null) {

				Statement stmt = con.createStatement();
				ResultSet result = stmt.executeQuery("SELECT * FROM customer");

				if (result != null) {
					while (result.next() == true) {
						if (name.equals(result.getString("CUST_NAME"))) {

							cust.setId(result.getLong("ID"));
							cust.setCustName(name);
							cust.setPassword(result.getString("PASSWORD"));
						}
					}
				}
			}
		} catch (SQLException e) {
			GeneralCouponSystemException message = new GeneralCouponSystemException(
					"Couldn't Get Data From DataBase, Data Not Exist Or Invaild NAME");
			throw message;
		} finally {
			if (con != null) {
				pool.returnConnection(con);
			}
		}
		return cust;

	}

	/**
	 * This "GET" method search for all objects from same "family" and return a
	 * collection of objects.
	 * 
	 * @return Collection.
	 * @throws GeneralCouponSystemException
	 */
	@Override
	public Collection<Customer> getAllCustomer() throws GeneralCouponSystemException {
		Set<Customer> customers = new HashSet<>();
		try {
			con = pool.getConnections();
			if (con != null) {

				Statement stmt = con.createStatement();
				ResultSet result = stmt.executeQuery("SELECT * FROM customer");
				while (result.next() == true) {
					Customer cust = new Customer();
					cust.setId(result.getLong("ID"));
					cust.setCustName(result.getString("CUST_NAME"));
					cust.setPassword(result.getString("PASSWORD"));
					customers.add(cust);
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
		return customers;
	}

	/**
	 * This "GET" method search for all objects from same "family" and return a
	 * collection of objects.
	 * 
	 * @return Collection.
	 * @throws GeneralCouponSystemException
	 */
	@Override
	public Collection<Coupon> getCoupons() throws GeneralCouponSystemException {
		Collection<Coupon> coupons = new HashSet<>();
		try {
			con = pool.getConnections();
			if (con != null) {
				CouponDBDAO CoupDBDAO = new CouponDBDAO();
				coupons = CoupDBDAO.getAllCoupon();
			}
		} catch (Exception e) {
			GeneralCouponSystemException message = new GeneralCouponSystemException(
					"Couldn't Get Data From DataBase, Data Not Exist Or Invaild ID");
			throw message;
		} finally {
			if (con != null) {
				pool.returnConnection(con);

			}
		}

		return coupons;
	}

	/**
	 * This LOGIN method takes the parameters that insert into and search in the
	 * data base for objent in same "family" whit same parameters, and if it find,
	 * it will return TRUE for the login.
	 * 
	 * @param custName
	 *            - Customer Name.
	 * @param password
	 *            - Customer Password.
	 * @return Boolean
	 * @throws GeneralCouponSystemException
	 */
	@Override
	public boolean login(String custName, String password) throws GeneralCouponSystemException {
		try {
			con = pool.getConnections();
			if (con != null) {

				Statement stmt = con.createStatement();
				ResultSet result = stmt.executeQuery("SELECT * FROM customer");
				if (result != null) {
					while (result.next()) {
						if (result.getString("PASSWORD").equals(password)
								&& result.getString("CUST_NAME").equals(custName)) {
							return true;
						}
					}
					GeneralCouponSystemException message = new GeneralCouponSystemException(
							"Worrg Password or Username. Try again later!");
					throw message;
				}
			}
		} catch (SQLException e) {
			GeneralCouponSystemException message = new GeneralCouponSystemException(
					"Wrong Password or Username. Try again later!");
			throw message;
		} finally {
			if (con != null) {
				pool.returnConnection(con);
			}
		}
		return false;
	}

}
