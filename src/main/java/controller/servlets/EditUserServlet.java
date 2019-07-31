package controller.servlets;

import factory.UserServiceFactory;
import model.User;
import service.UserService;
import utils.SHA256StringHashUtil;

import javax.security.auth.login.LoginException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(value = "/users/edit")
public class EditUserServlet extends HttpServlet {

    private static final UserService userService = UserServiceFactory.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        long id = Long.valueOf(req.getParameter("id"));
        if (userService.getById(id).isPresent()) {
            User user = userService.getById(id).get();
            req.setAttribute("id", id);
            req.setAttribute("email", user.getEmail());
            req.getRequestDispatcher("/edit_user.jsp").forward(req, resp);
        } else {
            resp.sendRedirect("/users");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Long id = Long.valueOf(req.getParameter("id"));
        String email = req.getParameter("email");
        String password = SHA256StringHashUtil.getSha256(req.getParameter("password"));
        String passwordAgain = SHA256StringHashUtil.getSha256(req.getParameter("repeatPassword"));
        try {
            userService.updateUser(id, email, password, passwordAgain);
            resp.sendRedirect("/users");
        } catch (IllegalArgumentException | LoginException e) {
            req.setAttribute("error", e.getMessage());
            doGet(req, resp);
        }
    }
}
