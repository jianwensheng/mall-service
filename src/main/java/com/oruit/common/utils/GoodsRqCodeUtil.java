package com.oruit.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.oruit.share.domain.GoodsInfoVO;
import lombok.extern.slf4j.Slf4j;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URL;

import javax.imageio.ImageIO;

@Slf4j
public class GoodsRqCodeUtil {


    public static void drawCouponPosterImage(String host, Long userId, GoodsInfoVO vo, String filePath,String imageName)  {
        log.info("打印图片内容:"+vo.toString());
        try {
            String pngString = vo.getPicUrl().substring(vo.getPicUrl().length() - 3);
            String savePath = ZshopConstants.UploadFilesConstants.STATICFILESTEMPPATH;
            if (pngString.equals("png") || pngString.equals("jpg")) {
                URL url = new URL(vo.getPicUrl());
                InputStream is = url.openStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                String urlPath = savePath + imageName;
                File imageFile = new File(urlPath);
                OutputStream os = new FileOutputStream(imageFile);

                if (!imageFile.exists()) {
                    imageFile.mkdirs();
                }
                while ((len = is.read(buffer)) != -1) {
                    os.write(buffer, 0, len);
                }
                os.close();
                is.close();
                System.out.println("保存图片");
            }

            JSONObject json = new JSONObject();
            json.put("userId", userId);
            json.put("goodsId", vo.getGoodsId());

            // 平台
            String platPath = host + "/resources/images/logo_tb.png";
            if ("tmall".equals(vo.getFrom())) {
                platPath = host + "/resources/images/mal.png";
            } else if ("jd".equals(vo.getFrom())) {
                platPath = host + "/resources/images/nav-002.png";
            } else if ("pdd".equals(vo.getFrom())) {
                platPath = host + "/resources/images/nav-003.png";
            }
            BufferedImage platImage = ImageIO.read(new URL(platPath));

            // 透明底
            BufferedImage bg = new BufferedImage(750, 1250, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bg.createGraphics();
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, 750, 1250);// 填充整个屏幕
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,         RenderingHints.VALUE_ANTIALIAS_ON);						// 消除画图锯齿
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);	// 消除文字锯齿

            // 写入商品图
            Image src = Toolkit.getDefaultToolkit().getImage(new URL(vo.getPicUrl()));
            BufferedImage goodsImg = BufferedImageBuilder.toBufferedImage(src);
            if (pngString.equals("png")) {	// png单独处理
                String strImg = ZshopConstants.UploadFilesConstants.STATICFILESTEMPPATH + imageName;
                goodsImg = ImageIO.read(new File(strImg));
            }
            int y = 10;
            Rectangle rectangle = new Rectangle(10, y, 730, 730);
            g.drawImage(goodsImg.getScaledInstance(rectangle.width, rectangle.height, Image.SCALE_SMOOTH), rectangle.x, rectangle.y, null);
            y+=735;

            // 优惠券背景
            String couponPath = host + "/resources/images/goods_quan.png";
            BufferedImage couponImg = ImageIO.read(new URL(couponPath));
            g.drawImage(couponImg.getScaledInstance(730, 158, Image.SCALE_SMOOTH), 10, y+10, 730, 158, null);
            y+=75;


            String couponPrice = vo.getCouponPrice() == null ? "0" : vo.getCouponPrice();
            String info = vo.getHasCoupon() ?  couponPrice + "元券":"";

            g.setFont(new Font("Microsoft YaHei", Font.BOLD, 30));
            g.setColor(new Color(255, 255, 255));
            g.drawString(info , 110, y+15);

            String btnTitle ="长按获取";
            g.setColor(new Color(255, 255, 255));
            g.setFont(new Font("Microsoft YaHei", Font.PLAIN, 32));
            g.drawString(btnTitle, 530, y+25);

            y+=88;

            // 写入二维码
            String prefix = "jd".equals(vo.getFrom()) ? ZshopConstants.JD_PREFIX : "pdd".equals(vo.getFrom()) ? ZshopConstants.PDD_PREFIX : ZshopConstants.TAOBAO_PREFIX;
            BufferedImage qrcode = QrcodeGenerator.encode(	ZshopConstants.MOBILE_HOST + "/#/detail/" + prefix + vo.getGoodsId(), 230, 230);
            g.drawImage(qrcode, 510, y+5, 230, 230, null);

            g.setFont(new Font("Microsoft YaHei", Font.PLAIN, 22));
            g.setColor(Color.gray);
            g.drawString("长按识别二维码" , 550, y+240);
            y+=25;

            // 写入平台图片
            g.drawImage(platImage.getScaledInstance(52, 40, Image.SCALE_SMOOTH), 15, y+2, 52, 40, null);
            y+=30;

