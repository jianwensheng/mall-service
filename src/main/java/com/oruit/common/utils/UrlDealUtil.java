package com.oruit.common.utils;

import com.oruit.common.constant.ConstUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * @author hanfeng
 */
public class UrlDealUtil {
    private static final Logger logger = LoggerFactory.getLogger(UrlDealUtil.class);

    // 倒数第一个参数随机的长度：纯字母
    private static final Integer LAST_PARAM_SIZE_RANGE = 5;
    //倒数第二个参数随机的长度:纯数字
    private static final Integer NEXT_TO_LAST_PARAM_SIZE_RANGE = 4;
    private static final String[] URL_LAST_PARAM_ARR = {"deal", "fhdeal", "ad", "ds", "ainfo", "rainfo", "cnzz", "ads", "ddzcnnz", "ruanwencnzz"};
    //随机的后缀
    private static final String[] SUFFIX_ARR = {".php", ".aspx", ".jsp"};

    public static String getUrlAppendString() {
        StringBuilder urlAppendString = new StringBuilder();
        String lastParam = RandomUtils.getRandomLetters(LAST_PARAM_SIZE_RANGE).toLowerCase();
        for (String item : URL_LAST_PARAM_ARR) {
            if (lastParam.indexOf(item) == 0) {
                lastParam = "mm" + lastParam;
            }
        }
        urlAppendString.append("/")
                .append(RandomUtils.getRandomNumbers(NEXT_TO_LAST_PARAM_SIZE_RANGE))
                .append("/")
                .append(lastParam)
                .append(".html");
        return urlAppendString.toString();
    }

    /**
     * 将逻辑处理链接转化为显示链接
     *
     * @param dealUrl
     * @return
     */
    public static String dealUrlToShowUrl(String dealUrl) {
        String showUrl = "";

        try {
            String[] dealUrlParts = dealUrl.split("/");
            if (dealUrlParts[3].length() > 30) {
                showUrl = dealUrlToShowUrl_1(dealUrl);
            } else {
                Random random = new Random();
                int methodType = random.nextInt(4) + 2;
                switch (methodType) {
                    case 2:
                        showUrl = dealUrlToShowUrl_2(dealUrl);
                        break;
                    case 3:
                        showUrl = dealUrlToShowUrl_3(dealUrl);
                        break;
                    case 4:
                        showUrl = dealUrlToShowUrl_4(dealUrl);
                        break;
                    case 5:
                        showUrl = dealUrlToShowUrl_5(dealUrl);
                        break;
                    default:
                        showUrl = dealUrlToShowUrl_2(dealUrl);
                        break;
                }
            }

        } catch (Exception e) {
            showUrl = dealUrl;
        }
        return showUrl;
    }

    /**
     * 将显示链接转化为逻辑处理链接
     *
     * @param showUrl
     * @return
     */
    public static String showUrlToDealUrl(String showUrl) {
        String dealUrl = "";
        try {
            String[] showUrlParts = showUrl.split("/");
            if (showUrlParts[3].length() > 30) {
                dealUrl = showUrlToDealUrl_1(showUrl);
            } else {
                switch (showUrlParts[3].charAt(1)) {
                    default:
                        dealUrl = showUrlToDealUrl_2(showUrl);
                        break;
                    case '2':
                        dealUrl = showUrlToDealUrl_2(showUrl);
                        break;
                    case '3':
                        dealUrl = showUrlToDealUrl_3(showUrl);
                        break;
                    case '4':
                        dealUrl = showUrlToDealUrl_4(showUrl);
                        break;
                    case '5':
                        dealUrl = showUrlToDealUrl_5(showUrl);
                        break;
                }
            }
        } catch (Exception e) {
            dealUrl = showUrl;
        }
        return dealUrl;
    }


    /**
     * 将逻辑处理链接转化为显示链接 注：展示链接处理方案：链接字符串参数只有个特殊标记key规则如下 uuid的第5位，作为表示为是否计量 偶数：不计量
     * 奇数：计量 eg:3df4d12b-6a34-43c9-8537-6835f3b24ab2
     *
     * @param dealUrl
     * @return
     */
    public static String dealUrlToShowUrl_1(String dealUrl) {
        //dealUrl：http://www.ddawd.cn/3df4d12b-6a34-43c9-8537-6835f3b24ab2/466720acd1e742f18de7f7d19a1ec/deal/1559/mitTy.html
        //showUrl：http://www.baidu.com/fe5f3m7f218d83490b9e1a6b640b76b6080Wed=
        // or  ads:  
        //dealUrl：http://www.udrpum.cn/7b0369c4-06f1-41df-90b5-01616b5c3ccc/466720acd1e742f18de7f7d19a1ec/15386/464683/2/ads/1742/umozi.html
        //showUrl：http://www.udrpum.cn/7b0369mc406f141df90b501616b5c3cccghg=/15386/464683/2
        String showUrl = "";
        try {
            String[] dealUrlParts = dealUrl.split("/");
            String uuid = dealUrlParts[3].replace("-", "");
            //realflag:偶数 showflag：奇数        
            String realFlag = "";
            Random random = new Random();
            if (dealUrlParts[4].equalsIgnoreCase(ConstUtils.REAL_URL_FLAG.getValue())) {
                //REAL_URL_FLAG
                realFlag = ((random.nextInt(5) * 2) + 1) + "";
            } else if (dealUrlParts[4].equalsIgnoreCase(ConstUtils.SHOW_URL_FLAG.getValue())) {
                //SHOW_URL_FLAG
                realFlag = random.nextInt(5) * 2 + "";
            }
            if (dealUrl.toLowerCase().contains("/ads/")) {
                realFlag = "w";
            }
            //将标志位插入第5位
            uuid = uuid.substring(0, 4) + realFlag + uuid.substring(4);

            //随机生成3到6位的随机字符串
            String randomString = RandomUtils.getRandomLetters(random.nextInt(3) + 3);

            if (realFlag.equals("w")) {
                showUrl = dealUrlParts[0] + "//" + dealUrlParts[2] + "/" + uuid + randomString
                        + "/" + dealUrlParts[5] + "/" + dealUrlParts[6] + "/" + dealUrlParts[7];
            } else {
                showUrl = dealUrlParts[0] + "//" + dealUrlParts[2] + "/" + uuid + randomString;
            }
        } catch (Exception e) {
            showUrl = dealUrl;
        }
        return showUrl;
    }

