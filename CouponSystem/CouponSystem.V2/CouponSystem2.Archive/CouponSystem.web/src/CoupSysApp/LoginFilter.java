package CoupSysApp;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@WebFilter("/api/*")
public class LoginFilter implements Filter {


	
	public void init(FilterConfig fConfig) throws ServletException {
	}
	
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		String path = request.getPathInfo();
		HttpSession session = request.getSession(false);
		if (path.contains("/login/")) {
            chain.doFilter(req, res);
            return;
        }

        if (path.contains("/logout")) {
            chain.doFilter(req, res);
            return;
        }

        if (session == null) {
            throw new ServletException("You are not logged in.");
        }

        if (path.startsWith("/company")) {
            if (session.getAttribute("company") != null) {
                chain.doFilter(req, res);
                return;
            } else {
                throw new ServletException("Access denied.");
            }
        }

        if (path.startsWith("/customer")) {
            if (session.getAttribute("customer") != null) {
                chain.doFilter(req, res);
                return;
            } else {
                throw new ServletException("Access denied.");
            }
        }

        if (path.startsWith("/admin")) {
            if (session.getAttribute("admin") != null) {
                chain.doFilter(req, res);
            } else {
                throw new ServletException("Access denied.");
            }
        }
    }

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}


}
