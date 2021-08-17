package com.sibat.wisebrain.k8s.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.util.HashMap;
import java.util.Map;

public class CommonController {
    public static String getType(Object a) {
        return a.getClass().toString();

    }


    public static String response(String code,String msg,String data) {
        Map<String, String> res = new HashMap<>();
        res.put("status",code);
        res.put("message",msg);
        res.put("data",data);
        JSONObject resObject = JSONUtil.parseObj(res);
        String response =  JSONUtil.toJsonPrettyStr(resObject);
        return response;


    }

    public static boolean isNull(Object obj) {
        return obj == null;
    }



}
