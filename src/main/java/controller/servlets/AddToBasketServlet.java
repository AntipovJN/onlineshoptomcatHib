package controller.servlets;

import factory.BasketServiceFactory;
import factory.ProductServiceFactory;
import model.Product;
import model.User;
import service.BasketService;
import service.ProductService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(value = "/products/buy")
public class AddToBasketServlet extends HttpServlet {

    private static final BasketService basketService = BasketServiceFactory.getInstance();
    private static final ProductService productService = ProductServiceFactory.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        long id = Long.valueOf(req.getParameter("id"));
        if (productService.getById(id).isPresent()) {
            Product product = productService.getById(id).get();
            basketService.addProduct((User) req.getSession().getAttribute("user"), product);
        }
        resp.sendRedirect("/products");
    }
}
