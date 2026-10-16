package Controller;

import Exceptions.ExchangeRateNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/exchangeRate")
public class ExchangeRateExchange extends BaseServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            String path = getPathInfo(req);
            String baseCurrencyCode = path.substring(0,3);
            String targetCurrencyCode = path.substring(3);

        } catch () {
            resp.sendError(400, "Currency pair codes are missing in the address");
        } catch (ExchangeRateNotFoundException e){
            resp.sendError(404, "Exchange rate not found for the pair");
        } catch (RuntimeException e){
            resp.sendError(404, e.getMessage());

        }

    }
}
