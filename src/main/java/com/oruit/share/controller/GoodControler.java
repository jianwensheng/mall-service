package com.oruit.share.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.oruit.common.utils.HttpUtils;
import com.oruit.common.utils.JsonDealUtil;
import com.oruit.common.utils.MethodUtil;
import com.oruit.common.utils.PddUtil;
import com.oruit.common.utils.cache.redis.RedisUtil;
import com.oruit.share.dao.TbBrandMapper;
import com.oruit.share.domain.*;
import com.oruit.share.service.GoodsService;
import com.oruit.share.service.OtherService;
import com.oruit.weixin.WxUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description: 商品列表控制层$
 * @Param: $
 * @return: $
 * @Author: xiecong
 * @date: 2019-12-20$
 */

@Controller
@RequestMapping("good")
@Slf4j
public class GoodControler {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OtherService otherService;

    @Autowired
    private TbBrandMapper tbBrandMapper;

    @Value("${taobao.apikey}")
    private String apikey;

    @RequestMapping("/goodSearch")
    public String goodSearch(HttpServletRequest request, Model model) {
        JSONObject obj = goodsService.hotGoods();
        if (obj.get("code").equals("1000")) {
            List<Map<String, Object>> list = JSONObject.parseObject(obj.get("data").toString(), List.class);
            model.addAttribute("list", list);
        }
        return "goods_search";
    }

