package com.oruit.share.service;

import java.util.List;
import java.util.Map;

/**
 * @author xc
 */
public interface TaobaoService {

    /**
     * 商品列表接口
     * @return
     */
    public List<Map<String,Object>> getGoodList(Integer pageNo, Integer pageSize);
}
