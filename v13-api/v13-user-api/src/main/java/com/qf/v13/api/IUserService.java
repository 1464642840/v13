package com.qf.v13.api;

import com.qf.v13.common.base.IBaseService;
import com.qf.v13.common.pojo.ResultBean;
import com.qf.v13.entity.TUser;

/**
 * @author blxf
 * @Date ${Date}
 */
public interface IUserService extends IBaseService<TUser> {
    public TUser savaUser(TUser user);

    public int checkUserEsxist(String userName);

    public ResultBean doLogin(String userName, String password);

    ResultBean queryIsLogin(String token);

    ResultBean logout(String token);
}
