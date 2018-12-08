import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import service.ConturExampleService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/hello")
public class Servlet extends HttpServlet {
    GsonBuilder builder = new GsonBuilder();
    Gson gson = builder.create();
    ConturExampleService service = new ConturExampleService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.addHeader("Access-Control-Allow-Origin", "*");

        if (req.getParameter("getResult") != null) {
            resp.getWriter().write(gson.toJson(service.getResult()));
        }
        if (req.getParameter("getErrors")!= null){
            resp.getWriter().write(
                    gson.toJson(service.getErrors()));
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }



}
