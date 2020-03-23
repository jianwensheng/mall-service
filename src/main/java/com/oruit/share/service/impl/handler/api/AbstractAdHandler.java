package com.oruit.share.service.impl.handler.api;


import java.util.List;
import java.util.Map;


/**
 * @author: wangyt
 * @date: 2019-08-23 10:17
 * @description: TODO
 */
public interface  AbstractAdHandler {

     List<Map<String, Object>> handle(List<Map<String, Object>> lsOriginalAdList,String domain,String ggc,String source,Integer type,Integer articleid,Integer useCurrentPageDomain,boolean isNoSexAdArticle,String uaid);

}
