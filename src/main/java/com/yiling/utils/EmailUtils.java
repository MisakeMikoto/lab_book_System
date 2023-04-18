package com.yiling.utils;


import org.springframework.beans.factory.annotation.Value;

import java.util.Properties;
import java.util.Random;
import javax.mail.*;
import javax.mail.internet.*;


public class EmailUtils {

    @Value("prop.username")
    private static String username = "WingLing_Studio";// 发件人邮箱用户名
    @Value("prop.password")
    private static String password = "MOWZCGWVBHNDKXQP";// 发件人邮箱密码

    private static String verificationCode = "000000";


    // 生成6位数字验证码
    public static String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }

    // 发送验证码到用户邮箱
    public static void sendVerificationCode(String email, String code) {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.163.com"); // 发件人邮箱SMTP服务器地址
        props.put("mail.smtp.port", "465"); // 发件人邮箱SMTP服务器端口号

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // 创建邮件对象
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject(code);

            // 创建邮件正文
            String htmlBody = "<div style='background-color: #f9f9f9; padding: 20px;'>" +
                    "<h1 style='font-size: 24px; color: #333;'>您的验证码是：</h1>" +
                    "<p style='font-size: 36px; color: #007bff;'>" + code + "</p>" +
                    "<p style='font-size: 16px; color: #999;'>该验证码将在5分钟后过期。</p>" +
                    "</div>";
            message.setContent(htmlBody, "text/html");

            // 发送邮件
            System.out.println("开始发送邮件");
            Transport.send(message);
            System.out.println("验证码已发送到邮箱：" + email);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}