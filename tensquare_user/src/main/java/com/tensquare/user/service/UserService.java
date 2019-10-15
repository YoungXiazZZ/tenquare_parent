package com.tensquare.user.service;

import com.tensquare.user.dao.UserDao;
import com.tensquare.user.pojo.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private UserDao userDao;

    @Autowired
    BCryptPasswordEncoder encoder;


    //更新关注数
    @Transactional
    public void incFollowcount(String userid, int x) {
        userDao.incFollowcount(userid, x);
    }

    //更新粉丝数
    @Transactional
    public void incFanscount(String userid, int x) {
        userDao.incFanscount(userid, x);
    }

    /**
     * 发送短信验证码
     */
    public void sendSms(String mobile) {
        //生成验证码
        String checkCode = RandomStringUtils.randomNumeric(6);
        System.out.println("验证码是：" + checkCode);
        //把验证码放在redis中
        redisTemplate.opsForValue().set("Code_" + mobile, checkCode, 5, TimeUnit.HOURS);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("mobile", mobile);
        hashMap.put("checkCode", checkCode);
        rabbitTemplate.convertAndSend("sms", hashMap);
    }

    /**
     * 用户注册
     */
    public void add(User user, String code) {
        String syscode = (String) redisTemplate.opsForValue().get("Code_" + user.getMobile());
        if (syscode == null) {
            throw new RuntimeException("请点击获取验证码");
        }
        if (!syscode.equals(code)) {
            throw new RuntimeException("验证码不正确");
        }

        user.setId(idWorker.nextId() + "");
        user.setFollowcount(0);//关注数
        user.setFanscount(0);//粉丝数
        user.setOnline(0L);//在线时长
        user.setRegdate(new Date());//注册日期
        user.setUpdatedate(new Date());//更新日期
        user.setLastdate(new Date());//最后登陆日期
        String newpassword = encoder.encode(user.getPassword());//加密后的密码
        user.setPassword(newpassword);
        userDao.save(user);
    }

    /**
     * 根据手机号和密码查询用户
     *
     * @param mobile
     * @param password
     * @return
     */
    public User findByMobileAndPassword(String mobile, String password) {
        User user = userDao.findByMobile(mobile);
        if (user != null && encoder.matches(password, user.getPassword())) {
            return user;
        } else {
            return null;
        }
    }

    /**
     * 删除
     *
     * @param id
     */
    public void deleteById(String id) {
        userDao.deleteById(id);
    }

}
