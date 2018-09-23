public class NetworkWithNewLearn extends Network {
    private final double SPEEDLEARN = 0.01;

    /**
     * В конструкторе задаем кол-во слоев сети без весов
     *
     * @param size
     */
    public NetworkWithNewLearn(int size) {
        super(size);
    }

    public double findDeltaError(double error, double proizvodSigmoid) {
        return error * proizvodSigmoid;
    }

    public double proizvodSigmoid(double sigmoid) {
        return proizvodnaya(sigmoid);
    }


    protected double findWeight(double w1, double input, double deltaError, double speedLearn) {
        // double deltaError = findDeltaError(d1, proizv);
        return w1 - input * deltaError * speedLearn;
    }


    public double[] backPropogation(double[] weight, double[] input, double[] deltaError) {
        double[] result = new double[weight.length];
        int wIndex = 0;
        for (int i = 0; i < input.length; i++) {   //в цикле находим правильные веса
            for (int j = 0; j < deltaError.length; j++) {
                result[wIndex] = findWeight(weight[wIndex], input[i], deltaError[j], SPEEDLEARN);
                 //todo когда дошли до левого слоя вход 1 нейрон Дельта 3
                wIndex++;
            }
        }
        return result;
    }

    protected double[] findDeltaErrorMas(double[] error, double[] sigmoid) {
        double[] result = new double[error.length];
       /* for (int i = 0; i < error.length; i++) {   //по формуле delta = e(ошибка) * призводная сигмоиды;
            result[i] = error[i] * proizvodSigmoid(sigmoid[i]);
        }
        int k = 0;*/
        for (int i = 0; i < error.length; i++) {    //перебираем входы
            for (int j = 0; j < sigmoid.length; j++) { //перебираем слой
                result[i] += error[i] * proizvodSigmoid(sigmoid[j]);

            }
        }
        return result;


    }

    @Override
    public Object[] learn(double[] enters, double[] correctAnswers) {

        double[][] layerF = new double[getNet().length / 2][];  //слой с сигмоидой
        double[][] layerE = new double[getNet().length / 2][]; //слой с ошибками
        double[][] layerD = new double[getNet().length / 2][]; //слой с дельта ошибки

        getNet()[0] = enters;
        int layerFIndex = 0;
        int layerDIndex = ((getNet().length - 2) / 2); //отнимаем 2 (убрав вход, выход,остаются веса и внутр слои
        int layerEIndex = ((getNet().length - 2) / 2); //отнимаем 2 (убрав вход, выход,остаются веса и внутр слои

        //*  СЧИТАЕМ  - ПОЛУЧАЕМ ОТВЕТ СЕТИ *//*
        for (int i = 0; i < getNet().length - 1; i += 2) { //идем вправо считаем, аналог. compute
            getNet()[i + 2] = multiplyMasFromL((double[]) getNet()[i], (double[]) getNet()[i + 1]); //умножили веса на входы и просумировали
            getNet()[i + 2] = activation((double[]) getNet()[i + 2]);                          //активировали
            layerF[layerFIndex] = (double[]) getNet()[i + 2];
            layerFIndex++;
        }


        //*  ПОЛУЧАЕМ ОШИБКУ СЕТИ БЕЗ Дельты*  формула error = actual-expected
        //берем самый крайний правый слой
        layerE[layerEIndex] = findErrorNetwork((double[]) getNet()[getNet().length - 1], correctAnswers); //находим ошибку сети
        layerD[layerDIndex] = findDeltaErrorMas(layerE[layerEIndex], layerF[layerFIndex - 1]);
        getNet()[(getNet().length-1) - 1] = backPropogation(   //по формуле w1=w1-input * weightDelta * alpha
                (double[]) getNet()[(getNet().length-1) - 1],  //слой весов
                (double[]) getNet()[(getNet().length - 2) - 1],//слой сигмоиды который стоит перед весом, он же inputs
                layerD[layerDIndex]      // слой ошибки дельты
        );
        layerEIndex--;
        layerFIndex--;

        // }

        for (int i = getNet().length - 2; i > 1; ) { //идем влево пересчитываем веса, i-индекс слоя весов

            //*  ПОЛУЧАЕМ ОШИБКУ СЕТИ для конкретного нейрона*  формула error = weightDelta*weight
            layerE[layerEIndex] = multiplyMasFromR(layerD[layerDIndex], (double[]) getNet()[i]);//?
            layerDIndex--;


            //* ПОЛУЧАЕМ ДЕЛЬТУ ОШИБКИ *//*
            layerD[layerDIndex] = findDeltaErrorMas(layerE[layerEIndex], layerF[layerFIndex - 1]);
            //todo tsikl zaponeniya layerD
            layerEIndex--;
            layerFIndex--;
            i -= 2;

            //* РАССЧИТЫВАЕМ НОВЫЕ ВЕСА ДЛЯ ТЕКУЩЕГО СЛОЯ*// // TODO: проверить override ли метод findWeight
            if ((i - 1) == 0) {                //если дошли до последнего левого слоя
                getNet()[i] = backPropogation( //по формуле w1=w1-input * weightDelta * alpha
                        (double[]) getNet()[i],//слой весов
                        (double[]) getNet()[i - 1],         //слой входов(0-слой), т.к сигмоиды там быть не может
                        layerD[layerDIndex]    // слой ошибки дельты
                );
            } else {
                getNet()[i] = backPropogation(   //по формуле w1=w1-input * weightDelta * alpha
                        (double[]) getNet()[i],  //слой весов
                        layerF[layerFIndex - 1], //слой сигмоиды который стоит перед весом, он же inputs
                        layerD[layerDIndex]      // слой ошибки дельты
                );

            }


        }


        return getNet();
    }


}

