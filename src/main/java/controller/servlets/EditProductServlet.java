package controller.servlets;

import factory.ProductServiceFactory;
import model.Product;
import service.ProductService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(value = "/products/edit")
public class EditProductServlet extends HttpServlet {

    private static final ProductService productService = ProductServiceFactory.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        long id = Long.valueOf(req.getParameter("id"));
        if (productService.getById(id).isPresent()) {
            Product product = productService.getById(id).get();
            req.setAttribute("id", id);
            req.setAttribute("name", product.getName());
            req.setAttribute("description", product.getDescription());
            req.setAttribute("priceValue", product.getPrice());
            req.getRequestDispatcher("/edit_product.jsp").forward(req, resp);
        } else {
            resp.sendRedirect("/products");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        long id = Long.valueOf(req.getParameter("id"));
        String name = req.getParameter("name");
        String description = req.getParameter("description");
        try {
            if (req.getParameter("price").isEmpty()) {
                throw new NumberFormatException("Empty price");
            }
            double price = Double.valueOf(req.getParameter("price"));
            productService.updateProduct(id, name, description, price);
            resp.sendRedirect("/products");
        } catch (NumberFormatException e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("name", name);
            req.setAttribute("description", description);
            doGet(req, resp);
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
            doGet(req, resp);
        }
    }
}
