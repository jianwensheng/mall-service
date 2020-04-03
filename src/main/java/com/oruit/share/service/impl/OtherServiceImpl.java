package com.oruit.share.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.oruit.common.utils.*;
import com.oruit.common.utils.cache.redis.RedisUtil;
import com.oruit.common.utils.web.RequestUtils;
import com.oruit.share.domain.AccessToken;
import com.oruit.share.domain.GoodsInfoVO;
import com.oruit.share.domain.TbUser;
import com.oruit.share.service.AccessTokenService;
import com.oruit.share.service.OtherService;
import com.oruit.share.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sun.rmi.runtime.Log;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

@Service
@Slf4j
public class OtherServiceImpl implements OtherService {

    @Autowired
    private AccessTokenService accessTokenService;

    @Autowired
    private UserService userService;

    @Value("${weixin.appId}")
    private String appId;

    @Value("${weixin.appSecret}")
    private String appSecret;

    @Override
    public JSONObject getGoodClassify() {
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
            JSONArray jsonArray = JSONObject.parseObject(jsonObject.get("data").toString(), JSONArray.class);

            for(int i=0;i<jsonArray.size();i++){
                JSONObject json = (JSONObject) jsonArray.get(i);
                json.put("order",i+1);
                list.add(json);
            }
            if(list.size()>0){
                RedisUtil.setByTime(classify_key, JsonDealUtil.getSuccJSONObject("0|操作成功", String.valueOf(list.size()), list).toJSONString(),MethodUtil.expires);
            }
        }
        return JsonDealUtil.getSuccJSONObject("0|操作成功", String.valueOf(list.size()), list);
    }

    @Override
    public JSONObject getDdqGoodList(String roundTime) {
        TreeMap<String,String> paraMap = new TreeMap<>();
        paraMap.put("version","v1.2.0");
        String good_ddq_key = MethodUtil.good_ddq_key;
        if(StringUtils.isNotEmpty(roundTime)){
            try{
                good_ddq_key = MethodUtil.good_ddq_key+"_time_"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(roundTime).getTime();
                paraMap.put("roundTime", roundTime);
            }catch (Exception e){
                e.getMessage();
            }
        }
        String obj = RedisUtil.get(good_ddq_key);
        if(obj!=null){
            return JSONObject.parseObject(obj,JSONObject.class);
        }
        JSONObject jsonObject = JSONObject.parseObject(HttpUtils.sendGet(MethodUtil.ddq_good_url, MethodUtil.postContent(paraMap)),JSONObject.class);
        List<JSONObject> list = new ArrayList<JSONObject>();
        if(jsonObject.get("code").equals(0)){
            JSONObject json = JSONObject.parseObject(jsonObject.get("data").toString(), JSONObject.class);
            JSONArray roundsList = JSONObject.parseObject(json.get("roundsList").toString(), JSONArray.class);
            for(int i=0;i<roundsList.size();i++){
                JSONObject rounds = (JSONObject) roundsList.get(i);//rounds.get("ddqTime")
                int hour = HttpUtils.getHour(rounds.get("ddqTime").toString());
                String minute = HttpUtils.getMinute(rounds.get("ddqTime").toString());
                rounds.put("time",hour+":"+minute);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Long strtotime = null;
                try{
                    strtotime = sdf.parse(rounds.get("ddqTime").toString()).getTime();
                }catch (Exception ex){
                    ex.getMessage();
                }
                rounds.put("strtotime",strtotime);
                Integer status = Integer.parseInt(rounds.get("status").toString());
                String statusMsg = "";
                if(status==0){
                    statusMsg = "已开抢";
                }else if(status==1){
                    statusMsg = "正在疯抢";
                }else if(status==2){
                    statusMsg = "即将开始";
                }
                rounds.put("statusMsg",statusMsg);
            }
            json.put("roundsList",roundsList);
            jsonObject.put("data",json);
            RedisUtil.setByTime(good_ddq_key, jsonObject.toJSONString(),MethodUtil.hour_expires);
        }
        return jsonObject;
    }

    @Override
    public JSONObject getThemeList() {
        String theme_key = MethodUtil.theme_key;
        String obj = RedisUtil.get(theme_key);
        if(obj!=null){
            return JSONObject.parseObject(obj,JSONObject.class);
        }
        try{
           JSONObject jsonObject = JSONObject.parseObject(PddUtil.pddDdkThemeList(),JSONObject.class);
           Integer total = Integer.parseInt(((JSONObject)jsonObject.get("theme_list_get_response")).get("total").toString());
           if(total>0){
               JSONArray jsonArray = JSONObject.parseObject(((JSONObject)jsonObject.get("theme_list_get_response")).get("theme_list").toString(),JSONArray.class);
               RedisUtil.setByTime(theme_key, jsonArray.toJSONString(),MethodUtil.hour_expires);
               return JsonDealUtil.getSuccJSONObject("0|操作成功", String.valueOf(jsonArray.size()), jsonArray);
           }

        }catch (Exception ex){
            ex.getMessage();
        }
        return JsonDealUtil.getSuccJSONObject("0|操作成功","", "");
    }

    @Override
    public JSONObject getThemeGoodsList(Long themeId) {
        String theme_goods_key = MethodUtil.theme_goods_key+themeId;
        String obj = RedisUtil.get(theme_goods_key);
        if(obj!=null){
            JSONArray objs = JSONObject.parseObject(obj,JSONArray.class);
            return JsonDealUtil.getSuccJSONObject("0|操作成功", String.valueOf(objs.size()), objs);
        }
        try{
            JSONObject jsonObject = JSONObject.parseObject(PddUtil.pddDdkThemeGoodsList(themeId),JSONObject.class);
            Integer total = Integer.parseInt(((JSONObject)jsonObject.get("theme_list_get_response")).get("total").toString());
            if(total>0){
                JSONArray jsonArray = JSONObject.parseObject(((JSONObject)jsonObject.get("theme_list_get_response")).get("goods_list").toString(),JSONArray.class);
                for(int i=0;i<jsonArray.size();i++){
                    JSONObject jsonObj = (JSONObject) jsonArray.get(i);
                    Double min_group_price = Double.parseDouble(MethodUtil.calculateProfit(jsonObj.getDoubleValue("min_group_price")/100));

                    Integer coupon_discount = jsonObj.getInteger("coupon_discount");
                    jsonObj.put("goods_id",jsonObj.getString("goods_id"));

                    if(coupon_discount>0){
                        jsonObj.put("qfee",coupon_discount/100);
                        min_group_price = min_group_price - (coupon_discount/100);
                    }else{
                        jsonObj.put("qfee",0);
                    }
                    jsonObj.put("min_group_price",min_group_price);
                    //佣金
                    Double rate = jsonObj.getDouble("promotion_rate")/1000;
                    Double comm = rate*min_group_price;
                    jsonObj.put("commission",MethodUtil.calculateProfit(comm));

                }
                RedisUtil.setByTime(theme_goods_key, jsonArray.toJSONString(),MethodUtil.hour_expires);
                return JsonDealUtil.getSuccJSONObject("0|操作成功", String.valueOf(jsonArray.size()), jsonArray);
            }

        }catch (Exception ex){
            ex.getMessage();
        }
        return JsonDealUtil.getSuccJSONObject("0|操作成功","", "");
    }

    @Override
    public String getCmsPromUrl(Integer channelType) {
        String cms_prom_url_key = MethodUtil.cms_prom_url_key+channelType;
        String obj = RedisUtil.get(cms_prom_url_key);
        if(obj!=null){
            return obj;
        }
        try{
            JSONObject jsonObject = JSONObject.parseObject(PddUtil.PddDdkCmsPromUrlGenerate(channelType),JSONObject.class);
            Integer total = Integer.parseInt(((JSONObject)jsonObject.get("cms_promotion_url_generate_response")).get("total").toString());
            if(total>0){
                JSONArray jsonArray = JSONObject.parseObject(((JSONObject)jsonObject.get("cms_promotion_url_generate_response")).get("url_list").toString(),JSONArray.class);
                String shortUlr = ((JSONObject)jsonArray.get(0)).get("short_url").toString();
                RedisUtil.setByTime(cms_prom_url_key,shortUlr,MethodUtil.hour_expires);
                return shortUlr;
            }

        }catch (Exception ex){
            ex.getMessage();
        }
        return null;
    }

    @Override
    public JSONObject getPddSearchList(HashMap map, HttpServletRequest request) {
        try{
            JSONObject jsonObject = JSONObject.parseObject(PddUtil.PddDdkGoodsSearch(map),JSONObject.class);
            Integer total = Integer.parseInt(((JSONObject)jsonObject.get("goods_search_response")).get("total_count").toString());
            if(total>0){
                JSONArray jsonArray = JSONObject.parseObject(((JSONObject)jsonObject.get("goods_search_response")).get("goods_list").toString(),JSONArray.class);
                jsonArray.forEach(json->{
                    JSONObject jsonObj = (JSONObject)json;
                    jsonObj.put("title",jsonObj.get("goods_name"));
                    jsonObj.put("pic",jsonObj.get("goods_image_url"));
                    jsonObj.put("itemmsell",jsonObj.get("sales_tip"));
                    String coupon_discount = jsonObj.get("coupon_discount").toString();
                    String min_group_price = jsonObj.get("min_group_price").toString();
                    String goods_id = jsonObj.get("goods_id").toString();
                    Double promotion_rate = jsonObj.getDoubleValue("promotion_rate");
                    Double itemfee2 = 0.0;
                    if(StringUtils.isNotEmpty(coupon_discount) && Integer.parseInt(coupon_discount)>0){
                        DecimalFormat df = new DecimalFormat("#.00");
                        itemfee2 = (Double.valueOf(min_group_price) - Double.valueOf(coupon_discount))/100;
                        BigDecimal value = new BigDecimal(df.format(itemfee2));
                        BigDecimal noZeros = value.stripTrailingZeros();
                        jsonObj.put("qfee",Integer.parseInt(coupon_discount)/100);
                        jsonObj.put("itemfee",Double.valueOf(min_group_price)/100);
                        jsonObj.put("itemfee2",noZeros);
                    }else{
                        jsonObj.put("qfee",0);
                        jsonObj.put("itemfee",0);
                        jsonObj.put("itemfee2",itemfee2);
                    }

                    Double commission = 0.0;
                    try{
                        commission = MethodUtil.getCommission(String.valueOf(itemfee2),String.valueOf(promotion_rate/10));
                        jsonObj.put("fanli",commission);
                    }catch (Exception ex){
                        log.error("commission 错误:"+ex.getMessage());
                    }

                    if(commission == 0){
                        jsonObj.put("fanli",0);
                    }

                    jsonObj.put("yhjurl", RequestUtils.getBasePath(request)+"/good/good_supper_detail?itemId="+goods_id+"&type=2");//type  1--淘宝 2.拼多多
                    jsonObj.remove("goods_name");
                    jsonObj.remove("goods_image_url");
                    jsonObj.remove("sales_tip");
                });
                return JsonDealUtil.getSuccJSONObject("0|操作成功", String.valueOf(jsonArray.size()), jsonArray);
            }

        }catch (Exception ex){
            ex.getMessage();
        }
        return JsonDealUtil.getSuccJSONObject("0|操作成功","", "");
    }

    @Override
    public JSONObject getPddGoodsDetailInfo(Long goodId) {
        String pdd_good_detail_key = MethodUtil.pdd_good_detail_key+goodId;
        String obj = RedisUtil.get(pdd_good_detail_key);
        if(obj!=null){
            return JSONObject.parseObject(obj,JSONObject.class);
        }
        try{
            JSONObject jsonObject = JSONObject.parseObject(PddUtil.PddDdkGoodsDetail(goodId),JSONObject.class);
            String goods_details = ((JSONObject)jsonObject.get("goods_detail_response")).get("goods_details").toString();
            if(goods_details!=null){
                JSONArray jsonArr = JSONObject.parseObject(goods_details,JSONArray.class);
                JSONObject jsonObj = (JSONObject)jsonArr.get(0);
                jsonObj.put("title",jsonObj.get("goods_name"));
                jsonObj.put("detailPics",jsonObj.get("goods_gallery_urls"));
                jsonObj.put("imgs",jsonObj.get("goods_gallery_urls"));
                jsonObj.put("goodsId",jsonObj.get("goods_id"));
                jsonObj.put("desc",jsonObj.get("goods_desc"));
                jsonObj.put("mainPic",jsonObj.get("goods_image_url"));


                String coupon_discount = jsonObj.get("coupon_discount").toString();
                String min_group_price = jsonObj.get("min_group_price").toString();
                String min_normal_price = jsonObj.get("min_normal_price").toString();

                Double promotion_rate = jsonObj.getDoubleValue("promotion_rate");
                Double rate = promotion_rate/1000;

                Double itemfee2 = 0.0;
                Double itemfee1 = 0.0;
                Double itemfee3 = 0.0;
                DecimalFormat df = new DecimalFormat("#.00");
                if(StringUtils.isNotEmpty(coupon_discount) && Integer.parseInt(coupon_discount)>0){
                    itemfee2 = (Double.valueOf(min_group_price) - Double.valueOf(coupon_discount))/100;
                    itemfee1 = (Double.valueOf(min_normal_price) - Double.valueOf(coupon_discount))/100;
                    itemfee3 = Double.valueOf(min_normal_price)/100;

                }else{
                    itemfee2 = (Double.valueOf(min_group_price))/100;
                    itemfee1 = (Double.valueOf(min_normal_price))/100;
                    itemfee3 = Double.valueOf(min_normal_price)/100;
                }

                BigDecimal value = new BigDecimal(df.format(itemfee2));
                BigDecimal value2 = new BigDecimal(df.format(itemfee1));
                BigDecimal value3 = new BigDecimal(df.format(itemfee3));
                BigDecimal noZeros = value.stripTrailingZeros();
                BigDecimal noZeros2 = value2.stripTrailingZeros();
                BigDecimal noZeros3 = value3.stripTrailingZeros();
                jsonObj.put("alonePrice",noZeros2);
                jsonObj.put("zkPrice",noZeros);
                jsonObj.put("actualPrice",noZeros3);
                jsonObj.put("commission",MethodUtil.calculateProfit(rate*noZeros.doubleValue()));

                jsonObj.put("etime",jsonObj.get("coupon_end_time"));
                jsonObj.put("couponDiscount",Integer.parseInt(jsonObj.get("coupon_discount").toString())/100);



                jsonObj.remove("goods_name");
                jsonObj.remove("goods_gallery_urls");
                jsonObj.remove("goods_id");
                jsonObj.remove("goods_desc");
                jsonObj.remove("goods_image_url");
                jsonObj.remove("coupon_discount");
                jsonObj.remove("promotion_rate");

                String webUrl = getPddGoodsPromotionUrl(goodId);
                String mall_id = jsonObj.get("mall_id").toString();
                JSONObject json = getMallInfo(mall_id);
                jsonObj.put("merchantType",json.get("merchantType"));
                jsonObj.put("webUrl",webUrl);

                RedisUtil.setByTime(pdd_good_detail_key,JsonDealUtil.getSuccJSONObject("0|操作成功", String.valueOf(1), jsonObj).toJSONString(),MethodUtil.expires);


                return JsonDealUtil.getSuccJSONObject("0|操作成功", "1", jsonObj);
            }

        }catch (Exception ex){
            ex.getMessage();
        }
        return JsonDealUtil.getSuccJSONObject("0|操作成功","", "");
    }

    @Override
    public String getPddGoodsPromotionUrl(Long goodId) {

        try{
            JSONObject jsonObject = JSONObject.parseObject(PddUtil.PddDdkGoodsPromotionUrl(goodId),JSONObject.class);
            JSONArray goodsDetails = JSONObject.parseObject(((JSONObject)jsonObject.get("goods_promotion_url_generate_response")).get("goods_promotion_url_list").toString(),JSONArray.class);
            if(goodsDetails.size()>0){
                String weburl =  ((JSONObject) goodsDetails.get(0)).get("we_app_web_view_url").toString();
                return weburl;
            }

        }catch (Exception ex){
            ex.getMessage();
        }
        return null;
    }

    /**
     * @param mallId
     * @return
     */
    @Override
    public JSONObject getMallInfo(String mallId) {
        String pdd_cms_good_url_key = MethodUtil.pdd_cms_good_url+mallId;
        String obj = RedisUtil.get(pdd_cms_good_url_key);
        if(obj!=null){
            return JSONObject.parseObject(obj,JSONObject.class);
        }
        JSONObject json = new JSONObject();
        try{
            JSONObject jsonObject = JSONObject.parseObject(PddUtil.PddDdkMerchantListGet(Long.valueOf(mallId)),JSONObject.class);
            Integer total = Integer.parseInt(((JSONObject)jsonObject.get("merchant_list_response")).get("total").toString());
            if(total>0){
                JSONArray jsonArray = JSONObject.parseObject(((JSONObject)jsonObject.get("merchant_list_response")).get("mall_search_info_vo_list").toString(),JSONArray.class);
                JSONObject jsonObj = (JSONObject) jsonArray.get(0);
                json.put("merchantType",jsonObj.get("merchant_type"));
                RedisUtil.setByTime(pdd_cms_good_url_key, json.toJSONString(),MethodUtil.hour_expires);
                return json;
            }

        }catch (Exception ex){
            ex.getMessage();
        }
        return json;
    }

    @Override
    public String createCouponPoster(HttpServletRequest request,Long userId, GoodsInfoVO vo) throws Exception{
        String fileUrl = RedisUtil.get("couponPoster:u"+userId+"_g"+vo.getGoodsId());
        if(StringUtils.isBlank(fileUrl)){
            //生成图片
            String fileName = System.currentTimeMillis()+".jpg";
            String imagePath = ZshopConstants.UploadFilesConstants.STATICFILESTEMPPATH + fileName;
            File file = new File(imagePath);
            GoodsRqCodeUtil.drawCouponPosterImage(RequestUtils.getBasePath(request), userId, vo, imagePath,fileName);

            //上传至阿里云
            OSSClientUtil ossClient = new OSSClientUtil();
            FileInputStream fileInputStream = new FileInputStream(file);
            MultipartFile multipartFile = new MockMultipartFile(file.getName(), file.getName(),
                    ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);
            String url = ossClient.uploadImg2Oss(multipartFile);
            if(url != null){
                fileUrl = url;
                RedisUtil.setByTime("couponPoster:u"+userId+"_g"+vo.getGoodsId(), fileUrl, MethodUtil.hour_expires);
            }
            //删除服务器端文件
            file.delete();
        }
        return fileUrl;
    }

    @Override
    public String createInvitationImg(String inviteCode,String token) throws Exception {
        String fileUrl = RedisUtil.get("invitationImg_"+inviteCode);
        if(StringUtils.isBlank(fileUrl)){
            //生成图片
            String fileName = "/invitationFriend.jpg";
            String fileOssName = "invitation_"+System.currentTimeMillis()+".jpg";
            String imagePath = ZshopConstants.UploadFilesConstants.STATICFILESTEMPPATH + fileName;
            File file = new File(imagePath);
            AccessToken accessToken = accessTokenService.getToken(appId,appSecret);
            TbUser tbUser = userService.queryTokenUserInfo(token);
            String codeUrl = accessTokenService.createPermanentQRCode(accessToken.getAccessToken(),Integer.parseInt(tbUser.getId().toString()));
            GoodsRqCodeUtil.drawInvitationImage(inviteCode,imagePath,codeUrl);
            //上传至阿里云
            OSSClientUtil ossClient = new OSSClientUtil();
            FileInputStream fileInputStream = new FileInputStream(file);
            MultipartFile multipartFile = new MockMultipartFile(fileOssName, fileOssName,
                    ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);
            String url = ossClient.uploadImg2Oss(multipartFile);
            if(url != null){
                fileUrl = url;
                RedisUtil.setByTime("invitationImg_"+inviteCode, fileUrl, MethodUtil.week_expires);
            }
        }

        return fileUrl;
    }
}
