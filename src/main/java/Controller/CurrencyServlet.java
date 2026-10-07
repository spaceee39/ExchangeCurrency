package Controller;

import Exceptions.CurrencyNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/currency/*")
public class CurrencyServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(getPathInfo(req));
        try {
            String currencyCode = getPathInfo(req);
            currencyService.getCurrencyByCode(currencyCode);
        } catch (CurrencyNotFoundException e) {
            resp.sendError(500, e.getMessage());
        }
    }

}
