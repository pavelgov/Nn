package model;

public class DataImp implements Data {

    public double[][] getEnters() {
        double[][] otvetSeti = new double[40][1];
        double counter = 0;
        for (int i = 0; i < otvetSeti.length; i++) {
            counter += 0.25;
            otvetSeti[i][0] = ((double) Math.round(counter * Math.pow(10, 2)) / Math.pow(10, 2));

        }
        return otvetSeti;
    }


    public double[][] getCorrectAnswers( double[][] enters) {
        double[][] result = new double[enters.length][1];
        for (int i = 0; i < enters.length; i++) {
             result[i][0] = enters[i][0] * Math.sin(enters[i][0]);
            // result[i][0] =  Math.sin(enters[i][0]);
        }
        return result;
    }


}
