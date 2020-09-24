package com.it.springaop.service.impl;

import com.it.springaop.annotation.ApiMonitorRequest;
import com.it.springaop.model.MonitorRequestResult;
import com.it.springaop.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TestServiceImpl implements TestService {

  @ApiMonitorRequest
  @Override
  public MonitorRequestResult countCallNumber() {
      log.info(System.currentTimeMillis()+"start ...countCallNumber");
    return new MonitorRequestResult<String>(200,"success   countCallNumber",("hello"+System.currentTimeMillis()));
  }

  @ApiMonitorRequest(type = 1 )
  @Override
  public MonitorRequestResult testCall(String name,String email,Integer age) {
    log.info("call :"+name);
    return new MonitorRequestResult<String>(200,"success",("hello"+name));
  }

  @ApiMonitorRequest(type = 1 )
  @Override
  public String  testVoidcountCallNumber(Integer age){
     log.info("testVoidcountCallNumber");
     return "success";
  }




}
