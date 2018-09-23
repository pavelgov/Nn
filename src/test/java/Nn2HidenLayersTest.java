import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class Nn2HidenLayersTest {
    double otvet;
    double[] enters;
    double[] wEh;
    double[] wHh;
    double[] wHo;
    double[] hiddenOne;
    double[] hiddenTwo;
    double[] out;
    final double SPEEDLEARN = 0.025;



    @Before
    public void init() {
        enters = new double[2];
        hiddenOne = new double[2];
        hiddenTwo = new double[2];
        out = new double[1];
        this.wEh = new double[enters.length * hiddenOne.length];
        this.wHh = new double[enters.length * hiddenTwo.length];
        this.wHo = new double[hiddenTwo.length * out.length];


        enters[0] = 1;
        enters[1] = 0;

        wEh = new double[]{0.13, -0.34, -0.42, 0.38};  //веса между входами и 1м скрытым слоем
        wHh = new double[]{0.25, 0.07, -0.20, 0.32};   //веса 1м скрытым слоем и веса
        wHo = new double[]{-0.41, 0.12};               //веса веса и выходом

    }


   /* @Test()
    public void testS() {
        double[] hiddenOne = Nn.sumMatrix(enters, wEh);  //умножили веса на входы и просумировали
        double[] hiddenOneS = hiddenOne;
        Assert.assertEquals(Double.toString(hiddenOne[0]), "0.13");
        Assert.assertEquals(Double.toString(hiddenOne[1]), "-0.34");
        hiddenOne = Nn.activation(hiddenOne);

        hiddenTwo = Nn.sumMatrix(hiddenOne, wHh);
        Assert.assertEquals(Double.toString(hiddenTwo[0]), "0.049951681183911134");
        Assert.assertEquals(Double.toString(hiddenTwo[1]), "0.170330834107782");
        hiddenTwo= Nn.activation(hiddenTwo);

        out = Nn.sumMatrix(hiddenTwo,wHo);
        Assert.assertEquals(Double.toString(out[0]), "-0.14502137655747732"); //ответ сети до активации
        out = Nn.activation(out);
        Assert.assertEquals(Double.toString(out[0]), "0.46380806362494087");  //ответ сети после акитвации

        double[] correctAnswer = new double[]{1.0};
        out = Nn.findErrorNetwork(out,correctAnswer);
        Assert.assertEquals(Double.toString(out[0]), "0.5361919363750591"); // out теперь d

        hiddenTwo = Nn.sumMatrixBack(out,wHo);
        Assert.assertEquals(Double.toString(hiddenTwo[0]), "-0.21983869391377422");
        Assert.assertEquals(Double.toString(hiddenTwo[1]), "0.06434303236500709");

        hiddenOne = Nn.sumMatrixBack(hiddenTwo,wHh);
        Assert.assertEquals(Double.toString(hiddenOne[0]), "-0.05045566121289306");
        Assert.assertEquals(Double.toString(hiddenOne[1]), "0.06455750913955711");

        wEh = Nn.backPropogation(enters,wEh,hiddenOne,hiddenOneS);
        Assert.assertEquals(Double.toString(wEh[0][0]), "0.1294293464716822");
        Assert.assertEquals(Double.toString(wEh[0][1]), "-0.34057065352831783");
        Assert.assertEquals(Double.toString(wEh[1][0]), "-0.42");
        Assert.assertEquals(Double.toString(wEh[1][1]), "0.38");
    }*/

    @Test
    public void anotherTestS() {

        Network network = new Network(4);
        network.init(2, 2, 2, 1);
        network.setValuesinNetLayer(0, enters);
        network.setValuesinNetLayer(1, wEh);
        network.setValuesinNetLayer(3, wHh);
        network.setValuesinNetLayer(5, wHo);
        double[] hiddenTwoF;
        double[] hiddenOneF;
        double[] outF;
        double[] hiddenTwoD;
        double[] hiddenOneD;
        double[] outD;

        double[] hiddenOne = roundMas(network.multiplyMasFromL(enters, wEh),2);  //умножили веса на входы и просумировали

        Assert.assertEquals(Double.toString(hiddenOne[0]), "0.13");
        Assert.assertEquals(Double.toString(hiddenOne[1]), "-0.34");

        hiddenOne =roundMas(network.activation(hiddenOne),2);
        Assert.assertEquals(Double.toString(hiddenOne[0]), "0.53");
        Assert.assertEquals(Double.toString(hiddenOne[1]), "0.42");
        hiddenOneF = hiddenOne;

        hiddenTwo =roundMas(network.multiplyMasFromL(hiddenOne, wHh),2);
        Assert.assertEquals(Double.toString(hiddenTwo[0]), "0.05");
        Assert.assertEquals(Double.toString(hiddenTwo[1]), "0.17");

        hiddenTwo = roundMas(network.activation(hiddenTwo),2);
        Assert.assertEquals(Double.toString(hiddenTwo[0]), "0.51");
        Assert.assertEquals(Double.toString(hiddenTwo[1]), "0.54");
        hiddenTwoF = hiddenTwo;

        out = roundMas(network.multiplyMasFromL(hiddenTwo, wHo),2);
        Assert.assertEquals(Double.toString(out[0]), "-0.14"); //ответ сети до активации

        out = roundMas(network.activation(out),2);
        outF = out;
        Assert.assertEquals(Double.toString(out[0]), "0.47");  //ответ сети после акитвации

        double[] correctAnswer = new double[]{1.0};
        out = network.findErrorNetwork(out, correctAnswer);
        Assert.assertEquals(Double.toString(out[0]), "0.53"); // out теперь d
        outD=out;

        hiddenTwo = roundMas(network.multiplyMasFromR(out, wHo),2);
        hiddenTwoD = hiddenTwo;
        Assert.assertEquals(Double.toString(hiddenTwo[0]), "-0.22");
        Assert.assertEquals(Double.toString(hiddenTwo[1]), "0.06");

        hiddenOne = roundMas(network.multiplyMasFromR(hiddenTwo, wHh),2);
        hiddenOneD = hiddenOne;
        Assert.assertEquals(Double.toString(hiddenOne[0]), "-0.05");
        Assert.assertEquals(Double.toString(hiddenOne[1]), "0.06");

        wEh =roundMas(network.backPropogation(wEh, hiddenOneD,hiddenOneF, enters),3);
        Assert.assertEquals(Double.toString(wEh[0]), "0.13");//TO DO проверить значения
        Assert.assertEquals(Double.toString(wEh[1]), "-0.34");//TO DO проверить значения
        Assert.assertEquals(Double.toString(wEh[2]), "-0.42");
        Assert.assertEquals(Double.toString(wEh[3]), "0.38");

        wEh[1] =-0.338; //в примере была ошибка округления

        wHh = roundMas(network.backPropogation(wHh,hiddenTwoD, hiddenTwoF,hiddenOneF),3);
        Assert.assertEquals(Double.toString(wHh[0]), "0.247");
        Assert.assertEquals(Double.toString(wHh[1]), "0.071");
        Assert.assertEquals(Double.toString(wHh[2]), "-0.202");
        Assert.assertEquals(Double.toString(wHh[3]), "0.321");

        wHo = roundMas(network.backPropogation(wHo,outD ,outF,hiddenTwoF),3);
        Assert.assertEquals(Double.toString(wHo[0]), "-0.403");
        Assert.assertEquals(Double.toString(wHo[1]), "0.127");
    }

    @Test
    public void networkComputeTest() {
        Network network = new Network(4);
        network.init(2, 2, 2, 1);

        network.setValuesinNetLayer(1, wEh);
        network.setValuesinNetLayer(3, wHh);
        network.setValuesinNetLayer(5, wHo);

        Assert.assertEquals(Double.toString(network.compute(enters)[0]),"0.46380806362494087");
    }

    @Test
    public void networkLearnTest() {
        Network network = new Network(4);
        network.init(2, 2, 2, 1);

        network.setValuesinNetLayer(1, wEh);
        network.setValuesinNetLayer(3, wHh);
        network.setValuesinNetLayer(5, wHo);
        double[] answers = new double[]{1};
        network.learn(enters,answers);



       /* Assert.assertEquals(Double.toString(((double[]) network.getNet()[1])[0]), "0.1287439228736686"); //TODO
        Assert.assertEquals(Double.toString(((double[]) network.getNet()[1])[1]), "-0.3384318209190244");*/
        Assert.assertEquals(Double.toString(((double[]) network.getNet()[1])[2]), "-0.42");
        Assert.assertEquals(Double.toString(((double[]) network.getNet()[1])[3]), "0.38");


        Assert.assertEquals(Double.toString(((double[]) network.getNet()[3])[0]), "0.24707547319545364");
        Assert.assertEquals(Double.toString(((double[]) network.getNet()[3])[1]), "0.07085031076117705");
        Assert.assertEquals(Double.toString(((double[]) network.getNet()[3])[2]), "-0.20228385036363145");
        Assert.assertEquals(Double.toString(((double[]) network.getNet()[3])[3]), "0.3206640330798456");

        Assert.assertEquals(Double.toString(((double[]) network.getNet()[5])[0]), "-0.40316623114514627");
        Assert.assertEquals(Double.toString(((double[]) network.getNet()[5])[1]), "0.12723373551924091");

    }

    private double round(double value, int scale){
        return Math.round(value*Math.pow(10, scale)) / Math.pow(10, scale);
    }

    private double[] roundMas(double[] mas,int scale ){
        for (int i = 0; i <mas.length ; i++) {
             mas[i] = round(mas[i],scale);
        }
       return mas;
    }
}

