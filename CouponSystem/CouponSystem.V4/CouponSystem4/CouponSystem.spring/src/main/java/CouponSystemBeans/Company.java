package CouponSystemBeans;

import java.util.Collection;
/**
 * Simple been class that represent company object. 
 * @author michael
 *
 */
public class Company {
	
	private long id;
	private String compName;
	private String password;
	private String email;
	private Collection<Coupon> coupons;
	
	
	public Company() {
		super();
	}

/**
 * company object constructor.
 * @param compName - company name must be specific for each company.
 * @param password - company password.
 * @param email - company email.
 */
	public Company(String compName, String password, String email) {
		this.compName = compName;
		this.password = password;
		this.email = email;
		
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getCompName() {
		return compName;
	}


	public void setCompName(String compName) {
		this.compName = compName;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public Collection<Coupon> getCoupons() {
		return coupons;
	}


	public void setCoupons(Collection<Coupon> coupons) {
		this.coupons = coupons;
	}


	@Override
	public String toString() {
		return "Company [id=" + id + ", compName=" + compName + ", password=" + password + ", email=" + email+"]";
	}
	
}
	
	