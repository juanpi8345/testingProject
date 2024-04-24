package com.mockito.junit5;

import com.mockito.junit5.service.impl.ServiceA;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TestServiceA {

    @Test
    public void addTest(){
        int a = 2;
        int b  =2;
        ServiceA serviceA = new ServiceA();
        assertEquals(serviceA.add(a,b),4);
    }
}
