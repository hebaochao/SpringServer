package com.it.springaop.service;

import com.it.springaop.model.MonitorRequestResult;

public interface TestService {

  public MonitorRequestResult countCallNumber();

  public MonitorRequestResult testCall(String name);

  public MonitorRequestResult testVoidcountCallNumber(Integer age);

}
