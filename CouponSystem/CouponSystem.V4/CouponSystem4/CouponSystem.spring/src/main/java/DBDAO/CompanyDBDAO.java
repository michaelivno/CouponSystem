package DBDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import CouponSystemBeans.Company;
import CouponSystemBeans.Coupon;
import CouponSystemBeans.CouponType;
import CouponSystemException.GeneralCouponSystemException;
import DAO.CompanyDAO;
import ConnectionPool.ConnectionPool;
/**
 * This specify class Override the DAO methods and Performs actions between The program and the Data Base Create/remove/update/get methods from and into data base.
 * @author michael
 *
 */
public class CompanyDBDAO implements CompanyDAO {

	private ConnectionPool pool;
/**
 * This constructor Creating access to the connectionPoll and open the access to the data base.
 * @throws GeneralCouponSystemException
 */
	public CompanyDBDAO() throws GeneralCouponSystemException {
		super();
		pool = ConnectionPool.getInstance();
	}

	/**
	 * This "create" method take object from java and change it to sql, after the change it sends sql to the data base and create the object.
	 * @param company
	 * @throws GeneralCouponSystemException
	 */
	@Override
	public void createCompany(Company company) throws GeneralCouponSystemException {

		Connection con = pool.getConnections();
		PreparedStatement preparedStatement;
		try {
			String sql = "INSERT INTO company (COMP_NAME, PASSWORD, EMAIL) VALUES (?,?,?)";

			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, company.getCompName());
			preparedStatement.setString(2, company.getPassword());
			preparedStatement.setString(3, company.getEmail());
			preparedStatement.executeUpdate();

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
	 * This "remove" method take object from java and change it to sql, after the change it sends sql to the data base and DELETE the object.
	 * @param company
	 * @throws GeneralCouponSystemException
	 */
	@Override
	public void removeCompany(Company company) throws GeneralCouponSystemException {
		Connection con = null;
		String sql = "DELETE FROM company WHERE ID =" + company.getId();

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
	 * This "update" method take object from java and change it to sql, after the change it sends sql to the data base and UPDATE the old object.
	 * @param company
	 * @throws GeneralCouponSystemException
	 * 
	 */
	@Override
	public void updateCompany(Company company) throws GeneralCouponSystemException {

		Connection con = pool.getConnections();
		PreparedStatement preparedStatement;
		try {
			String sql = "UPDATE company set COMP_NAME = ?, PASSWORD = ?, EMAIL = ?  WHERE ID=?";
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, company.getCompName());
			preparedStatement.setString(2, company.getPassword());
			preparedStatement.setString(3, company.getEmail());
			preparedStatement.setLong(4, company.getId());
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
	 * This "GET" method take ID from java and change it to sql, after the change it sends sql to the data base and search for the object whit the same ID, if it find it will return the object from the data base.
	 * @return Company.
	 * @param id - company ID.
	 * @throws GeneralCouponSystemException
	 */
	@Override
	public Company getCompany(long id) throws GeneralCouponSystemException {

		Company comp = new Company();
		Connection con = null;

		try {
			con = pool.getConnections();
			Statement stmt = con.createStatement();
			ResultSet result = stmt.executeQuery("SELECT * FROM company");

			if (result != null) {
				while (result.next() == true) {
					if (id == result.getLong("ID")) {
						comp.setId(id);
						comp.setCompName(result.getString("COMP_NAME"));
						comp.setPassword(result.getString("PASSWORD"));
						comp.setEmail(result.getString("EMAIL"));
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
		return comp;
	}

	/**
	 * This "GET" method take NAME from java and change it to sql, after the change it sends sql to the data base and search for the object whit the same NAME, if it find it will return the object from the data base.
	 * @param companyName - Company Name.
	 * @return Company.
	 * @throws GeneralCouponSystemException
	 */
	public Company getCompanyByName(String companyName) throws GeneralCouponSystemException {

		Company comp = new Company();
		Connection con = null;

		try {
			con = pool.getConnections();
			Statement stmt = con.createStatement();
			ResultSet result = stmt.executeQuery("SELECT * FROM company");

			if (result != null) {
				while (result.next() == true) {
					if (companyName.equals(result.getString("COMP_NAME"))) {

						comp.setCompName(companyName);
						comp.setId(result.getLong("ID"));
						comp.setPassword(result.getString("PASSWORD"));
						comp.setEmail(result.getString("EMAIL"));
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
		return comp;
	}

	/**
	 * This "GET" method search for all objects from same "family" and return a collection of objects.
	 * @return Collection.
	 * @throws GeneralCouponSystemException
	 */
	@Override
	public Collection<Company> getAllCompanies() throws GeneralCouponSystemException {
		Connection con = null;
		Set<Company> companies = new HashSet<>();
		try {
			con = pool.getConnections();
			Statement stmt = con.createStatement();
			ResultSet result = stmt.executeQuery("SELECT * FROM company");
			while (result.next() == true) {
				Company com = new Company();
				com.setId(result.getLong("ID"));
				com.setCompName(result.getString("COMP_NAME"));
				com.setPassword(result.getString("PASSWORD"));
				com.setEmail(result.getString("EMAIL"));
				companies.add(com);
			}

		} catch (SQLException e) {
			GeneralCouponSystemException message = new GeneralCouponSystemException(
					"Couldn't Get Data From DataBase, Data Not Exist Or Invaild ");
			throw message;
		} finally {
			if (con != null) {
				pool.returnConnection(con);
			}
		}
		return companies;
	}

	/**
	 * This "GET" method search for all objects from same "family" and return a collection of objects.
	 * @return Collection.
	 * @throws GeneralCouponSystemException
	 */
	@Override
	public Collection<Coupon> getCoupons() throws GeneralCouponSystemException {
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
			e.printStackTrace();
			GeneralCouponSystemException message = new GeneralCouponSystemException(
					"Couldn't Get Data From DataBase, Data Not Exist Or Invaild ");
			throw message;
		} finally {
			if (con != null) {
				pool.returnConnection(con);
			}
		}
		return coupons;
	}

	
	/**
	 * This LOGIN method takes the parameters that insert into and search in the data base for objent in same "family" whit same parameters, and if it find, it will return TRUE for the login.
	 * @param compName - Company Name.
	 * @param password - Company Password.
	 * @return Boolean
	 * @throws GeneralCouponSystemException
	 */
	@Override
	public Boolean login(String compName, String password) throws GeneralCouponSystemException {
		Connection con = null;
		try {
			con = pool.getConnections();
			Statement stmt = con.createStatement();
			ResultSet result = stmt.executeQuery("SELECT * FROM company");
			if (result != null) {
				while (result.next()) {
					if (result.getString("PASSWORD").equals(password)
							&& result.getString("COMP_NAME").equals(compName)) {
						return true;
					}
				}
				GeneralCouponSystemException message = new GeneralCouponSystemException(
						"Wrong Password or Username. Try again later!");
				throw message;
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
		return null;
	}

}