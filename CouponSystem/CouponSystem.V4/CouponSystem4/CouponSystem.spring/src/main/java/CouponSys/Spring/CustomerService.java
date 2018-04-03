package CouponSys.Spring;

import java.util.Collection;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import CouponSystem.CouponSystem;
import CouponSystemBeans.Coupon;
import CouponSystemBeans.CouponType;
import Facades.CouponClientFacade;
import Facades.CustomerFacade;

@RestController
public class CustomerService {
	
	
	private CustomerFacade getFacadeFromSession(HttpSession session) {
		CustomerFacade customer = (CustomerFacade) session.getAttribute("customer");
		if (customer==null) {
			throw new RuntimeException("You are not logged in...");
		}
		return customer;
	}
	
	
	@RequestMapping(path={"/api/customer/login/{username}/{password}"}, method={RequestMethod.GET})
	  public Message Login(@PathVariable("username") String username, @PathVariable("password") String password, HttpSession session)
	  {
	    try
	    {
	      CustomerFacade customerFacade = (CustomerFacade)CouponSystem.getInstance().login(username, password,CouponClientFacade.ClientType.CUSTOMER);
	      if (customerFacade != null) {
	        session.setAttribute("customer", customerFacade);
	        return new Message(session.getId());
	      }
	    } catch (Exception e) {
	      throw new RuntimeException(e);
	    }
	    return new Message("Error login");
	  }

	@RequestMapping(path= {"/api/customer/coupons"}, method= {RequestMethod.GET}, produces={"application/json"})
	public Coupon[] getAllCoup(HttpSession session){
		try {
			CustomerFacade customerFacade = getFacadeFromSession(session);
			Collection<Coupon> allCoup = customerFacade.getAllCoupons();
			Coupon[] arrCoup = allCoup.toArray(new Coupon[allCoup.size()]);
			return arrCoup;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@RequestMapping(path= {"/api/customer/coupons"}, method= {RequestMethod.POST}, consumes={"application/json"})
	public Message purchaseCoupon(@RequestBody Long id, HttpSession session){
		try {
			CustomerFacade customerFacade = getFacadeFromSession(session);
			customerFacade.purchaseCouponById(id);
			return new Message("coupon with: " + id + " id is purchase");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@RequestMapping(path= {"/api/customer/coupons/purch"}, method= {RequestMethod.GET}, produces={"application/json"})
	public Coupon[] getAllPurchasedCoupons(HttpSession session){
		try {
			CustomerFacade customerFacade = getFacadeFromSession(session);
			Collection<Coupon> purCoup = customerFacade.getAllPurchaseCoupons();
			Coupon[] purchaseCoupons = purCoup.toArray(new Coupon[purCoup.size()]);
			return purchaseCoupons;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@RequestMapping(path= {"/api/customer/coupons/type/{type}"}, method= {RequestMethod.GET}, produces={"application/json"})
	public Coupon[] getAllPurchasedCouponsByType(@PathVariable("type") CouponType type,
			HttpSession session) {
		try {
			CustomerFacade customerFacade = getFacadeFromSession(session);
			Collection<Coupon> coup = customerFacade.getAllPurchaseCouponsByType(type);
			Coupon[] typeCoupons = coup.toArray(new Coupon[coup.size()]);
			return typeCoupons;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@RequestMapping(path= {"/api/customer/coupons/price/{price}"}, method= {RequestMethod.GET}, produces={"application/json"})
	public Coupon[] getAllPurchasedCouponsByPrice(@PathVariable("price") Double price, HttpSession session){
		try {
			CustomerFacade customerFacade = getFacadeFromSession(session);
			Collection<Coupon> priceCoup = customerFacade.getAllPurchaseCouponsByPrice(price);
			Coupon[] typeCoupons = priceCoup.toArray(new Coupon[priceCoup.size()]);
			return typeCoupons;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	
}

