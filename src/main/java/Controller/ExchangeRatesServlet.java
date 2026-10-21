package Controller;

import DTO.ExchangeRateRequestDto;
import DTO.ExchangeRateResponseDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        try{
            String baseCurrencyCode = req.getParameter("baseCurrencyCode").toUpperCase();
            String targetCurrencyCode = req.getParameter("targetCurrencyCode").toUpperCase();
            BigDecimal rate = new BigDecimal(req.getParameter("rate"));
            if(isBlank(targetCurrencyCode) || isBlank(baseCurrencyCode) || rate == null){
                resp.sendError(400, "A required form field is missing.");
            }
            if(targetCurrencyCode.equals(baseCurrencyCode)){
                resp.sendError(400, "The base currency is the same as the target currency.");
            }
            if(rate.compareTo(BigDecimal.ZERO) < 0){
                resp.sendError(400, "Rate less than 0.");
            }

            exchangeRatesService.createNewRate(new ExchangeRateRequestDto(baseCurrencyCode, targetCurrencyCode, rate));

        } catch (RuntimeException e){
            resp.sendError(500, "Internal server error");
        }
    }

}
