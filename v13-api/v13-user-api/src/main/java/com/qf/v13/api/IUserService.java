package com.qf.v13.api;

import com.qf.v13.common.base.IBaseService;
import com.qf.v13.entity.TUser;

/**
 * @author blxf
 * @Date ${Date}
 */
public interface IUserService extends IBaseService<TUser> {
    public TUser savaUser(TUser user);

    int checkUserEsxist(String userName);
}
