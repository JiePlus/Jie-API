package com.jie1234.jieapiclientsdk.client;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.jie1234.jieapiclientsdk.model.QuestionRequest;
import com.jie1234.jieapiclientsdk.model.User;
import com.jie1234.jieapiclientsdk.utils.SignUtils;
import lombok.AllArgsConstructor;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 调用第三方接口客户端
 *
 * @author Jie
 */
@AllArgsConstructor
public class JieApiClient {

    private static final String GATEWAY_HOST = "http://localhost:8091";

    private String accessKey;

    private String secretKey;



    public String getNameByGet(String name) {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);

        String result= HttpUtil.get(GATEWAY_HOST + "/api/name/get", paramMap);
        System.out.println(result);

        return result;
    }

    public String getNameByPost(String name) {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);

        String result= HttpUtil.post( GATEWAY_HOST + "/api/name/user", paramMap);
        System.out.println(result);

        return result;
    }

    private Map<String, String> getHeaderMap(String body, String host){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("accessKey", accessKey);
        hashMap.put("secretKey", secretKey);
        hashMap.put("nonce", RandomUtil.randomNumbers(4));
        hashMap.put("body", HexUtil.encodeHexStr(body, CharsetUtil.CHARSET_UTF_8));
        hashMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        hashMap.put("sign", SignUtils.getSign(body, secretKey));
        hashMap.put("api_host", host);
        return hashMap;
    }



    public <T> String sendPostRequest(T data, String host, String path) {
        String json = JSONUtil.toJsonStr(data);
        HttpResponse httpResponse = HttpRequest.post(GATEWAY_HOST + path)
                .addHeaders(getHeaderMap(json, host))
                .body(json)
                .execute();
        System.out.println(httpResponse.getStatus());
        String result = httpResponse.body();
        System.out.println(result);

        return result;
    }

}
