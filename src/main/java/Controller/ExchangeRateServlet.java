package Controller;

import DTO.ExchangeRateResponseDto;
import Exceptions.ExchangeRateNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends BaseServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            String path = getPathInfo(req);
            if(path == null || !path.matches("[A-Za-z]{6}") ){
                resp.sendError(400, "Incorrect currency code");
            }
            String baseCurrencyCode = path.substring(0,3).toUpperCase();
            String targetCurrencyCode = path.substring(3).toUpperCase();

            ExchangeRateResponseDto exchangeRateResponseDto = exchangeRatesService.getRateByCodes(baseCurrencyCode, targetCurrencyCode);

            resp.getWriter().write(objectMapper.writeValueAsString(exchangeRateResponseDto));

        } catch (ExchangeRateNotFoundException e){
            resp.sendError(404, "Exchange rate not found for the pair");
        } catch (RuntimeException e){
            resp.sendError(500, e.getMessage());

        }

    }
}
