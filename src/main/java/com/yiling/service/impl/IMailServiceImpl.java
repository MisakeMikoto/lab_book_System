package com.yiling.service.impl;

import com.yiling.service.IMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @Author MisakiMikoto
 * @Date 2023/4/4 17:45
 */
@Service
@Transactional(rollbackFor = Exception.class)
class IMailServiceImpl implements IMailService {
    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.from}")
    private String from;


    @Override
    public void sendHtmlMail(String to, String subject, String content) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.send(message);
    }

    @Override
    public void sendHtmlMail(String to, String subject, String content, String... cc) {

    }
}
