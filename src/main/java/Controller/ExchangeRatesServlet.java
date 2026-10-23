package Controller;

import DTO.ExchangeRateRequestDto;
import DTO.ExchangeRateResponseDto;
import Exceptions.CurrencyAlreadyExists;
import Exceptions.CurrencyNotFoundException;
import Exceptions.ValidationException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

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
            String baseCurrencyCode = getCurrencyCode(req, "baseCurrencyCode");
            String targetCurrencyCode = getCurrencyCode(req, "targetCurrencyCode");
            BigDecimal rate = getBigDecimal(req, "rate");
            compareCodes(baseCurrencyCode, targetCurrencyCode);

            exchangeRatesService.createNewRate(new ExchangeRateRequestDto(baseCurrencyCode, targetCurrencyCode, rate));

        }catch (ValidationException e){
            resp.sendError(400, e.getMessage());
        }catch (CurrencyNotFoundException e){
            resp.sendError(404, e.getMessage());
        } catch (CurrencyAlreadyExists e) {
          resp.sendError(409, e.getMessage());
        } catch (RuntimeException e){
            resp.sendError(500, "Internal server error");
        }
    }

    protected String getCurrencyCode(HttpServletRequest req, String parameterName) {
        String value = req.getParameter(parameterName);
        if (isBlank(value)) {
            throw new ValidationException("Parameter " + parameterName + " is required.");
        }
        value = value.strip().toUpperCase(Locale.ROOT);
        if (!value.matches("[A-Z]{3}")) {
            throw new ValidationException("Parameter " + parameterName + " must be a 3-letter currency code.");
        }
        return value;
    }

    public BigDecimal getBigDecimal(HttpServletRequest req, String parameterName){
        try {
            String value = req.getParameter(parameterName);
            if (isBlank(value)) {
                throw new ValidationException("Parameter " + parameterName + " is required.");
            }
            String clear = value.trim().replace(',', '.');
            BigDecimal rate = new BigDecimal(clear);
            if (rate.compareTo(BigDecimal.ZERO) < 0) {
                throw new ValidationException("Rate less than 0.");
            }
            return rate;
        }catch (NumberFormatException e){
            throw new ValidationException("Parameter" + parameterName + " must be a number.");
        }
    }

    public void compareCodes(String baseCurrencyCode, String targetCurrencyCode){
        if(baseCurrencyCode.equals(targetCurrencyCode)) throw new ValidationException("The base currency matches the target currency");
    }



}
