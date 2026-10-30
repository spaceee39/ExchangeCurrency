package Controller;

import DTO.ExchangeRateRequestDto;
import DTO.ExchangeRateResponseDto;
import Exceptions.CurrencyAlreadyExists;
import Exceptions.CurrencyNotFoundException;
import Exceptions.ExchangeRateNotFoundException;
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

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String path = getPathInfo(req);
            String baseCurrencyCode = path.substring(0, 3).toUpperCase(Locale.ROOT);
            String targetCurrencyCode = path.substring(3, 6).toLowerCase(Locale.ROOT);
            compareCodes(baseCurrencyCode, targetCurrencyCode);
            BigDecimal rate = getBigDecimal(req, "rate");
            exchangeRatesService.patchRate(new ExchangeRateRequestDto(baseCurrencyCode, targetCurrencyCode, rate));
        }catch (ValidationException e){
            resp.sendError(400, e.getMessage());
        }catch (ExchangeRateNotFoundException e){
            resp.sendError(404, e.getMessage());
        }catch(RuntimeException e){
            resp.sendError(500, "Internal server error");
        }
    }





}
