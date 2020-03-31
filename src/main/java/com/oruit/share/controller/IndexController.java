package com.oruit.share.controller;

import com.alibaba.fastjson.JSONObject;
import com.oruit.common.utils.HttpUtils;
import com.oruit.common.utils.StringUtils;
import com.oruit.common.utils.cache.redis.RedisUtil;
import com.oruit.share.constant.RedisConstant;
import com.oruit.share.domain.TbBannerDO;
import com.oruit.share.domain.TbClassifyDO;
import com.oruit.share.domain.TbUser;
import com.oruit.share.service.AccessTokenService;
import com.oruit.share.service.GoodsService;
import com.oruit.share.service.UserService;
import com.oruit.weixin.WxUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

@Slf4j
@Controller
public class IndexController {

    @Autowired
    private GoodsService goodsService;

    @Value("${weixin.appId}")
    private String appId;

    @Value("${weixin.appSecret}")
    private String appSecret;

    @Value("${weixin.url}")
    private String NET_WEB;

    @Autowired
    private UserService userService;

    @Autowired
    private AccessTokenService accessTokenService;

    @RequestMapping("/MP_verify_saioSVJexjgyNclA.txt")
    public String verify(HttpServletRequest request,Model model) {
        return "verify";
    }

    @RequestMapping("/index")
    public String index(HttpServletRequest request,Model model,HttpSession session) {
       try{
           String openId = HttpUtils.getRequestParam(request, "openId");//商品名称
           JSONObject obj = goodsService.getGoodClassify(request);
           if(obj.get("code").equals("1000")){
               List<Map<String,Object>> list = JSONObject.parseObject(obj.get("data").toString(),List.class);

               List<Map<String,Object>> arr = new ArrayList<>();
               if(list.size()> 4){
                   for(int i=0;i<4;i++){
                       arr.add(list.get(i));
                   }
               }
               model.addAttribute("classifyList",arr);
               model.addAttribute("cid",list.get(0).get("cid"));
           }
           List<TbClassifyDO> tbClassifyDOList = goodsService.queryTbClassify(1);
           model.addAttribute("tbClassifyDOList",tbClassifyDOList);
           HashMap map = new HashMap();
           map.put("bannerType","1");
           List<TbBannerDO> tbBannerList = goodsService.queryTbBanner(map);
           model.addAttribute("tbBannerList", tbBannerList);
           if(StringUtils.isEmpty(openId)){
               openId = session.getAttribute("openId") != null?session.getAttribute("openId").toString():"";
           }
           log.info("index，openId:{}"+openId);
           TbUser tbUser = RedisUtil.getObject(RedisConstant.USER_LOGIN_OPEN_INFO_KEY+openId,TbUser.class);
           log.info("index，tbUser:{}"+tbUser.toString());
           model.addAttribute("openId", tbUser.getOpenId());
       }catch (Exception e){
           log.error("index error:{}",e.getMessage());
       }
       return "dtk_index";
    }


    @RequestMapping("/classify")
    public String classify(HttpServletRequest request, Model model) {
        JSONObject obj = goodsService.getGoodClassify(request);
        if(obj.get("code").equals("1000")){
            List<Map<String,Object>> list = JSONObject.parseObject(obj.get("data").toString(),List.class);
            model.addAttribute("classifyList",list);
            model.addAttribute("cid",list.get(0).get("cid"));
        }
        return "classify";
    }


    @RequestMapping("/changeText")
    @ResponseBody
    public JSONObject changeText(HttpServletRequest request,Model model){
        String cid = request.getParameter("cid");
        JSONObject obj = goodsService.getGoodClassifyText(cid);
        return obj;
    }

    @RequestMapping("/mineIndex")
    public void mineIndex(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        String url;
        String openId = session.getAttribute("openId")!=null?session.getAttribute("openId").toString():"";
        log.info("mineIndex user openId:{}",openId);
        if (StringUtils.isNotEmpty(openId)) {
            url = NET_WEB + "/mine";
            response.sendRedirect(url);
        } else {
            String inviteUrl = NET_WEB + "/mine";
            inviteUrl = URLEncoder.encode(inviteUrl, "utf-8");
            url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appId + "&redirect_uri=" + inviteUrl
                    + "&response_type=code&scope=snsapi_userinfo#wechat_redirect";
            response.sendRedirect(url);
        }
    }

    @RequestMapping("/mine")
    public String mine(HttpServletRequest request,HttpServletResponse response, Model model, HttpSession session, String code) {
        log.info("login code:{}", code);
        TbUser user = null;
        try {
            String openId = HttpUtils.getRequestParam(request, "openId");//商品名称
            if (StringUtils.isNotEmpty(code)) {
                user = WxUtils.openIdInfo(code, appId, appSecret, session);
            } else {
                user = RedisUtil.getObject(RedisConstant.USER_LOGIN_OPEN_INFO_KEY + openId, TbUser.class);
            }
        }catch (Exception e){
            log.info(e.getMessage());
        }
        model.addAttribute("headPic", user.getHeadPic());
        model.addAttribute("nickName", user.getUsername());
        return "mine";
    }

    @RequestMapping("/video")
    public String video(HttpServletRequest request, Model model) {
        model.addAttribute("videoUrl","https://m.baidu.com/sf/vsearch?pd=xsp&word=%E6%90%9E%E7%AC%91%E8%A7%86%E9%A2%91&tn=vsearch&sa=vs_ala_xsp_4660_n_1&lid=10137342229833437883&ms=1&atn=index");
        return "video_list";
    }


}
