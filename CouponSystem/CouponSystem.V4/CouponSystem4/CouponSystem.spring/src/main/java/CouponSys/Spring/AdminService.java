package CouponSys.Spring;

import CouponSystem.CouponSystem;
import CouponSystemBeans.Company;
import CouponSystemBeans.Customer;
import Facades.AdminFacade;
import Facades.CouponClientFacade;

import java.util.Collection;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class AdminService
{
  public AdminService() {}
  
  @RequestMapping(path={"/api/admin/login/{username}/{password}"}, method={RequestMethod.GET})
  public Message Login(@PathVariable("username") String username, @PathVariable("password") String password, HttpSession session)
  {
    try
    {
      AdminFacade adminFacade = (AdminFacade)CouponSystem.getInstance().login(username, password,CouponClientFacade.ClientType.ADMIN);
      if (adminFacade != null) {
        session.setAttribute("admin", adminFacade);
        return new Message(session.getId());
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return new Message("Error login");
  }
  
  @RequestMapping(path={"/api/admin/company"}, method={RequestMethod.POST}, produces={"application/json"}, consumes={"application/json"})
  public Company createCompany(@RequestBody Company company, HttpSession session) {
    try {
      AdminFacade adminFacade = getFacadeFromSession(session);
      adminFacade.createCompany(company);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return company;
  }
  
  @RequestMapping(path={"/api/admin/company/{id}"}, method={RequestMethod.DELETE})
  public Message removeCompanyById(@PathVariable("id") Long id, HttpSession session) {
    try {
      AdminFacade adminFacade = getFacadeFromSession(session);
      adminFacade.removeCompany(id);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return new Message("Company whit id: " + id + " as been remove");
  }
  
  @RequestMapping(path={"/api/admin/company/{id}"}, method={RequestMethod.PUT}, consumes={"application/json"})
  public Message updateCompany(@PathVariable("id")Long id, @RequestBody Company company, HttpSession session) {
    try {
    	if (id == null || !id.equals(company.getId())) {
			throw new RuntimeException("id must match copany id");
		}
      AdminFacade adminFacade = getFacadeFromSession(session);
      adminFacade.updateCompany(company);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return new Message("Update company:" + company.getCompName());
  }
  
  @RequestMapping(path={"/api/admin/company/{id}"}, method={RequestMethod.GET}, produces={"application/json"})
  public Company getCompanyById(@PathVariable("id") Long id, HttpSession session) {
    try {
      AdminFacade adminFacade = getFacadeFromSession(session);
      if (id == null) {
        throw new RuntimeException("id must match coupon id");
      }
      return adminFacade.getCompany(id);
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  @RequestMapping(path={"/api/admin/company"}, method={RequestMethod.GET}, produces={"application/json"})
  public Company[] getAllCompanys(HttpSession session) {
    try {
      AdminFacade adminFacade = getFacadeFromSession(session);
      Collection<Company> CompColl = adminFacade.getaAllCompanys();
      return (Company[])CompColl.toArray(new Company[CompColl.size()]);
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  @RequestMapping(path={"/api/admin/customer"}, method={RequestMethod.POST}, consumes={"application/json"})
  public Message createCustomer(@RequestBody Customer customer, HttpSession session) {
    try {
      AdminFacade adminFacade = getFacadeFromSession(session);
      adminFacade.createCustomer(customer);
      return new Message("customer: " + customer.getCustName() + " hes been create");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  @RequestMapping(path={"/api/admin/customer/{id}"}, method={RequestMethod.DELETE})
  public Message removeCustomer(@PathVariable("id") Long id, HttpSession session) {
    try {
      AdminFacade adminFacade = getFacadeFromSession(session);
      adminFacade.removeCustomer(id.longValue());
      return new Message("customer with " + id + " is been removed!");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  @RequestMapping(path={"/api/admin/customer/{id}"}, method={RequestMethod.PUT}, consumes={"application/json"})
  public Message updateCustomer(@PathVariable("id")Long id,@RequestBody Customer customer, HttpSession session) {
    try {
    	if (id == null || !id.equals(customer.getId())) {
			throw new RuntimeException("id must match copany id");
		}
      AdminFacade adminFacade = getFacadeFromSession(session);
      adminFacade.updateCustomer(customer);
      return new Message("customer:" + customer.getCustName() + " was update.");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  @RequestMapping(path={"/api/admin/customer/{id}"}, method={RequestMethod.GET}, produces={"application/json"})
  public Customer getCustomerById(@PathVariable("id") Long id, HttpSession session) {
    try {
      AdminFacade adminFacade = getFacadeFromSession(session);
      return adminFacade.getCustomer(id);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  @RequestMapping(path={"/api/admin/customer"}, method={RequestMethod.GET}, produces={"application/json"})
  public Customer[] getAllCustomers(HttpSession session) {
    try {
      AdminFacade adminFacade = getFacadeFromSession(session);
      Collection<Customer> custColl = adminFacade.getAllCustomers();
      Customer[] customers = (Customer[])custColl.toArray(new Customer[custColl.size()]);
      return customers;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  @RequestMapping(path={"/api/admin/logout"}, method={RequestMethod.GET})
  public Message Logout(HttpSession session) {
    try {
      session.invalidate();
      return new Message("logout complite");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  private AdminFacade getFacadeFromSession(HttpSession session) {
    AdminFacade admin = (AdminFacade)session.getAttribute("admin");
    if (admin == null) {
      throw new RuntimeException("You are not logged in...");
    }
    return admin;
  }
  
  @ExceptionHandler
  @ResponseStatus(code=HttpStatus.INTERNAL_SERVER_ERROR)
  public Message handleException(Exception e) {
    String message = e.getMessage();
    if (message == null) {
      message = "Error while invoking request";
    }
    
    return new Message(message);
  }
}
