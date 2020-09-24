package com.it.cloud.nacosclient.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/testC")
public class TestController {

  @GetMapping("/test")
  public String test(@RequestParam("name") String name){
    System.out.println("test :"+name);
      return name;

  }

}
