package DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import CouponSystemException.GeneralCouponSystemException;

public class DBSetUp {
	/**
	 * This class represent Main class That you might run one time and it will build The DATA BASE in (Apache derby),
	 * with 5 Tables: company, customer, coupon, company_coupon, customer_coupon.
	 * comapny_coupon is inner join table of company ID and coupon ID of the coupons that the company made.
	 * customer_coupon is inner join table of customer ID and coupon ID of the coupons that customer purchase.
	 * @param args
	 * @throws GeneralCouponSystemException
	 */
	public static void main(String[] args) throws GeneralCouponSystemException{
		String url = "jdbc:derby://localhost:1527/CouponSystemDB; create=true;";

		try (Connection con = DriverManager.getConnection(url);) {
			Statement stmt = con.createStatement();
			String sql = "CREATE TABLE Company(ID BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), COMP_NAME varchar(50),PASSWORD varchar(50),EMAIL varchar(50))";
			stmt.executeUpdate(sql);
			
			sql = "CREATE TABLE Customer(ID BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), CUST_NAME varchar(50), PASSWORD varchar(50))";
			stmt.executeUpdate(sql);
			
			sql = "CREATE TABLE Coupon(ID BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), TITLE varchar(50), START_DATE DATE, END_DATE DATE, AMOUNT INTEGER, TYPE varchar(50), DESCRIPTION varchar(100), PRICE FLOAT, IMAGE varchar(255))";
			stmt.executeUpdate(sql);
			
			sql = "CREATE TABLE Company_Coupon(COMPANY_ID BIGINT, COUPON_ID BIGINT, PRIMARY KEY(COMPANY_ID, COUPON_ID))";
			stmt.executeUpdate(sql);
			
			sql = "CREATE TABLE Customer_Coupon(CUSTOMER_ID BIGINT, COUPON_ID BIGINT, PRIMARY KEY(CUSTOMER_ID, COUPON_ID))";
			stmt.executeUpdate(sql);
			
		} catch (SQLException e) {
			e.printStackTrace();
			GeneralCouponSystemException gcs = new GeneralCouponSystemException("Data base Create Error / Data base allrady exist");
			throw gcs;
		}

	}
}
