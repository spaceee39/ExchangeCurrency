package Controller;

import Exceptions.CurrencyNotFoundException;
import Model.Currency;
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
        try {
            String currencyCode = getPathInfo(req);
            Currency currency = currencyService.getCurrencyByCode(currencyCode);
            setHttpResponse(resp, 200, objectMapper.writeValueAsString(currency));
        } catch (CurrencyNotFoundException e) {
            resp.sendError(400, "Invalid currency name.");
        } catch (RuntimeException e){
            resp.sendError(500, "An unexpected error occurred.");
        }
    }

}
