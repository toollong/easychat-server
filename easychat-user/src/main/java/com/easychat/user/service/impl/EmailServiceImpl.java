package com.easychat.user.service.impl;

import com.easychat.common.protocol.ResponseResult;
import com.easychat.user.service.EmailService;
import com.easychat.user.vo.SendCodeRequest;
import com.easychat.user.vo.ValidateCodeRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @Author: long
 * @Date: 2022-06-30 18:09
 */
@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public ResponseResult<Object> sendCodeToEmail(SendCodeRequest request) {
        if (request == null
                || StringUtils.isBlank(request.getEmail())
                || request.getType() == null) {
            return ResponseResult.fail();
        }
        try {
            HtmlEmail mail = new HtmlEmail();
            /*发送邮件的服务器 126邮箱为smtp.126.com,163邮箱为smtp.163.com，QQ为smtp.qq.com*/
            mail.setHostName("smtp.163.com");
            /*不设置发送的消息有可能是乱码*/
            mail.setCharset("UTF-8");
            /*IMAP/SMTP服务的密码*/
            mail.setAuthentication("******", "******");
            /*发送邮件的邮箱和发件人*/
            mail.setFrom("******", "EasyChat");
            /*使用安全链接*/
            mail.setSSLOnConnect(true);
            /*接收的邮箱*/
            mail.addTo(request.getEmail());
            /*验证码*/
            String code = this.generateVerifyCode(6);
            if (request.getType() == 0) {
                /*设置邮件的主题*/
                mail.setSubject("EasyChat | 注册验证码");
                /*设置邮件的内容*/
                mail.setMsg("您好，欢迎使用EasyChat，验证码为：" + code +
                        "，该验证码5分钟内有效，请及时输入。如非本人操作，请忽略。");
            }
            if (request.getType() == 1) {
                /*设置邮件的主题*/
                mail.setSubject("EasyChat | 找回密码");
                /*设置邮件的内容*/
                mail.setMsg("尊敬的用户，您好！您正在进行找回密码操作，验证码为：" + code +
                        "，打死也不要告诉别人哦。如非本人操作，请检查账号安全。");
            }
            mail.send();//发送
            redisTemplate.opsForValue()
                    .set("verifyCode:" + request.getEmail(), code, 5, TimeUnit.MINUTES);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.fail();
        }
        return ResponseResult.success();
    }

    @Override
    public ResponseResult<Object> validateEmailAndCode(ValidateCodeRequest request) {
        if (request == null
                || StringUtils.isBlank(request.getEmail())
                || StringUtils.isBlank(request.getVerifyCode())) {
            return ResponseResult.fail();
        }
        String code = redisTemplate.opsForValue().get("verifyCode:" + request.getEmail());
        if (request.getVerifyCode().equals(code)) {
            return ResponseResult.success();
        }
        return ResponseResult.fail();
    }

    private String generateVerifyCode(int number) {
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i <= number; i++) {
            builder.append(random.nextInt(10));
        }
        return builder.toString();
    }
}
