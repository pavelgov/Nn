import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Data;
import model.DataImp;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/hello")
public class Servlet extends HttpServlet {
    NetworkWithNewLearn network;
    Data data;
    GsonBuilder builder = new GsonBuilder();
    Gson gson = builder.create();
    double[][] networkAnswers;
    double[][] enters;
    double[][] correctAnswers;

    {
        network = new NetworkWithNewLearn(4);
        network.init(1, 3,3, 1);
        network.setRandomWeights(-0.5, 0.5);
        data = new DataImp();

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.addHeader("Access-Control-Allow-Origin", "*");

        enters = (data.getEnters());

        correctAnswers = normalize(data.getCorrectAnswers(enters));


        if (req.getParameter("getResult") != null) {

            learnWithEnters((enters),correctAnswers ); //учимся 1000 раз по 40входов
            networkAnswers = computeWithEnters((enters)); //считаем ответ сети


            resp.getWriter().write(gson.toJson(convertMas2inOne(networkAnswers)));

        }
        if (req.getParameter("getErrors")!= null){
            resp.getWriter().write(
                    gson.toJson(
                            convertMas2inOne(getNetworkErrors(networkAnswers,correctAnswers))));
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }


    private double[] convertMas2inOne(double[][] answer) {
        double[] res = new double[answer.length];

        for (int i = 0; i < answer.length; i++) {
            res[i] = answer[i][0];
            //System.out.println(res[i]);
        }

        return res;
    }

    private Network learnWithEnters(double[][] enters, double[][] answers) {
        for (int j = 0; j < 1000; j++) {
            for (int i = 0; i < enters.length; i++) {
                network.learn(enters[i], answers[i]);
            }
        }
        return network;
    }

    private double[][] computeWithEnters(double[][] enters) {
        double[][] networkAnswer = new double[enters.length][1];

        for (int i = 0; i < enters.length; i++) {
            networkAnswer[i] = network.compute(enters[i]);
        }

        return networkAnswer;
    }

    private double[][] getNetworkErrors(double[][] networkAnswers, double[][] correctAnswers){
       double[][] errors = new double[networkAnswers.length][1];

        for (int i = 0; i <correctAnswers.length ; i++) {
            errors[i][0] = correctAnswers[i][0] - networkAnswers[i][0];

        }

        return errors;
    }

    private double[][] normalize(double[][] data) {
        double[][] enters = new double[data.length][1];
        for (int i = 0; i < data.length; i++) {
            enters[i][0] = (data[i][0]/10.0);
        }
        return enters;
    }

    private double[][] deNormalize(double[][] data) {
        double[][] enters = new double[data.length][1];
        for (int i = 0; i < data.length; i++) {
            enters[i][0] = (data[i][0]*10.0);
        }
        return enters;
    }
}
