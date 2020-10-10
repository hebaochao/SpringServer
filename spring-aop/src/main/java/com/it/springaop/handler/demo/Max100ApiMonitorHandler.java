package com.it.springaop.handler.demo;

import com.it.springaop.handler.impl.ApiMHCountHandler;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;

@Component
public class Max100ApiMonitorHandler  extends ApiMHCountHandler {


  public Max100ApiMonitorHandler(){
      super();
      super.maxCount = 3L;
  }


  @Override
  public boolean checkApi(Method method, Map<String, Object> params, int type, boolean isPersist) {

    boolean result = super.checkApi(method, params, type, isPersist);
    if (!result){
      return result;
    }

    if (isPersist){  //持久化

    }

    return result;
  }




}
