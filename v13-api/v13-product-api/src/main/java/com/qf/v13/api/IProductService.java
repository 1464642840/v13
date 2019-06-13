package com.qf.v13.api;

import com.github.pagehelper.PageInfo;
import com.qf.v13.common.base.IBaseService;
import com.qf.v13.entity.TProduct;
import com.qf.v13.entity.TProductType;
import com.qf.v13.pojo.TProductVo;

import java.util.List;

/**
 * @author blxf
 * @Date ${Date}
 */
public interface IProductService extends IBaseService<TProduct> {
    public PageInfo<TProduct> page(Integer pageIndex, Integer pageSize);

    public Long sava(TProductVo vo);

    public  Long batchDelete(List<Long> ids);
}

