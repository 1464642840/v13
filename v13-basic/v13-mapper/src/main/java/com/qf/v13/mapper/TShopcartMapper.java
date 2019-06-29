package com.qf.v13.mapper;

import com.qf.v13.common.base.IBaseDao;
import com.qf.v13.entity.TShopcart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TShopcartMapper extends IBaseDao<TShopcart> {
    List<TShopcart> queryShopCatByUserId(Long id);

    void addList(@Param("list") List<TShopcart> list, @Param("id") Long id);
}