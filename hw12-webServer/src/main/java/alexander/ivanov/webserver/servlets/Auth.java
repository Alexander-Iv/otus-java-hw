package alexander.ivanov.webserver.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

public class Auth extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(Auth.class);
    private static final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter printWriter = resp.getWriter();
        StringBuilder result = new StringBuilder();
        JsonObject jsonObject = new JsonObject();

        //jsonObject.addProperty("user", String.valueOf(req.getParameter("user")));
        //jsonObject.addProperty("password", String.valueOf(req.getParameter("password")));

        String user = Optional.ofNullable(String.valueOf(req.getParameter("user"))).orElse("");
        String password = Optional.ofNullable(String.valueOf(req.getParameter("password"))).orElse("");

        jsonObject.addProperty("user", user);
        jsonObject.addProperty("password", password);

        if (user.isEmpty() || password.isEmpty()) {
            jsonObject.addProperty("result status", HttpServletResponse.SC_BAD_REQUEST);
        } else {
            jsonObject.addProperty("result status", HttpServletResponse.SC_OK);
        }


        gson.toJson(jsonObject, result);
        resp.setContentType("application/json");
        printWriter.print(result);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
