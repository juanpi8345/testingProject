package com.mockito.junit5.service;

public interface IServiceB {
     IServiceA getServiceA();
     void setServiceA(IServiceA serviceA);

     int multiplyAdd(int a, int b, int multiplier);

     int multiply(int a, int b);
}
