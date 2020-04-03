package com.oruit.share.service;

import com.alibaba.fastjson.JSONObject;
import com.oruit.share.domain.GoodsInfoVO;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * @author xc
 */
public interface OtherService {

     JSONObject getGoodClassify();

     JSONObject getDdqGoodList(String roundTime);

     JSONObject getThemeList();

     JSONObject getThemeGoodsList(Long themeId);

     String getCmsPromUrl(Integer channelType);

     JSONObject getPddSearchList(HashMap map,HttpServletRequest request);

     JSONObject getPddGoodsDetailInfo(Long goodId);

     String getPddGoodsPromotionUrl(Long goodId);

    /**
     * 根据店铺ID查询店铺信息
     * @param mallId
     * @return
     */
    JSONObject getMallInfo(String mallId);

    String createCouponPoster(HttpServletRequest request, Long userId, GoodsInfoVO vo) throws Exception;

    String createInvitationImg(String inviteCode,String token) throws Exception;

}
