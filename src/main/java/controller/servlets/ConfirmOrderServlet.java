package controller.servlets;

import factory.BasketServiceFactory;
import factory.MailServiceFactory;
import factory.OrderServiceFactory;
import model.Order;
import model.User;
import service.BasketService;
import service.MailService;
import service.OrderService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet(value = "/confirmOrder")
public class ConfirmOrderServlet extends HttpServlet {

    private static final OrderService orderService = OrderServiceFactory.getInstance();
    private static final BasketService basket_service = BasketServiceFactory.getInstance();
    private static final MailService mailService = MailServiceFactory.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        long id = Long.valueOf(req.getSession().getAttribute("orderId").toString());
        req.setAttribute("orderId", id);
        User user = (User) req.getSession().getAttribute("user");
        Optional<Order> optional = orderService.getById(id);
        if (optional.isPresent()) {
            Order order = optional.get();
            req.setAttribute("sum", order.getSum());
            mailService.sendMessage(order.getCode(), user.getEmail());
            req.getRequestDispatcher("/confirmOrder.jsp").forward(req, resp);
        } else {
            resp.sendRedirect("/products");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        long id = 0;
        try {
            id = Long.valueOf(req.getParameter("id"));
            int code = Integer.valueOf(req.getParameter("code"));
            if (code != orderService.getById(id).get().getCode().getCodeValue()) {
                throw new NumberFormatException("invalid code");
            } else {
                basket_service.removeProducts((User) req.getSession().getAttribute("user"));
                req.getSession().removeAttribute("orderId");
                resp.sendRedirect("/products");
            }
        } catch (NumberFormatException e) {
            req.setAttribute("error", "invalid code");
            req.getRequestDispatcher("/confirmOrder.jsp").forward(req, resp);
        }
    }
}
