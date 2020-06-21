package com.lamdaer.sso.controller;

import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.lamdaer.sso.util.SignUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lamdaer
 * createTime 2020/6/20
 */
@Controller
public class SsoController {

    /**
     * 网页开播
     *
     * @param appId
     * @param appSecret
     * @param channelId
     * @param response
     * @throws IOException
     */
    @GetMapping("/classRoom")
    public void classRoom(@RequestParam("appId") String appId,
                          @RequestParam("appSecret") String appSecret,
                          @RequestParam("channelId") Integer channelId,
                          HttpServletResponse response) throws IOException {

        Map<String, Object> paramMap = getParamMap(appId, appSecret);
        String tokenUrl = "https://api.polyv.net/live/v2/channels/" + channelId + "/set-token?";
        String message = HttpUtil.post(tokenUrl, paramMap);
        JSONObject obj = JSONUtil.parseObj(message);
        Object object = obj.get("code");
        if ((Integer) object == HttpStatus.HTTP_OK) {
            String callback = "https://live.polyv.net/teacher/auth-login?channelId=" + channelId + "&token=" + paramMap.get("token") + "&redirect=https://live.polyv.net/web-start/classroom?channelId=" + channelId;
            response.sendRedirect(callback);
        } else {
            response.getWriter().println(obj);

        }
    }

    /**
     * 直播后台
     *
     * @param appId
     * @param appSecret
     * @param userId
     * @param response
     * @throws IOException
     */
    @GetMapping("/liveBackGround")
    public void channel(@RequestParam("appId") String appId,
                        @RequestParam("appSecret") String appSecret,
                        @RequestParam("userId") String userId,
                        HttpServletResponse response) throws IOException {
        Map<String, Object> paramMap = getParamMap(appId, appSecret);
        String tokenUrl = "https://api.polyv.net/live/v3/user/set-sso-token";
        String message = HttpUtil.post(tokenUrl, paramMap);
        JSONObject obj = JSONUtil.parseObj(message);
        Object object = obj.get("code");
        if ((Integer) object == HttpStatus.HTTP_OK) {
            String callback = "https://live.polyv.net/v2/sso/userLogin.do?userId=" + userId + "&token=" + paramMap.get("token") + "&redirect=http://live.polyv.net/#/channel/";
            response.sendRedirect(callback);
        } else {
            response.getWriter().println(obj);

        }
    }

    private Map<String, Object> getParamMap(@RequestParam("appId") String appId, @RequestParam("appSecret") String appSecret) {
        String timestamp = System.currentTimeMillis() + "";
        Map<String, Object> paramMap = new HashMap<>(4);
        paramMap.put("appId", appId);
        paramMap.put("timestamp", timestamp);
        paramMap.put("token", IdUtil.simpleUUID());
        String sign = SignUtil.sign(paramMap, appSecret);
        paramMap.put("sign", sign);
        return paramMap;
    }

}
