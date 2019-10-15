package com.tensquare.sms.listener;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.tensquare.sms.utils.SmsUtil;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = "sms")
public class SmsListener {

    @Autowired
    private SmsUtil smsUtil;

    @Value("${aliyun.sms.template_code}")
    private String templateCode;//模板编号

    @Value("${aliyun.sms.sign_name}")
    private String signName;//签名

    //发送短信
    @RabbitHandler
    public void sendSms(Map<String, String> message) {
        System.out.println(message.get("mobile"));
        System.out.println(message.get("checkCode"));
        try {
            SendSmsResponse sendSmsResponse = smsUtil.sendSms(message.get("mobile"), templateCode, signName, message.get("checkCode"));
            System.out.println(sendSmsResponse.getMessage());
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

}
