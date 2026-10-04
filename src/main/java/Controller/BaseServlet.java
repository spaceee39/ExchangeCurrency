package Controller;

import Exceptions.IncorrectRequestException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

public class BaseServlet extends HttpServlet {

    ObjectMapper objectMapper = new ObjectMapper();


    public void sendHttpResponse(HttpServletResponse response, int status, String responseString){
        try {
            response.setStatus(status);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(responseString);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public String pathCurrencyInfo(HttpServletRequest req){
        String[] path = req.getPathInfo().split("/");
        if (path.length < 3){
        return path[1];
        }else throw new IncorrectRequestException("invalid path name");
    }

}
