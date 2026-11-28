package Controller;

import DTO.ExchangeRateRequestDto;
import DTO.ExchangeRateResponseDto;
import Exceptions.ExchangeRateNotFoundException;
import Exceptions.InvalidRequestException;
import Exceptions.ValidationException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Locale;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends BaseServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            String path = getPathInfo(req);
            String baseCurrencyCode = path.substring(0,3);
            String targetCurrencyCode = path.substring(3);
            compareCodes(baseCurrencyCode, targetCurrencyCode);
            if(path == null || !path.matches("[A-Z]{6}")){
                sendError(resp, 400, "Incorrect currency code");
                return;
            }
            ExchangeRateResponseDto exchangeRateResponseDto = exchangeRatesService.getRateByCodes(baseCurrencyCode, targetCurrencyCode);
            setHttpResponse(resp, 200, objectMapper.writeValueAsString(exchangeRateResponseDto));
        } catch (ValidationException e) {
            sendError(resp, 400, e.getMessage());
        } catch (ExchangeRateNotFoundException e){
            sendError(resp, 404, "Exchange rate not found for the pair");
        } catch (RuntimeException e){
            sendError(resp, 500, "An unexpected error occurred");
        }
    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String path = getPathInfo(req);
            String baseCurrencyCode = path.substring(0, 3);
            String targetCurrencyCode = path.substring(3, 6);
            compareCodes(baseCurrencyCode, targetCurrencyCode);
            if(path == null || !path.matches("[A-Z]{6}")){
                sendError(resp, 400, "Incorrect currency code");
                return;
            }
            BigDecimal rate = getBigDecimal(req, "rate");
            ExchangeRateResponseDto exchangeRateResponseDto = exchangeRatesService.patchRate(new ExchangeRateRequestDto(baseCurrencyCode, targetCurrencyCode, rate));
            setHttpResponse(resp, 200, objectMapper.writeValueAsString(exchangeRateResponseDto));
        }catch (ValidationException e) {
            sendError(resp, 400, e.getMessage());
        }catch (ExchangeRateNotFoundException e){
            sendError(resp, 404, e.getMessage());
        }catch(RuntimeException e){
            sendError(resp, 500, "Internal server error");
        }
    }
}
