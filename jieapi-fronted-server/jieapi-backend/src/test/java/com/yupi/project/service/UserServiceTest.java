package com.jie1234.project.service;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.system.oshi.OshiUtil;
import com.jie1234.jieapicommon.model.entity.InterfaceInfo;
import com.jie1234.project.service.InterfaceInfoService;
import com.jie1234.project.service.UserInterfaceInfoService;
import net.coobird.thumbnailator.Thumbnails;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * 测试
 *
 * @author jie
 */
@SpringBootTest(classes = {com.jie1234.project.MyApplication.class})
class UserServiceTest {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Test
    void testAddUser() {
        System.out.println(111);
    }

    public static void main(String[] args) throws IOException {

    }

    // 定义一个包含所有合法手机号前缀的数组
    private static final String[] TEL_FIRST_PREFIXES = {
            "130", "131", "132", "133", "134", "135", "136", "137", "138", "139",
            "150", "151", "152", "153", "155", "156", "157", "158", "159", "180",
            "181", "182", "183", "184", "185", "186", "187", "188", "189", "190",
            "191", "199" // 根据实际情况添加更多有效的移动和联通、电信等运营商号码段
    };

    public static String generateRandomPhoneNumber() {
        Random random = new Random();

        // 随机选择一个前缀
        String prefix = TEL_FIRST_PREFIXES[random.nextInt(TEL_FIRST_PREFIXES.length)];

        // 生成后八位随机数字
        StringBuilder sb = new StringBuilder(prefix);
        for (int i = 0; i < 8; i++) {
            sb.append(random.nextInt(10));
        }

        return sb.toString();
    }


}