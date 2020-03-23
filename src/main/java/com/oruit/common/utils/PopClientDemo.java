package com.oruit.common.utils;

import com.pdd.pop.sdk.common.util.JsonUtil;
import com.pdd.pop.sdk.http.PopClient;
import com.pdd.pop.sdk.http.PopHttpClient;
import com.pdd.pop.sdk.http.api.request.*;
import com.pdd.pop.sdk.http.api.response.*;



public class PopClientDemo {

    public static void main(String[] args) throws Exception {
        String clientId = "90d396de5d6f4d5eb8927a9677d83e6a";
        String clientSecret = "c3a374fcadaddbc9568bf76d1911de71aae7235c";
        PopClient client = new PopHttpClient(clientId, clientSecret);

        PddGoodsOptGetRequest request = new PddGoodsOptGetRequest();
        request.setParentOptId(-1);
        PddGoodsOptGetResponse response = client.syncInvoke(request);
        System.out.println(JsonUtil.transferToJson(response));
    }
}
