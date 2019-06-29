package com.qf.v13userservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.v13.api.IUserService;
import com.qf.v13.common.base.BaseServiceImpl;
import com.qf.v13.common.base.IBaseDao;
import com.qf.v13.common.constant.RedisConstant;
import com.qf.v13.common.pojo.ResultBean;
import com.qf.v13.entity.TUser;
import com.qf.v13.mapper.TUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author blxf
 * @Date ${Date}
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<TUser> implements IUserService {
    @Autowired
    private TUserMapper userMapper;

    @Resource(name = "redisTemplate")
    private RedisTemplate redisTemplate;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public IBaseDao<TUser> getBaseDao() {
        return userMapper;
    }


    @Override
    public TUser savaUser(TUser user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        super.insertSelective(user);
        return user;
    }

    @Override
    public int checkUserEsxist(String userName) {
        return userMapper.checkUserEsxist(userName);
    }

    @Override
    public ResultBean doLogin(String userName, String password) {
        TUser user = userMapper.doLogin(userName, password);
        if (user != null) {
            if (bCryptPasswordEncoder.matches(password, user.getPassword())) {
                String token = UUID.randomUUID().toString();
                String key = RedisConstant.USER_SESSION_KEY + ":" + token;
                user.setPassword(null);
                redisTemplate.opsForValue().set(key, user);
                redisTemplate.expire(key, RedisConstant.USER_SESSION_MAX_TIME, TimeUnit.SECONDS);
                return new ResultBean("200", token);
            }
            return new ResultBean("404", "账户或密码错误");

        }
        return new ResultBean("404", "用户不存在");
    }

    @Override
    public ResultBean queryIsLogin(String token) {
        String key = RedisConstant.USER_SESSION_KEY + ":" + token;
        TUser user = (TUser) redisTemplate.opsForValue().get(key);
        if (user == null) {
            return new ResultBean("404", "登陆信息未找到");
        }
        redisTemplate.expire(key, 30, TimeUnit.MINUTES);
        return new ResultBean("200", user);
    }

    @Override
    public ResultBean logout(String token) {
        String key = RedisConstant.USER_SESSION_KEY + ":" + token;
        redisTemplate.delete(key);
        return new ResultBean("200", "注销成功");
    }

}
