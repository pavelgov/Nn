/**
 * Created by Pavel on 09.03.2018.
 */
public abstract class Nn {
    private final double SPEEDLEARN = 0.01;

    public Nn() {
    }

    public double[] backPropogation(double[] weight, double[] hiddenOneD, double[] hiddenOneF, double[] enters) {
        double[] result = new double[weight.length];
        int wIndex = 0;
        for (int i = 0; i < enters.length; i++) {   //в цикле находим правильные веса
            for (int j = 0; j < hiddenOneD.length; j++) {
                result[wIndex] = findWeight(weight[wIndex], hiddenOneD[j], proizvodnaya(hiddenOneF[j]), enters[i], SPEEDLEARN);

                wIndex++;
            }
        }
        return result;
    }

    /**
     * Суммирует и умножает входные данные на веса
     *
     * @param input  входные данные
     * @param weight веса
     * @return массив который просто надо активировать
     */
    @Deprecated
    public double[] sumMatrix(double input, double[] weight) {
        double[] result = new double[weight.length];
        for (int i = 0; i < weight.length; i++) {
            result[i] += weight[i] * input;
        }
        return result;
    }

    public double[] multiplyMasFromR(double[] input, double[] weight) { //todo проверить размер массива
        int layerSize = weight.length / input.length;
        double[] layer = new double[layerSize];
        int k = 0;
        for (int i = 0; i < layerSize; i++) {    //перебираем входы
            for (int j = 0; j < input.length; j++) { //перебираем слой
                layer[i] += input[j] * weight[k];
                ++k;
            }
        }

        return layer;
    }

    public double[] multiplyMasFromL(double[] input, double[] weight) {
        int layerSize = weight.length / input.length;
        double[] layer = new double[layerSize];
        int k = 0;
        for (int i = 0; i < layerSize; i++) {    //перебираем входы
            k = i;
            for (int j = 0; j < input.length; j++) { //перебираем слой
                layer[i] += input[j] * weight[k];
                k += layerSize;
            }
        }
        return layer;
    }

    public double[] findErrorNetwork(double[] answerNetwork, double[] correctAnswer) {
        double[] result = new double[answerNetwork.length];
        for (int i = 0; i < answerNetwork.length; i++) {   //по формуле d = e(прав.ответ) - y(ответ сети);
            result[i] = correctAnswer[i] - answerNetwork[i];
        }
        return result;
    }

    /**
     * функция активации
     *
     * @param sum предварительно просумированные и умноженные на веса данные
     * @return
     */
   /* private  double activation1(double sum) { //
       return 1 / (1 + Math.pow(2.71828182845904523536, -sum)); //сигмоида

    }*/
    private double activation1(double sum) { //
       // return ((Math.pow(Math.E, 2 * sum) - 1) / (Math.pow(Math.E, 2 * sum) + 1));//гиперболич тангенс
        return  Math.tanh(sum*0.1);
    }

    public double[] activation(double[] sum) { //
        double[] result = new double[sum.length];

        for (int i = 0; i < sum.length; i++) {
            result[i] = activation1(sum[i]);
        }
        return result;
    }

   /* protected   double proizvodnaya(double activatF) { //производная от функции активации
        return activatF * (1 - activatF);
    } */

    protected double proizvodnaya(double activatF) { //производная от функции активации
        return 0.1*(1 - activatF * activatF);
    }

    protected double findWeight(double w1, double d1, double proizv, double x1, double speedLearn) {
        return w1 - d1 * proizv * x1 * speedLearn;
    }

}
