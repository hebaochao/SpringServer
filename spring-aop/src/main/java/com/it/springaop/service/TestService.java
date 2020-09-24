package com.it.springaop.service;

import com.it.springaop.model.MonitorRequestResult;
import org.springframework.stereotype.Service;


public interface TestService {

  public  MonitorRequestResult  countCallNumber();

  public  MonitorRequestResult  testCall(String name,String email,Integer age);
  public String  testVoidcountCallNumber(Integer age);

}
