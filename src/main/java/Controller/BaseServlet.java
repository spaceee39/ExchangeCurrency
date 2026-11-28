package Controller;

import Exceptions.CurrencyNotFoundException;
import Exceptions.InvalidRequestException;
import Exceptions.ValidationException;
import Service.CurrencyService;
import Service.ExchangeRatesService;
import Service.ExchangeService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Locale;

@Setter
public abstract class BaseServlet extends HttpServlet {

    protected CurrencyService currencyService;
    protected ExchangeRatesService exchangeRatesService;
    protected ExchangeService exchangeService;
    protected ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        setCurrencyService((CurrencyService) servletContext.getAttribute("currencyService"));
        setExchangeRatesService((ExchangeRatesService) servletContext.getAttribute("exchangeRatesService"));
        setExchangeService((ExchangeService) servletContext.getAttribute("exchangeService"));
        setObjectMapper((ObjectMapper) servletContext.getAttribute("objectMapper"));
    }

    // метод возвращает ответ, чтобы при желании можно было добавить самому заголовки или что-то еще
    public HttpServletResponse setHttpResponse(HttpServletResponse response, int status, String responseBody) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(responseBody);
        return response;
    }

    public void sendError(HttpServletResponse response, int status, String responseBody) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"message\":"+responseBody+"}");
    }

    public String getPathInfo(HttpServletRequest req){
        String[] path = req.getPathInfo().split("/");
        if (path.length < 3){
            if(path[1] == null) throw new NullPointerException("The path is missing from the address.");
            return path[1];
        } else throw new InvalidRequestException("Invalid path name");
    }

    protected boolean isBlank(String s) {
        return s == null || s.isBlank();
    }

    protected BigDecimal getBigDecimal(HttpServletRequest req, String parameterName){
        try {
            String value = req.getParameter(parameterName);
            if (isBlank(value)) {
                throw new ValidationException("Parameter " + parameterName + " is required.");
            }
            String clear = value.trim().replace(',', '.');
            BigDecimal rate = new BigDecimal(clear);
            if (rate.compareTo(BigDecimal.ZERO) < 0 || rate.equals(BigDecimal.ZERO)) {
                throw new ValidationException("Rate less than 0 or equals 0.");
            }
            return rate;
        }catch (NumberFormatException e){
            throw new ValidationException("Parameter" + parameterName + " must be a number.");
        }
    }

    protected String getCurrencyCode(HttpServletRequest req, String parameterName) {
        String value = req.getParameter(parameterName);
        if (isBlank(value)) {
            throw new ValidationException("Parameter " + parameterName + " is required.");
        }
        if (!value.matches("[A-Z]{3}")) {
            throw new ValidationException("Parameter " + parameterName + " must be a 3-letter currency code or in upper case.");
        }
        return value;
    }

    protected void compareCodes(String baseCurrencyCode, String targetCurrencyCode){
        if(baseCurrencyCode.equals(targetCurrencyCode)) throw new ValidationException("The base currency matches the target currency");
    }


}
