package com.oruit.share.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.oruit.common.utils.*;
import com.oruit.common.utils.cache.redis.RedisUtil;
import com.oruit.common.utils.web.RequestUtils;
import com.oruit.share.dao.*;
import com.oruit.share.domain.*;
import com.oruit.share.service.GoodsService;
import com.oruit.share.service.OtherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

@Service
@Slf4j
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private TbClassifyDOMapper tbClassifyDOMapper;

    @Autowired
    private TbBannerDOMapper tbBannerDOMapper;

    @Autowired
    private TbLuckyMapper tbLuckyMapper;

    @Autowired
    private OtherService otherService;

    @Autowired
    private TbBrandMapper tbBrandMapper;

    @Autowired
    private TbPddOptMapper tbPddOptMapper;

    @Override
    public JSONObject getGoodClassify(HttpServletRequest request) {
        //先从redis里面查询
        String classify_key = MethodUtil.good_classfiy_key;
        String obj = RedisUtil.get(classify_key);
        if(obj!=null){
           return JSONObject.parseObject(obj,JSONObject.class);
        }
        TreeMap<String,String> paraMap = new TreeMap<>();
        paraMap.put("version","v1.1.0");
        JSONObject jsonObject = JSONObject.parseObject(HttpUtils.sendGet(MethodUtil.category_url, MethodUtil.postContent(paraMap)),JSONObject.class);
        List<JSONObject> list = new ArrayList<JSONObject>();
        if(jsonObject.get("code").equals(0)){
            //请求成功
            JSONArray jsonArray = JSONObject.parseObject(jsonObject.get("data").toString(),JSONArray.class);

            for(int i=0;i<jsonArray.size();i++){
                JSONObject json = (JSONObject) jsonArray.get(i);
                json.put("order",i+1);
                list.add(json);
                //存入redis
                JSONArray subcategoriesArray = JSONObject.parseObject(json.get("subcategories").toString(),JSONArray.class);
                String subcategoriesText = " <section class=\"menu-right padding-all j-content\">\n" +
                        "    <h5>"+json.get("cname")+"</h5>\n" +
                        "    <ul>\n" ;
                for(int j=0;j<subcategoriesArray.size();j++){
                    JSONObject subObj = (JSONObject)subcategoriesArray.get(j);
                    subcategoriesText +="<li class=\"w-3\"><a href=\""+ RequestUtils.getBasePath(request)+"/good/classfyGoodList?subcid="+subObj.get("subcid")+"\"></a> <img src=\""+subObj.get("scpic")+"\"><span>"+subObj.get("subcname")+"</span></li>\n";
                }
                subcategoriesText += "    </ul>\n" ;
                RedisUtil.setByTime(MethodUtil.good_classfiy_info_key+json.get("cid"),JsonDealUtil.getSuccJSONObject("0|操作成功", String.valueOf(subcategoriesArray.size()), subcategoriesText).toJSONString(),MethodUtil.expires);
            }
            if(list.size()>0){
                RedisUtil.setByTime(MethodUtil.good_classfiy_key,JsonDealUtil.getSuccJSONObject("0|操作成功", String.valueOf(list.size()), list).toJSONString(),MethodUtil.expires);
            }
        }
        return JsonDealUtil.getSuccJSONObject("0|操作成功", String.valueOf(list.size()), list);
    }

    @Override
    public JSONObject getGoodClassifyText(String cid) {
        String classify_info_key = MethodUtil.good_classfiy_info_key + cid;
        String obj = RedisUtil.get(classify_info_key);
        return JSONObject.parseObject(obj,JSONObject.class);
    }

    @Override
    public JSONObject getGoodList(String pageId,String cids,String subcid,String sort,String pageSize,String brandIds) {
        String goods_info_key = null;
        if(StringUtils.isEmpty(sort)){
            sort = "0";
        }
        if(StringUtils.isEmpty(pageSize)){
            pageSize = "10";
        }
        if(StringUtils.isNotEmpty(cids)){
             goods_info_key = MethodUtil.good_info_key+cids+pageId+"_sort_"+sort+"_"+pageSize;
        }else if(StringUtils.isNotEmpty(subcid)){
             goods_info_key = MethodUtil.good_info_key+subcid+pageId+"_sort_"+sort+"_"+pageSize;
        }
        String obj = RedisUtil.get(goods_info_key);
        if(obj!=null){
            return JSONObject.parseObject(obj,JSONObject.class);
        }
        TreeMap<String,String> paraMap = new TreeMap<>();
        paraMap.put("version","v1.2.0");
        paraMap.put("pageId",pageId);
        paraMap.put("pageSize",String.valueOf(pageSize));
        paraMap.put("sort",sort);

        if(StringUtils.isNotEmpty(brandIds)){
            paraMap.put("brandIds",brandIds);
        }

        if(cids!=null){
            paraMap.put("cids",cids);
        }
        if(subcid!=null){
            paraMap.put("subcid",subcid);
        }
        JSONObject jsonObject = JSONObject.parseObject(HttpUtils.sendGet(MethodUtil.goods_url,MethodUtil.postContent(paraMap)),JSONObject.class);
        if(jsonObject.get("code").equals(0)){
            String list = ((JSONObject)jsonObject.get("data")).get("list").toString();
            Integer totalNum = Integer.parseInt(((JSONObject)jsonObject.get("data")).get("totalNum").toString());
            JSONArray array =  JSONObject.parseObject(list,JSONArray.class);
            array.forEach(json->{
                JSONObject jsonObj =(JSONObject) json;
                String actualPrice = jsonObj.getString("actualPrice");
                String commissionRate = jsonObj.getString("commissionRate");
                Double commission = MethodUtil.getCommission(actualPrice,commissionRate);
                jsonObj.put("commission",commission);
            });
            RedisUtil.setByTime(goods_info_key,JsonDealUtil.getSuccJSONObject("0|操作成功", String.valueOf(totalNum), array).toJSONString(),MethodUtil.expires);
            return JsonDealUtil.getSuccJSONObject("0|操作成功", String.valueOf(totalNum), array);
        }
        return JsonDealUtil.getSuccJSONObject("0|操作成功", "", "");
    }

    @Override
    public JSONObject getBannerList() {
        TreeMap<String,String> paraMap = new TreeMap<>();
        paraMap.put("version","v1.1.0");
        JSONObject jsonObject = JSONObject.parseObject(HttpUtils.sendGet(MethodUtil.banner_url,MethodUtil.postContent(paraMap)),JSONObject.class);
        if(jsonObject.get("code").equals(0)){
            String list = ((JSONObject)jsonObject.get("data")).toString();
            JSONArray array =  JSONObject.parseObject(list,JSONArray.class);
            RedisUtil.setByTime(MethodUtil.banner_List_key,JsonDealUtil.getSuccJSONObject("0|操作成功", String.valueOf(array.size()), list).toJSONString(),MethodUtil.expires);
            return JsonDealUtil.getSuccJSONObject("0|操作成功", String.valueOf(array.size()), list);
        }
        return JsonDealUtil.getSuccJSONObject("0|操作成功", "", "");
    }

    @Override
    public JSONObject getPrivilege(String goodsId) {
        String goods_key = MethodUtil.goods_privilege_key+goodsId;
        String obj = RedisUtil.get(goods_key);
        if(obj!=null){
            return JSONObject.parseObject(obj,JSONObject.class);
        }
        TreeMap<String,String> paraMap = new TreeMap<>();
        paraMap.put("version","v1.1.1");
        paraMap.put("goodsId",goodsId);
        JSONObject jsonObject = JSONObject.parseObject(HttpUtils.sendGet(MethodUtil.privilege_url,MethodUtil.postContent(paraMap)),JSONObject.class);
        if(jsonObject.get("code").equals(0)){
            JSONObject list = ((JSONObject)jsonObject.get("data"));
            RedisUtil.setByTime(MethodUtil.goods_privilege_key+goodsId,JsonDealUtil.getSuccJSONObject("0|操作成功", "", list).toJSONString(),0);
            return JsonDealUtil.getSuccJSONObject("0|操作成功", "", list);
        }
        return JsonDealUtil.getSuccJSONObject("0|操作成功", "", "");
    }

    @Override
    public JSONObject hotGoods() {
        String goods_key = MethodUtil.goods_hot_care;
        String obj = RedisUtil.get(goods_key);
        if(obj!=null){
            return JSONObject.parseObject(obj,JSONObject.class);
        }
        TreeMap<String,String> paraMap = new TreeMap<>();
        paraMap.put("version","v1.0.1");
        JSONObject jsonObject = JSONObject.parseObject(HttpUtils.sendGet(MethodUtil.get_top_url,MethodUtil.postContent(paraMap)),JSONObject.class);
        List<JSONObject> mapList = new ArrayList<JSONObject>();
        if(jsonObject.get("code").equals(0)){
            JSONObject list = ((JSONObject)jsonObject.get("data"));
            JSONArray array = (JSONArray) list.get("hotWords");
            for(int i=0;i<10;i++){
                Random random = new Random();
                int num = random.nextInt(array.size());
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("words",array.get(num));
                mapList.add(jsonObject1);
            }
            RedisUtil.setByTime(MethodUtil.goods_hot_care,JsonDealUtil.getSuccJSONObject("0|操作成功", "", mapList).toJSONString(),60*60);
            return JsonDealUtil.getSuccJSONObject("0|操作成功", "", mapList);
        }
        return null;
    }

    @Override
    public JSONObject superGoodsList(HttpServletRequest request,HashMap map) {
        TreeMap<String,String> paraMap = new TreeMap<>();
        paraMap.put("version","v1.2.1");
        paraMap.put("type",map.get("type")!=null?map.get("type").toString():"0");
        paraMap.put("pageId",map.get("pageId")!=null?map.get("pageId").toString():"1");
        paraMap.put("pageSize",map.get("pageSize")!=null?map.get("pageSize").toString():String.valueOf(MethodUtil.pageSize));
        paraMap.put("keyWords",map.get("keyWords").toString());
        if(map.get("sort")!=null){
            paraMap.put("sort",map.get("sort").toString());
        }
        JSONObject jsonObject = JSONObject.parseObject(HttpUtils.sendGet(MethodUtil.supper_url,MethodUtil.postContent(paraMap)),JSONObject.class);
        if(jsonObject.get("code").equals(0)){
            String list = ((JSONObject)jsonObject.get("data")).get("list").toString();
            JSONArray array =  JSONObject.parseObject(list,JSONArray.class);
            JSONArray arr1 = new JSONArray();
            for(int i=0;i<array.size();i++){
                JSONObject jsonObj = (JSONObject)array.get(i);
                String id = jsonObj.getString("id");
                String goodsId = jsonObj.getString("goodsId");
                if("-1".equals(id)){
                    continue;
                }
                jsonObj.put("pic",jsonObj.get("mainPic"));
                jsonObj.put("itemmsell",jsonObj.get("monthSales"));
                String coupon_amount = jsonObj.get("couponPrice").toString();
                String zk_final_price = jsonObj.get("actualPrice").toString();
                String commission_rate = jsonObj.get("commissionRate").toString();
                Double itemfee2 = 0.0;
                if(StringUtils.isNotEmpty(coupon_amount) && Double.parseDouble(coupon_amount)>0){
                    DecimalFormat df = new DecimalFormat("#.00");
                    itemfee2 = Double.valueOf(zk_final_price);
                    BigDecimal value = new BigDecimal(df.format(itemfee2));
                    BigDecimal noZeros = value.stripTrailingZeros();
                    jsonObj.put("qfee",coupon_amount);
                    jsonObj.put("itemfee",zk_final_price);
                    jsonObj.put("itemfee2",noZeros);

                }else{
                    jsonObj.put("qfee",0);
                    jsonObj.put("itemfee",0);
                    jsonObj.put("itemfee2",itemfee2);
                }
                Double commission = 0.0;
                try{
                    commission = MethodUtil.getCommission(zk_final_price,commission_rate);
                    jsonObj.put("fanli",commission);
                }catch (Exception ex){
                    log.error("commission 错误:"+ex.getMessage());
                }

                if(commission == 0){
                    jsonObj.put("fanli",0);
                }


                jsonObj.put("yhjurl",RequestUtils.getBasePath(request)+"/good/good_supper_detail?itemId="+goodsId+"&type=1");//type  1--淘宝 2.拼多多
                jsonObj.remove("pict_url");
                jsonObj.remove("volume");
                jsonObj.remove("coupon_amount");
                arr1.add(jsonObj);
            }
            //RedisUtil.setByTime(good_info_key,JsonDealUtil.getSuccJSONObject("0|操作成功", String.valueOf(array.size()), list).toJSONString(),hour_expires);
            return JsonDealUtil.getSuccJSONObject("0|操作成功", String.valueOf(arr1.size()), arr1);
        }
        return JsonDealUtil.getSuccJSONObject("0|操作成功", "", "");
    }

    @Override
    public JSONObject tbGoodsList(HashMap map,HttpServletRequest request) {
        TreeMap<String,String> paraMap = new TreeMap<>();
        paraMap.put("version","v2.0.0");
        paraMap.put("pageNo",map.get("pageNo")!=null?map.get("pageNo").toString():"1");
        paraMap.put("pageSize",(map.get("pageSize")!=null && !"".equals(map.get("pageSize").toString()))?map.get("pageSize").toString():String.valueOf(MethodUtil.pageSize));
        paraMap.put("keyWords",map.get("keyWords").toString());
        paraMap.put("source",map.get("source")!=null?map.get("source").toString():"1");
        paraMap.put("hasCoupon",map.get("hasCoupon").toString());
        paraMap.put("sort",map.get("sort")!=null?map.get("sort").toString():"total_sales_des");
        String is_tb = map.get("is_tb").toString();
        String is_pdd = map.get("is_pdd").toString();
        if(is_pdd.equals("1")){
            //是否拼多多
            return otherService.getPddSearchList(map,request);
        }else{
            //是否淘宝
            String is_site = map.get("is_site").toString();
            JSONObject jsonObject = null;
            if("1".equals(is_site)){
                HashMap jsonMap = new HashMap();
                jsonMap.put("type","0");
                jsonMap.put("pageId",map.get("pageNo")!=null?map.get("pageNo").toString():"1");
                jsonMap.put("pageSize",map.get("pageSize")!=null?map.get("pageSize").toString():String.valueOf(MethodUtil.pageSize));
                jsonMap.put("keyWords",map.get("keyWords").toString());
                jsonObject = superGoodsList(request,jsonMap);
                //return jsonObject;
                if(jsonObject.get("code").equals("1000")){
                    JSONArray array =  JSONObject.parseObject(jsonObject.get("data").toString(),JSONArray.class);
                    if(array.size()==0){
                        jsonObject = JSONObject.parseObject(HttpUtils.sendGet(MethodUtil.tb_good_url,MethodUtil.postContent(paraMap)),JSONObject.class);
                    }else{
                        return jsonObject;
                    }
                }
            }else{
                jsonObject = JSONObject.parseObject(HttpUtils.sendGet(MethodUtil.tb_good_url,MethodUtil.postContent(paraMap)),JSONObject.class);
            }
            if(jsonObject.get("code").equals(0)){
                JSONArray array =  JSONObject.parseObject(jsonObject.get("data").toString(),JSONArray.class);
                array.forEach(json->{
                    JSONObject jsonObj = (JSONObject)json;
                    String goodsId = jsonObj.getString("goodsId");
                    if(goodsId==null){
                        goodsId = jsonObj.getString("item_id");
                    }
                    if("-1".equals(goodsId)){
                        array.remove(jsonObj);
                        return;
                    }
                    Double commission = 0.0;
                    jsonObj.put("pic",jsonObj.get("pict_url"));
                    jsonObj.put("itemmsell",jsonObj.get("volume"));
                    String coupon_amount = jsonObj.get("coupon_amount").toString();
                    String zk_final_price = jsonObj.get("zk_final_price").toString();
                    String commission_rate = jsonObj.get("commission_rate").toString();
                    Double itemfee2 = 0.0;
                    if(StringUtils.isNotEmpty(coupon_amount) && Integer.parseInt(coupon_amount)>0){
                        DecimalFormat df = new DecimalFormat("#.00");
                        itemfee2 = Double.valueOf(zk_final_price) - Double.valueOf(coupon_amount);
                        BigDecimal value = new BigDecimal(df.format(itemfee2));
                        BigDecimal noZeros = value.stripTrailingZeros();
                        jsonObj.put("qfee",coupon_amount);
                        jsonObj.put("itemfee",zk_final_price);
                        jsonObj.put("itemfee2",noZeros);

                        try{
                            commission = MethodUtil.getCommission(zk_final_price,commission_rate);
                            jsonObj.put("fanli",commission);
                        }catch (Exception ex){
                            log.error("commission 错误:"+ex.getMessage());
                        }
                    }else{
                        jsonObj.put("qfee",0);
                        jsonObj.put("itemfee",0);
                        jsonObj.put("itemfee2",zk_final_price);
                    }

                    if(commission == 0){
                        jsonObj.put("fanli",0);
                    }

                    jsonObj.put("yhjurl",RequestUtils.getBasePath(request)+"/good/good_supper_detail?itemId="+goodsId+"&type=1");//type  1--淘宝 2.拼多多
                    jsonObj.remove("pict_url");
                    jsonObj.remove("volume");
                    jsonObj.remove("coupon_amount");
                });
                //RedisUtil.setByTime(good_info_key,JsonDealUtil.getSuccJSONObject("0|操作成功", String.valueOf(array.size()), list).toJSONString(),hour_expires);
                return JsonDealUtil.getSuccJSONObject("0|操作成功", String.valueOf(array.size()), array);
            }
        }


        return JsonDealUtil.getSuccJSONObject("0|操作成功", "0", "");
    }

    @Override
    public JSONObject goodsDetail(String goodsId) {
        String goods_key = MethodUtil.good_detail_key+goodsId;
        String obj = RedisUtil.get(goods_key);
        if(obj!=null){
            return JSONObject.parseObject(obj,JSONObject.class);
        }
        TreeMap<String,String> paraMap = new TreeMap<>();
        paraMap.put("version","v1.2.0");
        paraMap.put("goodsId",goodsId);
        JSONObject jsonObject = JSONObject.parseObject(HttpUtils.sendGet(MethodUtil.good_detail_url,MethodUtil.postContent(paraMap)),JSONObject.class);
        if(jsonObject.get("code").equals(0)){
            JSONObject list = ((JSONObject)jsonObject.get("data"));
            RedisUtil.setByTime(MethodUtil.good_detail_key+goodsId,JsonDealUtil.getSuccJSONObject("0|操作成功", "", list).toJSONString(),60*10);
            return JsonDealUtil.getSuccJSONObject("0|操作成功", "", list);
        }
        return JsonDealUtil.getSuccJSONObject("0|操作成功", "", "");
    }

    @Override
    public List<TbClassifyDO> queryTbClassify(Integer levelId) {
        List<TbClassifyDO> tbClassifyDOList = tbClassifyDOMapper.queryTbClassify(levelId);
        tbClassifyDOList.forEach(tbClassifyDO -> {
             if(tbClassifyDO.getClassifyId() == 1){
                  //1.9包邮
                 tbClassifyDO.setClassifyUrl(otherService.getCmsPromUrl(0));
             }else if(tbClassifyDO.getClassifyId() == 6){
                 //今日爆款
                 tbClassifyDO.setClassifyUrl(otherService.getCmsPromUrl(1));
             }else if(tbClassifyDO.getClassifyId() == 10){
                 //手机充值
                 tbClassifyDO.setClassifyUrl(PddUtil.PddDdkResourceUrlGen(39997));
             }else if(tbClassifyDO.getClassifyId() == 11){
                 //限时秒杀
                 tbClassifyDO.setClassifyUrl(PddUtil.PddDdkResourceUrlGen(4));
             }else if(tbClassifyDO.getClassifyId() == 12){
                 //百亿补贴
                 tbClassifyDO.setClassifyUrl(PddUtil.PddDdkResourceUrlGen(39996));
             }else if(tbClassifyDO.getClassifyId() == 13){
                 //品牌清仓
                 tbClassifyDO.setClassifyUrl(otherService.getCmsPromUrl(2));
             }else if(tbClassifyDO.getClassifyId() == 14){
                 //电器城
                 tbClassifyDO.setClassifyUrl(PddUtil.PddDdkResourceUrlGen(39999));
             }
        });
        return tbClassifyDOList;
    }

    @Override
    public List<TbBannerDO> queryTbBanner(Map map) {
        //return tbBannerDOMapper.queryTbBanner(map);
        String theme_key = MethodUtil.theme_goods_key;
        List<TbBannerDO> tbBannerDOS = RedisUtil.getObject(theme_key,List.class);
        if(tbBannerDOS!=null){
            return tbBannerDOS;
        }
        List<TbBannerDO> tbBannerDOList = new ArrayList<>();
        try{
            JSONObject jsonObject = JSONObject.parseObject(PddUtil.pddDdkThemeList(),JSONObject.class);
            Integer total = Integer.parseInt(((JSONObject)jsonObject.get("theme_list_get_response")).get("total").toString());
            if(total>0) {
                JSONArray jsonArray = JSONObject.parseObject(((JSONObject) jsonObject.get("theme_list_get_response")).get("theme_list").toString(), JSONArray.class);

                jsonArray.forEach(json->{
                    JSONObject obj = (JSONObject) json;
                    if(obj.containsKey("image_url")){
                        TbBannerDO tbBannerDO = new TbBannerDO();
                        tbBannerDO.setBannerId(obj.get("id").toString());
                        tbBannerDO.setBannerImg(obj.get("image_url").toString());
                        tbBannerDO.setBannerName(obj.get("name").toString());
                        tbBannerDOList.add(tbBannerDO);
                    }
                });
                if(tbBannerDOList.size()>0){
                    RedisUtil.setObject(theme_key,MethodUtil.hour_expires,tbBannerDOList);
                }
            }
        }catch (Exception ex){
            ex.getMessage();
        }

        return tbBannerDOList;
    }


    @Override
    public JSONObject getApGoodList(String pageId,String nineCid,String pageSize) {
        if(StringUtils.isEmpty(nineCid)||nineCid.equals("0")){
            nineCid = "-1";//默认精选
        }
        if(StringUtils.isEmpty(pageSize)){
            pageSize = "10";//默认10条
        }
        String good_ap_list_key =  MethodUtil.good_ap_key+nineCid+pageId+"_"+pageSize;
        String obj = RedisUtil.get(good_ap_list_key);
        if(obj!=null){
            return JSONObject.parseObject(obj,JSONObject.class);
        }
        TreeMap<String,String> paraMap = new TreeMap<>();
        paraMap.put("version","v1.1.0");
        paraMap.put("pageId",pageId);
        paraMap.put("pageSize",String.valueOf(pageSize));

        paraMap.put("nineCid",nineCid);

        JSONObject jsonObject = JSONObject.parseObject(HttpUtils.sendGet(MethodUtil.ap_good_url,MethodUtil.postContent(paraMap)),JSONObject.class);
        if(jsonObject.get("code").equals(0)){
            String list = ((JSONObject)jsonObject.get("data")).get("list").toString();
            Integer totalNum = Integer.parseInt(((JSONObject)jsonObject.get("data")).get("totalNum").toString());
            JSONArray array =  JSONObject.parseObject(list,JSONArray.class);
            array.forEach(json->{
                JSONObject jsonObj =(JSONObject) json;
                String actualPrice = jsonObj.getString("actualPrice");
                String commissionRate = jsonObj.getString("commissionRate");
                Double commission = MethodUtil.getCommission(actualPrice,commissionRate);
                jsonObj.put("commission",commission);
            });
            RedisUtil.setByTime(good_ap_list_key,JsonDealUtil.getSuccJSONObject("0|操作成功", String.valueOf(totalNum), array).toJSONString(),MethodUtil.expires);
            return JsonDealUtil.getSuccJSONObject("0|操作成功", String.valueOf(totalNum), array);
    }
        return JsonDealUtil.getSuccJSONObject("0|操作成功", "", "");
    }

    @Override
    public List<TbLucky> getLuckyGoodsList() {
        return tbLuckyMapper.queryTbLuckyList();
    }

    @Override
    public TbLucky getLuckyGoods(Integer id) {
        return tbLuckyMapper.selectByPrimaryKey(id);
    }


    @Override
    public JSONObject getNewUserGiftList() {
        return null;
    }

    @Override
    public JSONObject similerGoodsList(String id) {
        String good_similer_ap_key =  MethodUtil.good_similer_ap_key+"_goodId_"+id;
        String obj = RedisUtil.get(good_similer_ap_key);
        if(obj!=null){
            return JSONObject.parseObject(obj,JSONObject.class);
        }
        TreeMap<String,String> paraMap = new TreeMap<>();
        paraMap.put("version","v1.1.1");
        paraMap.put("id",id);
        JSONObject jsonObject = JSONObject.parseObject(HttpUtils.sendGet(MethodUtil.ap_similer_good_url,MethodUtil.postContent(paraMap)),JSONObject.class);
        if(jsonObject.get("code").equals(0)){
            String list = ((JSONObject)jsonObject.get("data")).get("list").toString();
            JSONArray array =  JSONObject.parseObject(list,JSONArray.class);
            RedisUtil.setByTime(good_similer_ap_key,JsonDealUtil.getSuccJSONObject("0|操作成功", String.valueOf(array.size()), list).toJSONString(),MethodUtil.expires);
            return JsonDealUtil.getSuccJSONObject("0|操作成功", String.valueOf(array.size()), list);
        }
        return JsonDealUtil.getSuccJSONObject("0|操作成功", "0", "");
    }

    @Override
    public List<TbBrand> brandList(Long cid,String pageId, String pageSize) {
        String goods_info_key = MethodUtil.brand_good_info_key + "cid_"+cid+"_pageId"+pageId+"_pageSize"+pageSize;
        List<TbBrand> tbBrands = RedisUtil.getObject(goods_info_key,List.class);
        if (tbBrands != null) {
            return tbBrands;
        }

        List<TbBrand>  tbBrandList = null;
        List<TbBrand>  brandList = new ArrayList<>();
        HashMap map = new HashMap();
        map.put("pageIndex",pageId);
        map.put("pageSize","10");
        if(cid==0){
            tbBrandList = tbBrandMapper.queryTbBrandTopList();
        }else{
            map.put("cid",cid);
            tbBrandList = tbBrandMapper.queryTbBrandList(map);
        }
        tbBrandList.forEach(brand->{
             //根据品牌ID查询商品信息
            JSONObject obj = brandGoodList(String.valueOf(brand.getBrandId()),pageId,"3");
            if(obj.get("code").equals(0)){
                String list = ((JSONObject)obj.get("data")).get("list").toString();
                JSONArray array =  JSONObject.parseObject(list,JSONArray.class);
                if(array.size()>0){
                    int count = array.size();
                    if(count>3){
                        count = 3;
                    }
                    JSONArray jsonArray = new JSONArray();
                    for(int i=0;i<count;i++){
                        JSONObject jsonObjects = (JSONObject)array.get(i);
                        jsonArray.add(jsonObjects);
                    }
                    brand.setGoodsList(jsonArray);
                    brandList.add(brand);
                }
            }
        });
        if(brandList.size()>0){
            RedisUtil.setObject(goods_info_key,MethodUtil.expires,brandList);
        }
        return brandList;
    }

    @Override
    public JSONObject brandGoodList(String brandIds, String pageId, String pageSize) {
        String brand_good_list_key = MethodUtil.brand_good_list_key + "brandId_"+brandIds+"_pageId"+pageId+"_pageSize"+pageSize;
        String obj = RedisUtil.get(brand_good_list_key);
        if (obj != null) {
            return JSONObject.parseObject(obj,JSONObject.class);
        }
        TreeMap<String, String> paraMap = new TreeMap<>();
        paraMap.put("version", "v1.2.0");
        paraMap.put("pageId", pageId);
        paraMap.put("pageSize", pageSize);

        if (StringUtils.isNotEmpty(brandIds)) {
            paraMap.put("brandIds", brandIds);
        }
        JSONObject jsonObject = JSONObject.parseObject(HttpUtils.sendGet(MethodUtil.goods_url, MethodUtil.postContent(paraMap)), JSONObject.class);
        if(jsonObject.get("code").equals(0)){
            RedisUtil.setByTime(brand_good_list_key,jsonObject.toJSONString(),MethodUtil.expires);
        }
        return jsonObject;
    }

    @Override
    public List<TbPddOpt> getTbPddOptList() {
        return tbPddOptMapper.queryTbPddOptList();
    }

}
