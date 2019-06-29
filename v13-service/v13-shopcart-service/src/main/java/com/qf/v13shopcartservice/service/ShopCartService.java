package com.qf.v13shopcartservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.api.IShopCartService;
import com.qf.pojo.CartItem;
import com.qf.v13.common.constant.RedisConstant;
import com.qf.v13.common.pojo.ResultBean;
import com.qf.v13.entity.TProduct;
import com.qf.v13.entity.TShopcart;
import com.qf.v13.mapper.TProductMapper;
import com.qf.v13.mapper.TShopcartMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author blxf
 * @Date ${Date}
 */

@Service
public class ShopCartService implements IShopCartService {
    @Resource(name = "redisTemplate")
    private RedisTemplate redisTemplate;

    @Autowired
    private TProductMapper tProductMapper;

    @Autowired
    private TShopcartMapper tShopcartMapper;


    @Override
    public ResultBean addShopCart(CartItem cartItem, String token) {
        String key = RedisConstant.SHOPCART_TOKEN_KEY + ":" + token;
        try {

            if (!redisTemplate.opsForHash().hasKey(key, cartItem.getProduct_id())) {
                redisTemplate.opsForHash().put(key, cartItem.getProduct_id(), cartItem);
            } else {
                CartItem cartItem1 = (CartItem) redisTemplate.opsForHash().get(key, cartItem.getProduct_id());
                cartItem1.setCount(cartItem1.getCount() + cartItem.getCount());
                cartItem1.setUpdateTime(new Date());
                redisTemplate.opsForHash().put(key, cartItem.getProduct_id(), cartItem1);

            }
            return new ResultBean("200", "添加购物车成功");

        } catch (Exception e) {
            return new ResultBean("404", "添加购物车失败");
        }
    }

    @Override
    public ResultBean queryShopCart(String token) {
        List<TShopcart> shopList = new ArrayList();
        String key = RedisConstant.SHOPCART_TOKEN_KEY + ":" + token;
        Set<Long> keys = redisTemplate.opsForHash().keys(key);
        for (Long o : keys) {
            CartItem c = (CartItem) redisTemplate.opsForHash().get(key, o);
            TShopcart shopcart = new TShopcart();
            TProduct tProduct = tProductMapper.selectByPrimaryKey(o);
            shopcart.setCount(c.getCount());
            shopcart.setProductImage(tProduct.getImage());
            shopcart.setPrice(tProduct.getSalePrice().longValue());
            shopcart.setProductName(tProduct.getName());
            shopcart.setProductId(o);
            shopcart.setCreateTime(new Date());
            shopcart.setUpdateTime(new Date());
            shopList.add(shopcart);
        }

        return new ResultBean("200", shopList);
    }

    @Override
    public ResultBean delete(String token, Long id) {
        String key = RedisConstant.SHOPCART_TOKEN_KEY + ":" + token;
        Long delete = redisTemplate.opsForHash().delete(key, id);
        if (delete > 0) {
            return new ResultBean("200", "删除成功");
        }
        return new ResultBean("404", "删除失败");

    }

    //查询用户的购物车
    @Override
    public List<TShopcart> queryUserShopCart(Long id) {
        return tShopcartMapper.queryShopCatByUserId(id);
    }

    //将购物车的信息合并到登陆的用户
    @Override
    public void combine(String token, Long id) {
        String key = RedisConstant.SHOPCART_TOKEN_KEY + ":" + token;
        List<TShopcart> tShopcarts = tShopcartMapper.queryShopCatByUserId(id);
        for (TShopcart tShopcart : tShopcarts) {
            CartItem c = (CartItem) redisTemplate.opsForHash().get(key, tShopcart.getProductId());
            if (c != null) {
                addShopCart(c, token);
            }
        }
        ResultBean resultBean = queryShopCart(token);
        tShopcartMapper.addList((List<TShopcart>) resultBean.getData(), id);
    }
}
