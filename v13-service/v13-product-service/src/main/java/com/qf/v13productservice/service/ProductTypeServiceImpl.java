package com.qf.v13productservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.v13.api.IProducTypeService;
import com.qf.v13.common.base.BaseServiceImpl;
import com.qf.v13.common.base.IBaseDao;
import com.qf.v13.entity.TProduct;
import com.qf.v13.entity.TProductType;
import com.qf.v13.mapper.TProductTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

/**
 * @author blxf
 * @Date ${Date}
 */
@Service
public class ProductTypeServiceImpl extends BaseServiceImpl<TProductType> implements IProducTypeService {
    @Autowired
    private TProductTypeMapper typeMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public IBaseDao<TProductType> getBaseDao() {
        return typeMapper;
    }

    @Override
    public List<TProductType> selectListByCache() {
        List<TProductType> typeList = (List<TProductType>) redisTemplate.opsForValue().get("typeList");
        System.out.println("从缓存中读取");
        if (typeList == null) {
            typeList = typeMapper.selectList();
            redisTemplate.opsForValue().set("typeList", typeList);
            System.out.println("从数据库中读取");
        }
        return typeList;
    }
}
