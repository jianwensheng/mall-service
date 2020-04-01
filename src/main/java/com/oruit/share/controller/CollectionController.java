package com.oruit.share.controller;

import com.alibaba.fastjson.JSONObject;
import com.oruit.common.utils.HttpUtils;
import com.oruit.common.utils.JsonDealUtil;
import com.oruit.share.domain.TbCollection;
import com.oruit.share.domain.TbUser;
import com.oruit.share.service.CollectionService;
import com.oruit.share.service.GoodsService;
import com.oruit.share.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RequestMapping("collection")
@Controller
public class CollectionController {

    @Autowired
    private CollectionService collectionService;

    @Autowired
    private UserService userService;

    @Autowired
    private GoodsService goodsService;

    @RequestMapping("/index")
    public String index(HttpServletRequest request, Model model) throws Exception {
        return "collection";
    }

    @RequestMapping("/collectionList")
    @ResponseBody
    public JSONObject collectionList(HttpServletRequest request, Model model) throws Exception {
        String token = HttpUtils.getRequestParam(request, "token");//用户token
        TbUser user = userService.queryTokenUserInfo(token);
        String pageIndex = HttpUtils.getRequestParam(request, "pageIndex");//显示页数
        String pageSize = HttpUtils.getRequestParam(request, "pageSize");//显示条数
        HashMap map = new HashMap();
        map.put("userId", user.getId());
        map.put("pageIndex", Integer.parseInt(pageIndex)-1);
        map.put("pageSize", pageSize);
        List<TbCollection> tbCollectionList = collectionService.queryTbCollectionList(map);

        return JsonDealUtil.getSuccJSONObject("0|操作成功", String.valueOf(tbCollectionList.size()), tbCollectionList);

    }

    /**
     * 商品收藏
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/collect")
    @ResponseBody
    public JSONObject collect(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String userToken = HttpUtils.getRequestParam(request, "userToken");//用户userToken
        String goodsId = HttpUtils.getRequestParam(request, "goodsId");//商品ID
        String plat = HttpUtils.getRequestParam(request, "plat");//平台
        String goodName = HttpUtils.getRequestParam(request, "goodName");//商品名称
        String goodImg = HttpUtils.getRequestParam(request, "goodImg");//商品主图
        String goodDesc = HttpUtils.getRequestParam(request, "goodDesc");//商品详情
        String couponPrice = HttpUtils.getRequestParam(request, "couponPrice");//优惠券面额
        String actualPrice = HttpUtils.getRequestParam(request, "actualPrice");//券后价
        String originalPrice = HttpUtils.getRequestParam(request, "originalPrice");//商品原价
        String msg="";
        String code = "";
        try {
            TbUser user = userService.queryTokenUserInfo(userToken);
            Boolean flag = false;
            int count;
            TbCollection tbCollection = collectionService.queryTbCollection(user.getId(), Long.valueOf(goodsId));
            if (tbCollection == null) {
                tbCollection = new TbCollection();
                tbCollection.setGoodId(Long.parseLong(goodsId));
                tbCollection.setUserId(user.getId());
                tbCollection.setCreateTime(new Date());
                tbCollection.setUpdateTime(new Date());
                tbCollection.setPlat(plat);
                tbCollection.setStatus(1);
                tbCollection.setGoodName(goodName);
                tbCollection.setGoodImg(goodImg);
                tbCollection.setGoodDesc(goodDesc);
                tbCollection.setCouponPrice(couponPrice);
                tbCollection.setActualPrice(new BigDecimal(actualPrice));
                tbCollection.setOriginalPrice(new BigDecimal(originalPrice));
                count = collectionService.insertSelective(tbCollection);
                if (count > 0) {
                    flag = true;
                    msg = "收藏成功";
                    code = "1";
                } else {
                    msg = "收藏失败";
                }
            }else{
                //不为空,检查两种状态
                if(tbCollection.getStatus()==0){
                    //更改为收藏
                    tbCollection.setStatus(1);
                    tbCollection.setUpdateTime(new Date());
                    tbCollection.setGoodName(goodName);
                    tbCollection.setGoodImg(goodImg);
                    tbCollection.setGoodDesc(goodDesc);
                    tbCollection.setCouponPrice(couponPrice);
                    tbCollection.setActualPrice(new BigDecimal(actualPrice));
                    tbCollection.setOriginalPrice(new BigDecimal(originalPrice));
                    tbCollection.setPlat(plat);
                    collectionService.updateByPrimaryKeySelective(tbCollection);
                    msg = "收藏成功";
                    code = "1";
                    flag = true;
                }else if(tbCollection.getStatus()==1){
                    tbCollection.setStatus(0);
                    tbCollection.setUpdateTime(new Date());
                    tbCollection.setGoodName(goodName);
                    tbCollection.setGoodImg(goodImg);
                    tbCollection.setGoodDesc(goodDesc);
                    tbCollection.setCouponPrice(couponPrice);
                    tbCollection.setActualPrice(new BigDecimal(actualPrice));
                    tbCollection.setOriginalPrice(new BigDecimal(originalPrice));
                    tbCollection.setPlat(plat);
                    collectionService.updateByPrimaryKeySelective(tbCollection);
                    msg = "已取消收藏";
                    code = "0";
                    flag = true;
                }
            }

            if (flag) {
                return JsonDealUtil.getCodeJSONObject(msg, code);
            } else {
                return JsonDealUtil.getErrJSONObject(msg);
            }
        } catch (Exception ex) {
            log.error("collect error:{}", ex.getMessage());
            return JsonDealUtil.getErrJSONObject(msg);
        }

    }


}
