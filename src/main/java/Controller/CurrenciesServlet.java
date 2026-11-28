package Controller;

import DTO.CurrencyRequestDto;
import DTO.CurrencyResponseDto;
import Exceptions.CurrencyAlreadyExists;
import Exceptions.CurrencyNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;


@WebServlet("/currencies")
public class CurrenciesServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<CurrencyResponseDto> currencyDtoList = currencyService.getCurrencyList();
            String responseBody = objectMapper.writeValueAsString(currencyDtoList);
            setHttpResponse(resp, 200, responseBody);
        } catch (CurrencyNotFoundException e) {
            sendError(resp, 500, e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String code = req.getParameter("code");
        String sign = req.getParameter("sign");
        if (isBlank(code) || isBlank(name) || isBlank(sign) || code.length()>3) {
            sendError(resp, 400, "Missing required field");
            return;
        }
        try {
            CurrencyRequestDto currencyRequestDto = new CurrencyRequestDto(name, code, sign);
             CurrencyResponseDto currencyResponse = currencyService.addNewCurrency(currencyRequestDto);
            setHttpResponse(resp, 201, objectMapper.writeValueAsString(currencyResponse));
        } catch (IllegalArgumentException e) {
            sendError(resp, 400, "A required form field is missing.");
        } catch (CurrencyAlreadyExists e) {
            sendError(resp, 409, "A currency with this code already exists.");
        } catch (RuntimeException e){
            sendError(resp, 500, "An unexpected error occurred.");
        }
    }


}