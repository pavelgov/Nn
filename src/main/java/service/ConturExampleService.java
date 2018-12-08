package service;

import aforge.ActivationNetwork;
import aforge.BackPropagationLearning;
import aforge.Tanh;
import model.Data;
import model.DataImp;
import java.util.ArrayList;
import java.util.List;

public class ConturExampleService {
    double[][] networkAnswers;
    double[][] enters;
    double[][] correctAnswers;
    List<Double> errors = new ArrayList<>();
    ActivationNetwork aNetwork = new ActivationNetwork(new Tanh(1), 1, 5, 1);
    BackPropagationLearning teacher = new BackPropagationLearning(aNetwork);
    Data data = new DataImp();


   public double[] getResult(){
       teacher.setLearningRate(0.1);
       enters = (data.getEnters());
       correctAnswers = normalize(data.getCorrectAnswers(enters));

       for (int i = 0; i <100 ; i++) {
           teacher.runEpoch(enters, correctAnswers); //учимся по 40входов
           errors.add(getError(enters,correctAnswers));
       }
       networkAnswers = computeWithEnters(enters); //считаем ответ сети

       return  convertMas2inOne(networkAnswers);
   }

    public Object[] getErrors() {
       return errors.toArray();
    }

    private double[] convertMas2inOne(double[][] answer) {
        double[] res = new double[answer.length];

        for (int i = 0; i < answer.length; i++) {
            res[i] = answer[i][0];
        }

        return res;
    }

    private double[][] computeWithEnters(double[][] enters) {
        double[][] networkAnswer = new double[enters.length][1];

        for (int i = 0; i < enters.length; i++) {
            networkAnswer[i] = aNetwork.Compute(enters[i]);
        }

        return networkAnswer;
    }


    double getError(double[][] enters, double[][] correctAnswers)
    {
        double sum=0;
        for (int i=0;i<enters.length;i++)
        {
            sum+=Math.abs(aNetwork.Compute(enters[i])[0]-correctAnswers[i][0]);
        }
        sum/=enters.length;

        return sum;
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
