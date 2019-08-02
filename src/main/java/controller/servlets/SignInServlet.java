package controller.servlets;

import factory.UserServiceFactory;
import model.User;
import service.UserService;
import utils.SHA256StringHashUtil;
import utils.SaltGeneratorUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet(value = "/")
public class SignInServlet extends HttpServlet {

    private static final UserService userService = UserServiceFactory.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String email = req.getParameter("email");
        Optional<User> optionalUser = userService.getByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String password = SHA256StringHashUtil.getSha256(SaltGeneratorUtil.saltPassword(
                    req.getParameter("password"), user.getSalt()));
            if (user.getPassword().equals(password)) {
                req.getSession().setAttribute("user", user);
                resp.sendRedirect("/users");
            } else {
                req.setAttribute("error", "Incorrect login or password");
                req.getRequestDispatcher("index.jsp").forward(req, resp);
            }
        } else {
            req.setAttribute("error", "Incorrect login or password");
            req.getRequestDispatcher("index.jsp").forward(req, resp);
        }
    }
}
