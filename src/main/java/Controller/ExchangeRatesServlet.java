package Controller;

import DTO.ExchangeRateResponseDto;
import Model.ExchangeRate;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            List<ExchangeRateResponseDto> exchangeRateList = exchangeRatesService.getAllExchangeRates();
            resp.getWriter().write(objectMapper.writeValueAsString(exchangeRateList));
        } catch (RuntimeException e) {
            resp.sendError(500, "Internal server error");
        }
    }
}
