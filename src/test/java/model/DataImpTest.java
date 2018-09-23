package model;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;


public class DataImpTest {

    @Test
    public void getEnters() {
        Data data = new DataImp();
        Assert.assertEquals(data.getEnters().length, 40);
    }

    @Test
    public void getAnswers() {
        Data data = new DataImp();
        Assert.assertEquals(data.getCorrectAnswers(data.getEnters()).length, 40);
    }
}
