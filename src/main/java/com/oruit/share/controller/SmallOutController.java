package com.oruit.share.controller;

import com.alibaba.fastjson.JSONObject;
import com.oruit.common.utils.HttpUtils;
import com.oruit.common.utils.JsonDealUtil;
import com.oruit.common.utils.web.RequestUtils;
import com.oruit.share.domain.GoodsInfoVO;
import com.oruit.share.service.GoodsService;
import com.oruit.share.service.OtherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 针对小程序对外提供接口
 */
@Controller
@RequestMapping("small")
@Slf4j
public class SmallOutController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OtherService otherService;

    /**
     * 商品列表
     * @param request
     * @param response
     * @param model
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/goodApList")
    @ResponseBody
    public JSONObject goodApList(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        String pageNo = HttpUtils.getRequestParam(request, "pageNo");//显示页数
        String pageSize = HttpUtils.getRequestParam(request, "pageSize");//显示条数
        String cid = HttpUtils.getRequestParam(request, "cid");//分类id
        JSONObject json = goodsService.getApGoodList(pageNo,cid,pageSize);
        return json;
    }

    /**
     * 商品分类
     * @param request
     * @param response
     * @param model
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/goodClassfiyList")
    @ResponseBody
    public JSONObject goodClassfiyList(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        JSONObject json = otherService.getGoodClassify();
        return json;
    }

    @PostMapping(value = "/goodList")
    @ResponseBody
    public JSONObject list(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        String pageNo = HttpUtils.getRequestParam(request, "pageNo");//显示页数
        String pageSize = HttpUtils.getRequestParam(request, "pageSize");//显示条数
        String cid = HttpUtils.getRequestParam(request, "cid");//分类id
        String subcid = HttpUtils.getRequestParam(request, "subcid");//二级分类id
        String sort  = HttpUtils.getRequestParam(request, "subcid");//排序字段
        String brands  = HttpUtils.getRequestParam(request, "brands");//品牌ID
        JSONObject json = goodsService.getGoodList(pageNo,cid,subcid,sort,pageSize,brands);
        return json;
    }

    @PostMapping(value = "/privilege")
    @ResponseBody
    public JSONObject privilege(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        String goodsId = HttpUtils.getRequestParam(request, "goodsId");//商品ID
        JSONObject obj = goodsService.getPrivilege(goodsId);
        JSONObject goodsDetailObj = goodsService.goodsDetail(goodsId);
        JSONObject jsonObject = null;
        JSONObject goodsDetail = null;
        String [] imgs = null;
        String [] detailPics = null;
        if(obj.get("code").equals("1000")){
            jsonObject = JSONObject.parseObject(obj.get("data").toString(),JSONObject.class);
        }
        if(goodsDetailObj.get("code").equals("1000")){
             goodsDetail = JSONObject.parseObject(goodsDetailObj.get("data").toString(),JSONObject.class);
             imgs = goodsDetail.get("imgs").toString().split(",");
             detailPics = goodsDetail.get("detailPics").toString().split(",");
        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("coupon",jsonObject);
        resultMap.put("good",goodsDetail);
        resultMap.put("imgs",imgs);
        resultMap.put("detailPics",detailPics);
        return JsonDealUtil.getSuccJSONObject("0|操作成功", resultMap.size()+"", resultMap);
    }

    /**
     * 商品详情(超级搜索)
     * @param request
     * @param response
     * @param model
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/goodPrivilege")
    @ResponseBody
    public JSONObject goodPrivilege(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        String goodsId = HttpUtils.getRequestParam(request, "goodsId");//商品ID
        JSONObject obj = goodsService.getPrivilege(goodsId);
        JSONObject goodsDetailObj = goodsService.goodsDetail(goodsId);
        JSONObject jsonObject = null;
        JSONObject goodsDetail = null;
        String [] imgs = null;
        String [] detailPics = null;
        if(obj.get("code").equals("1000")){
            jsonObject = JSONObject.parseObject(obj.get("data").toString(),JSONObject.class);
        }
        if(goodsDetailObj.get("code").equals("1000")){
            goodsDetail = JSONObject.parseObject(goodsDetailObj.get("data").toString(),JSONObject.class);
            imgs = goodsDetail.get("imgs").toString().split(",");
            detailPics = goodsDetail.get("detailPics").toString().split(",");
        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("coupon",jsonObject);
        resultMap.put("good",goodsDetail);
        resultMap.put("imgs",imgs);
        resultMap.put("detailPics",detailPics);
        return JsonDealUtil.getSuccJSONObject("0|操作成功", resultMap.size()+"", resultMap);
    }


    @PostMapping(value = "/ddqGoodList")
    @ResponseBody
    public JSONObject ddqGoodList(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        String roundTime = HttpUtils.getRequestParam(request, "roundTime");//场次时间
        JSONObject json = otherService.getDdqGoodList(roundTime);
        return json;
    }

    /**
     * 猜你喜欢的商品列表
     * @param request
     * @param response
     * @param model
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/likeGoodList")
    @ResponseBody
    public JSONObject likeGoodList(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        String goodId = HttpUtils.getRequestParam(request, "goodId");//场次时间
        JSONObject json = goodsService.similerGoodsList(goodId);
        return json;
    }

    /*** 拼多多接口  start  ****/

    /**
     * 主题接口
     * @param request
     * @param response
     * @param model
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/themeList")
    @ResponseBody
    public JSONObject themeList(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        JSONObject json = otherService.getThemeList();
        return json;
    }

    /**
     * 主题商品接口
     * @param request
     * @param response
     * @param model
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/themeGoodsList")
    @ResponseBody
    public JSONObject themeGoodsList(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        String themeId = HttpUtils.getRequestParam(request, "themeId");//主题ID
        JSONObject json = otherService.getThemeGoodsList(Long.parseLong(themeId));
        return json;
    }


    @PostMapping(value = "/goodDetailInfo")
    @ResponseBody
    public JSONObject goodDetailInfo(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        String goodsId = HttpUtils.getRequestParam(request, "goodsId");//商品ID
        JSONObject json = otherService.getPddGoodsDetailInfo(Long.parseLong(goodsId));
        return json;
    }

    @PostMapping(value = "/goodCmsUrl")
    @ResponseBody
    public JSONObject goodCmsUrl(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        String goodsId = HttpUtils.getRequestParam(request, "goodsId");//商品ID
        String webUrl = otherService.getPddGoodsPromotionUrl(Long.parseLong(goodsId));
        return JsonDealUtil.getSuccJSONObject("0|操作成功","", webUrl);
    }

    /*** 拼多多接口  end ****/

    /**
     * 生成优惠券
     * @param vo
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/loadGoodImgsUrl")
    @ResponseBody
    public JSONObject loadGoodImgsUrl(GoodsInfoVO vo,HttpServletRequest request)throws Exception {
        String userId = "123";
        String fileName = otherService.createCouponPoster(request,Long.parseLong(userId),vo);
        if(fileName!=null){
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("picUrl",fileName);
            return JsonDealUtil.getSuccJSONObject("0|操作成功","", resultMap);
        }else{
            return JsonDealUtil.getErrJSONObject("生成失败");
        }

    }


    /**
     * 获取淘口令
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/tklContentUrl")
    @ResponseBody
    public JSONObject tklContentUrl(HttpServletRequest request, HttpServletResponse response)throws Exception {
        String goodsId = HttpUtils.getRequestParam(request, "goodsId");//主题ID
        JSONObject obj = goodsService.getPrivilege(goodsId);
        return obj;
    }

    /**
     * 商品收藏
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/collect")
    @ResponseBody
    public JSONObject collect(HttpServletRequest request, HttpServletResponse response)throws Exception {

        return null;
    }



}
