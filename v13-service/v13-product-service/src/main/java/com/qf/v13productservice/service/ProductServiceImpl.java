package com.qf.v13productservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.v13.api.IProductService;
import com.qf.v13.common.base.BaseServiceImpl;
import com.qf.v13.common.base.IBaseDao;
import com.qf.v13.entity.TProduct;
import com.qf.v13.mapper.TProductMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author blxf
 * @Date ${Date}
 */
@Service
public class ProductServiceImpl extends BaseServiceImpl<TProduct> implements IProductService {

    @Autowired
    private TProductMapper productMapper;
    @Override
    public IBaseDao<TProduct> getBaseDao() {
        return productMapper;
    }
}
