package com.qf.v13.api;

import com.qf.v13.common.base.IBaseService;
import com.qf.v13.common.pojo.ResultBean;
import com.qf.v13.entity.TUser;

/**
 * @author blxf
 * @Date ${Date}
 */
public interface IEmailService extends IBaseService<TUser> {
    public ResultBean sendEmail(TUser user);

    public ResultBean checkActive(Long id, String v_key);

}
