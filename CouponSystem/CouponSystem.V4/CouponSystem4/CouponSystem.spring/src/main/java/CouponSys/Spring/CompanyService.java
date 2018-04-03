package CouponSys.Spring;

import CouponSystem.CouponSystem;
import CouponSystemBeans.Company;
import CouponSystemBeans.Coupon;
import CouponSystemBeans.CouponType;
import CouponSystemException.GeneralCouponSystemException;
import Facades.CompanyFacade;
import Facades.CouponClientFacade;

import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class CompanyService
{
  public CompanyService() {}
  
  public CompanyFacade getFacafeFromSession(HttpSession session)
  {
    CompanyFacade company = (CompanyFacade)session.getAttribute("company");
    return company;
  }
  
  @RequestMapping(path={"/api/company/coupon"}, method={RequestMethod.POST},consumes={"application/json"})
  public Message createCoupon(@RequestBody Coupon coupon, HttpSession session) throws GeneralCouponSystemException {
    CompanyFacade companyFacade = getFacafeFromSession(session);
    companyFacade.createCoupon(coupon);
    return new Message("coupon with id: " + coupon.getId() + " hes been created.");
  }
  
  @RequestMapping(path={"/api/company/coupon/{id}"}, method={RequestMethod.DELETE})
  public Message removeCoupon(@PathVariable("id") Long id, HttpSession session) {
    try {
      CompanyFacade companyFacade = getFacafeFromSession(session);
      companyFacade.removeCouponById(id);
      return new Message("coupon with id: " + id + "hes been removed.");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  @RequestMapping(path={"/api/company/coupon/{id}"}, method={RequestMethod.PUT}, consumes={"application/json"})
  public Message updateCoupon(@PathVariable("id") Long id, @RequestBody Coupon coupon, HttpSession session) {
    try {
      CompanyFacade companyFacade = getFacafeFromSession(session);
      companyFacade.updateCoupon(coupon);
      return new Message("coupon with id:" + coupon.getId() + " hes been update");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  @RequestMapping(path={"/api/company/coupon/id/{id}"}, method={RequestMethod.GET}, produces={"application/json"})
  public Coupon getCouponById(@PathVariable("id") Long id, HttpSession session) {
    try {
      CompanyFacade companyFacade = getFacafeFromSession(session);
      return companyFacade.getCoupon(id);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  @RequestMapping(path={"/api/company/coupon"}, method={RequestMethod.GET}, produces={"application/json"})
  public Coupon[] getAllcoupons(HttpSession session) {
    try {
      CompanyFacade companyFacade = getFacafeFromSession(session);
      Collection<Coupon> coupColl = companyFacade.getAllCompanyCoupons();
      return (Coupon[])coupColl.toArray(new Coupon[coupColl.size()]);
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  @RequestMapping(path={"/api/company/coupon/type/{type}"}, method={RequestMethod.GET}, produces={"application/json"})
  public Coupon[] grtAllcouponsByType(@PathVariable("type") CouponType type, HttpSession session) {
    try {
      CompanyFacade companyFacade = getFacafeFromSession(session);
      Collection<Coupon> coupColl = companyFacade.getCouponByType(type);
      return (Coupon[])coupColl.toArray(new Coupon[coupColl.size()]);
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  @RequestMapping(path={"/api/company/coupon/date/{date}"}, method={RequestMethod.GET}, produces={"application/json"})
  public Coupon[] getCouponsByDate(@PathVariable("date") Date date, HttpSession session) {
    try {
      CompanyFacade companyFacade = getFacafeFromSession(session);
      Collection<Coupon> dateCoup = companyFacade.getCouponByDate(date);
      return (Coupon[])dateCoup.toArray(new Coupon[dateCoup.size()]);
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  @RequestMapping(path={"/api/company/coupon/price/{price}"}, method={RequestMethod.GET}, produces={"application/json"})
	public Coupon[] grtAllcouponsByPrice(@PathVariable("price") long price,
			HttpSession session) {
	  try {
		  CompanyFacade companyFacade = getFacafeFromSession(session);
		  Collection<Coupon> coupColl = companyFacade.getCouponByPrice(price);
		  Coupon[] couponsByPrice = coupColl.toArray(new Coupon[coupColl.size()]);
		  return couponsByPrice;
	} catch (Exception e) {
		throw new RuntimeException(e);
	}
	}

  
  @RequestMapping(path={"/api/company/info"}, method={RequestMethod.GET}, produces={"application/json"})
  public Company getUsingCompanyInf(HttpSession session) {
    try {
      CompanyFacade companyFacade = getFacafeFromSession(session);
      return companyFacade.getUsingFacafeCompany();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  @RequestMapping(path={"/api/company/login/{username}/{password}"}, method={RequestMethod.GET})
  public Message login(@PathVariable("username") String username, @PathVariable("password") String password, HttpSession session)
  {
    try {
      CompanyFacade companyFacade = (CompanyFacade)CouponSystem.getInstance().login(username, password,CouponClientFacade.ClientType.COMPANY);
      if (companyFacade != null) {
        session.setAttribute("company", companyFacade);
        return new Message(session.getId());
      }
      return new Message("Error login");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
