package com.it.springaop.controller;

import com.it.springaop.model.MonitorRequestResult;
import com.it.springaop.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

  @Autowired
  private TestService testService;

  @GetMapping("/count")
  public MonitorRequestResult testCount(){
      return this.testService.countCallNumber();
  }


  @GetMapping("/countCall/{name}")
  public MonitorRequestResult countCall(@PathVariable("name") String name){
    return this.testService.testCall(name);
  }

  @GetMapping("/countAge/{age}")
  public MonitorRequestResult testVoidcountCallNumber(@PathVariable("age") Integer age){
    return this.testService.testVoidcountCallNumber(age);
  }


}
