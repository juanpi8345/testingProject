package com.mockito.junit5;

import com.mockito.junit5.service.impl.ServiceA;
import com.mockito.junit5.service.impl.ServiceB;
import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class TestServiceB {

    @Test
    public void multiplyTest(){
        ServiceB serviceB = new ServiceB();
        assertEquals(serviceB.multiply(2,3),6);
    }

    @Test
    public void multiplyAddTest(){
        ServiceA serviceA = new ServiceA();
        ServiceB serviceB = new ServiceB();
        serviceB.setServiceA(serviceA);
        //El servicio b tiene una dependencia que es el servicio A, si este se llega a cambiar la prueba fallaria
        //Por eso existe mockito para poder aislar una clase de sus dependencias
        assertEquals(serviceB.multiplyAdd(2,3,5),10);
    }

    @Test
    public void multiplyAddTestMockito(){
        //Simulamos la clase serviceA
        ServiceA serviceA = Mockito.mock(ServiceA.class);
        //Cuando llamemos al servicio A y le pasemos 2,3 retornara 5
        Mockito.when(serviceA.add(2,3)).thenReturn(5);

        ServiceB serviceB = new ServiceB();
        serviceB.setServiceA(serviceA);
        assertEquals(serviceB.multiplyAdd(2,3,2),10);
    }
}