    /**
     * 将显示链接转化为逻辑处理链接
     *
     * @param showUrl
     * @return
     */
    public static String showUrlToDealUrl_1(String showUrl) {
        //showUrl：http://www.baidu.com/fe5f3m7f218d83490b9e1a6b640b76b6080Wed=
        //dealUrl：http://www.ddawd.cn/3df4d12b-6a34-43c9-8537-6835f3b24ab2/466720acd1e742f18de7f7d19a1ec/deal/1559/mitTy.html

        // or  ads:    
        //showUrl：http://www.udrpum.cn/7b0369mc406f141df90b501616b5c3cccghg=/15386/464683/2
        //dealUrl：http://www.udrpum.cn/7b0369c4-06f1-41df-90b5-01616b5c3ccc/466720acd1e742f18de7f7d19a1ec/15386/464683/2/ads/1742/umozi.html
        String dealUrl = "";
        try {
            String[] showlUrlParts = showUrl.split("[/?&]");
            if (showlUrlParts[3].length() <= 32 || showlUrlParts[3].contains("-")) {
                return showUrl;
            }
            String uuid = showlUrlParts[3].substring(0, 4) + showlUrlParts[3].substring(5, 33);
            //增加上-
            uuid = uuid.substring(0, 8)
                    + "-" + uuid.substring(8, 12)
                    + "-" + uuid.substring(12, 16)
                    + "-" + uuid.substring(16, 20)
                    + "-" + uuid.substring(20);
            //realflag:偶数 showflag：奇数 w:abs
            String strRealFlag = "";
            if (Character.isDigit(showlUrlParts[3].charAt(4))) {
                int realFlag = Character.getNumericValue(showlUrlParts[3].charAt(4));
                if (realFlag % 2 != 0) {
                    //REAL_URL_FLAG
                    strRealFlag = ConstUtils.REAL_URL_FLAG.getValue();
                } else {
                    //SHOW_URL_FLAG
                    strRealFlag = ConstUtils.SHOW_URL_FLAG.getValue();
                }
            } else if (showlUrlParts[3].charAt(4) == 'w') {
                //REAL_URL_FLAG
                strRealFlag = ConstUtils.REAL_URL_FLAG.getValue();
            }

            if (showlUrlParts[3].charAt(4) == 'w') {
                dealUrl = showlUrlParts[0] + "//" + showlUrlParts[2] + "/" + uuid + "/" + strRealFlag
                        + "/" + showlUrlParts[4] + "/" + showlUrlParts[5] + "/" + showlUrlParts[6]
                        + "/ads/1742/umozi.html";
            } else {
                dealUrl = showlUrlParts[0] + "//" + showlUrlParts[2] + "/" + uuid + "/" + strRealFlag + "/deal/1559/mitTy.html";
            }
        } catch (Exception e) {
            dealUrl = showUrl;
        }
        return dealUrl;
    }

