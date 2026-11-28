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
           setHttpResponse(resp, 200, objectMapper.writeValueAsString(exchangeRateList));
        } catch (RuntimeException e) {
            sendError(resp, 500, "An unexpected error occurred");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        try{
            String baseCurrencyCode = getCurrencyCode(req, "baseCurrencyCode");
            String targetCurrencyCode = getCurrencyCode(req, "targetCurrencyCode");
            BigDecimal rate = getBigDecimal(req, "rate");
            compareCodes(baseCurrencyCode, targetCurrencyCode);
            ExchangeRateResponseDto exchangeRateResponseDto = exchangeRatesService.createNewRate(new ExchangeRateRequestDto(baseCurrencyCode, targetCurrencyCode, rate));
            setHttpResponse(resp, 201, objectMapper.writeValueAsString(exchangeRateResponseDto));
        }catch (ValidationException e){
            sendError(resp, 400, e.getMessage());
        }catch(CurrencyNotFoundException e){
            sendError(resp, 404, "currency not found");
        } catch (CurrencyAlreadyExists e) {
            sendError(resp, 409, "A currency pair with this code already exists.");
        } catch (RuntimeException e){
            sendError(resp, 500, "An unexpected error occurred");
        }
    }







}
