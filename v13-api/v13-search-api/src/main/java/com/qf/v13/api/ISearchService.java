package com.qf.v13.api;

import com.qf.v13.common.pojo.ResultBean;

/**
 * @author blxf
 * @Date ${Date}
 */
public interface ISearchService {
    /**
     * 全量同步
     */
    ResultBean synAllData();

    ResultBean queryByKeywords(String keywords, int currentPage);

    ResultBean synAddOrUpdateById(Long id);

    ResultBean synDeleteById(Long id);
}
