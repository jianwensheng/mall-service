package com.oruit.share.controller;

import com.oruit.share.domain.TextMessage;
import com.oruit.share.service.WeixinService;
import com.oruit.weixin.util.MessageUtil;
import com.thoughtworks.xstream.XStream;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    WeixinService weixinService;

    @RequestMapping(value="/check",method = {RequestMethod.GET, RequestMethod.POST})
    public String check(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
                    log.info("微信服務驗證成功!");
                }
            }else{
                try {
                    String respMessage = "异常消息！";
                    respMessage = weixinPost(request);
                    out.write(respMessage);
                    log.info("The request completed successfully");
                    log.info("to weixin server "+respMessage);
                    return respMessage;
                } catch (Exception e) {
                    log.error("Failed to convert the message from weixin!");
                }
            }
        }catch (Exception ex){
            log.error("微信异常错误:{}",ex.getMessage());
        }finally {
            out.close();
            out = null;
        }

       return null;
    }

    /**
     * 处理微信发来的请求
     *
     * @param request
     * @return
     */
    public String weixinPost(HttpServletRequest request) {
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
                if(content.equals("xxx")){

                }

                //自动回复
                TextMessage text = new TextMessage();
                text.setContent("the text is" + content);
                text.setToUserName(fromUserName);
                text.setFromUserName(toUserName);
                text.setCreateTime(new Date().getTime() + "");
                text.setMsgType(msgType);

                respMessage = textMessageToXml(text);

            } /*else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {// 事件推送
                String eventType = requestMap.get("Event");// 事件类型

                if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {// 订阅
                    respContent = "欢迎关注xxx公众号！";
                    return MessageResponse.getTextMessage(fromUserName , toUserName , respContent);
                } else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {// 自定义菜单点击事件
                    String eventKey = requestMap.get("EventKey");// 事件KEY值，与创建自定义菜单时指定的KEY值对应
                    logger.info("eventKey is:" +eventKey);
                    return xxx;
                }
            }
            //开启微信声音识别测试 2015-3-30
            else if(msgType.equals("voice"))
            {
                String recvMessage = requestMap.get("Recognition");
                //respContent = "收到的语音解析结果："+recvMessage;
                if(recvMessage!=null){
                    respContent = TulingApiProcess.getTulingResult(recvMessage);
                }else{
                    respContent = "您说的太模糊了，能不能重新说下呢？";
                }
                return MessageResponse.getTextMessage(fromUserName , toUserName , respContent);
            }
            //拍照功能
            else if(msgType.equals("pic_sysphoto"))
            {

            }
            else
            {
                return MessageResponse.getTextMessage(fromUserName , toUserName , "返回为空");
            }*/
            // 事件推送
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
                String eventType = requestMap.get("Event");// 事件类型
                // 订阅
                if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {

                    TextMessage text = new TextMessage();
                    text.setContent("欢迎关注，xxx");
                    text.setToUserName(fromUserName);
                    text.setFromUserName(toUserName);
                    text.setCreateTime(new Date().getTime() + "");
                    text.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);

                    respMessage = textMessageToXml(text);
                }
                // TODO 取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息
                else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {// 取消订阅


                }
                // 自定义菜单点击事件
                else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
                    String eventKey = requestMap.get("EventKey");// 事件KEY值，与创建自定义菜单时指定的KEY值对应
                    if (eventKey.equals("customer_telephone")) {
                        TextMessage text = new TextMessage();
                        text.setContent("0755-86671980");
                        text.setToUserName(fromUserName);
                        text.setFromUserName(toUserName);
                        text.setCreateTime(new Date().getTime() + "");
                        text.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);

                        respMessage = textMessageToXml(text);
                    }
                }
            }
        }
        catch (Exception e) {
            log.error("weixinPost error {}",e.getMessage());
        }
        return respMessage;
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
