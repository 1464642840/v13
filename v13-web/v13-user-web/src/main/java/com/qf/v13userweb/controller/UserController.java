package com.qf.v13userweb.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.v13.api.IUserService;
import com.qf.v13.common.constant.RabbitMQConstant;
import com.qf.v13.common.pojo.ResultBean;
import com.qf.v13.entity.TUser;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

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
        System.out.println(1);
        int result = userService.checkUserEsxist(userName);
        if (result == 0) {
            return new ResultBean("200", "用户名可用");
        }
        return new ResultBean("404", "用户名存在");
    }
}
