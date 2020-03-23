package com.oruit.share.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.oruit.common.utils.HttpUtils;
import com.oruit.common.utils.JsonDealUtil;
import com.oruit.common.utils.MethodUtil;
import com.oruit.common.utils.PddUtil;
import com.oruit.common.utils.cache.redis.RedisUtil;
import com.oruit.share.dao.TbBrandMapper;
import com.oruit.share.dao.TbPddCatMapper;
import com.oruit.share.dao.TbPddOptMapper;
import com.oruit.share.domain.TbBannerDO;
import com.oruit.share.domain.TbBrand;
import com.oruit.share.domain.TbPddCat;
import com.oruit.share.domain.TbPddOpt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.TreeMap;

@Slf4j
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class GoodTask {

     @Autowired
     private TbBrandMapper tbBrandMapper;

     @Autowired
     private TbPddCatMapper tbPddCatMapper;

     @Autowired
     private TbPddOptMapper tbPddOptMapper;


     Integer pageId = 1;
    /**
     * 品牌列表刷入
     * 每天 16：15分执行
     */
    //@Scheduled(cron = "0 */1 * * * ?")
    private void brandOperation() {
        String key = "brand_operation_pageId";
        Object obj = RedisUtil.get(key);
        if(obj!=null){
            pageId = Integer.parseInt(obj.toString());
        }
        TreeMap<String,String> paraMap = new TreeMap<>();
        paraMap.put("version","v1.1.1");
        paraMap.put("pageId",pageId+"");
        paraMap.put("pageSize","100");
        JSONObject jsonObject = JSONObject.parseObject(HttpUtils.sendGet(MethodUtil.brand_url,MethodUtil.postContent(paraMap)),JSONObject.class);
        if(jsonObject.get("code").equals(0)){
            String list = ((JSONArray)jsonObject.get("data")).toString();
            JSONArray array =  JSONObject.parseObject(list,JSONArray.class);
            array.forEach(arr->{
                TbBrand tbBrand =  JSONObject.parseObject(arr.toString(),TbBrand.class);
                tbBrandMapper.insertSelective(tbBrand);
            });
            pageId++;
            RedisUtil.setByTime(key,String.valueOf(pageId),MethodUtil.week_expires);
        }

    }

    //@Scheduled(cron = "0 00 16 ? * *")
    private void pddCatInsert(){
        try{
            String json = PddUtil.PddGoodsOptGet();
            JSONObject jsonObject = JSONObject.parseObject(json,JSONObject.class);
            JSONArray jsonArray = JSONObject.parseObject(((JSONObject)jsonObject.get("goods_opt_get_response")).get("goods_opt_list").toString(),JSONArray.class);
            jsonArray.forEach(arr->{
                JSONObject obj = (JSONObject)arr;
                String optName = obj.get("opt_name").toString();
                Long optId = Long.valueOf(obj.get("opt_id").toString());
                String parent_opt_id  = obj.get("parent_opt_id").toString();
                TbPddOpt tbPddOpt = new TbPddOpt();
                tbPddOpt.setOptId(optId);
                tbPddOpt.setOptName(optName);
                tbPddOpt.setParentCatId(parent_opt_id);
                tbPddOptMapper.insertSelective(tbPddOpt);
            });
        }catch (Exception ex){
            log.error(ex.getMessage());
        }

    }




}
