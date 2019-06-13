package com.qf.v13productservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qf.v13.api.IProductService;
import com.qf.v13.common.base.BaseServiceImpl;
import com.qf.v13.common.base.IBaseDao;
import com.qf.v13.entity.TProduct;
import com.qf.v13.entity.TProductDesc;
import com.qf.v13.mapper.TProductDescMapper;
import com.qf.v13.mapper.TProductMapper;
import com.qf.v13.pojo.TProductVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author blxf
 * @Date ${Date}
 */
@Service
public class ProductServiceImpl extends BaseServiceImpl<TProduct> implements IProductService {

    @Autowired
    private TProductMapper productMapper;
    @Autowired
    private TProductDescMapper productDescMapper;

    @Override
    public IBaseDao<TProduct> getBaseDao() {
        return productMapper;
    }

    @Override
    public PageInfo<TProduct> page(Integer pageIndex, Integer pageSize) {
        PageHelper.startPage(pageIndex, pageSize);
        List<TProduct> list = selectList();
        PageInfo<TProduct> pageInfo = new PageInfo<TProduct>(list, 5);
        return pageInfo;
    }

    @Override
    @Transactional
    public Long sava(TProductVo vo) {
        TProduct product = vo.getProduct();
        product.setFlag(true);
        product.setCreateTime(new Date());
        productMapper.insertSelective(product);
        TProductDesc productDesc = new TProductDesc();
        productDesc.setProductDesc(vo.getProductDesc());
        productDesc.setProductId(product.getId());
        productDescMapper.insert(productDesc);
        return product.getId();
    }

    @Override
    public Long batchDelete(List<Long> ids) {
        return productMapper.batchUpdateFlag(ids);
    }

    @Override
    public int deleteByPrimaryKey(Long id) {
        TProduct t = new TProduct();
        t.setId(id);
        t.setFlag(false);
        return productMapper.updateByPrimaryKeySelective(t);
    }
}
