package Controller;

import DTO.ExchangeRequestDto;
import DTO.ExchangeResponseDto;
import Exceptions.ExchangeRateNotFoundException;
import Exceptions.ValidationException;
import Service.ExchangeService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/exchange")
public class ExchangeServlet extends BaseServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String baseCurrencyCode = getCurrencyCode(req, "from");
            String targetCurrencyCode = getCurrencyCode(req, "to");
            super.compareCodes(baseCurrencyCode, targetCurrencyCode);
            BigDecimal amount = getBigDecimal(req, "amount");
            ExchangeResponseDto exchange = exchangeService.makeExchange(new ExchangeRequestDto(baseCurrencyCode, targetCurrencyCode, amount));
            setHttpResponse(resp, 400, objectMapper.writeValueAsString(exchange));
        } catch (ValidationException e) {
            setHttpResponse(resp, 400, e.getMessage());
        } catch (ExchangeRateNotFoundException e){
            setHttpResponse(resp, 404, e.getMessage());
        } catch (RuntimeException e) {
            setHttpResponse(resp, 500, "Internal server error");
        }
    }

}
