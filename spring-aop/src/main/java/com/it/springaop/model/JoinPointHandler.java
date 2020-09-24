package com.it.springaop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinPointHandler implements Serializable {

  /***
   * 参数列表以及其属性
   */
  private Map<String, Object> params = new LinkedHashMap<>();
  private ProceedingJoinPoint pjd;
  private MethodSignature signature;


  /***
   * 便捷构建
   * @param pjd
   * @return
   */
  public static JoinPointHandler buildJoinPointParam(ProceedingJoinPoint pjd) {
    JoinPointHandler joinPointParam = new JoinPointHandler();
    /*获取signature 该注解作用在方法上，强转为 MethodSignature*/
    MethodSignature signature = (MethodSignature) pjd.getSignature();
    joinPointParam.setParams(getJoinPointRequestParams(pjd));
    joinPointParam.setSignature(signature);
    joinPointParam.setPjd(pjd);
    return joinPointParam;
  }





  /****
   * 获取切点中的方法参数
   * @param pjd
   * @return
   */
  public static Map<String, Object> getJoinPointRequestParams(ProceedingJoinPoint pjd) {
    Object[] values = pjd.getArgs();
    /*获取signature 该注解作用在方法上，强转为 MethodSignature*/
    MethodSignature signature = (MethodSignature) pjd.getSignature();
    /*参数名称数组(与args参数值意义对应)*/
    String[] parameterNames = signature.getParameterNames();            //  [i] 参数名称
    Map<String, Object> params = new HashMap<>();
    if (parameterNames != null && values.length == parameterNames.length) {
      for (int i = 0; i < parameterNames.length; i++) {
        params.put(parameterNames[i], values[i]);
      }
    }
    return params;
  }

  /***
   * 執行方法
   * @return
   * @throws Throwable
   */
  public Object executeMethod() throws Throwable {
    return this.executeMethod(null);
  }

  /***
   * 执行方法
   * @param params
   * @return
   * @throws Throwable
   */
  public Object executeMethod(Object[] params) throws Throwable {
    Object result = null;
    /*获取返回值类型*/
    Class returnType = this.signature.getReturnType();
    if (returnType == Void.TYPE) { //执行目标方法
      this.pjd.proceed();
    } else {
      Object[] args = params == null ? this.pjd.getArgs() : params;
      result = this.pjd.proceed(args);
    }
    return result;
  }

  /***
   * 获取参数值
   * @param paramName
   * @return
   */
  public Object getParamByKey(String paramName) {
    if (!this.params.containsKey(paramName)) {
      throw new RuntimeException("not exit param name!");
    }
    return this.params.get(paramName);
  }


}
