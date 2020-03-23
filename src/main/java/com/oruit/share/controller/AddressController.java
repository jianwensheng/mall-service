package com.oruit.share.controller;

import com.alibaba.fastjson.JSONObject;
import com.oruit.common.utils.HttpUtils;
import com.oruit.common.utils.JsonDealUtil;
import com.oruit.share.domain.TbAddress;
import com.oruit.share.service.AddressService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("address")
@Slf4j
public class AddressController {

    @Autowired
    private AddressService addressService;

    @RequestMapping("/index")
    public String index(HttpServletRequest request, Model model) {
        String userId = null;
        try{
             userId = HttpUtils.getRequestParam(request, "userId");//商品ID
        }catch (Exception ex){
            log.error("AddressController index error:{}",ex.getMessage());
        }
        List<Address> addressList = addressService.addressList(Long.parseLong(userId));
        model.addAttribute("addressList",addressList);
        return "address";
    }

    @RequestMapping("/edit")
    public String edit(HttpServletRequest request, Model model) {
        try{
            String id = HttpUtils.getRequestParam(request, "id");//id
            TbAddress tbAddress = addressService.queryAddressById(Integer.parseInt(id));
            model.addAttribute("tbAddress",tbAddress);
        }catch (Exception ex){
            log.error("AddressController edit error:{}",ex.getMessage());
        }
        return "edit-address";
    }

    @RequestMapping("/add")
    public String add(HttpServletRequest request, Model model) {
        return "add-address";
    }

    /**
     * 新增收货地址
     * @param request
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping("/addAddress")
    public JSONObject addAddress(HttpServletRequest request, Model model) {
        int count = 0;
        try{
            String receiver = HttpUtils.getRequestParam(request, "receiver");//收货人
            String mobile = HttpUtils.getRequestParam(request, "mobile");//手机号
            String region = HttpUtils.getRequestParam(request, "region");//地区
            String detailAddress = HttpUtils.getRequestParam(request, "detailAddress");//详细地址
            String addDefault = HttpUtils.getRequestParam(request, "addDefault");//默认地址
            TbAddress tbAddress = new TbAddress();
            tbAddress.setReceiver(receiver);
            tbAddress.setMobile(mobile);
            tbAddress.setRegion(region);
            tbAddress.setDetailAddress(detailAddress);
            tbAddress.setAddDefault(Integer.parseInt(addDefault));
            tbAddress.setCreateTime(new Date());
            tbAddress.setUpdateTime(new Date());
            tbAddress.setUserId(0l);
            count = addressService.insertSelective(tbAddress);
        }catch (Exception ex){
            log.error("AddressController addAddress error:{}",ex.getMessage());
        }

        if(count>0){
            return JsonDealUtil.getSuccJSONObject("0|操作成功", "", null);
        }else{
            return JsonDealUtil.getErrJSONObject("操作失败");
        }
    }

}
