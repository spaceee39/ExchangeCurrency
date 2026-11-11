package Controller;

import DTO.CurrencyRequestDto;
import DTO.CurrencyResponseDto;
import Exceptions.CurrencyAlreadyExists;
import Exceptions.CurrencyNotFoundException;
import Exceptions.CurrencySaveException;
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
            setHttpResponse(resp, 500, "message: "+ e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fullName = req.getParameter("fullname");
        String code = req.getParameter("code");
        String sign = req.getParameter("sign");
        if (isBlank(code) || isBlank(fullName) || isBlank(sign)) {
            setHttpResponse(resp, 400, "message: Missing required field");
            return;
        }
        try {
            CurrencyRequestDto currencyRequestDto = new CurrencyRequestDto(fullName, code, sign);
             CurrencyResponseDto currencyResponse = currencyService.addNewCurrency(currencyRequestDto);
            setHttpResponse(resp, 201, objectMapper.writeValueAsString(currencyResponse));
        } catch (IllegalArgumentException e) {
            setHttpResponse(resp, 400, "message: A required form field is missing");
        } catch (CurrencyAlreadyExists e) {
            setHttpResponse(resp, 409, "message: A currency with this code already exists.");
        } catch (RuntimeException e){
            setHttpResponse(resp, 500, "message: An unexpected error occurred");
        }
    }


}