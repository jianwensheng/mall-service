package com.oruit.share.controller;

import com.alibaba.fastjson.JSONObject;
import com.oruit.common.utils.MethodUtil;
import com.oruit.common.utils.StringUtils;
import com.oruit.share.dao.TbInviteDetailMapper;
import com.oruit.share.domain.TbInviteDetail;
import com.oruit.share.domain.TextMessage;
import com.oruit.share.service.GoodsService;
import com.oruit.share.service.TbInviteDetailService;
import com.oruit.share.service.TbInviteService;
import com.oruit.share.service.WeixinService;
import com.oruit.weixin.WxUtils;
import com.oruit.weixin.util.MessageUtil;
import com.thoughtworks.xstream.XStream;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("wx")
@Slf4j
public class WeixinController {

    @Autowired
    private WeixinService weixinService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private TbInviteDetailService tbInviteDetailService;

    @Autowired
    private TbInviteService tbInviteService;

    @Value("${taobao.apikey}")
    private String apikey;

    @Value("weixin.url")
    private String wxUrl;

    @RequestMapping(value="/check",method = {RequestMethod.GET, RequestMethod.POST})
    public void check(HttpServletRequest request, HttpServletResponse response,HttpSession session) throws Exception {
        // 将请求、响应的编码均设置为UTF-8（防止中文乱码）
        request.setCharacterEncoding("UTF-8");  //微信服务器POST消息时用的是UTF-8编码，在接收时也要用同样的编码，否则中文会乱码；
        response.setCharacterEncoding("UTF-8"); //在响应消息（回复消息给用户）时，也将编码方式设置为UTF-8，原理同上；
        boolean isGet = request.getMethod().toLowerCase().equals("get");
        PrintWriter out = response.getWriter();
        try {
            if (isGet) {
                String signature = request.getParameter("signature");
                String timestamp = request.getParameter("timestamp");
                String nonce = request.getParameter("nonce");
                String echostr = request.getParameter("echostr");
                if (weixinService.checkSignature(signature, timestamp, nonce)) {
                    out.print(echostr);
                    log.info("微信服务验证成功!");
                }
            }else{
                try {
                    String respMessage = "异常消息！";
                    respMessage = weixinPost(request,response,session);
                    out.write(respMessage);
                } catch (Exception e) {
                    log.error("Failed to convert the message from weixin!异常:{}",e.getMessage());
                }
            }
        }catch (Exception ex){
            log.error("微信异常错误:{}",ex.getMessage());
        }finally {
            out.close();
            out = null;
        }

    }

