package com.it.springaop.aspect;

import com.it.springaop.annotation.ApiMonitorRequest;
import com.it.springaop.handler.ApiMonitortHandler;
import com.it.springaop.model.JoinPointHandler;
import com.it.springaop.model.MonitorRequestResult;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Aspect
@Slf4j
@Component
public class ApiRequestAspect {

  @Autowired
  private ApplicationContext applicationContext;
  /*
   **
   * 未执行返回默认错误消息
   */
  private Serializable defaultResult = new MonitorRequestResult<String>(-1, "api not execute", null);

  @Around(value = "@annotation(com.it.springaop.annotation.ApiMonitorRequest)")
  public Object around(ProceedingJoinPoint pjd) throws Throwable {
    Object result = null;
    JoinPointHandler joinPointHandler = JoinPointHandler.buildJoinPointParam(pjd);
    ApiMonitorRequest myAnnotation = joinPointHandler.getMethod().getAnnotation(ApiMonitorRequest.class);
    log.info(myAnnotation.type() + "...");// 参数p0的值
    ApiMonitortHandler apiMonitortHandler = this.createApiMonitortHandler(myAnnotation);
    boolean checkResult = apiMonitortHandler != null ? apiMonitortHandler.checkApi(joinPointHandler.getMethod(), joinPointHandler.getParams(), myAnnotation.type(), myAnnotation.isPersist()) : true;
    if (checkResult) {
      result = joinPointHandler.executeMethod();
    } else {
      result = apiMonitortHandler != null ? apiMonitortHandler.returnFailResult(joinPointHandler.getMethod(), joinPointHandler.getParams(), checkResult, myAnnotation.type(), myAnnotation.isPersist()) : this.defaultResult;
    }
    return result;
  }

  private ApiMonitortHandler createApiMonitortHandler(ApiMonitorRequest myAnnotation) {
    ApiMonitortHandler apiMonitortHandler = null;
    try {
      apiMonitortHandler = this.applicationContext.getBean(myAnnotation.handler());
    } catch (Exception e) {
      e.printStackTrace();
    }
    return apiMonitortHandler;
  }

}
