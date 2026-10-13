package Controller;

import DTO.CurrencyResponseDto;
import Exceptions.CurrencyNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/currency/*")
public class CurrencyServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String currencyCode = getPathInfo(req);
            CurrencyResponseDto currencyResponseDto = currencyService.getCurrencyByCode(currencyCode);
            setHttpResponse(resp, 200, objectMapper.writeValueAsString(currencyResponseDto));
        } catch (CurrencyNotFoundException e) {
            resp.sendError(404, "Currency is not found.");
        } catch (NullPointerException e){
            resp.sendError(400, "The currency code is missing from the address.");
        } catch (RuntimeException e){
            resp.sendError(500, "An unexpected error occurred.");
        }
    }
}
