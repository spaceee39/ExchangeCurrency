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
                resp.sendError(400, "message: Incorrect currency code");
            }
            String baseCurrencyCode = path.substring(0,3).toUpperCase();
            String targetCurrencyCode = path.substring(3).toUpperCase();

            ExchangeRateResponseDto exchangeRateResponseDto = exchangeRatesService.getRateByCodes(baseCurrencyCode, targetCurrencyCode);

            setHttpResponse(resp, 200, objectMapper.writeValueAsString(exchangeRateResponseDto));

        } catch (ExchangeRateNotFoundException e){
            setHttpResponse(resp, 404, "message: Exchange rate not found for the pair");
        } catch (RuntimeException e){
            setHttpResponse(resp, 500, "message: An unexpected error occurred");
        }
    }
}
