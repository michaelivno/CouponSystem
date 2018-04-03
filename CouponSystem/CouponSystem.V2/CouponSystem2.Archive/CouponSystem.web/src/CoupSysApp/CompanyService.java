package CoupSysApp;

import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import CouponSystem.CouponSystem;
import CouponSystemBeans.Company;
import CouponSystemBeans.Coupon;
import CouponSystemBeans.CouponType;
import CouponSystemException.GeneralCouponSystemException;
import Facades.CompanyFacade;
import Facades.CouponClientFacade.ClientType;

@Path("/company")
public class CompanyService {

	public CompanyService() {

	}

	public CompanyFacade getFacafeFromSession(HttpServletRequest request) {
		HttpSession session = request.getSession();
		CompanyFacade company = (CompanyFacade) session.getAttribute("company");
		return company;
	}

	@Path("/coupon")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Message createCoupon(Coupon coupon, @Context HttpServletRequest request)
			throws GeneralCouponSystemException {
		CompanyFacade companyFacade = getFacafeFromSession(request);
		companyFacade.createCoupon(coupon);
		return new Message("coupon with id: " + coupon.getId() + " hes been created.");
	}

	@Path("/coupon/{id}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Message removeCoupon(@PathParam("id") Long id, @Context HttpServletRequest request)
			throws GeneralCouponSystemException {
		CompanyFacade companyFacade = getFacafeFromSession(request);
		companyFacade.removeCouponById(id);
		return new Message("coupon with id: " + id + "hes been removed.");
	}

	@Path("/coupon/{id}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Message updateCoupon(@PathParam("id")Long id, Coupon coupon, @Context HttpServletRequest request)
			throws GeneralCouponSystemException {
			if (id == null || !id.equals(coupon.getId())) {
				throw new GeneralCouponSystemException("id must match coupon id");
			}
		CompanyFacade companyFacade = getFacafeFromSession(request);
		companyFacade.updateCoupon(coupon);
		return new Message("coupon with id:" + coupon.getId() + " hes been update");
	}

	@Path("/coupon/id/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Coupon getCouponById(@PathParam("id") Long id, @Context HttpServletRequest request)
			throws GeneralCouponSystemException {
		CompanyFacade companyFacade = getFacafeFromSession(request);
		return companyFacade.getCoupon(id);
	}

	@Path("/coupon")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Coupon[] getAllcoupons(@Context HttpServletRequest request) throws GeneralCouponSystemException {
		CompanyFacade companyFacade = getFacafeFromSession(request);
		Collection<Coupon> coupColl = companyFacade.getAllCompanyCoupons();
		Coupon[] coupons = coupColl.toArray(new Coupon[coupColl.size()]);
		return coupons;
	}

	@Path("/coupon/type/{type}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Coupon[] grtAllcouponsByType(@PathParam("type") CouponType type,
			@Context HttpServletRequest request) throws GeneralCouponSystemException {
		CompanyFacade companyFacade = getFacafeFromSession(request);
		Collection<Coupon> coupColl = companyFacade.getCouponByType(type);
		Coupon[] couponsByType = coupColl.toArray(new Coupon[coupColl.size()]);
		return couponsByType;
	}
	
	@Path("/coupon/price/{price}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Coupon[] grtAllcouponsByPrice(@PathParam("price") long price,
			@Context HttpServletRequest request) throws GeneralCouponSystemException {
		CompanyFacade companyFacade = getFacafeFromSession(request);
		Collection<Coupon> coupColl = companyFacade.getCouponByPrice(price);
		Coupon[] couponsByPrice = coupColl.toArray(new Coupon[coupColl.size()]);
		return couponsByPrice;
	}

	@Path("/coupon/date/{date}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Coupon[] getCouponsByDate(@PathParam("date")Date date, @Context HttpServletRequest request)
			throws GeneralCouponSystemException {
		CompanyFacade companyFacade = getFacafeFromSession(request);
		Collection<Coupon> dateCoup = companyFacade.getCouponByDate(date);
		Coupon[] couponsByDate = dateCoup.toArray(new Coupon[dateCoup.size()]);
		return couponsByDate ;

	}

	@Path("/info")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Company getUsingCompanyInf(@Context HttpServletRequest request) {
		CompanyFacade companyFacade = getFacafeFromSession(request);
		return companyFacade.getUsingFacafeCompany();
	}

	@Path("/login/{username}/{password}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Message login(@PathParam("username") String username, @PathParam("password") String password,
			@Context HttpServletRequest request) throws GeneralCouponSystemException {
		CompanyFacade companyFacade = (CompanyFacade) CouponSystem.getInstance().login(username, password,
				ClientType.COMPANY);
		if (companyFacade != null) {
			HttpSession session = request.getSession(true);
			session.setAttribute("company", companyFacade);
			return new Message(session.getId());
		}
		return new Message("Error login");
	}

}
