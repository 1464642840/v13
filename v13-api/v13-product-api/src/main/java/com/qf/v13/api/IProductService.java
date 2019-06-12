package com.qf.v13.api;

import com.github.pagehelper.PageInfo;
import com.qf.v13.common.base.IBaseService;
import com.qf.v13.entity.TProduct;
import com.qf.v13.entity.TProductType;

/**
 * @author blxf
 * @Date ${Date}
 */
public interface IProductService extends IBaseService<TProduct> {
    public PageInfo<TProduct> page(Integer pageIndex, Integer pageSize);
}

