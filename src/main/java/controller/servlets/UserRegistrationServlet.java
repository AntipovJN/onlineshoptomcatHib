package controller.servlets;

import factory.UserServiceFactory;
import service.UserService;
import sun.security.provider.SHA;
import utils.SHA256StringHashUtil;
import utils.SaltGeneratorUtil;

import javax.security.auth.login.LoginException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(value = "/register")
public class UserRegistrationServlet extends HttpServlet {

    private static final UserService userService = UserServiceFactory.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String salt = SaltGeneratorUtil.getSalt();
        String email = req.getParameter("email");
        String password = SHA256StringHashUtil.getSha256(SaltGeneratorUtil.
                saltPassword(req.getParameter("password"), salt));
        String repeatPassword = SaltGeneratorUtil.saltPassword(
                SHA256StringHashUtil.getSha256(req.getParameter("repeatPassword")),
                SHA256StringHashUtil.getSha256(salt));
        String role = req.getParameter("role");
        try {

            userService.addUser(email, password, repeatPassword, role, salt);
            resp.sendRedirect("/users");
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("email", email);
            req.getRequestDispatcher("register.jsp").forward(req, resp);
        } catch (LoginException e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("register.jsp").forward(req, resp);
        }
    }
}
