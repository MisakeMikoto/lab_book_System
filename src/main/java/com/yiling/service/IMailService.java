package com.yiling.service;

import javax.mail.MessagingException;

/**
 * @Author MisakiMikoto
 * @Date 2023/4/4 17:44
 */
public interface IMailService {


    public void sendHtmlMail(String to, String subject, String content) throws MessagingException;

    public void sendHtmlMail(String to, String subject, String content, String... cc);


}
