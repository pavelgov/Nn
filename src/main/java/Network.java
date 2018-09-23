public class Network extends Nn {

    private double[][] layers;
    private Object[] net;



    public double[][] getLayers() {
        return layers;
    }

    public void setLayers(double[][] layers, int index) {
        net[index] = layers;

    }



    public Object[] getNet() {
        return net;
    }

    public void setNet(Object[] net) {
        this.net = net;
    }

    /**
     * В конструкторе задаем кол-во слоев сети без весов
     *
     * @param size
     */
    public Network(int size) {
        this.layers = new double[size][];

    }

    public Object[] init(int... size) {
        if (size.length != layers.length) {
            throw new IndexOutOfBoundsException("Размер не совпадает");
        }
        //заполняем каждый слой своим размером (вход + слой без весов + выход)
        for (int i = 0; i < layers.length; i++) {
            layers[i] = new double[size[i]];
        }
        //слои + веса
        net = new Object[(layers.length * 2) - 1];

        //заполняем слои, потом вес
        for (int i = 0; i < layers.length; i++) {
            net[i + i] = layers[i];
            if (i + 1 < layers.length) {
                net[(i + i) + 1] = new double[layers[i].length * layers[i + 1].length];
            }
        }
        return net;
    }

    public Object[] setRandomWeights(double from, double to) {

        for (int i = 1; i < net.length-1; i += 2) {
            for (int j = 0; j < ((double[]) (net[i])).length; j++) {
                ((double[]) net[i])[j] = from + (Math.random() * ((to - from)));
            }
        }
        return net;
    }

    public Object[] setValuesinNetLayer(int indexLayer, double[] layerWithValues) {
        if (((double[]) net[indexLayer]).length != layerWithValues.length) {
            throw new IndexOutOfBoundsException("Размер переданого массива не равен массиву слоя сети");
        }
        net[indexLayer] = layerWithValues;
        return net;
    }

    /**
     * Важно не забыть заполнить все веса(слои) перед подсчетом
     *
     * @param enters - массив входных данных на 1 проход
     * @return - последний слой сети и есть ответ
     */
    public double[] compute(double[] enters) {
        net[0] = enters;

        for (int i = 0; i < net.length - 1; i += 2) {
            net[i + 2] = multiplyMasFromL((double[]) net[i], (double[]) net[i + 1]); //умножили веса на входы и просумировали
            net[i + 2] = activation((double[]) net[i + 2]);                          //активировали
        }

        return (double[]) net[net.length - 1];

    }

    /**
     * обучение (пересчет весов) сети, 1 итерация
     * @param enters массив входов на 1 итерацию
     * @param correctAnswers правильный ответ
     * @return обученную сеть
     */
    public Object[] learn(double[] enters, double[] correctAnswers) {
        double[][] layerF = new double[net.length / 2][];
        double[][] layerD = new double[net.length / 2][];

        net[0] = enters;
        int layerFIndex = 0;
        int layerDIndex = ((net.length - 2) / 2); //отнимаем 2 (убрав вход, выход,остаются веса и внутр слои
        /*  СЧИТАЕМ  - ПОЛУЧАЕМ ОТВЕТ СЕТИ */
        for (int i = 0; i < net.length - 1; i += 2) { //идем вправо считаем, аналог. compute
            net[i + 2] = multiplyMasFromL((double[]) net[i], (double[]) net[i + 1]); //умножили веса на входы и просумировали
            net[i + 2] = activation((double[]) net[i + 2]);                          //активировали
            layerF[layerFIndex] = (double[]) net[i + 2];
            layerFIndex++;
        }
        /*  ПОЛУЧАЕМ ОШИБКУ СЕТИ */
        net[net.length - 1] = findErrorNetwork((double[]) net[net.length - 1], correctAnswers); //находим ошибку сети
        layerD[layerDIndex] = (double[]) net[net.length - 1];

        /*  НАХОДИМ d ДЛЯ СКРЫТЫХ СЛОЕВ (ВКЛЮЧАЯ ПОСЛЕДНИЙ СЛОЙ - ОТВЕТ)*/
        for (int i = net.length - 1; i > 2; i -= 2) { //идем влево до 2, потому что рассчитываем d (веса и входы не изменяются)
            layerDIndex--; //отняли вначале,  потому что length=3, а надо 2
            net[i - 2] = multiplyMasFromR((double[]) net[i], (double[]) net[i - 1]);
            layerD[layerDIndex] = (double[]) net[i - 2];
        }

        layerFIndex = 0;
        /*  ПЕРЕСЧИТЫВАЕМ ВЕСА */
        for (int i = 0; i < net.length - 1; i += 2) { //идем вправо пересчитываем веса

            if (i == 0) {
                net[i + 1] = backPropogation((double[]) net[i + 1], layerD[layerDIndex], layerF[layerFIndex], (double[]) net[i]);
            } else {
                net[i + 1] = backPropogation((double[]) net[i + 1], layerD[layerDIndex], layerF[layerFIndex], layerF[layerFIndex - 1]);
            }
            layerFIndex++;
            layerDIndex++;
        }
       /* Пересчет весов по примеру
        wEh = Nn.backPropogation(wEh, hiddenOneD, hiddenOneF, enters);
        wHh = Nn.backPropogation(wHh, hiddenTwoD, hiddenTwoF, hiddenOneF);
        wHo = Nn.backPropogation(wHo, outD, outF, hiddenTwoF);
       */

        return net;
    }


}
