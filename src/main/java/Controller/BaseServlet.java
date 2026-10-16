package Controller;

import Exceptions.InvalidRequestException;
import Service.CurrencyService;
import Service.ExchangeRatesService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
@Setter
public abstract class BaseServlet extends HttpServlet {

    protected CurrencyService currencyService;
    protected ExchangeRatesService exchangeRatesService;
    protected ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        setCurrencyService((CurrencyService) servletContext.getAttribute("currencyService"));
        setExchangeRatesService((ExchangeRatesService) servletContext.getAttribute("exchangeRatesService"));
        setObjectMapper((ObjectMapper) servletContext.getAttribute("objectMapper"));
    }

    // метод возвращает ответ, чтобы при желании можно было добавить самому заголовки или что то еще
    public HttpServletResponse setHttpResponse(HttpServletResponse response, int status, String responseBody) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(responseBody);
        return response;
    }

    public String getPathInfo(HttpServletRequest req){
        String[] path = req.getPathInfo().split("/");
        if (path.length < 3){
            if(path[1] == null) throw new NullPointerException("The currency code is missing from the address.");
        return path[1];
        } else throw new InvalidRequestException("Invalid path name");
    }

}