    /**
     * 将逻辑处理链接转化为显示链接 注：展示链接处理方案：
     * 链接字符串参数第2位表示用哪个转换方法，例如2表示dealUrlToShowUrl_2
     * 第6位，作为表示为是否计量 偶数：不计量 奇数：计量
     * 第6位如果是W，表示是ads广告链接
     *
     * @param dealUrl
     * @return
     */
    public static String dealUrlToShowUrl_2(String dealUrl) {
        //dealUrl：http://www.ddawd.cn/2QQYYIBF7B/466720acd1e742f18de7f7d19a1ec/deal/1559/mitTy.html
        //showUrl：http://www.ctdzlt.com/2QQY1YIBF7B.xdGrzzGP
        // or  ads:  
        //dealUrl：http://www.udrpum.cn/2QQYYIBF7B/466720acd1e742f18de7f7d19a1ec/15386/464683/2/ads/1742/umozi.html
        //showUrl：http://www.udrpum.cn/2Q2QYWYIBF7B/15386/464683/2.eUn
        String showUrl = "";
        //Url转换方法：方法2
        String methodType = "2";
        try {
            String[] dealUrlParts = dealUrl.split("/");
            String uuid = dealUrlParts[3].replace("-", "");
            //realflag:偶数 showflag：奇数        
            String realFlag = "";
            Random random = new Random();
            if (dealUrlParts[4].equalsIgnoreCase(ConstUtils.REAL_URL_FLAG.getValue())) {
                //REAL_URL_FLAG
                realFlag = ((random.nextInt(5) * 2) + 1) + "";
            } else if (dealUrlParts[4].equalsIgnoreCase(ConstUtils.SHOW_URL_FLAG.getValue())) {
                //SHOW_URL_FLAG
                realFlag = random.nextInt(5) * 2 + "";
            }
            if (dealUrl.toLowerCase().contains("/ads/")) {
                realFlag = "W";
            }
            //将方法位插入第2位,将计量标志位插入第6位
            uuid = uuid.substring(0, 1) + methodType + uuid.substring(1, 4) + realFlag + uuid.substring(4);

            //随机生成3到6位的随机字符串
            String randomString = RandomUtils.getRandomLetters(random.nextInt(3) + 3);

            String[] specialStrArr = {".css", ".js", ".jsp", ".png", ".txt", ".jpeg", ".jpg", ".gif", ".ico"};
            for (String specialStrArr1 : specialStrArr) {
                if (randomString.toLowerCase().contains(specialStrArr1.toLowerCase())) {
                    randomString = ".aspx";
                }
            }

            if (realFlag.equals("W")) {
                showUrl = dealUrlParts[0] + "//" + dealUrlParts[2] + "/" + uuid
                        + "/" + dealUrlParts[5] + "/" + dealUrlParts[6] + "/" + dealUrlParts[7] + "." + randomString;
            } else {
                showUrl = dealUrlParts[0] + "//" + dealUrlParts[2] + "/" + uuid + "." + randomString;
            }
        } catch (Exception e) {
            showUrl = dealUrl;
        }
        return showUrl;
    }

    /**
     * 将显示链接转化为逻辑处理链接
     *
     * @param showUrl
     * @return
     */
    public static String showUrlToDealUrl_2(String showUrl) {
        //showUrl：http://www.ddawd.cn/22QQY5YIBF7B.ytZhE
        //dealUrl：http://www.ddawd.cn/2QQYYIBF7B/466720acd1e742f18de7f7d19a1ec/deal/1559/mitTy.html
        // or  ads:  
        //showUrl：http://www.udrpum.cn/22QQYWYIBF7B/15386/464683/2.eUn
        //dealUrl：http://www.udrpum.cn/2QQYYIBF7B/466720acd1e742f18de7f7d19a1ec/15386/464683/2/ads/1742/umozi.html
        String dealUrl = "";
        try {
            String[] showlUrlParts = showUrl.split("[/?&]");
            //realflag:偶数 showflag：奇数 w:abs
            String strRealFlag = "";
            String uuidPartString = "";
            if (showlUrlParts[3].contains(".")) {
                uuidPartString = showlUrlParts[3].substring(0, showlUrlParts[3].indexOf("."));
            } else {
                uuidPartString = showlUrlParts[3];
            }
            String uuid = "";
            if (Character.isDigit(uuidPartString.charAt(5))) {
                int realFlag = Character.getNumericValue(uuidPartString.charAt(5));
                if (realFlag % 2 != 0) {
                    //REAL_URL_FLAG
                    strRealFlag = ConstUtils.REAL_URL_FLAG.getValue();
                } else {
                    //SHOW_URL_FLAG
                    strRealFlag = ConstUtils.SHOW_URL_FLAG.getValue();
                }
            } else if (uuidPartString.charAt(5) == 'W') {
                //REAL_URL_FLAG
                strRealFlag = ConstUtils.REAL_URL_FLAG.getValue();
            }
            uuid = uuidPartString.substring(0, 1) + uuidPartString.substring(2, 5) + uuidPartString.substring(6);

            if (uuidPartString.charAt(5) == 'W') {
                dealUrl = showlUrlParts[0] + "//" + showlUrlParts[2] + "/" + uuid + "/" + strRealFlag
                        + "/" + showlUrlParts[4] + "/" + showlUrlParts[5] + "/" + showlUrlParts[6].substring(0, showlUrlParts[6].lastIndexOf("."))
                        + "/ads/1742/umozi.html";
            } else {
                dealUrl = showlUrlParts[0] + "//" + showlUrlParts[2] + "/" + uuid + "/" + strRealFlag + "/deal/1559/mitTy.html";
            }
        } catch (Exception e) {
            dealUrl = showUrl;
        }
        return dealUrl;
    }