    /**
     * 处理微信发来的请求
     *
     * @param request
     * @return
     */
    public String weixinPost(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        String respMessage = null;
        try {

            // xml请求解析
            Map<String, String> requestMap = xmlToMap(request);

            // 发送方帐号（open_id）
            String fromUserName = requestMap.get("FromUserName");
            // 公众帐号
            String toUserName = requestMap.get("ToUserName");
            // 消息类型
            String msgType = requestMap.get("MsgType");
            // 消息内容
            String content = requestMap.get("Content");

            log.info("FromUserName is:" + fromUserName + ", ToUserName is:" + toUserName + ", MsgType is:" + msgType);

            // 文本消息
            if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
               //这里根据关键字执行相应的逻辑，只有你想不到的，没有做不到的
                String word = MethodUtil.getWord(content);
                if(StringUtils.isNotEmpty(word)){
                    String tpwd = null;
                   //根据获得的淘口令去查询相应的信息
                   String goodsId = WxUtils.TbGoodInfoByGoodId(word,apikey);
                   log.info("goodsId:"+goodsId);
                   JSONObject obj = goodsService.getPrivilege(goodsId);
                   if (obj.get("code").equals("1000")) {
                        JSONObject jsonObject = JSONObject.parseObject(obj.get("data").toString(), JSONObject.class);
                        tpwd = jsonObject.getString("tpwd");//淘口令
                        log.info("tpwd:"+tpwd);
                        JSONObject goodsDetailObj = goodsService.goodsDetail(goodsId);
                        content = getContent(goodsDetailObj,tpwd);
                    }else{
                       content = "该产品没有优惠,换个产品吧!";
                   }

                }
                //自动回复
                TextMessage text = new TextMessage();
                text.setContent(content);
                text.setToUserName(fromUserName);
                text.setFromUserName(toUserName);
                text.setCreateTime(new Date().getTime() + "");
                text.setMsgType(msgType);

                respMessage = textMessageToXml(text);

            }else if(msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)){
                //

            }else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
                String eventType = requestMap.get("Event");// 事件类型
                // 订阅
                if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
                    TextMessage text = new TextMessage();
                    text.setContent("欢迎关注，滴答快乐!");
                    text.setToUserName(fromUserName);
                    text.setFromUserName(toUserName);
                    text.setCreateTime(new Date().getTime() + "");
                    text.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);

                    //关注公众号的同时检查是否存在邀请关系
                    String eventKey = requestMap.get("EventKey");// 事件KEY值，与创建自定义菜单时指定的KEY值对应
                    if(eventKey.contains("qrscene_")){
                        Long userId =  Long.valueOf(eventKey.split("_")[1]);
                        //更新邀请人数以及邀请详细记录表
                        TbInviteDetail tbInviteDetail = tbInviteDetailService.queryTbInviteDetailUserId(userId,fromUserName);
                        if(tbInviteDetail==null){
                            tbInviteDetail = new TbInviteDetail();
                            tbInviteDetail.setUserId(userId);
                            tbInviteDetail.setInvitedOpenId(fromUserName);
                            tbInviteDetail.setCreateTime(new Date());
                            tbInviteDetail.setUpdateTime(new Date());
                            tbInviteDetailService.insertSelective(tbInviteDetail);
                            tbInviteService.addInviteNumber(userId);

                        }
                    }

                    respMessage = textMessageToXml(text);


                }else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {// 取消订阅
                    // TODO 取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息

                }else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
                   /* // 自定义菜单点击事件
                    String eventKey = requestMap.get("EventKey");// 事件KEY值，与创建自定义菜单时指定的KEY值对应
                    if (eventKey.equals("V1001_Mall")) {
                        //商城
                        String url = wxUrl+"?openId="+fromUserName;
                        response.sendRedirect(url);
                        return null;
                    }else if (eventKey.equals("V1001_UserCenter")) {
                        //进入用户中心
                        String url = wxUrl+"/mineIndex?openId="+fromUserName;
                        response.sendRedirect(url);
                        return null;
                    }*/
                    /**
                     TextMessage text = new TextMessage();
                     text.setContent("0755-86671980");
                     text.setToUserName(fromUserName);
                     text.setFromUserName(toUserName);
                     text.setCreateTime(new Date().getTime() + "");
                     text.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
                     respMessage = textMessageToXml(text);
                     */
                }
            }
        }
        catch (Exception e) {
            log.error("weixinPost error {}",e.getMessage());
        }
        return respMessage;
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

    /**
     * xml转换为map
     * @param request
     * @return
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> xmlToMap(HttpServletRequest request) throws IOException {
        Map<String, String> map = new HashMap<String, String>();
        SAXReader reader = new SAXReader();

        InputStream ins = null;
        try {
            ins = request.getInputStream();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        Document doc = null;
        try {
            doc = reader.read(ins);
            Element root = doc.getRootElement();

            List<Element> list = root.elements();

            for (Element e : list) {
                map.put(e.getName(), e.getText());
            }

            return map;
        } catch (DocumentException e1) {
            e1.printStackTrace();
        }finally{
            ins.close();
        }

        return null;
    }

    /**
     * 文本消息对象转换成xml
     *
     * @param textMessage 文本消息对象
     * @return xml
     */
    public static String textMessageToXml(TextMessage textMessage){
        XStream xstream = new XStream();
        xstream.alias("xml", textMessage.getClass());
        return xstream.toXML(textMessage);
    }

}
