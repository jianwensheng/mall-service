package com.oruit.share.service;

import com.alibaba.fastjson.JSONObject;
import com.oruit.share.domain.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xc
 */
public interface GoodsService {
 
    /**
     * 商品分类
     * @return
     */
    public JSONObject getGoodClassify(HttpServletRequest request);

    public JSONObject getGoodClassifyText(String cid);

    /**
     * 商品列表
     * @return
     */
    public JSONObject getGoodList(String pageId,String cids,String subcid,String sort,String pageSize,String brands);

    /**
     * 商品主页轮播
     * @return
     */
    public JSONObject getBannerList();

    /**
     * 商品高效转链
     * @return
     */
    public JSONObject getPrivilege(String goodsId);

    /**
     * 热搜关键词
     * @return
     */
    public JSONObject hotGoods();

    /**
     * 超级搜索
     * @return
     */
    public JSONObject superGoodsList(HttpServletRequest request,HashMap map);

    /**
     * 商品详情
     * @param goodsId
     * @return
     */
    public JSONObject goodsDetail(String goodsId);

    /**
     * 商品分类
     * @return
     */
    public List<TbClassifyDO> queryTbClassify(Integer levelId);

    /**
     * 首页顶部图片
     * @return
     */
    public List<TbBannerDO> queryTbBanner(Map map);

    /**
     * 9块9包邮
     * @param pageId
     * @param nineCid
     * @return
     */
    public JSONObject getApGoodList(String pageId,String nineCid,String pageSize);

    /**
     * 抽奖列表
     * @return
     */
    public List<TbLucky> getLuckyGoodsList();

    /**
     * 抽奖详情
     * @return
     */
    public TbLucky getLuckyGoods(Integer id);


    /**
     * 新人礼品购物
     * @return
     */
    public JSONObject getNewUserGiftList();

    /**
     * 淘宝联盟搜索
     * @return
     */
    public JSONObject tbGoodsList(HashMap map,HttpServletRequest request);


    /**
     * 猜你喜欢
     * @param id
     * @return
     */
    public JSONObject similerGoodsList(String id);

    /**
     * 品牌列表
     * @param cid
     * @param pageId
     * @param pageSize
     * @return
     */
    public List<TbBrand> brandList(Long cid,String pageId, String pageSize);

    /**
     * 品牌商品列表
     * @param brandIds
     * @param pageId
     * @param pageSize
     * @return
     */
    public JSONObject brandGoodList(String brandIds, String pageId, String pageSize);


    /**
     * 拼多多标签类目
     * @return
     */
    public List<TbPddOpt> getTbPddOptList();

}