    /**
     * 将逻辑处理链接转化为显示链接 注：展示链接处理方案：链接字符串参数第2位表示用哪个转换方法，例如2表示dealUrlToShowUrl_2
     * 第5位，作为表示为是否计量 偶数：不计量 奇数：计量
     * 第5位如果是W，表示是ads广告链接     *
     *
     * @param dealUrl
     * @return
     */
    public static String dealUrlToShowUrl_3(String dealUrl) {
        //dealUrl：http://www.ddawd.cn/2QQYYIBF7B/466720acd1e742f18de7f7d19a1ec/deal/1559/mitTy.html
        //showUrl：http://www.ddawd.cn/43mrPhqvyqQeoScQZ/2QQY5YIBF7B/9052.php
        // or  ads:  
        //dealUrl：http://www.ddawd.cn/2QQYYIBF7B/466720acd1e742f18de7f7d19a1ec/15386/464683/2/ads/1742/umozi.html
        //showUrl：http://www.ddawd.cn/43mrPhqvyqQeoScQZ/2QQY5YIBF7B/15386/464683/2/9052.php
        String showUrl = "";
        //Url转换方法：方法3
        String methodType = "3";
        try {
            String[] dealUrlParts = dealUrl.split("/");
            String uuid = dealUrlParts[3].replace("-", "");
            //realflag:偶数 showflag：奇数        
            String realFlag = "";
            Random random = new Random();
            if (dealUrlParts[4].equalsIgnoreCase(ConstUtils.REAL_URL_FLAG.getValue())) {
                //REAL_URL_FLAG
                realFlag = ((random.nextInt(5) * 2) + 1) + "";
            } else if (dealUrlParts[4].equalsIgnoreCase(ConstUtils.SHOW_URL_FLAG.getValue())) {
                //SHOW_URL_FLAG
                realFlag = random.nextInt(5) * 2 + "";
            }
            if (dealUrl.toLowerCase().contains("/ads/")) {
                realFlag = "W";
            }
            //将计量标志位插入第5位
            uuid = uuid.substring(0, 4) + realFlag + uuid.substring(4);

            //随机生成4到15位的随机字符串
            String randomString1 = RandomUtils.getRandomLetters(random.nextInt(12) + 4);
            randomString1 = randomString1.substring(0, 1) + methodType + randomString1.substring(1);
            //随机生成8位数字
            String randomString2 = RandomUtils.getRandomNumbers(8);
            //根据域名长度，确定用哪个后缀，以保证同一域名后缀相同
            String randomSuffix = SUFFIX_ARR[dealUrlParts[2].length() % 3];
            if (realFlag.equals("W")) {
                showUrl = dealUrlParts[0] + "//" + dealUrlParts[2] + "/" + randomString1 + "/" + uuid
                        + "/" + dealUrlParts[5] + "/" + dealUrlParts[6] + "/" + dealUrlParts[7] + "/" + randomString2 + randomSuffix;
            } else {
                showUrl = dealUrlParts[0] + "//" + dealUrlParts[2] + "/" + randomString1 + "/" + uuid + "/" + randomString2 + randomSuffix;
            }
        } catch (Exception e) {
            showUrl = dealUrl;
        }
        return showUrl;
    }

    /**
     * 将显示链接转化为逻辑处理链接
     *
     * @param showUrl
     * @return
     */
    public static String showUrlToDealUrl_3(String showUrl) {
        //showUrl：http://www.ddawd.cn/43mrPhqvyqQeoScQZ/2QQY5YIBF7B/9052.php
        //dealUrl：http://www.ddawd.cn/2QQYYIBF7B/466720acd1e742f18de7f7d19a1ec/deal/1559/mitTy.html
        // or  ads:  
        //showUrl：http://www.ddawd.cn/43mrPhqvyqQeoScQZ/2QQY5YIBF7B/15386/464683/2/9052.php
        //dealUrl：http://www.ddawd.cn/2QQYYIBF7B/466720acd1e742f18de7f7d19a1ec/15386/464683/2/ads/1742/umozi.html
        String dealUrl = "";
        try {
            String[] showlUrlParts = showUrl.split("[/?&]");
            //realflag:偶数 showflag：奇数 w:abs
            String strRealFlag = "";
            String uuidPartString = showlUrlParts[4];
            String uuid = "";
            if (Character.isDigit(uuidPartString.charAt(4))) {
                int realFlag = Character.getNumericValue(uuidPartString.charAt(4));
                if (realFlag % 2 != 0) {
                    //REAL_URL_FLAG
                    strRealFlag = ConstUtils.REAL_URL_FLAG.getValue();
                } else {
                    //SHOW_URL_FLAG
                    strRealFlag = ConstUtils.SHOW_URL_FLAG.getValue();
                }
            } else if (uuidPartString.charAt(4) == 'W') {
                //REAL_URL_FLAG
                strRealFlag = ConstUtils.REAL_URL_FLAG.getValue();
            }
            uuid = uuidPartString.substring(0, 4) + uuidPartString.substring(5);
            if (uuidPartString.charAt(4) == 'W') {
                dealUrl = showlUrlParts[0] + "//" + showlUrlParts[2] + "/" + uuid + "/" + strRealFlag
                        + "/" + showlUrlParts[5] + "/" + showlUrlParts[6] + "/" + showlUrlParts[7]
                        + "/ads/1742/umozi.html";
            } else {
                dealUrl = showlUrlParts[0] + "//" + showlUrlParts[2] + "/" + uuid + "/" + strRealFlag + "/deal/1559/mitTy.html";
            }
        } catch (Exception e) {
            dealUrl = showUrl;
        }
        return dealUrl;
    }


