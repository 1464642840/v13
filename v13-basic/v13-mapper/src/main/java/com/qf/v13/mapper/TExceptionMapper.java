package com.qf.v13.mapper;

import com.qf.v13.common.base.IBaseDao;
import com.qf.v13.entity.TException;
import com.qf.v13.entity.TUser;

public interface TExceptionMapper extends IBaseDao<TException> {
    @Override
    int deleteByPrimaryKey(Long id);

    @Override
    int insert(TException record);

    @Override
    int insertSelective(TException record);

    @Override
    TException selectByPrimaryKey(Long id);

    @Override
    int updateByPrimaryKeySelective(TException record);

    @Override
    int updateByPrimaryKey(TException record);
}