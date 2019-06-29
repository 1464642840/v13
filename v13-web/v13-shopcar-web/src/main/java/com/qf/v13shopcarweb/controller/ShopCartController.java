package com.qf.v13shopcarweb.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.api.IShopCartService;
import com.qf.pojo.CartItem;
import com.qf.v13.api.IUserService;
import com.qf.v13.common.pojo.ResultBean;
import com.qf.v13.entity.TProduct;
import com.qf.v13.entity.TShopcart;
import com.qf.v13.entity.TUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author blxf
 * @Date ${Date}
 */
@RequestMapping("shopCart")
@Controller
public class ShopCartController {


    @Reference
    private IShopCartService shopCartService;

    @Reference
    private IUserService userService;

    @RequestMapping("/")
    @ResponseBody
    public String ss() {
        return "1";
    }

    @CrossOrigin(origins = "*", allowCredentials = "true")
    @ResponseBody
    @RequestMapping("add")
    public ResultBean add(@CookieValue(name = "SHOPCART_TOKEN", required = false) String token, HttpServletResponse response, CartItem cartItem) {
        cartItem.setUpdateTime(new Date());
        if (token == null) {
            token = UUID.randomUUID().toString();
            Cookie cookie = new Cookie("SHOPCART_TOKEN", token);
            cookie.setMaxAge(3600 * 7);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);
        }
        return shopCartService.addShopCart(cartItem, token);

    }

    @RequestMapping("showShopCat")
    public String shopShopCat(HttpServletResponse response, @CookieValue(name = "SHOPCART_TOKEN", required = false) String token, @CookieValue(name = "USER_TOKEN", required = false) String loginToken, Model model) {
        //查询用户是否登陆
        ResultBean isLogin = userService.queryIsLogin(loginToken);
        //查询redis中的购物车数据
        ResultBean resultBean = shopCartService.queryShopCart(token);
        if ("200".equals(isLogin.getStatusCode())) {
            List<TShopcart> data = (List<TShopcart>) resultBean.getData();
            //登陆的用户信息
            TUser user = (TUser) isLogin.getData();
            if (data.size() != 0) {
                shopCartService.combine(token, user.getId());
                Cookie cookie = new Cookie("SHOPCART_TOKEN", token);
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
            }

            //将查询出来的结果给data
            resultBean.setData(shopCartService.queryUserShopCart(user.getId()));
        }
        model.addAttribute("productList", (List<TShopcart>) resultBean.getData());
        return "shopCar";
    }

    @RequestMapping("delete/{id}")
    @ResponseBody
    public ResultBean delete(@CookieValue(name = "SHOPCART_TOKEN", required = false) String token, @PathVariable Long id) {
        return shopCartService.delete(token, id);
    }
}
