package com.maoy;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmailApplicationTests {

    @Autowired
    private JavaMailSender mailSender;

    @Test
    public void sendSimpleMail() throws Exception {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("316988959@qq.com");
        message.setTo("316988959@qq.com");
        message.setSubject("主题：简单邮件");
        message.setText("测试邮件内容");

        mailSender.send(message);
    }

    @Test
    public void sendAttachmentsMail() throws Exception {

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom("316988959@qq.com");
        helper.setTo("316988959@qq.com");
        helper.setSubject("主题：有附件");
        helper.setText("有附件的邮件");

        FileSystemResource file = new FileSystemResource(new File("aaa.jpg"));
        helper.addAttachment("附件-1.jpg", file);
        helper.addAttachment("附件-2.jpg", file);

        mailSender.send(mimeMessage);

    }

    @Test
    public void sendInlineMail() throws Exception {

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom("316988959@qq.com");
        helper.setTo("316988959@qq.com");
        helper.setSubject("主题：嵌入静态资源");
        helper.setText("<html><body><img src=\"cid:aaa\" ></body></html>", true);

        FileSystemResource file = new FileSystemResource(new File("aaa.jpg"));
        helper.addInline("aaa", file);

        mailSender.send(mimeMessage);

    }

    @Autowired
    private VelocityEngine velocityEngine;

    @Test
    public void sendTemplateMail() throws Exception {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom("316988959@qq.com");//邮箱写自己可以测试用的
        helper.setTo("316988959@qq.com");//邮箱写自己可以测试用的
        helper.setSubject("主题：模板邮件");
        VelocityContext context = new VelocityContext();
        context.put("username", "didi");
        StringWriter stringWriter = new StringWriter();
        // 需要注意第1个参数要全路径，否则会抛异常
        velocityEngine.mergeTemplate("/templates/template.vm", "UTF-8", context, stringWriter);
        helper.setText(stringWriter.toString(), true);
        mailSender.send(mimeMessage);
    }

}
