import model.DataImp;
import org.junit.Before;
import org.junit.Test;

public class NetworkWithNewLearnTest {
    double[] enters;
    double[] answers;
    double[] wEh;
    double[] wHh;
    double[] wHo;
    double[] hiddenOne;
    double[] hiddenTwo;
    double[] out;
    final double SPEEDLEARN = 0.01;


    @Before
    public void init() {
        enters = new double[2];
        answers = new double[1];
        hiddenOne = new double[2];
        hiddenTwo = new double[2];
        out = new double[1];
        this.wEh = new double[enters.length * hiddenOne.length];
        this.wHh = new double[enters.length * hiddenTwo.length];
        this.wHo = new double[hiddenTwo.length * out.length];


        enters[0] = 1;
        enters[1] = 0;
        answers[0] = 1;

        wEh = new double[]{0.13, -0.34, -0.42, 0.38};  //веса между входами и 1м скрытым слоем
        wHh = new double[]{0.25, 0.07, -0.20, 0.32};   //веса 1м скрытым слоем и веса
        wHo = new double[]{-0.41, 0.12};               //веса веса и выходом

    }

    @Test
    public void testLearn() {
       /* DataImp data = new DataImp();
        NetworkWithNewLearn network1L = new NetworkWithNewLearn(3);
        network1L.init(2, 2, 1);
        network1L.setValuesinNetLayer(0, enters);
        network1L.setValuesinNetLayer(1, wEh);
        network1L.setValuesinNetLayer(3, wHo);
        network1L.learn(enters, answers);
*/
        NetworkWithNewLearn network2L = new NetworkWithNewLearn(4);
        network2L.init(2, 2, 2, 1);
        network2L.setValuesinNetLayer(0, enters);
        network2L.setValuesinNetLayer(1, wEh);
        network2L.setValuesinNetLayer(3, wHh);
        network2L.setValuesinNetLayer(5, wHo);
        network2L.learn(enters, answers);
    }
}