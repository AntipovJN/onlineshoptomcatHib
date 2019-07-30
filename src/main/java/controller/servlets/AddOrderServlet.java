package controller.servlets;

import factory.BasketServiceFactory;
import factory.CodeServiceFactory;
import factory.OrderServiceFactory;
import model.Code;
import model.Order;
import model.User;
import service.BasketService;
import service.CodeService;
import service.OrderService;
import utils.CodeGenerator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet(value = "/order")
public class AddOrderServlet extends HttpServlet {

    private static final OrderService orderService = OrderServiceFactory.getInstance();
    private static final BasketService basketService = BasketServiceFactory.getInstance();
    private static final CodeService codeService = CodeServiceFactory.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/order.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String address = req.getParameter("address");
        String payment = req.getParameter("payment");
        User user = (User) req.getSession().getAttribute("user");
        codeService.addCode(new Code(CodeGenerator.generateCode(), user));
        Optional<Code> codeOptional = codeService.getLastCodeForUser(user);
        if (codeOptional.isPresent()) {
            Code code = codeOptional.get();
            orderService.addOrder(code, address, payment, basketService.getBasket(user));
            Optional<Order> orderOptional = orderService.getByCode(code);
            if (orderOptional.isPresent()) {
                Long id = orderOptional.get().getId();
                req.getSession().setAttribute("orderId", id);
                resp.sendRedirect("/confirmOrder");
                return;
            }
        }
        resp.sendRedirect("/products");
    }
}