    /**
     * 将逻辑处理链接转化为显示链接 注：展示链接处理方案：链接字符串参数第3位表示用哪个转换方法，例如2表示dealUrlToShowUrl_2
     * 第6位，作为表示为是否计量 偶数：不计量 奇数：计量
     * 第6位如果是W，表示是ads广告链接
     *
     * @param dealUrl
     * @return
     */
    public static String dealUrlToShowUrl_4(String dealUrl) {
        //dealUrl：http://www.ddawd.cn/2QQYYIBF7B/466720acd1e742f18de7f7d19a1ec/deal/1559/mitTy.html
        //showUrl：http://www.ddawd.cn/00844081QqAHeP/uFFcG9PvElIoTr/22QQY5YIBF7B.html
        // or  ads:  
        //dealUrl：http://www.ddawd.cn/2QQYYIBF7B/466720acd1e742f18de7f7d19a1ec/15386/464683/2/ads/1742/umozi.html
        //showUrl：http://www.ddawd.cn/00844081QqAHeP/uFFcG9PvElIoTr/15386/464683/2/22QQY5YIBF7B.php
        String showUrl = "";
        //Url转换方法：方法4
        String methodType = "4";
        try {
            String[] dealUrlParts = dealUrl.split("/");
            String uuid = dealUrlParts[3].replace("-", "");
            //realflag:偶数 showflag：奇数        
            String realFlag = "";
            Random random = new Random();
            if (dealUrlParts[4].equalsIgnoreCase(ConstUtils.REAL_URL_FLAG.getValue())) {
                //REAL_URL_FLAG
                realFlag = ((random.nextInt(5) * 2) + 1) + "";
            } else if (dealUrlParts[4].equalsIgnoreCase(ConstUtils.SHOW_URL_FLAG.getValue())) {
                //SHOW_URL_FLAG
                realFlag = random.nextInt(5) * 2 + "";
            }
            if (dealUrl.toLowerCase().contains("/ads/")) {
                realFlag = "W";
            }
            //将计量标志位插入第5位
            uuid = uuid.substring(0, 4) + realFlag + uuid.substring(4);

            //随机生成4到6位的随机字符串
            String randomString1 = RandomUtils.getRandomLetters(random.nextInt(5) + 4);
            randomString1 = randomString1.substring(0, 1) + methodType + randomString1.substring(1);
            //随机生成6到10位的随机字符串
            String randomString2 = RandomUtils.getRandomLetters(random.nextInt(5) + 6);

            //根据域名长度，确定用哪个后缀，以保证同一域名后缀相同
            String randomSuffix = SUFFIX_ARR[dealUrlParts[2].length() % 3];
            if (realFlag.equals("W")) {
                //showUrl：http://www.ddawd.cn/00844081QqAHeP/uFFcG9PvElIoTr/15386/464683/2/22QQY5YIBF7B.php
                showUrl = dealUrlParts[0] + "//" + dealUrlParts[2] + "/" + randomString1 + "/" + randomString2
                        + "/" + dealUrlParts[5] + "/" + dealUrlParts[6] + "/" + dealUrlParts[7] + "/" + uuid + randomSuffix;
            } else {
                showUrl = dealUrlParts[0] + "//" + dealUrlParts[2] + "/" + randomString1 + "/" + randomString2 + "/" + uuid + randomSuffix;
            }
        } catch (Exception e) {
            showUrl = dealUrl;
        }
        return showUrl;
    }

    /**
     * 将显示链接转化为逻辑处理链接
     *
     * @param showUrl
     * @return
     */
    public static String showUrlToDealUrl_4(String showUrl) {
        //showUrl：http://www.ddawd.cn/00844081QqAHeP/uFFcG9PvElIoTr/22QQY5YIBF7B.html
        //dealUrl：http://www.ddawd.cn/2QQYYIBF7B/466720acd1e742f18de7f7d19a1ec/deal/1559/mitTy.html
        // or  ads:  
        //showUrl：http://www.ddawd.cn/00844081QqAHeP/uFFcG9PvElIoTr/15386/464683/2/22QQY5YIBF7B.php
        //dealUrl：http://www.ddawd.cn/2QQYYIBF7B/466720acd1e742f18de7f7d19a1ec/15386/464683/2/ads/1742/umozi.html
        String dealUrl = "";
        try {
            String[] showlUrlParts = showUrl.split("[/?&]");
            //realflag:偶数 showflag：奇数 w:abs
            String strRealFlag = "";
            String uuid = "";
            //未剥离的uuid，带着标识位的uuid            
            String uuidPartString = "";
            //寻找.aspx的位置
            int suffixIndex = -1;
            for (int i = 0; i < SUFFIX_ARR.length && suffixIndex < 0; i++) {
                suffixIndex = showUrl.lastIndexOf(SUFFIX_ARR[i]);
            }
            uuidPartString = showUrl.substring(0, suffixIndex);
            uuidPartString = uuidPartString.substring(uuidPartString.lastIndexOf("/") + 1);
            if (Character.isDigit(uuidPartString.charAt(4))) {
                int realFlag = Character.getNumericValue(uuidPartString.charAt(4));
                if (realFlag % 2 != 0) {
                    //REAL_URL_FLAG
                    strRealFlag = ConstUtils.REAL_URL_FLAG.getValue();
                } else {
                    //SHOW_URL_FLAG
                    strRealFlag = ConstUtils.SHOW_URL_FLAG.getValue();
                }
            } else if (uuidPartString.charAt(4) == 'W') {
                //REAL_URL_FLAG
                strRealFlag = ConstUtils.REAL_URL_FLAG.getValue();
            }
            uuid = uuidPartString.substring(0, 4) + uuidPartString.substring(5);
            if (uuidPartString.charAt(4) == 'W') {
                //dealUrl：http://www.ddawd.cn/2QQYYIBF7B/466720acd1e742f18de7f7d19a1ec/15386/464683/2/ads/1742/umozi.html
                dealUrl = showlUrlParts[0] + "//" + showlUrlParts[2] + "/" + uuid + "/" + strRealFlag
                        + "/" + showlUrlParts[5] + "/" + showlUrlParts[6] + "/" + showlUrlParts[7]
                        + "/ads/1742/umozi.html";
            } else {

                //dealUrl：http://www.ddawd.cn/2QQYYIBF7B/466720acd1e742f18de7f7d19a1ec/deal/1559/mitTy.html
                dealUrl = showlUrlParts[0] + "//" + showlUrlParts[2] + "/" + uuid + "/" + strRealFlag + "/deal/1559/mitTy.html";
            }
        } catch (Exception e) {
            dealUrl = showUrl;
        }
        return dealUrl;
    }

