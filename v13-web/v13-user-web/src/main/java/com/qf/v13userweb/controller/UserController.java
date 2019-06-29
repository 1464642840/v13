package com.qf.v13userweb.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.v13.api.IUserService;
import com.qf.v13.common.constant.RabbitMQConstant;
import com.qf.v13.common.constant.RedisConstant;
import com.qf.v13.common.pojo.ResultBean;
import com.qf.v13.entity.TUser;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author blxf
 * @Date ${Date}
 */
@Controller
@RequestMapping("user")
public class UserController {
    @Reference
    private IUserService userService;


    @Autowired
    private RabbitTemplate rabbitTemplate;


    @RequestMapping("register")
    public String userRegister() {
        return "register";
    }

    @RequestMapping("login")
    public String userLogin(HttpServletRequest request) {
        //记录从哪跳来的网站
        String referer = request.getHeader("Referer");
        System.out.println(referer);
        //存入session中
        request.getSession().setAttribute("url", referer);
        return "login";
    }

    @RequestMapping("temp")
    public String toTemp() {
        return "temp";
    }

    @PostMapping("submit")
    @ResponseBody
    public String submit(TUser user) {
        //1.生成随机uuid激活码
        UUID active_id = UUID.randomUUID();
        user.setActiveCode(active_id.toString());
        user = userService.savaUser(user);
        rabbitTemplate.convertAndSend(RabbitMQConstant.USER_EMAIL_EXCHANGE, "user.sendEamil", user);
        return "注册成功,请查收邮件";
    }

    @RequestMapping("checkExist")
    @ResponseBody
    public ResultBean checkExist(String userName) {
        int result = userService.checkUserEsxist(userName);
        if (result == 0) {
            return new ResultBean("200", "用户名可用");
        }
        return new ResultBean("404", "用户名存在");
    }


    @RequestMapping("user_info")
    public String userInfo(HttpServletRequest request) {
        if (request.getAttribute("user") == null) {
            return "login";
        }
        return "user_info";
    }

    @RequestMapping("sendMsg")
    @ResponseBody
    public String sendMsg(String phoneNumber) {
        Map<String, String> data = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        Random r = new Random();
        for (int i = 0; i < 6; i++) {
            sb.append(r.nextInt(10));
        }
        data.put("code", sb.toString());
        data.put("phone", phoneNumber);
        rabbitTemplate.convertAndSend(RabbitMQConstant.USER_EMAIL_EXCHANGE, "user.sendMsg", data);

        return "发送成功";
    }

}
