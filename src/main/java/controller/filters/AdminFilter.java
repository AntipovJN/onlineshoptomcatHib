package controller.filters;

import model.User;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@WebFilter(value = {"/users/*", "/products/add", "/products/edit"})
public class AdminFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        User user = (User) request.getSession().getAttribute("user");
        if (Objects.isNull(user)) {
            response.sendRedirect("/");
        } else {
            request.getSession().setAttribute("isLogin", true);
            if (user.getRole().equals("user")) {
                request.getSession().removeAttribute("isAdmin");
                response.sendRedirect("/products");
            } else {
                filterChain.doFilter(request, response);
                request.getSession().setAttribute("isAdmin", true);
            }
        }
    }

    @Override
    public void destroy() {

    }
}
