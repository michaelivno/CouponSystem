package CoupSysApp;

import java.util.Collection;

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
import CouponSystemBeans.Customer;
import CouponSystemException.GeneralCouponSystemException;
import Facades.AdminFacade;
import Facades.CouponClientFacade.ClientType;

@Path("/admin")
public class AdminService {

	public AdminService() {
	}

	private AdminFacade getFacadeFromSession(HttpServletRequest request) {
		HttpSession session = request.getSession();
		AdminFacade admin = (AdminFacade) session.getAttribute("admin");
		return admin;
	}

	@Path("/company")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Message createCompany(Company company, @Context HttpServletRequest request) throws GeneralCouponSystemException {
			AdminFacade adminFacade = getFacadeFromSession(request);
			adminFacade.createCompany(company);
		return new Message("company"+company.getCompName()+ "was created!");
	}

	@Path("/company/{id}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Message removeCompanyById(@PathParam("id") Long id, @Context HttpServletRequest request) throws GeneralCouponSystemException {
			AdminFacade adminFacade = getFacadeFromSession(request);
			adminFacade.removeCompany(id);
		return new Message("Company whit id: " + id + " is been remove");
	}

	@Path("/company/{id}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Message updateCompany(@PathParam("id") Long id, Company company, @Context HttpServletRequest request) throws GeneralCouponSystemException {
			if (id == null || !id.equals(company.getId())) {
				throw new GeneralCouponSystemException("id must match copany id");
			}
			AdminFacade adminFacade = getFacadeFromSession(request);
			adminFacade.updateCompany(company);
		return new Message("Update company:" + company.getCompName());
	}

	@Path("/company/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Company getCompanyById(@PathParam("id") Long id, @Context HttpServletRequest request)
			throws GeneralCouponSystemException {
		AdminFacade adminFacade = getFacadeFromSession(request);
		if (id == null) {
			throw new GeneralCouponSystemException("id must match coupon id");
		}
		Company company = adminFacade.getCompany(id);
		return company;
	}

	@Path("/company")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Company[] getAllCompanys(@Context HttpServletRequest request) throws GeneralCouponSystemException {
		AdminFacade adminFacade = getFacadeFromSession(request);
		Collection<Company> CompColl = adminFacade.getaAllCompanys();
		Company Companies[] = CompColl.toArray(new Company[CompColl.size()]);
		return Companies;
	}

	@Path("/customer")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Message createCustomer(Customer customer, @Context HttpServletRequest request)
			throws GeneralCouponSystemException {
			AdminFacade adminFacade = getFacadeFromSession(request);
			adminFacade.createCustomer(customer);
		return new Message("customer: " + customer.getCustName() + " hes been create");
	}

	@Path("/customer/{id}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Message removeCustomer(@PathParam("id") Long id, @Context HttpServletRequest request)
			throws GeneralCouponSystemException {
		AdminFacade adminFacade = getFacadeFromSession(request);
		adminFacade.removeCustomer(id);
		return new Message("customer with " + id + " is been removed!");
	}

	@Path("/customer/{id}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Message updateCustomer(@PathParam("id") Long id, Customer customer, @Context HttpServletRequest request)
			throws GeneralCouponSystemException {
			if (id == null || !id.equals(customer.getId())) {
				throw new GeneralCouponSystemException("id must match customer id");
			}
			AdminFacade adminFacade = getFacadeFromSession(request);
			adminFacade.updateCustomer(customer);
		return new Message("customer:" + customer.getCustName() + " was update.");
	}

	@Path("/customer/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Customer getCustomerById(@PathParam("id") Long id, @Context HttpServletRequest request)
			throws GeneralCouponSystemException {
		AdminFacade adminFacade = getFacadeFromSession(request);
		return adminFacade.getCustomer(id);
	}

	@Path("/customer")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Customer[] getAllCustomers(@Context HttpServletRequest request) throws GeneralCouponSystemException {
		AdminFacade adminFacade = getFacadeFromSession(request);
		Collection<Customer> custColl = adminFacade.getAllCustomers();
		Customer customers[] = custColl.toArray(new Customer[custColl.size()]);
		return customers;
	}

	@Path("/login/{username}/{password}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Message Login(@PathParam("username") String username, @PathParam("password") String password,
			@Context HttpServletRequest request) throws GeneralCouponSystemException {
		AdminFacade adminFacade = (AdminFacade) CouponSystem.getInstance().login(username, password, ClientType.ADMIN);
		if (adminFacade != null) {
			HttpSession session = request.getSession(true);
			session.setAttribute("admin", adminFacade);
			return new Message("Successfully login");
		}
		return new Message("Error login");
	}

	@Path("/logout")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Message Logout(@Context HttpServletRequest request) throws GeneralCouponSystemException{
  		HttpSession session = request.getSession(false);
		session.invalidate();
		return new Message("logout complite");
	}
	
}