    /**
     * 将逻辑处理链接转化为显示链接 注：展示链接处理方案：链接字符串参数第3位表示用哪个转换方法，例如2表示dealUrlToShowUrl_2
     * 第6位，作为表示为是否计量 偶数：不计量 奇数：计量
     * 第6位如果是W，表示是ads广告链接
     *
     * @param dealUrl
     * @return
     */
    public static String dealUrlToShowUrl_5(String dealUrl) {
        //dealUrl：http://www.ddawd.cn/2QQYYIBF7B/466720acd1e742f18de7f7d19a1ec/deal/1559/mitTy.html
        //showUrl：http://www.ddawd.cn/MtqEbqGEvoWnb8ezrgQ2/2QQYYIBF7B.php?ors=5797041&Dfx=826
        // or  ads:  
        //dealUrl：http://www.ddawd.cn/2QQYYIBF7B/466720acd1e742f18de7f7d19a1ec/15386/464683/2/ads/1742/umozi.html
        //showUrl：http://www.ddawd.cn/MtqEbqGEvoWnb8ezrgQ2/15386/464683/2/22QQY5YIBF7B.php?ors=5797041&Dfx=826
        String showUrl = "";
        //Url转换方法：方法5
        String methodType = "5";
        try {
            String[] dealUrlParts = dealUrl.split("/");
            String uuid = dealUrlParts[3].replace("-", "");
            //realflag:偶数 showflag：奇数        
            String realFlag = "";
            Random random = new Random();
            if (dealUrlParts[4].equalsIgnoreCase(ConstUtils.REAL_URL_FLAG.getValue())) {
                //REAL_URL_FLAG
                realFlag = ((random.nextInt(5) * 2) + 1) + "";
            } else if (dealUrlParts[4].equalsIgnoreCase(ConstUtils.SHOW_URL_FLAG.getValue())) {
                //SHOW_URL_FLAG
                realFlag = random.nextInt(5) * 2 + "";
            }
            if (dealUrl.toLowerCase().contains("/ads/")) {
                realFlag = "W";
            }
            //将计量标志位插入第5位
            uuid = uuid.substring(0, 4) + realFlag + uuid.substring(4);

            //随机生成4到10位的随机字符串
            String randomString1 = RandomUtils.getRandomLetters(random.nextInt(7) + 4);
            randomString1 = randomString1.substring(0, 1) + methodType + randomString1.substring(1);
            //随机出一个?rtd=1225类似的字符串
            String randomString2 = "?" + RandomUtils.getRandomLowerCaseLetters(3) + "=" + RandomUtils.getRandomNumbers(random.nextInt(2) + 4);
            //根据域名长度，确定用哪个后缀，以保证同一域名后缀相同
            String randomSuffix = SUFFIX_ARR[dealUrlParts[2].length() % 3];
            if (realFlag.equals("W")) {
                //showUrl：http://www.ddawd.cn/MtqEbqGEvoWnb8ezrgQ2/15386/464683/2/22QQY5YIBF7B.php?ors=5797041&Dfx=826
                showUrl = dealUrlParts[0] + "//" + dealUrlParts[2] + "/" + randomString1
                        + "/" + dealUrlParts[5] + "/" + dealUrlParts[6] + "/" + dealUrlParts[7] + "/" + uuid + randomSuffix + randomString2;
            } else {
                //showUrl：http://www.ddawd.cn/MtqEbqGEvoWnb8ezrgQ2/2QQYYIBF7B.php?ors=5797041&Dfx=826
                showUrl = dealUrlParts[0] + "//" + dealUrlParts[2] + "/" + randomString1 + "/" + uuid + randomSuffix + randomString2;
            }
        } catch (Exception e) {
            showUrl = dealUrl;
        }
        return showUrl;
    }

