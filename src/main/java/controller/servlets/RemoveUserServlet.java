package controller.servlets;

import factory.UserServiceFactory;
import service.UserService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(value = "/users/remove")
public class RemoveUserServlet extends HttpServlet {

    private static final UserService userService = UserServiceFactory.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        long id = Long.valueOf(req.getParameter("id"));
        userService.removeUser(id);
        resp.sendRedirect("/users");
    }
}