    /**
     * 超级搜索
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/goodSupperSearch")
    public String goodSupperSearch(HttpServletRequest request, Model model) {
        JSONObject obj = goodsService.hotGoods();
        if (obj.get("code").equals("1000")) {
            List<Map<String, Object>> list = JSONObject.parseObject(obj.get("data").toString(), List.class);
            model.addAttribute("list", list);
        }
        return "goods_supper_search";
    }

    @RequestMapping("/list")
    public String list(HttpServletRequest request, Model model) throws Exception {
        String goodsName = getRequestParam(request, "goodsName");//商品名称
        model.addAttribute("goodsName", goodsName);
        return "goods_list";
    }

    @RequestMapping("/friend")
    public String friendList(HttpServletRequest request, Model model) throws Exception {
        return "friend";
    }

    /**
     * 搜索查询商品
     *
     * @param request
     * @param response
     * @param model
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/goodSearchList")
    @ResponseBody
    public JSONObject goodSearchList(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        String pageNo = getRequestParam(request, "pageNo");//显示条数
        String goodsName = getRequestParam(request, "key");//商品名称
        String sort = getRequestParam(request, "sort");//排序
        String source = getRequestParam(request, "source");//是否商城商品
        String hasCoupon = getRequestParam(request, "is_q");//是否有优惠券
        String is_tb = getRequestParam(request, "hasCoupon");//是否淘宝 1.是 2.否
        String is_pdd = getRequestParam(request, "is_pdd");//是否拼多多 1.是 2.否
        String is_site = getRequestParam(request, "is_site");//是否为站内搜索  1.是 0.否

        if (StringUtils.isNotEmpty(hasCoupon) && "1".equals(hasCoupon)) {
            hasCoupon = "true";
        } else {
            hasCoupon = "false";
        }

        HashMap map = new HashMap();
        map.put("keyWords", goodsName);
        map.put("pageNo", pageNo);
        map.put("pageSize", MethodUtil.pageSize);
        map.put("sort", sort);
        map.put("source", source);
        map.put("hasCoupon", hasCoupon);
        map.put("is_tb", is_tb);
        map.put("is_pdd", is_pdd);
        map.put("is_site", is_site);
        JSONObject json = goodsService.tbGoodsList(map, request);
        return json;
    }

    @PostMapping(value = "/goodList")
    @ResponseBody
    public JSONObject list(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        String pageNo = getRequestParam(request, "pageNo");//显示页数
        String pageSize = HttpUtils.getRequestParam(request, "pageSize");//显示条数
        String cid = getRequestParam(request, "cid");//分类id
        String subcid = getRequestParam(request, "subcid");//二级分类id
        String sort = getRequestParam(request, "subcid");//排序字段
        String brands = HttpUtils.getRequestParam(request, "brands");//品牌ID
        JSONObject json = goodsService.getGoodList(pageNo, cid, subcid, sort, pageSize, brands);
        return json;
    }

    @GetMapping(value = "/classfyGoodList")
    public String classfyGoodList(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        String subcid = getRequestParam(request, "subcid");//二级分类id
        model.addAttribute("subcid", subcid);
        return "goods_classify_list";
    }

    @PostMapping(value = "/goodByClassfyList")
    @ResponseBody
    public JSONObject goodByClassfyList(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        String pageNo = getRequestParam(request, "pageNo");//显示页数
        String pageSize = HttpUtils.getRequestParam(request, "pageSize");//显示条数
        String cid = getRequestParam(request, "cid");//分类id
        String subcid = getRequestParam(request, "subcid");//二级分类id
        String sort = getRequestParam(request, "sort");//排序字段
        String brandIds = getRequestParam(request, "brandIds");//品牌ID
        JSONObject json = goodsService.getGoodList(pageNo, cid, subcid, sort,pageSize, brandIds);
        return json;
    }


    /**
     * 高效转链
     *
     * @param request
     * @param response
     * @param model
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/privilege")
    public String privilege(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        String goodsId = getRequestParam(request, "goodsId");//商品ID
        JSONObject obj = goodsService.getPrivilege(goodsId);
        JSONObject goodsDetailObj = goodsService.goodsDetail(goodsId);
        if (obj.get("code").equals("1000")) {
            JSONObject jsonObject = JSONObject.parseObject(obj.get("data").toString(), JSONObject.class);
            model.addAttribute("coupon", jsonObject);
        }
        if (goodsDetailObj.get("code").equals("1000")) {
            JSONObject goodsDetail = JSONObject.parseObject(goodsDetailObj.get("data").toString(), JSONObject.class);
            String[] imgs = goodsDetail.get("imgs").toString().split(",");
            String[] detailPics = goodsDetail.get("detailPics").toString().split(",");
            model.addAttribute("good", goodsDetail);
            model.addAttribute("goodImgs", imgs);
            model.addAttribute("detailPics", detailPics);
        }
        return "pro_info";
    }

    /**
     * 商品详情(超级搜索)
     *
     * @param request
     * @param response
     * @param model
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/good_supper_detail")
    public String good_detail(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        String goodsId = getRequestParam(request, "itemId");//商品ID
        String type = getRequestParam(request, "type");  // type 1.淘宝 2.拼多多
        if (type.equals("1")) {
            //淘宝
            JSONObject obj = goodsService.getPrivilege(goodsId);
            JSONObject goodsDetailObj = goodsService.goodsDetail(goodsId);
            if (obj.get("code").equals("1000")) {
                JSONObject jsonObject = JSONObject.parseObject(obj.get("data").toString(), JSONObject.class);
                model.addAttribute("coupon", jsonObject);
            }
            if (goodsDetailObj.get("code").equals("1000")) {
                JSONObject goodsDetail = JSONObject.parseObject(goodsDetailObj.get("data").toString(), JSONObject.class);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String couponStartTime = goodsDetail.getString("couponStartTime");
                String couponEndTime = goodsDetail.getString("couponEndTime");
                Double couponReceiveNum = goodsDetail.getDouble("couponReceiveNum");
                Double couponTotalNum = goodsDetail.getDouble("couponTotalNum");
                Double commissionRate = goodsDetail.getDouble("commissionRate");
                Double actualPrice = goodsDetail.getDouble("actualPrice");
                Double rate = commissionRate/100;
                if(StringUtils.isNotEmpty(couponStartTime)){
                    goodsDetail.put("couponStartTime", sdf.format(sdf.parse(couponStartTime)));
                }
                if(StringUtils.isNotEmpty(couponEndTime)){
                    goodsDetail.put("couponEndTime", sdf.format(sdf.parse(couponEndTime)));
                }
                if(couponTotalNum>couponReceiveNum){
                    goodsDetail.put("margin", ((couponTotalNum - couponReceiveNum) / couponTotalNum) * 100);//券剩余量,单位%
                }

                goodsDetail.put("prize",MethodUtil.calculateProfit(rate*actualPrice));

                model.addAttribute("good", goodsDetail);
            }

        } else if (type.equals("2")) {
            //拼多多
            model.addAttribute("goodsId", goodsId);

            return "pro_pdd_info";
        }
        return "pro_supper_info";
    }


    @RequestMapping("/seckill")
    public String seckill(HttpServletRequest request, Model model) {
       /* JSONObject obj = goodsService.getGoodClassify(request);
        if(obj.get("code").equals("1000")){
            List<Map<String,Object>> list = JSONObject.parseObject(obj.get("data").toString(),List.class);
            model.addAttribute("classifyList",list);
            model.addAttribute("cid",list.get(0).get("cid"));
        }*/
        return "seckill";
    }

    @RequestMapping("/luckyIndex")
    public String luckyIndex(HttpServletRequest request, Model model) {
        List<TbLucky> tbLuckyList = goodsService.getLuckyGoodsList();
        model.addAttribute("tbLuckyList", tbLuckyList);
        return "lucky_gift_list";
    }

    /**
     * 拼团商品详情页
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/collage")
    public String luckyDetail(HttpServletRequest request, Model model) throws Exception {
        String id = getRequestParam(request, "id");//商品ID
        TbLucky tbLuckyWithBLOBs = goodsService.getLuckyGoods(Integer.parseInt(id));
        model.addAttribute("tbLucky", tbLuckyWithBLOBs);
        String[] imgs = tbLuckyWithBLOBs.getGoodImgs().split("#");
        model.addAttribute("imgs", imgs);
        String[] detailPics = tbLuckyWithBLOBs.getDetailPic().split("#");
        model.addAttribute("detailPics", detailPics);
        return "collage_page";
    }


    @PostMapping(value = "/goodApList")
    @ResponseBody
    public JSONObject goodApList(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        String pageNo = getRequestParam(request, "pageNo");//显示页数
        String pageSize = HttpUtils.getRequestParam(request, "pageSize");//显示条数
        String cid = getRequestParam(request, "cid");//分类id
        JSONObject json = goodsService.getApGoodList(pageNo, cid, pageSize);
        return json;
    }

    /**
     * 品牌榜分类
     *
     * @param request
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/brand")
    public String barand(HttpServletRequest request, Model model) throws Exception {
        String cid = getRequestParam(request, "cid");//分类ID
        String pageId = getRequestParam(request, "pageId");//页码
        String pageSize = getRequestParam(request, "pageSize");//条
        JSONObject obj = goodsService.getGoodClassify(request);
        if (obj.get("code").equals("1000")) {
            List<Map<String, Object>> list = JSONObject.parseObject(obj.get("data").toString(), List.class);
            model.addAttribute("classifyList", list);
        }

        return "brand_list";
    }

    /**
     * 品牌列表
     * @param request
     * @param response
     * @param model
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/brandList")
    @ResponseBody
    public JSONObject brandList(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        String pageId = getRequestParam(request, "pageNo");//显示页数
        String pageSize = HttpUtils.getRequestParam(request, "pageSize");//显示条数
        String cid = getRequestParam(request, "cid");//分类id
        List<TbBrand> tbBrandList = goodsService.brandList(Long.parseLong(cid),pageId,pageSize);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("tbBrandList",tbBrandList);
        if(tbBrandList.size()>0){
            return JsonDealUtil.getSuccJSONObject("0|操作成功", resultMap.size()+"", resultMap);
        }else{
            return JsonDealUtil.getErrJSONObject("操作失败");
        }
    }

    /**
     * 品牌商品列表
     * @param request
     * @param model
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/brandGoodsDetail")
    public String brandGoodsDetail(HttpServletRequest request, Model model) throws Exception {
        String brandId = getRequestParam(request, "brandId");//品牌ID
        TbBrand tbBrand = tbBrandMapper.selectByBrandId(Long.parseLong(brandId));
        model.addAttribute("brand",tbBrand);
        return "brand_good_list";
    }



    /**
     * 品牌商品列表
     * @param request
     * @param response
     * @param model
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/brandGoodsList")
    @ResponseBody
    public JSONObject brandGoodsList(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        String brandId = getRequestParam(request, "brandId");//品牌ID
        String pageId = getRequestParam(request, "pageNo");//显示页数
        String pageSize = HttpUtils.getRequestParam(request, "pageSize");//显示条数
        JSONObject json = goodsService.brandGoodList(brandId,pageId,pageSize);
        Map<String, Object> resultMap = new HashMap<>();
        if(json.get("code").equals(0)) {
            String list = ((JSONObject) json.get("data")).get("list").toString();
            JSONArray array =  JSONObject.parseObject(list,JSONArray.class);
            resultMap.put("goodList",array);
        }
        if(resultMap.size()>0){
            return JsonDealUtil.getSuccJSONObject("0|操作成功", resultMap.size()+"", resultMap);
        }else{
            return JsonDealUtil.getErrJSONObject("操作失败");
        }
    }




    /**
     * 获取request请求的参数内容
     *
     * @param request
     * @param paramName
     * @return
     */
    private String getRequestParam(HttpServletRequest request, String paramName) throws UnsupportedEncodingException {
        if (!StringUtils.isBlank(request.getParameter(paramName))) {
            return request.getParameter(paramName);
        } else {
            return "";
        }
    }

    @RequestMapping("/themeList")
    public String themeList(HttpServletRequest request, Model model) throws Exception {
        String themeId = getRequestParam(request, "themeId");//主题ID
        JSONObject json = otherService.getThemeGoodsList(Long.parseLong(themeId));
        if (json.get("code").equals("1000")) {
            JSONArray goodList = JSONObject.parseObject(json.get("data").toString(), JSONArray.class);
            model.addAttribute("goodList", goodList);
        }
        return "theme_goods_list";
    }

    /**
     * 手机充值
     * @param request
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/recharge")
    public String recharge(HttpServletRequest request, Model model) throws Exception {
        String url = PddUtil.PddDdkResourceUrlGen(39997);
        model.addAttribute("rechargeUrl",url);
        return "recharge_list";
    }

    /**
     * 拼多多首页（优惠券）
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/pddIndex")
    public String pddIndex(HttpServletRequest request,Model model) {
        List<TbPddOpt> tbPddOptList = goodsService.getTbPddOptList();
        model.addAttribute("tbPddOptList",tbPddOptList);
        List<TbClassifyDO> tbClassifyDOList = goodsService.queryTbClassify(2);
        model.addAttribute("tbClassifyDOList",tbClassifyDOList);
        try{
            /**实时热销榜**/
            Map map = new HashMap();
            map.put("sortType",1);
            map.put("limit",3);
            String json = PddUtil.PddDdkTopGoodsListQuery(map);
            JSONObject jsonObject = JSONObject.parseObject(json,JSONObject.class);
            DecimalFormat df = new DecimalFormat("#.00");
            JSONArray jsonArray = JSONObject.parseObject(((JSONObject)jsonObject.get("top_goods_list_get_response")).get("list").toString(),JSONArray.class);
            jsonArray.forEach(arr->{
                JSONObject obj = (JSONObject)arr;
                Double min_group_price = obj.getDouble("min_group_price")/100;//最小拼团价格 原价
                Integer coupon_discount = obj.getInteger("coupon_discount")/100;//优惠券面价格
                Double fee= 0.0;
                if(coupon_discount>0&& min_group_price>coupon_discount){
                     fee= min_group_price-Double.parseDouble(coupon_discount+"");//券后价格
                }else{
                    fee = min_group_price;
                }
                String dfee = df.format(fee);
                obj.put("goods_name",obj.getString("goods_name").trim());
                obj.put("min_group_price",min_group_price);
                obj.put("coupon_discount",coupon_discount);
                obj.put("fee",dfee);
                obj.put("goods_id",obj.getString("goods_id"));
            });
            model.addAttribute("goodList",jsonArray);
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return "pdd_index";
    }

    /**
     * 拼多多商品列表
     * @param request
     * @param response
     * @param model
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/pddGoodList")
    @ResponseBody
    public JSONObject pddGoodList(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {

        String keyword = getRequestParam(request, "keyword");//关键词
        String opt_id = getRequestParam(request, "opt_id");//商品标签类目ID
        String page = getRequestParam(request, "pageNo");//显示页数
        String pageSize = HttpUtils.getRequestParam(request, "pageSize");//显示条数
        String list_id = getRequestParam(request, "list_id");//翻页时必填前页返回的list_id值


        String key = MethodUtil.pdd_goods_list_key + "_opt_id_"+opt_id+"_page_"+page;
        String obj = RedisUtil.get(key);
        if(obj!=null){
            return JSONObject.parseObject(obj,JSONObject.class);
        }

        HashMap<String,String> paraMap = new HashMap<>();
        paraMap.put("version","v2.0.0");
        paraMap.put("pageNo",StringUtils.isNotEmpty(page)?page:"1");
        paraMap.put("pageSize",StringUtils.isNotEmpty(pageSize)?pageSize:"10");
        if(StringUtils.isNotEmpty(keyword)){
            paraMap.put("keyWords",keyword);
        }
        if(StringUtils.isNotEmpty(opt_id)){
            paraMap.put("opt_id",opt_id);
        }
        if(StringUtils.isNotEmpty(list_id)){
            paraMap.put("list_id",list_id);
        }
        if(opt_id!=null &&"0".equals(opt_id)){
            paraMap.put("sort_type","2");//默认佣金比例降序
            paraMap.remove("opt_id");
        }
        paraMap.put("hasCoupon","false");
        JSONObject json = otherService.getPddSearchList(paraMap,request);
        RedisUtil.setObject(key,MethodUtil.expires,json);
        return json;
    }

    @RequestMapping("/test")
    public String test(HttpServletRequest request, Model model) {
        String content = "fu植这行话₤GI9D1RlFjCc₤转移至淘宀┡ē【实木餐桌现代简约可伸缩折叠钢化玻璃餐桌家用带电磁炉餐桌椅组合】；或https://m.tb.cn/h.VgRpc9Y?sm=a4d58b 掂击鏈→接，再选择瀏lan嘂..大开";
        String word = MethodUtil.getWord(content);
        String goodsId = WxUtils.TbGoodInfoByGoodId(word,apikey);
        JSONObject obj = goodsService.getPrivilege(goodsId);
        String tpwd = null;
        try {
            if (obj.get("code").equals("1000")) {
                JSONObject jsonObject = JSONObject.parseObject(obj.get("data").toString(), JSONObject.class);
                tpwd = jsonObject.getString("tpwd");//淘口令
                log.info("tpwd:"+tpwd);
                JSONObject goodsDetailObj = goodsService.goodsDetail(goodsId);
                content = getContent(goodsDetailObj,tpwd);
            }else{
                content = "该产品没有优惠,换个产品吧!";
            }
        } catch (Exception e) {
            log.error("异常:{}",e.getMessage());
        }
        return "test";
    }


    public String getContent(JSONObject goodsDetailObj,String tpwd) throws Exception{
        String priceStr = null;
        String commissStr = null;
        String title = null;
        String content = null;
        if (goodsDetailObj.get("code").equals("1000")) {
            JSONObject goodsDetail = JSONObject.parseObject(goodsDetailObj.get("data").toString(), JSONObject.class);
            Integer couponPrice = goodsDetail.getInteger("couponPrice");
            //计算佣金
            String actualPrice = goodsDetail.getString("actualPrice");
            String commissionRate = goodsDetail.getString("commissionRate");
            Double commiss = MethodUtil.getPddCommission(actualPrice,commissionRate);
            title = goodsDetail.getString("title");
            if(couponPrice>0){
                priceStr = "【优惠券】:"+couponPrice+" 圓";
            }else{
                //无优惠券
                priceStr = "【预估价格】:"+actualPrice+" 圓";
            }

            commissStr = "【佣金】:"+commiss+" 沅";
        }
        content = title+"\n" +
                "----\n" +
                priceStr+"\n" +
                commissStr+"\n" +
                "----\n" +
                "複製本条消息，打开'手机tao宝'即可下单("+tpwd+")";
        return content;
    }

}