    /**
     * 将显示链接转化为逻辑处理链接
     *
     * @param showUrl
     * @return
     */
    public static String showUrlToDealUrl_5(String showUrl) {
        //showUrl：http://www.ddawd.cn/MtqEbqGEvoWnb8ezrgQ2/2QQYYIBF7B.php?ors=5797041
        //dealUrl：http://www.ddawd.cn/2QQYYIBF7B/466720acd1e742f18de7f7d19a1ec/deal/1559/mitTy.html
        // or  ads:  
        //showUrl：http://www.ddawd.cn/MtqEbqGEvoWnb8ezrgQ2/15386/464683/2/22QQY5YIBF7B.php?ors=5797041
        //dealUrl：http://www.ddawd.cn/2QQYYIBF7B/466720acd1e742f18de7f7d19a1ec/15386/464683/2/ads/1742/umozi.html
        String dealUrl = "";
        try {
            String[] showlUrlParts = showUrl.split("[/?&]");
            //realflag:偶数 showflag：奇数 w:abs
            String strRealFlag = "";
            String uuid = "";
            //未剥离的uuid，带着标识位的uuid 
            String uuidPartString = "";
            //寻找.aspx的位置
            int suffixIndex = -1;
            for (int i = 0; i < SUFFIX_ARR.length && suffixIndex < 0; i++) {
                suffixIndex = showUrl.lastIndexOf(SUFFIX_ARR[i]);
            }
            uuidPartString = showUrl.substring(0, suffixIndex);
            uuidPartString = uuidPartString.substring(uuidPartString.lastIndexOf("/") + 1);
            if (Character.isDigit(uuidPartString.charAt(4))) {
                int realFlag = Character.getNumericValue(uuidPartString.charAt(4));
                if (realFlag % 2 != 0) {
                    //REAL_URL_FLAG
                    strRealFlag = ConstUtils.REAL_URL_FLAG.getValue();
                } else {
                    //SHOW_URL_FLAG
                    strRealFlag = ConstUtils.SHOW_URL_FLAG.getValue();
                }
            } else if (uuidPartString.charAt(4) == 'W') {
                //REAL_URL_FLAG
                strRealFlag = ConstUtils.REAL_URL_FLAG.getValue();
            }
            uuid = uuidPartString.substring(0, 4) + uuidPartString.substring(5);
            if (uuidPartString.charAt(4) == 'W') {
                //dealUrl：http://www.ddawd.cn/2QQYYIBF7B/466720acd1e742f18de7f7d19a1ec/15386/464683/2/ads/1742/umozi.html
                dealUrl = showlUrlParts[0] + "//" + showlUrlParts[2] + "/" + uuid + "/" + strRealFlag
                        + "/" + showlUrlParts[4] + "/" + showlUrlParts[5] + "/" + showlUrlParts[6]
                        + "/ads/1742/umozi.html";
            } else {
                //dealUrl：http://www.ddawd.cn/2QQYYIBF7B/466720acd1e742f18de7f7d19a1ec/deal/1559/mitTy.html
                dealUrl = showlUrlParts[0] + "//" + showlUrlParts[2] + "/" + uuid + "/" + strRealFlag + "/deal/1559/mitTy.html";
            }
        } catch (Exception e) {
            dealUrl = showUrl;
        }
        return dealUrl;
    }

    /**
     * 字符串值 &adb=dddd=dddds=-&dd=fdas
     *
     * @param url
     * @return
     */
    public static String getUrlParamValue(String url, String key) {
        String paramString = url.replace("?", "");
        String[] paramArr = paramString.split("&");
        for (String item : paramArr) {
            if (item.contains(key + "=")) {
                return item.replace(key + "=", "");
            }
        }
        return "";
    }



