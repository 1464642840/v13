package com.qf.v13.api;

import com.qf.v13.common.base.IBaseService;
import com.qf.v13.entity.TProductType;

import java.util.List;

/**
 * @author blxf
 * @Date ${Date}
 */
public interface IProducTypeService extends IBaseService<TProductType> {
    public List<TProductType> selectListByCache();
}
