package com.qf.v13userservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.v13.api.IUserService;
import com.qf.v13.common.base.BaseServiceImpl;
import com.qf.v13.common.base.IBaseDao;
import com.qf.v13.entity.TUser;
import com.qf.v13.mapper.TUserMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author blxf
 * @Date ${Date}
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<TUser> implements IUserService {
    @Autowired
    private TUserMapper userMapper;

    @Override
    public IBaseDao<TUser> getBaseDao() {
        return userMapper;
    }


    @Override
    public TUser savaUser(TUser user) {
         super.insertSelective(user);
         return user;
    }

    @Override
    public int checkUserEsxist(String userName) {
        return  userMapper.checkUserEsxist(userName);
    }
}
