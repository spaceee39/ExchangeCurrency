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
                setHttpResponse(resp, 400, "message: Incorrect currency code");
                return;
            }
            ExchangeRateResponseDto exchangeRateResponseDto = exchangeRatesService.getRateByCodes(baseCurrencyCode, targetCurrencyCode);
            setHttpResponse(resp, 200, objectMapper.writeValueAsString(exchangeRateResponseDto));
        } catch (ValidationException e) {
            setHttpResponse(resp, 400, " message: "+e.getMessage());
        } catch (ExchangeRateNotFoundException e){
            setHttpResponse(resp, 404, "message: Exchange rate not found for the pair");
        } catch (RuntimeException e){
            setHttpResponse(resp, 500, "message: An unexpected error occurred");
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
                setHttpResponse(resp, 400, "message: Incorrect currency code");
                return;
            }
            BigDecimal rate = getBigDecimal(req, "rate");
            ExchangeRateResponseDto exchangeRateResponseDto = exchangeRatesService.patchRate(new ExchangeRateRequestDto(baseCurrencyCode, targetCurrencyCode, rate));
            setHttpResponse(resp, 200, objectMapper.writeValueAsString(exchangeRateResponseDto));
        }catch (ValidationException e) {
            setHttpResponse(resp, 400, "message: " + e.getMessage());
        }catch (ExchangeRateNotFoundException e){
            setHttpResponse(resp, 404, "message: " + e.getMessage());
        }catch(RuntimeException e){
            setHttpResponse(resp, 500, "Internal server error");
        }
    }
}
