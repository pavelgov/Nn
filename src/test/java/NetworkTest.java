
import org.junit.Assert;
import org.junit.Test;


public class NetworkTest {


    @Test
    public void initTest() {

        Network network = new Network(4);
        network.init(2, 2, 2, 1);
        Assert.assertEquals(network.getNet().length, 7);
        Object[] net = network.getNet();
        //входы
        Assert.assertEquals(((double[]) net[0]).length, 2);
        //веса между входом и 1-м слоем
        Assert.assertEquals(((double[]) net[1]).length, 4);
        //1-й внутренний слой
        Assert.assertEquals(((double[]) net[2]).length, 2);
        //веса между 1-м слоем и 2-м
        Assert.assertEquals(((double[]) net[3]).length, 4);
        //веса 2-го внутреннего слоя
        Assert.assertEquals(((double[]) net[4]).length, 2);
        //веса между 2-м слоем и выходом
        Assert.assertEquals(((double[]) net[5]).length, 2);
        //выходной слой
        Assert.assertEquals(((double[]) net[6]).length, 1);
    }
    @Test
    public void cSharpTest() {
        double[] enters = new double[]{0.25};  //веса между входами и 1м скрытым слоем
        double[] correctAnswers = new double[]{0.061850989813630734};  //веса между входами и 1м скрытым слоем
        double[] wieght = new double[]{0.5, 0.5, 0.5, 0.5, 0.5};

        Network network = new Network(3);
        network.init(1, 5, 1);
        network.setValuesinNetLayer(1,wieght);
        network.setValuesinNetLayer(3,wieght);
        network.learn(enters,correctAnswers);
        network.compute(enters);

    }

    @Test
    public void newLearningTest() {
        double[] enters = new double[]{0.25};  //веса между входами и 1м скрытым слоем
        double[] correctAnswers = new double[]{0.061850989813630734};  //веса между входами и 1м скрытым слоем
        double[] wieght = new double[]{0.5, 0.5, 0.5, 0.5, 0.5};

        Network network = new Network(3);
        network.init(1, 5, 1);
        network.setValuesinNetLayer(1,wieght);
        network.setValuesinNetLayer(3,wieght);


        /*network.learn(enters,correctAnswers);
        network.compute(enters);
*/
    }

    @Test
    public void oneMoreInitTest() {

        Network network = new Network(3);
        network.init(1, 6, 1);
        Assert.assertEquals(network.getNet().length, 5);
        Object[] net = network.getNet();
        //входы
        Assert.assertEquals(((double[]) net[0]).length, 1);
        //веса между входом и 1-м слоем
        Assert.assertEquals(((double[]) net[1]).length, 6);
       //1-й внутренний слой
        Assert.assertEquals(((double[]) net[2]).length, 6);
        //веса между 1-м слоем и выходом
        Assert.assertEquals(((double[]) net[3]).length, 6);
        //выходной слой
        Assert.assertEquals(((double[]) net[4]).length, 1);

    }
    @Test
    public void smallTest() {
        double[] enters = new double[]{1.0, 0.5};  //веса между входами и 1м скрытым слоем
        double[] wieght = new double[]{0.9, 0.2, 0.3, 0.8};
        Network network = new Network(2);
        network.init(2, 2);
       // network.setValuesinNetLayer(0, enters);
        network.setValuesinNetLayer(1, wieght);
        network.compute(enters);
        Object[] net = network.getNet();
        Assert.assertEquals(Double.toString(((double[]) net[2])[0]),"0.7407748991821539");
        Assert.assertEquals(Double.toString(((double[]) net[2])[1]),"0.6456563062257954");
    }

    @Test
    public void setRandomWeights() {
        Network network = new Network(4);
        network.init(2, 2, 2, 1);
        network.setRandomWeights(-0.5,0.5);
        Object[] net = network.getNet();
        Assert.assertNotEquals(Double.toString(((double[]) net[1])[0]),"0.0");
        Assert.assertNotEquals(Double.toString(((double[]) net[3])[0]),"0.0");
        Assert.assertNotEquals(Double.toString(((double[]) net[5])[0]),"0.0");
        Assert.assertNotEquals(Double.toString(((double[]) net[5])[1]),"0.0");
    }
}
