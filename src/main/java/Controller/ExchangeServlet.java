package Controller;

import DTO.ExchangeRequestDto;
import DTO.ExchangeResponseDto;
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
            BigDecimal amount = getBigDecimal(req, "amount");
            ExchangeResponseDto exchange = exchangeService.makeExchange(new ExchangeRequestDto(baseCurrencyCode, targetCurrencyCode, amount));
            resp.getWriter().write(objectMapper.writeValueAsString(exchange));
        } catch (RuntimeException e) {
            resp.sendError(401, e.getMessage()  );
        }
    }

}
