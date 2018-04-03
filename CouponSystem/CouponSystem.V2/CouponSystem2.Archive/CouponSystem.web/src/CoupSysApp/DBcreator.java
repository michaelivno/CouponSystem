package CoupSysApp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import CouponSystem.CouponSystem;
import CouponSystemBeans.Company;
import CouponSystemBeans.Coupon;
import CouponSystemBeans.CouponType;
import CouponSystemBeans.Customer;
import CouponSystemException.GeneralCouponSystemException;
import Facades.AdminFacade;
import Facades.CompanyFacade;
import Facades.CouponClientFacade.ClientType;


public class DBcreator {
	public static void main(String[] args) throws GeneralCouponSystemException {
		String url = "jdbc:derby://localhost:1527/CouponSystemDB; create=true;";
		CouponSystem cs = CouponSystem.getInstance();
		AdminFacade adminFacade;
		CompanyFacade companyFacade;

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
			GeneralCouponSystemException message = new GeneralCouponSystemException("Data base Create Error / Data base allrady exist");
		throw message;
		}

		
		Customer customer1=new Customer("michael", "123");
		Customer customer2=new Customer("eyal", "eyal2");
		Customer customer3=new Customer("nastya", "m123");
		Company company1=new Company("Bitcoin","btc","btc@bitcoin.com");
		Company company2=new Company("Sony","sony123","sony@sony.com");
		Company company3=new Company("LG","123","lg@gmail.com");
		Coupon coupon1=new Coupon("Milk", 2018, 8, 16, 10, CouponType.FOOD, "message", 6.99, "https://cdn.shopify.com/s/files/1/0206/9470/products/southcoast-milk-1l_1024x1024.jpg?v=1494139427");
		Coupon coupon2=new Coupon("Computer", 2019, 1, 22, 10, CouponType.ELECTRICITY, "message", 359.99, "https://media.gcflearnfree.org/content/55e0730c7dd48174331f5164_01_17_2014/desktop_full_view_alt.jpg");
		Coupon coupon3=new Coupon("Boots", 2022, 9, 1, 5, CouponType.SPORTS, "message", 100, "https://cdn.shopify.com/s/files/1/0419/1525/products/mens-natural-leather-president-boot-1_large.jpg?v=1520452299");
		Coupon coupon4=new Coupon("Vacation", 2020, 9, 25, 3, CouponType.TRAVELLING, "message", 199.99, "https://www.staypromo.com/wp-content/uploads/2017/10/vacation.jpg");
		Coupon coupon5=new Coupon("Vitamins", 2019, 2, 12, 7, CouponType.HEALTH, "message", 19, "https://www.tutorialspoint.com/biology_part2/images/vitamins.jpg");
		Coupon coupon6=new Coupon("Tent", 2019, 1, 11, 7, CouponType.CAMPING, "message", 69.90, "http://defd230db96761500ca7-61c6d5aeae250d28854ed3e240a16b15.r17.cf3.rackcdn.com/Products/47380-190116165002301331578.jpg");
		
		

		
		
		try {
			adminFacade= (AdminFacade) cs.login("admin", "1234", ClientType.ADMIN);
			
			adminFacade.createCustomer(customer1);
			adminFacade.createCustomer(customer2);
			adminFacade.createCustomer(customer3);
			
			adminFacade.createCompany(company1);
			adminFacade.createCompany(company2);
			adminFacade.createCompany(company3);
			
			companyFacade= (CompanyFacade) cs.login("Sony", "sony123", ClientType.COMPANY);
			companyFacade.createCoupon(coupon1);
			companyFacade.createCoupon(coupon2);
			companyFacade= (CompanyFacade) cs.login("Bitcoin", "btc", ClientType.COMPANY);
			companyFacade.createCoupon(coupon3);
			companyFacade.createCoupon(coupon4);
			companyFacade= (CompanyFacade) cs.login("LG", "123", ClientType.COMPANY);
			companyFacade.createCoupon(coupon5);
			companyFacade.createCoupon(coupon6);
			System.out.println("Database created");

			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		
	}
		
	}

