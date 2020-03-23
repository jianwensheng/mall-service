package com.oruit.share.service;

import com.alibaba.fastjson.JSONObject;
import com.oruit.share.domain.GoodsInfoVO;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * @author xc
 */
public interface OtherService {

    public JSONObject getGoodClassify();

    public JSONObject getDdqGoodList(String roundTime);

    public JSONObject getThemeList();

    public JSONObject getThemeGoodsList(Long themeId);

    public String getCmsPromUrl(Integer channelType);

    public JSONObject getPddSearchList(HashMap map,HttpServletRequest request);

    public JSONObject getPddGoodsDetailInfo(Long goodId);

    public String getPddGoodsPromotionUrl(Long goodId);

    /**
     * 根据店铺ID查询店铺信息
     * @param mallId
     * @return
     */
    public JSONObject getMallInfo(String mallId);

    public String createCouponPoster(HttpServletRequest request, Long userId, GoodsInfoVO vo) throws Exception;

}
