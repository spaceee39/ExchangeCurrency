package Controller;

import DTO.CurrencyDto;
import Exceptions.CurrencyAlreadyExists;
import Exceptions.CurrencyNotFoundException;
import Exceptions.CurrencySaveException;
import Model.Currency;
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
            List<Currency> currencyList = currencyService.getCurrencyList();
            String responseBody = objectMapper.writeValueAsString(currencyList);
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
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing required field");
            return;
        }
        try {
            CurrencyDto currencyDto = new CurrencyDto(fullName, code, sign);
            currencyService.addNewCurrency(currencyDto);
        } catch (IllegalArgumentException e) {
            resp.sendError(400, e.getMessage());
        } catch (CurrencyAlreadyExists e) {
            resp.sendError(409, e.getMessage());
        } catch (CurrencySaveException e) {
            resp.sendError(500, e.getMessage());
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}