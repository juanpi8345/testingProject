package com.mockito.junit5.service.impl;

import com.mockito.junit5.service.IServiceA;
import com.mockito.junit5.service.IServiceB;

public class ServiceB implements IServiceB {

    private IServiceA serviceA;

    @Override
    public IServiceA getServiceA() {
        return serviceA;
    }

    @Override
    public void setServiceA(IServiceA serviceA) {
        this.serviceA = serviceA;
    }

    @Override
    public int multiplyAdd(int a, int b, int multiplier) {
        return serviceA.add(a,b) * multiplier;
    }

    @Override
    public int multiply(int a, int b) {

        return a*b;
    }
}
