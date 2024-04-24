package com.mockito.junit5.service.impl;

import com.mockito.junit5.service.IServiceA;

public class ServiceA implements IServiceA {
    @Override
    public int add(int a, int b) {
        return a + b;
    }
}
