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
            resp.sendError(500, e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fullName = req.getParameter("fullname");
        String code = req.getParameter("code");
        String sign = req.getParameter("sign");
        if (isBlank(code) || isBlank(fullName) || isBlank(sign)) {
            resp.sendError(400, "Missing required field");
            return;
        }
        try {
            CurrencyRequestDto currencyRequestDto = new CurrencyRequestDto(fullName, code, sign);
            currencyService.addNewCurrency(currencyRequestDto);
        } catch (IllegalArgumentException e) {
            resp.sendError(400, e.getMessage());
        } catch (CurrencyAlreadyExists e) {
            resp.sendError(409, e.getMessage());
        } catch (CurrencySaveException e) {
            resp.sendError(500, e.getMessage());
        }
    }


}