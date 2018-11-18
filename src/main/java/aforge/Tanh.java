package aforge;

public class Tanh implements IActivationFunction {
    public double Beta = 1;

    public Tanh(double Beta) {
        this.Beta = Beta;
    }

    public double function(double x) {
        return Math.tanh(x * Beta);
    }

    public double derivative(double x) {
       // return activatF * (1 - activatF);
        double f =function(x);
         return Beta * (1 - f * f);
    }

    public double derivative2(double y) {
        return Beta * (1 - y * y);
    }
}
