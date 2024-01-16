package com.jie1234.jieapiinterface.controller;

import com.jie1234.jieapiclientsdk.model.User;
import com.jie1234.jieapiinterface.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 名称API
 *
 * @author Jie
 */
@RestController
@RequestMapping("/name")
@Slf4j
public class NameController {

    @GetMapping("/")
    public R getNameByGet(String name, HttpServletRequest request) {
        log.info("请求参数：{}", request.getHeader("name"));
        return new R("GET 你的名字是 " + name);
    }

    @PostMapping("/post")
    public R getNameByPost(@RequestParam String name) {
        return new R("POST 你的名字是 " + name);
    }

    @PostMapping("/user")
    public R getNameByPost(@RequestBody User user) {
        // String accessKey = request.getHeader("accessKey");
        // String secretKey = request.getHeader("secretKey");
        // String nonce = request.getHeader("nonce");
        // String timestamp = request.getHeader("timestamp");
        // String sign = request.getHeader("sign");
        // String body = request.getHeader("body");
        // if (!accessKey.equals("jie")){
        //     throw new RuntimeException("无权限");
        // }
        // if (Long.parseLong(nonce) > 10000){
        //     throw new RuntimeException("无权限");
        // }
        // // 校验timestamp不超过当前时间半小时
        // if (System.currentTimeMillis() / 1000 - Long.parseLong(timestamp) > 30 * 60 * 1000){
        //     throw new RuntimeException("无权限");
        // }
        // String sign1 = SignUtils.getSign(body, "abcdefgh");
        // if (!sign.equals(sign1)){
        //     throw new RuntimeException("无权限");
        // }

        String result =  "Post 你的名字是 " + user.getUsername();

        // 调用成功后
        R r = new R(result);
        return r;
    }
}
