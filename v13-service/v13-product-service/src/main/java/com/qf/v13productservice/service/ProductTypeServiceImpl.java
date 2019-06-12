package com.qf.v13productservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.v13.api.IProducTypeService;
import com.qf.v13.common.base.BaseServiceImpl;
import com.qf.v13.common.base.IBaseDao;
import com.qf.v13.entity.TProduct;
import com.qf.v13.entity.TProductType;
import com.qf.v13.mapper.TProductTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author blxf
 * @Date ${Date}
 */
@Service
public class ProductTypeServiceImpl extends BaseServiceImpl<TProductType> implements IProducTypeService {
    @Autowired
    private TProductTypeMapper typeMapper;

    @Override
    public IBaseDao<TProductType> getBaseDao() {
        return typeMapper;
    }
}
