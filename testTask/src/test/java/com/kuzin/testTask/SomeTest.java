package com.kuzin.testTask;

import org.junit.Assert;
import org.junit.Test;

public class SomeTest {

    @Test
    public void test(){

        int a=5;
        int b=2;

        Assert.assertEquals(10,a*b);
    }
}
