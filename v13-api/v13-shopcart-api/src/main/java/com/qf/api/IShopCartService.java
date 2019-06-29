package com.qf.api;



import com.qf.pojo.CartItem;
import com.qf.v13.common.pojo.ResultBean;
import com.qf.v13.entity.TShopcart;

import java.util.List;

/**
 * @author blxf
 * @Date ${Date}
 */
public interface IShopCartService {
    ResultBean addShopCart(CartItem cartItem, String token);

    ResultBean queryShopCart(String token);

    ResultBean delete(String token, Long id);

    List<TShopcart> queryUserShopCart(Long id);

    void combine(String token, Long id);
}
