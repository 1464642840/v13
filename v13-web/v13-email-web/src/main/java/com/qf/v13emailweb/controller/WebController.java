package com.qf.v13emailweb.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.v13.api.IEmailService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author blxf
 * @Date ${Date}
 */
@Controller
@RequestMapping("email")
public class WebController {
    @Reference
    private IEmailService emailService;

    @RequestMapping("active")
    public String userActive(String userid, String v_key) {
        System.out.println(v_key);
        emailService.checkActive(Long.parseLong(userid), v_key);
        return "success";
    }
}
