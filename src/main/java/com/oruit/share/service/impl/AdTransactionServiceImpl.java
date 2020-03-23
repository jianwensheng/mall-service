package com.oruit.share.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.oruit.common.utils.Base64Util;
import com.oruit.common.utils.StringUtils;
import com.oruit.share.dao.ArticleAdTransactionRecordMapper;
import com.oruit.share.domain.ArticleAdTransactionRecord;
import com.oruit.share.service.AdTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;

import static com.oruit.common.utils.web.RequestUtils.getRequestParam;

/**
 * @ClassName AdTransactionServiceImpl
 * @Description TODO
 * @Author xc
 * @Date 2019/9/28 10:13
 * @Version 1.0
 **/

@Service
public class AdTransactionServiceImpl implements AdTransactionService {

    @Autowired
    ArticleAdTransactionRecordMapper adTransactionRecordMapper;

    @Override
    public void adDeall(HttpServletRequest request)throws  Exception {
        String aid = getRequestParam(request, "uaid");
        String ggc = getRequestParam(request, "ggc");
        String adType = getRequestParam(request, "adtype");
        String mediaarticleid = getRequestParam(request, "mid");
        String aname = getRequestParam(request, "aname");
        String payamount = getRequestParam(request, "payout");
        if(!StringUtils.isEmpty(aid)&&!StringUtils.isEmpty(ggc)&&!StringUtils.isEmpty(mediaarticleid)){
            ArticleAdTransactionRecord articleAdTransactionRecord = new ArticleAdTransactionRecord();
            articleAdTransactionRecord.setArticleId(Integer.parseInt(aid));
            articleAdTransactionRecord.setGgc(ggc);
            articleAdTransactionRecord.setAdType(Integer.parseInt(adType));
            articleAdTransactionRecord.setMediaArticleId(Integer.parseInt(mediaarticleid));
            articleAdTransactionRecord.setAdvertiser(aname);
            if(payamount!=null && !"".equals(payamount)){
                BigDecimal amount = new BigDecimal(payamount);
                articleAdTransactionRecord.setPayAmount(amount);
            }else{
                articleAdTransactionRecord.setPayAmount(BigDecimal.ZERO);
            }
            adTransactionRecordMapper.insertSelective(articleAdTransactionRecord);
        }
    }
}