            // 写入商品名
            g.setFont(new Font("Microsoft YaHei", Font.PLAIN, 32));
            g.setColor(Color.BLACK);
            String title = vo.getTitle();
            char[] sc = title.toCharArray();
            int titleOffsetY = 0;
            String titleSub = "";
            int row = 0;
            int j = 0;
            for (int i = 0; i < title.length(); i++) {
                titleSub = title.substring(j, i);
                int titleSubW = g.getFontMetrics().charsWidth(sc, j, i-j);
                int len = row >0 ?  450  : 400;
                if(titleSubW > len){
                    j = i;
                    int ox = row > 0 ? 15 : 75;
                    row++;
                    if( row == 3 && j < title.length()-1  ){
                        titleSub = titleSub.substring(0, titleSub.length()-1)  + "...";
                    }
                    g.drawString(titleSub, ox, y + titleOffsetY );

                    if(row  == 3){
                        break;
                    }else{
                        titleOffsetY += 40;
                    }
                }
            }
            if(row == 0){
                titleSub = title.substring(j,  title.length());
                g.drawString(titleSub, 75, y + titleOffsetY );
            }else if(row == 1 || row == 2){
                titleSub = title.substring(j,  title.length());
                g.drawString(titleSub, 10, y + titleOffsetY );
            }
            y =y + titleOffsetY + 40;

            g.setFont(new Font("Microsoft YaHei", Font.PLAIN, 25));
            g.setColor(Color.gray);
            g.drawString("商品价格以实际价格为准", 15, y);
            y+=30;

            // 到手价背景
            g.drawRect(15, y +10,90,40);
            g.setColor(new Color(255, 0, 61));
            g.fillRect(15, y +10,90,40);

            // 到手价
            g.setFont(new Font("Microsoft YaHei", Font.BOLD, 25));
            g.setColor(Color.white);
            g.drawString("到手价" , 23, y+38);


            // 到手价人民币图标
            g.setFont(new Font("Microsoft YaHei", Font.PLAIN, 36));
            g.setColor(Color.red);
            g.drawString("¥" , 125, y+45);

            Double price = Double.parseDouble(vo.getPrice());

            // 到手价金额
            g.setFont(new Font("Microsoft YaHei", Font.BOLD, 60));
            g.setColor(Color.red);
            g.drawString(price <0?  vo.getPrice() : price.toString() , 145, y+45);

            // 原价
            g.setFont(new Font("Microsoft YaHei", Font.PLAIN, 30));
            g.setColor(new Color(89, 89, 89));
            g.drawString("原价: " , 350, y+45);

            // 原价金额
            g.setFont(new Font("Microsoft YaHei", Font.PLAIN, 30));
            g.setColor(new Color(89, 89, 89));
            g.drawString("¥" + vo.getOrgPrice() , 415, y+45);

           /* y+=110;

            g.setFont(new Font("Microsoft YaHei", Font.PLAIN, 36));
            g.setColor(Color.gray);
            g.drawString("水滴券", 110, y);

            g.setFont(new Font("Microsoft YaHei", Font.PLAIN, 25));
            g.setColor(Color.gray);
            g.drawString("—— 淘宝、天猫、京东...优惠专享", 225, y-5);
            y+=10;*/

            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);						// 消除画图锯齿
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);	// 消除文字锯齿

            g.dispose();
            ImageIO.write(bg, "jpg", new File(filePath));
        } catch (IOException e) {
            log.error("生成优惠券海报失败", e);
        }
    }


    public static void drawInvitationImage(String inviteCode,String filePath,String codeUrl)  {
        log.info("打印图片二维码:"+codeUrl);
        try {

            // 设置背景图片
            BufferedImage bg =ImageIO.read(new File(filePath));
            Graphics2D g = bg.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,         RenderingHints.VALUE_ANTIALIAS_ON);	    // 消除画图锯齿
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);	// 消除文字锯齿

            int y = 1050;

            //我的邀请码
            g.setFont(new Font("Microsoft YaHei", Font.PLAIN, 23));
            g.setColor(Color.white);
            g.drawString(inviteCode , 290, y+65);

            // 写入二维码
            BufferedImage qrcode = QrcodeGenerator.encode(	codeUrl, 155, 155);
            g.drawImage(qrcode, 475, y+5, 150, 150, null);

            g.setFont(new Font("Microsoft YaHei", Font.PLAIN, 22));
            g.setColor(Color.gray);
            g.drawString("长按识别二维码" , 470, y+170);
            y+=25;

            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);				// 消除画图锯齿
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);	// 消除文字锯齿

            g.dispose();
            ImageIO.write(bg, "jpg", new File(filePath));
        } catch (IOException e) {
            log.error("生成邀请码海报失败", e);
        }
    }



}
