package Controller;

import Exceptions.CurrencyNotFoundException;
import Model.Currency;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/currencies")
public class CurrenciesServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Currency> currencyList = currencyService.getCurrencyList();
            String responseBody = objectMapper.writeValueAsString(currencyList);
            setHttpResponse(resp, 200, responseBody);
        } catch (CurrencyNotFoundException e){
            resp.sendError(500, e.getMessage());
        }

    }
}