    public static void main(String[] args) {
        //method 1
//        if(logger.isDebugEnabled()){ logger.debug(dealUrlToShowUrl("http://www.ddawd.cn/3df4d12b-6a34-43c9-8537-6835f3b24ab2/466720acd1e742f18de7f7d19a1ec/deal/1559/mitTy.html")); }
//        if(logger.isDebugEnabled()){ logger.debug(showUrlToDealUrl("http://www.ddawd.cn/3df41d12b6a3443c985376835f3b24ab2QJH")); }
//        if(logger.isDebugEnabled()){ logger.debug(dealUrlToShowUrl("http://www.udrpum.cn/7b0369c4-06f1-41df-90b5-01616b5c3ccc/466720acd1e742f18de7f7d19a1ec/15386/464683/2/ads/1742/umozi.html")); }
//        if(logger.isDebugEnabled()){ logger.debug(showUrlToDealUrl("http://www.udrpum.cn/7b03w69c406f141df90b501616b5c3cccfxEkw/15386/464683/2")); }

        if(logger.isDebugEnabled()){ logger.debug("===" + showUrlToDealUrl("http://wwx.obotounmg.cn/g5AJxY/JofH5t2daj.aspx?ooh=8551")); }
        //method 2

        if(logger.isDebugEnabled()){ logger.debug("----------------method 2--------------------------------"); }
        if(logger.isDebugEnabled()){ logger.debug(dealUrlToShowUrl_2("http://wwx.jjvuj.cn/u1zhX6Q/466720acd1e742f18de7f7d19a1ec/deal/1559/mitTy.html")); }
//        if(logger.isDebugEnabled()){ logger.debug(showUrlToDealUrl_2("http://www.ddawd.cn/22QQY5YIBF7B.ate")); }
//        if(logger.isDebugEnabled()){ logger.debug(dealUrlToShowUrl_2("http://www.udrpum.cn/2QQYYIBF7B/466720acd1e742f18de7f7d19a1ec/15386/464683/2/ads/1742/umozi.html")); }
//        if(logger.isDebugEnabled()){ logger.debug(showUrlToDealUrl_2("http://www.udrpum.cn/22QQYWYIBF7B/15386/464683/2.aWOKa")); }

        //method 3
        if(logger.isDebugEnabled()){ logger.debug("----------------method 3--------------------------------"); }
//        if(logger.isDebugEnabled()){ logger.debug(dealUrlToShowUrl_3("http://www.ddawd.cn/2QQYYIBF7B/466720acd1e742f18de7f7d19a1ec/deal/1559/mitTy.html")); }
//        if(logger.isDebugEnabled()){ logger.debug(showUrlToDealUrl_3("http://www.ddawd.cn/j3FnqkNcMtfczt/2QQY9YIBF7B/70863224.php")); }
//        if(logger.isDebugEnabled()){ logger.debug(dealUrlToShowUrl_3("http://www.udrpum.cn/2QQYYIBF7B/466720acd1e742f18de7f7d19a1ec/15386/464683/2/ads/1742/umozi.html")); }
//        if(logger.isDebugEnabled()){ logger.debug(showUrlToDealUrl_3("http://www.udrpum.cn/i3VSHHVEvuBkOfkF/2QQYWYIBF7B/15386/464683/2/19222603.aspx")); }

        //method 4
        if(logger.isDebugEnabled()){ logger.debug("----------------method 4--------------------------------"); }
//        if(logger.isDebugEnabled()){ logger.debug(dealUrlToShowUrl_4("http://www.ddawd.cn/2QQYYIBF7B/466720acd1e742f18de7f7d19a1ec/deal/1559/mitTy.html")); }
//        if(logger.isDebugEnabled()){ logger.debug(showUrlToDealUrl_4("http://www.ddawd.cn/voQYX/JddedUp/2QQY5YIBF7B.php")); }
//        if(logger.isDebugEnabled()){ logger.debug(dealUrlToShowUrl_4("http://www.udrpum.cn/2QQYYIBF7B/466720acd1e742f18de7f7d19a1ec/15386/464683/2/ads/1742/umozi.html")); }
//        if(logger.isDebugEnabled()){ logger.debug(showUrlToDealUrl_4("http://www.udrpum.cn/aEOJvgoG/VMjphLLA/15386/464683/2/2QQYWYIBF7B.aspx")); }

        //method 5
        if(logger.isDebugEnabled()){ logger.debug("----------------method 5--------------------------------"); }
//        if(logger.isDebugEnabled()){ logger.debug(dealUrlToShowUrl_5("http://www.ddawd.cn/2QQYYIBF7B/466720acd1e742f18de7f7d19a1ec/deal/1559/mitTy.html")); }
        if(logger.isDebugEnabled()){ logger.debug(showUrlToDealUrl_5("http://www.changaimou.cn/a5kNaRG/DsiE1uC6Je.jsp?dvs=29205")); }
//        if(logger.isDebugEnabled()){ logger.debug(dealUrlToShowUrl_5("http://www.udrpum.cn/2QQYYIBF7B/466720acd1e742f18de7f7d19a1ec/15386/464683/2/ads/1742/umozi.html")); }
//        if(logger.isDebugEnabled()){ logger.debug(showUrlToDealUrl_5("http://www.udrpum.cn/CjLp/15386/464683/2/2QQYWYIBF7B.aspx?tvw=1946")); }

        //method
        if(logger.isDebugEnabled()){ logger.debug("----------------method random dealUrlToShowUrl--------------------------------"); }
//        if(logger.isDebugEnabled()){ logger.debug(dealUrlToShowUrl("http://www.ddawd.cn/2QQYYIBF7B/466720acd1e742f18de7f7d19a1ec/deal/1559/mitTy.html")); }
//        if(logger.isDebugEnabled()){ logger.debug(dealUrlToShowUrl("http://www.udrpum.cn/2QQYYIBF7B/466720acd1e742f18de7f7d19a1ec/15386/464683/2/ads/1742/umozi.html")); }

//           if(logger.isDebugEnabled()){ logger.debug("----------------method random  showUrlToDealUrl--------------------------------"); }
//            if(logger.isDebugEnabled()){ logger.debug(showUrlToDealUrl("http://www.ddawd.cn/22QQY9YIBF7B.vEu")); }
//if(logger.isDebugEnabled()){ logger.debug(showUrlToDealUrl("http://www.ddawd.cn/J3yGGvMQIO/2QQY7YIBF7B/64053580.php")); }
//if(logger.isDebugEnabled()){ logger.debug(showUrlToDealUrl("http://www.ddawd.cn/q4YsMRCcY/LLrEWgTC/2QQY9YIBF7B.php")); }
//if(logger.isDebugEnabled()){ logger.debug(showUrlToDealUrl("http://www.ddawd.cn/L5FpYkzUg/2QQY3YIBF7B.php?rcq=21083")); }
//         if(logger.isDebugEnabled()){ logger.debug("----------------method random  showUrlToDealUrl  ads--------------------------------"); }
//if(logger.isDebugEnabled()){ logger.debug(showUrlToDealUrl("http://www.udrpum.cn/22QQYWYIBF7B/15386/464683/2.rok")); }
//if(logger.isDebugEnabled()){ logger.debug(showUrlToDealUrl("http://www.udrpum.cn/b3oKUCPvxPrwFtsf/2QQYWYIBF7B/15386/464683/2/71308979.aspx")); }
//if(logger.isDebugEnabled()){ logger.debug(showUrlToDealUrl("http://www.udrpum.cn/I4ASL/GdfPQb/15386/464683/2/2QQYWYIBF7B.aspx")); }
//if(logger.isDebugEnabled()){ logger.debug(showUrlToDealUrl("http://www.udrpum.cn/s5mXr/15386/464683/2/2QQYWYIBF7B.aspx?sun=93541")); }

    }


}
