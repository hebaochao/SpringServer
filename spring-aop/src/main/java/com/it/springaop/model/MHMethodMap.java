package com.it.springaop.model;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class MHMethodMap<T> implements Serializable {

  private Map<String, T> methodsMap = new HashMap<>();

  public boolean containsKey(Method method, int type) {
    return this.methodsMap.containsKey(this.buildMethodKey(method, type));
  }

  public String buildMethodKey(Method method, int type) {
    return method.getDeclaringClass().getName()+"_"+method.getName() + "_" + type;
  }

  public T getObj(Method method, int type) {
    T result = this.methodsMap.get(this.buildMethodKey(method, type));
    return result;
  }


  public void updateObj(Method method, int type, T obj) {
    synchronized (methodsMap) {
      this.methodsMap.put(this.buildMethodKey(method, type), obj);
    }
  }



}
