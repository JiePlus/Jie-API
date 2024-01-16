package com.jie1234.jieapiinterface;

import com.jie1234.jieapiclientsdk.client.JieApiClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;

@SpringBootTest
class JieapiInterfaceApplicationTests {

    @Resource
    private JieApiClient jieApiClient;

    @Test
    void contextLoads() {

    }



}
