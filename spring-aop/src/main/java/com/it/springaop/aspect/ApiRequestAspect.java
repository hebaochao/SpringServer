package com.it.springaop.aspect;

import com.it.springaop.annotation.ApiMonitorRequest;
import com.it.springaop.model.JoinPointHandler;
import com.it.springaop.model.MonitorRequestResult;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class ApiRequestAspect {

  @Around(value = "@annotation(com.it.springaop.annotation.ApiMonitorRequest)")
  public Object around(ProceedingJoinPoint pjd) throws Throwable {
    Object result = null;
    JoinPointHandler joinPointHandler = JoinPointHandler.buildJoinPointParam(pjd);
    boolean checkResult = this.checkApi(joinPointHandler);
    if (checkResult) {
      //前置通知
      log.info("executeMethod :..."+joinPointHandler.getSignature().getMethod().getName());
      result = joinPointHandler.executeMethod();
    } else {
      return this.getDefaultResult();
    }
    return result;
  }

  /***
   * 实现判断业务逻辑
   * @param joinPointHandler
   * @return
   */
  public boolean checkApi(JoinPointHandler joinPointHandler) {
    ApiMonitorRequest apiMonitorRequest = joinPointHandler.getSignature().getMethod().getAnnotation(ApiMonitorRequest.class);
    log.info("check apit annotation　type :" + apiMonitorRequest.type());
    if (apiMonitorRequest.type() == 1) {
      Integer age = (Integer) joinPointHandler.getParamByKey("age");
      if (age.intValue() > 18) {
        return true;
      }
    } else {
      return true;
    }
    return false;
  }

  /***
   * 实现默认失败结果模型
   * @return
   */
  public Object getDefaultResult() {
    return new MonitorRequestResult<String>(200, "age must > 18 ", null);
  }

}
