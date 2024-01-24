package com.damao.service.impl;

import com.damao.constant.RabbitMQConstant;
import com.damao.constant.RedisNameConstant;
import com.damao.properties.MailProperties;
import com.damao.pojo.dto.SendEmailDTO;
import com.damao.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@RabbitListener(queues = RabbitMQConstant.QUEUE)
public class EmailServiceImpl implements EmailService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private MailProperties mailProperties;

    @Autowired
    private JavaMailSender mailSender;

    @RabbitHandler
    public void sendVerifyCode(SendEmailDTO sendEmailDTO) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            String email = sendEmailDTO.getEmail();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(mailProperties.getUsername());
            helper.setTo(email);
            helper.setSubject("用户注册验证");
            String code = Integer.toString(new Random().nextInt(3001) + 3000);
            helper.setText(buildContent(code), true);
            helper.setCc("3034906016@qq.com");

            redisTemplate.opsForHash().put(RedisNameConstant.VERIFY_CODE_SET, email, code);
            redisTemplate.expire(RedisNameConstant.VERIFY_CODE_SET + '_' + email, 10, TimeUnit.MINUTES);

            mailSender.send(message);
        } catch (RuntimeException | MessagingException e) {
            log.info("发送邮件出错{}", e.getMessage());
            // 出错就发到死信队列
            rabbitTemplate.convertAndSend(RabbitMQConstant.DEAD_MEG_EXCHANGE,"114514",sendEmailDTO);
        }
    }

    public String buildContent(String title) {
        //加载邮件html模板
        Resource resource = new ClassPathResource("templates/mail-template.ftl");
        InputStream inputStream = null;
        BufferedReader fileReader = null;
        StringBuilder buffer = new StringBuilder();
        String line = "";
        try {
            inputStream = resource.getInputStream();
            fileReader = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = fileReader.readLine()) != null) {
                buffer.append(line);
            }
        } catch (Exception e) {
            log.info("发送邮件读取模板失败{}", e.getMessage());
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //替换html模板中的参数
        return MessageFormat.format(buffer.toString(), title);
    }

}