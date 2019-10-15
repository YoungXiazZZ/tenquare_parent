package com.tensquare.user.controller;

import com.tensquare.user.pojo.User;
import com.tensquare.user.service.UserService;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    JwtUtil jwtUtil;


    //增加关注数
    @RequestMapping(value = "/incfollow/{userid}/{x}", method = RequestMethod.POST)
    public void incFollowcount(@PathVariable String userid, @PathVariable int x) {
        userService.incFollowcount(userid, x);
    }

    //增加粉丝数
    @RequestMapping(value = "/incfans/{userid}/{x}", method = RequestMethod.POST)
    public void incFanscount(@PathVariable String userid, @PathVariable int x) {
        userService.incFanscount(userid, x);
    }


    //发送验证码
    @RequestMapping(value = "/sendsms/{mobile}", method = RequestMethod.POST)
    public Result sendSms(@PathVariable String mobile) {
        userService.sendSms(mobile);
        return new Result(true, StatusCode.OK, "发送成功");
    }

    //用户注册
    @RequestMapping(value = "/register/{code}", method = RequestMethod.POST)
    public Result register(@RequestBody User user, @PathVariable String code) {
        userService.add(user, code);
        return new Result(true, StatusCode.OK, "注册成功");
    }

    //用户登录
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result login(@RequestBody Map<String, String> loginmap) {
        User user = userService.findByMobileAndPassword(loginmap.get("mobile"), loginmap.get("password"));
        if (user != null) {
            //登录成功返回token
            String token = jwtUtil.createJWT(user.getId(), user.getNickname(), "user");
            Map map = new HashMap();
            map.put("token", token);
            map.put("name", user.getNickname());    //昵称
            map.put("avatar", user.getAvatar());    //头像
            return new Result(true, StatusCode.OK, "登录成功", map);
        } else {
            return new Result(false, StatusCode.LOGINERROR, "用户名或密码错误");
        }
    }


    /**
     * 删除
     *
     * @param id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable String id) {
        //删除用户必须有管理员权限
        Claims claims = (Claims) request.getAttribute("admin_claims");
        if (claims == null) {
            return new Result(true, StatusCode.ACCESSERROR, "无权访问");
        }
        userService.deleteById(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }
}
