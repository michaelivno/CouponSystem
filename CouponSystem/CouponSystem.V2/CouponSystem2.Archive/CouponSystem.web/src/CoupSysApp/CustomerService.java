package CoupSysApp;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import CouponSystem.CouponSystem;
import CouponSystemBeans.Coupon;
import CouponSystemBeans.CouponType;
import CouponSystemException.GeneralCouponSystemException;
import Facades.CouponClientFacade.ClientType;
import Facades.CustomerFacade;

@Path("/customer")
public class CustomerService {

	

	private CustomerFacade getFacadeFromSession(HttpServletRequest request) {
		HttpSession session = request.getSession();
		CustomerFacade customer = (CustomerFacade) session.getAttribute("customer");
		return customer;
	}

	@Path("/coupons")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Coupon[] getAllCoup(@Context HttpServletRequest request) throws GeneralCouponSystemException {
		CustomerFacade customerFacade = getFacadeFromSession(request);
	Collection<Coupon> allCoup = customerFacade.getAllCoupons();
		 Coupon[] arrCoup = allCoup.toArray(new Coupon[allCoup.size()]);
		return arrCoup;
	}

	@Path("/coupons")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Message purchaseCoupon(Long id, @Context HttpServletRequest request) throws GeneralCouponSystemException {
		CustomerFacade customerFacade = getFacadeFromSession(request);
		customerFacade.purchaseCouponById(id);
		return new Message("coupon with: " + id + " id is purchase");
	}

	@Path("/coupons/purch")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Coupon[] getAllPurchasedCoupons(@Context HttpServletRequest request) throws GeneralCouponSystemException {
		CustomerFacade customerFacade = getFacadeFromSession(request);
		Collection<Coupon> purCoup = customerFacade.getAllPurchaseCoupons();
		Coupon[] purchaseCoupons = purCoup.toArray(new Coupon[purCoup.size()]);
		return purchaseCoupons;
	}

	@Path("/coupons/type/{type}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Coupon[] getAllPurchasedCouponsByType(@PathParam("type") CouponType type,
			@Context HttpServletRequest request) throws GeneralCouponSystemException {
		CustomerFacade customerFacade = getFacadeFromSession(request);
		Collection<Coupon> coup = customerFacade.getAllPurchaseCouponsByType(type);
		Coupon[] typeCoupons = coup.toArray(new Coupon[coup.size()]);
		return typeCoupons;
	}

	@Path("/coupons/price/{price}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Coupon[] getAllPurchasedCouponsByPrice(@PathParam("price") Double price, @Context HttpServletRequest request)
			throws GeneralCouponSystemException {
		CustomerFacade customerFacade = getFacadeFromSession(request);
		Collection<Coupon> priceCoup = customerFacade.getAllPurchaseCouponsByPrice(price);
		Coupon[] typeCoupons = priceCoup.toArray(new Coupon[priceCoup.size()]);
		return typeCoupons;
	}

	@Path("login/{username}/{password}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Message Login(@PathParam("username") String username, @PathParam("password") String password,
			@Context HttpServletRequest request) throws GeneralCouponSystemException {
		CustomerFacade customerFacade = (CustomerFacade) CouponSystem.getInstance().login(username, password,
				ClientType.CUSTOMER);
		if (customerFacade != null) {
			HttpSession session = request.getSession(true);
			session.setAttribute("customer", customerFacade);
			return new Message(session.getId());
		}
		return new Message("Error login");
	}
}
