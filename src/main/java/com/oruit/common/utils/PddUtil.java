package com.oruit.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.oruit.common.utils.cache.redis.RedisUtil;
import com.pdd.pop.sdk.common.util.JsonUtil;
import com.pdd.pop.sdk.http.PopClient;
import com.pdd.pop.sdk.http.PopHttpClient;
import com.pdd.pop.sdk.http.api.request.*;
import com.pdd.pop.sdk.http.api.response.*;
import lombok.extern.slf4j.Slf4j;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class PddUtil {

    public static final String clientSecret = "c3a374fcadaddbc9568bf76d1911de71aae7235c";//应用sercret

    public static final String clientId = "90d396de5d6f4d5eb8927a9677d83e6a"; //应用key

    /**
     * 拼多多主题信息
     * @return
     * @throws Exception
     */
    public static String pddDdkThemeList() throws Exception{
        PopClient client = new PopHttpClient(clientId, clientSecret);
        PddDdkThemeListGetRequest request = new PddDdkThemeListGetRequest();
        request.setPageSize(10);
        request.setPage(1);
        PddDdkThemeListGetResponse response = client.syncInvoke(request);
        String json = JsonUtil.transferToJson(response);
        return json;
    }

    /**
     * 主题商品列表
     * @param themeId
     * @return
     * @throws Exception
     */
    public static String pddDdkThemeGoodsList(Long themeId) throws Exception{
        PopClient client = new PopHttpClient(clientId, clientSecret);
        PddDdkThemeGoodsSearchRequest request = new PddDdkThemeGoodsSearchRequest();
        request.setThemeId(themeId);
        PddDdkThemeGoodsSearchResponse response = client.syncInvoke(request);
        String json = JsonUtil.transferToJson(response);
        return json;
    }

    public static String pddPddDdkTopGoodsList(Integer sortType)throws Exception{
        PopClient client = new PopHttpClient(clientId, clientSecret);
        PddDdkTopGoodsListQueryRequest request = new PddDdkTopGoodsListQueryRequest();
        request.setOffset(0);
        request.setSortType(sortType);
        request.setLimit(10);
        PddDdkTopGoodsListQueryResponse response = client.syncInvoke(request);
        String json = JsonUtil.transferToJson(response);
        return json;
    }

    /**
     * 获取推广ID
     * @return
     * @throws Exception
     * 测试推广位:9788423_129408501
     */
    public static String PddDdkGoodsPid()throws Exception{
        PopClient client = new PopHttpClient(clientId, clientSecret);
        PddDdkGoodsPidQueryRequest request = new PddDdkGoodsPidQueryRequest();
        request.setPage(1);
        request.setPageSize(10);
        PddDdkGoodsPidQueryResponse response = client.syncInvoke(request);
        String json = JsonUtil.transferToJson(response);
        return json;
    }

    /**
     * 获取拼多多商品类目
     * @return
     * @throws Exception
     */
    public static String PddGoodsCatsGet()throws Exception{
        PopClient client = new PopHttpClient(clientId, clientSecret);
        PddGoodsCatsGetRequest request = new PddGoodsCatsGetRequest();
        request.setParentCatId(0L);
        PddGoodsCatsGetResponse response = client.syncInvoke(request);
        String json = JsonUtil.transferToJson(response);
         return json;
    }

    /**
     * 商品标签类目
     * @return
     * @throws Exception
     */
    public static String PddGoodsOptGet()throws Exception{
        PopClient client = new PopHttpClient(clientId, clientSecret);
        PddGoodsOptGetRequest request = new PddGoodsOptGetRequest();
        request.setParentOptId(0);
        PddGoodsOptGetResponse response = client.syncInvoke(request);
        String json = JsonUtil.transferToJson(response);
        return json;
    }

    /**
     * 生成商品短链
     * @return
     * @throws Exception
     */
    public static String PddDdkGoodsPromotionUrl(Long goodId)throws Exception{
        PopClient client = new PopHttpClient(clientId, clientSecret);
        PddDdkGoodsPromotionUrlGenerateRequest request = new PddDdkGoodsPromotionUrlGenerateRequest();
        request.setPId("9788423_129408501");
        List<Long> goodsIdList = new ArrayList<Long>();
        goodsIdList.add(goodId);
        request.setGoodsIdList(goodsIdList);
        request.setGenerateShortUrl(false);
        request.setMultiGroup(false);
        request.setCustomParameters("str");
        request.setGenerateWeappWebview(true);
        request.setGenerateWeApp(false);
        request.setGenerateWeiboappWebview(false);
        request.setGenerateMallCollectCoupon(false);
        request.setGenerateSchemaUrl(false);
        request.setGenerateQqApp(false);
        PddDdkGoodsPromotionUrlGenerateResponse response = client.syncInvoke(request);
        String json = JsonUtil.transferToJson(response);
        return json;
    }

    /**
     * 多多进宝商品查询
     * @param map
     * @return
     * @throws Exception
     */
    public static String PddDdkGoodsSearch(HashMap map) throws Exception{
        PopClient client = new PopHttpClient(clientId, clientSecret);
        PddDdkGoodsSearchRequest request = new PddDdkGoodsSearchRequest();
        if(map.containsKey("keyWords") && StringUtils.isNotEmpty(map.get("keyWords").toString())){
            request.setKeyword(map.get("keyWords").toString());
        }
        if(map.containsKey("opt_id") && StringUtils.isNotEmpty(map.get("opt_id").toString())){
            request.setOptId(Long.valueOf(map.get("opt_id").toString()));
        }
        String pageNo = map.get("pageNo").toString();
        String pageSize = map.get("pageSize").toString();
        request.setPage(Integer.parseInt(pageNo));
        request.setPageSize(Integer.parseInt(pageSize));
        if(map.containsKey("sort_type") && map.get("sort_type")!=null){
            request.setSortType(Integer.parseInt(map.get("sort_type").toString()));
        }else{
            request.setSortType(0);//排序 排序方式:0-综合排序;1-按佣金比率升序;2-按佣金比例降序;3-按价格升序;4-按价格降序;5-按销量升序;6-按销量降序;7-优惠券金额排序升序;8-优惠券金额排序降序;
        }
        request.setWithCoupon(Boolean.valueOf(map.get("hasCoupon").toString()!=null?map.get("hasCoupon").toString():"false"));
        request.setPid("9788423_129408501");
        PddDdkGoodsSearchResponse response = client.syncInvoke(request);
        String json = JsonUtil.transferToJson(response);
        return json;
    }

    /**
     * 多多进宝商品详情
     * @param goodId
     * @return
     * @throws Exception
     */
    public static String PddDdkGoodsDetail(Long goodId) throws Exception{
        PopClient client = new PopHttpClient(clientId, clientSecret);
        PddDdkGoodsDetailRequest request = new PddDdkGoodsDetailRequest();
        List<Long> goodsIdList = new ArrayList<Long>();
        goodsIdList.add(goodId);
        request.setGoodsIdList(goodsIdList);
        request.setPid("9788423_129408501");
        request.setCustomParameters("str");
        request.setZsDuoId(0L);
        PddDdkGoodsDetailResponse response = client.syncInvoke(request);
        String json = JsonUtil.transferToJson(response);
        return json;
    }

    /**
     * 根据店铺ID查询店铺信息
     * @param mallId
     * @return
     * @throws Exception
     */
    public static String PddDdkMerchantListGet(Long mallId) throws Exception{
        PopClient client = new PopHttpClient(clientId, clientSecret);

        PddDdkMerchantListGetRequest request = new PddDdkMerchantListGetRequest();
        List<Long> mallIdList = new ArrayList<Long>();
        mallIdList.add(mallId);
        request.setMallIdList(mallIdList);
        PddDdkMerchantListGetResponse response = client.syncInvoke(request);
        String json = JsonUtil.transferToJson(response);
        return json;
    }

    public static JSONObject getDdqGoodList(String roundTime) {
        TreeMap<String, String> paraMap = new TreeMap<>();
        paraMap.put("version", "v1.2.0");
        String good_ddq_key = MethodUtil.good_ddq_key;
        if (StringUtils.isNotEmpty(roundTime)) {
            try {
                good_ddq_key = MethodUtil.good_ddq_key + "_time_" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(roundTime).getTime();
                paraMap.put("roundTime", roundTime);
            } catch (Exception e) {
                e.getMessage();
            }
        }
        JSONObject jsonObject = JSONObject.parseObject(HttpUtils.sendGet(MethodUtil.ddq_good_url, MethodUtil.postContent(paraMap)), JSONObject.class);
        return jsonObject;
    }

    public static String PddDdkCmsPromUrlGenerate(Integer channelType)throws Exception{

        PopClient client = new PopHttpClient(clientId, clientSecret);
        PddDdkCmsPromUrlGenerateRequest request = new PddDdkCmsPromUrlGenerateRequest();
        request.setGenerateShortUrl(true);
        request.setGenerateMobile(true);
        request.setMultiGroup(true);
        request.setCustomParameters("tag"+"_"+channelType);
        request.setGenerateWeappWebview(false);
        request.setChannelType(channelType);
        List<String> pIdList = new ArrayList<String>();
        pIdList.add("9788423_129408501");
        request.setPIdList(pIdList);
        request.setGenerateSchemaUrl(false);
        PddDdkCmsPromUrlGenerateResponse response = client.syncInvoke(request);
        String json = JsonUtil.transferToJson(response);

        return json;
    }

    /**
     *
     * @param resourceType
     * @return 频道来源：4-限时秒杀,39997-充值中心, 39998-转链type，39999-电器城，39996-百亿补贴
     * @throws Exception
     */
    public static String PddDdkResourceUrlGen(Integer resourceType){
        String key = MethodUtil.pdd_cms_url_key + resourceType;
        String obj = RedisUtil.get(key);
        if(obj!=null){
            return obj;
        }
        PopClient client = new PopHttpClient(clientId, clientSecret);
        PddDdkResourceUrlGenRequest request = new PddDdkResourceUrlGenRequest();
        request.setCustomParameters("str");
        request.setGenerateQqApp(false);
        request.setGenerateSchemaUrl(false);
        request.setGenerateWeApp(false);
        request.setPid("9788423_129408501");
        request.setResourceType(resourceType);
        request.setUrl("str");
        try{
            PddDdkResourceUrlGenResponse response = client.syncInvoke(request);
            String json = JsonUtil.transferToJson(response);
            JSONObject jsonObject = JSONObject.parseObject(json,JSONObject.class);
            JSONObject jsonObj = JSONObject.parseObject(((JSONObject)jsonObject.get("resource_url_response")).get("single_url_list").toString());
            String shortUrl = jsonObj.get("short_url").toString();
            if(StringUtils.isNotEmpty(shortUrl)){
                return shortUrl;
            }
        }catch (Exception e){
            log.error("PddDdkResourceUrlGen error:{}",e.getMessage());
        }

        return null;
    }

    public static String PddDdkTopGoodsListQuery(Map map) throws Exception{
        String sortType = map.get("sortType").toString();
        String limit = map.get("limit").toString();

        String key = MethodUtil.pdd_top_goods_list_query_key + "_sortType_"+sortType+"_limit_"+limit;
        String obj = RedisUtil.get(key);
        if(obj!=null){
            return obj;
        }
        PopClient client = new PopHttpClient(clientId, clientSecret);
        PddDdkTopGoodsListQueryRequest request = new PddDdkTopGoodsListQueryRequest();
        request.setPId("9788423_129408501");
        request.setOffset(0);
        request.setSortType(Integer.parseInt(sortType));
        request.setLimit(Integer.parseInt(limit));
        PddDdkTopGoodsListQueryResponse response = client.syncInvoke(request);
        String json = JsonUtil.transferToJson(response);
        RedisUtil.setByTime(key,json,MethodUtil.expires);
        return json;
    }


    public static void main(String[] args) {
       try{
            HashMap map = new HashMap();
            map.put("keyWords","【纯棉】 宽松弹力单件两件大码短袖t恤女夏装韩版胖mm白色上衣女");
            map.put("pageNo","1");
            map.put("pageSize","10");
           map.put("hasCoupon","true");

            System.out.println(PddDdkGoodsSearch(map));
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

}
