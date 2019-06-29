package com.qf.v13ssoweb.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.v13.api.IUserService;
import com.qf.v13.common.constant.RedisConstant;
import com.qf.v13.common.pojo.ResultBean;
import com.qf.v13.entity.TUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author blxf
 * @Date ${Date}
 */
@Controller
@RequestMapping("sso")
public class SSOController {
    @Resource(name = "redisTemplate")
    private RedisTemplate redisTemplate;
    @Reference
    private IUserService userService;

    @RequestMapping("queryIsLogin")
    @ResponseBody
    @CrossOrigin(origins = "*", allowCredentials = "true")
    public ResultBean queryIsLogin(@CookieValue(name = "USER_TOKEN", required = false) String token) {

        if (token != null) {
            return userService.queryIsLogin(token);
        }
        return new ResultBean("404", "暂未登陆");
    }

    @PostMapping("doLogin")
    public String doLogin(String userName, String password, HttpServletRequest request, HttpServletResponse response) {
        String referer = request.getHeader("Referer");
        ResultBean resultBean = userService.doLogin(userName, password);
        if ("200".equals(resultBean.getStatusCode())) {
            Cookie cookie = new Cookie("USER_TOKEN", resultBean.getData().toString());
            cookie.setMaxAge(RedisConstant.COOKIE_MAXTIME);
            cookie.setPath("/");
            response.addCookie(cookie);
            return "redirect:http://localhost:9095/user/temp";
        }
        return "redirect:http://localhost:9095/user/login";
    }

    @RequestMapping("logout")
    public String logout(HttpServletResponse response, HttpServletRequest request, @CookieValue(name = "USER_TOKEN", required = false) String token) {
        String redirect = request.getHeader("Referer");
        if (token != null) {
            userService.logout(token);
            Cookie cookie = new Cookie("USER_TOKEN", null);
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
        return "redirect:" + redirect;
    }
}
