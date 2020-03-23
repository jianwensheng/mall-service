package com.oruit.share.controller;

import com.alibaba.fastjson.JSONObject;
import com.oruit.common.utils.HttpUtils;
import com.oruit.common.utils.JsonDealUtil;
import com.oruit.share.domain.TbCollection;
import com.oruit.share.service.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@RequestMapping("collection")
public class CollectionController {

    @Autowired
    private CollectionService collectionService;

    @RequestMapping("/index")
    public String index(HttpServletRequest request, Model model) throws Exception{
       String userId = HttpUtils.getRequestParam(request, "userId");//显示页数
       String pageIndex = HttpUtils.getRequestParam(request, "pageIndex");//显示页数
       String pageSize = HttpUtils.getRequestParam(request, "pageSize");//显示条数
       HashMap map = new HashMap();
       map.put("userId",userId);
       map.put("pageIndex",pageIndex);
       map.put("pageSize",pageSize);
       List<TbCollection> tbCollectionList = collectionService.queryTbCollectionList(map);
       model.addAttribute("tbCollectionList",tbCollectionList);
       return "collection";
    }

    @RequestMapping("/collectionList")
    @ResponseBody
    public JSONObject collectionList(HttpServletRequest request, Model model) throws Exception{
        String userId = HttpUtils.getRequestParam(request, "userId");//显示页数
        String pageIndex = HttpUtils.getRequestParam(request, "pageIndex");//显示页数
        String pageSize = HttpUtils.getRequestParam(request, "pageSize");//显示条数
        HashMap map = new HashMap();
        map.put("userId",userId);
        map.put("pageIndex",pageIndex);
        map.put("pageSize",pageSize);
        List<TbCollection> tbCollectionList = collectionService.queryTbCollectionList(map);
        return JsonDealUtil.getSuccJSONObject("0|操作成功", String.valueOf(tbCollectionList.size()), tbCollectionList);

    }




}
