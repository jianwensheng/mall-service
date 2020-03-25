package com.oruit.common.utils;

import java.math.BigDecimal;
import java.util.TreeMap;

public class MethodUtil {

    public static final String appSecret = "b6adf9856994f2a30df85c3c76fb89ab";//应用sercret
    
    public static final String appKey = "5d2c1519c5fe9"; //应用key

    public static final String good_classfiy_key = "good_classfiy_list";

    public static final String good_classfiy_info_key = "good_classfiy_info_";

    public static final String good_info_key = "good_info_";

    public static final String banner_List_key = "goods_banner_List";

    public static final String goods_privilege_key = "goods_privilege_";

    public static final String goods_hot_care = "goods_hot_care";

    public static final String good_detail_key = "good_detail_";

    public static final String good_ap_key = "good_ap_list_";

    public static final String good_ddq_key = "good_ddq_list";

    public static final String theme_key = "theme_list";

    public static final String theme_goods_key = "theme_goods_list_theme_Id_";

    public static final String good_theme_key = "good_theme";

    public static final String cms_prom_url_key = "cms_prom_url_";

    public static final String good_similer_ap_key = "good_similer_ap_list_";

    public static final String pdd_cms_good_url = "pdd_cms_good_url_";

    public static final String pdd_good_detail_key = "pdd_good_detail_";

    public static final String brand_good_info_key = "brand_good_info_";

    public static final String brand_good_list_key = "brand_good_list_";

    public static final String pdd_cms_url_key = "pdd_cms_url_";

    public static final String pdd_top_goods_list_query_key = "pdd_top_goods_list_query_";

    public static final String pdd_goods_list_key = "pdd_goods_list_query_";

    public static final String gpdd_ood_info_key = "pdd_good_info_";

    public static final Integer pageSize = 10;

    public static final Integer pageNo = 1;

    //商品一级和二级分类接口
    public static final String category_url = "https://openapi.dataoke.com/api/category/get-super-category";

    //商品列表
    public static final String goods_url = "https://openapi.dataoke.com/api/goods/get-goods-list";

    //主页商品轮播
    public static final String banner_url = "https://openapi.dataoke.com/api/goods/topic/catalogue";

    //高效转链
    public static final String privilege_url = "https://openapi.dataoke.com/api/tb-service/get-privilege-link";

    //热搜记录
    public static final String get_top_url = "https://openapi.dataoke.com/api/category/get-top100";

    //超级搜索
    public static final String supper_url = "https://openapi.dataoke.com/api/goods/list-super-goods";

    //商品详情
    public static final String good_detail_url = "https://openapi.dataoke.com/api/goods/get-goods-details";

    //9块9包邮
    public static final String ap_good_url = "https://openapi.dataoke.com/api/goods/nine/op-goods-list";

    //咚咚抢
    public static final String ddq_good_url = "https://openapi.dataoke.com/api/category/ddq-goods-list";

    //联盟搜索
    public static final String tb_good_url = "https://openapi.dataoke.com/api/tb-service/get-tb-service";

    //猜你喜欢
    public static final String ap_similer_good_url = "https://openapi.dataoke.com/api/goods/list-similer-goods-by-open";

    //品牌库
    public static final String brand_url = "https://openapi.dataoke.com/api/tb-service/get-brand-list";

    public static int expires = 60*60*24;

    public static int hour_expires = 60*60;

    public static int week_expires = 60*60*24*7;

    public static TreeMap<String,String> postContent(TreeMap<String,String> paraMap){
        paraMap.put("appKey",appKey);
        paraMap.put("sign", SignMD5Util.getSignStr(paraMap,appSecret));
        return paraMap;
    }

    public static  Double getCommission(String aPrice,String cRate){
        BigDecimal actualPrice = new BigDecimal(aPrice);
        BigDecimal commissionRate = new BigDecimal(cRate);
        BigDecimal commission =  actualPrice.multiply(commissionRate).divide(new BigDecimal(100));
        Double num = commission.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return num;
    }

    public static  Double getPddCommission(String aPrice,String cRate){
        BigDecimal actualPrice = new BigDecimal(aPrice);
        BigDecimal commissionRate = new BigDecimal(cRate);
        BigDecimal commission =  actualPrice.multiply(commissionRate).divide(new BigDecimal(100));
        Double num = commission.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return num;
    }

    public static String calculateProfit(double doubleValue) {
        // 保留4位小数
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.0000");
        String result = df.format(doubleValue);

        // 截取第一位
        String index = result.substring(0, 1);

        if (".".equals(index)) {
            result = "0" + result;
        }

        // 获取小数 . 号第一次出现的位置
        int inde = firstIndexOf(result, ".");

        // 字符串截断
        return result.substring(0, inde + 3);
    }

    /**
     * 查找字符串pattern在str中第一次出现的位置
     *
     * @param str
     * @param pattern
     * @return
     */
    public static int firstIndexOf(String str, String pattern) {
        for (int i = 0; i < (str.length() - pattern.length()); i++) {
            int j = 0;
            while (j < pattern.length()) {
                if (str.charAt(i + j) != pattern.charAt(j))
                    break;
                j++;
            }
            if (j == pattern.length())
                return i;
        }
        return -1;
    }




    public static void main(String[] args) {

    }

}
