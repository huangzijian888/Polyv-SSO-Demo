package com.lamdaer.sso.util;

import cn.hutool.crypto.SecureUtil;

import java.util.Arrays;
import java.util.Map;

/**
 * @author lamdaer
 * createTime 2020/6/20
 */
public class SignUtil {
    public static String sign(Map<String, Object> params, String appSecret) {
        //对参数名进行字典排序
        String[] keyArray = params.keySet().toArray(new String[0]);
        Arrays.sort(keyArray);

        //拼接有序的参数串
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(appSecret);
        for (String key : keyArray) {
            stringBuilder.append(key).append(params.get(key));
        }
        stringBuilder.append(appSecret);
        String signSource = stringBuilder.toString();

        //生成的sign
        return SecureUtil.md5(signSource).toUpperCase();
    }
}
