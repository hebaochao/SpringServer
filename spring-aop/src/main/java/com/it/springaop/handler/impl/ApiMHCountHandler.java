package com.it.springaop.handler.impl;

import com.it.springaop.handler.ApiMonitortHandler;
import com.it.springaop.model.MHCountModel;
import com.it.springaop.model.MHMethodMap;
import com.it.springaop.model.MonitorRequestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Map;

/***
 * 统计接口调用次数，调用上限限制
 */
@Component
public class ApiMHCountHandler implements ApiMonitortHandler {

  /***
   * 内存缓存计数器
   */
  private MHMethodMap<MHCountModel>  mehMethodMap =  new MHMethodMap<>();

  /***
   * 最大调用次数
   */
  protected  Long maxCount = -1L;


  @Override
  public boolean checkApi(Method method, Map<String, Object> params, int type, boolean isPersist) {
    MHCountModel countModel = this.getMHCountModel(method,type);
    boolean result = countModel.autoAddOne();//统计+1
    this.mehMethodMap.updateObj(method, type, countModel);//更新集合
    return result;
  }


  @Override
  public Serializable returnFailResult(Method method, Map<String, Object> params, boolean result, int type, boolean isPersist) {
    MHCountModel countModel = this.getMHCountModel(method,type);
    return new MonitorRequestResult<String>(-100, " call method  count("+countModel.getCount()+") >= maxCount("+countModel.getMaxCount()+") !", null);
  }


  /***
   * 获取或创建对象
   * @param method
   * @param type
   * @return
   */
  protected   MHCountModel getMHCountModel(Method method, int type){
    MHCountModel countModel = this.mehMethodMap.getObj(method, type);
    if (countModel == null){
      countModel  = new MHCountModel(0L,maxCount);
    }
    return  countModel;
  }

}
