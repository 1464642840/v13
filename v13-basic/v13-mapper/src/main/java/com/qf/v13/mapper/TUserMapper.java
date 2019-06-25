package com.qf.v13.mapper;

import com.qf.v13.common.base.IBaseDao;
import com.qf.v13.entity.TUser;
import org.apache.ibatis.annotations.Param;

public interface TUserMapper extends IBaseDao<TUser> {
    @Override
    int deleteByPrimaryKey(Long id);

    @Override
    int insert(TUser record);

    @Override
    int insertSelective(TUser record);

    @Override
    TUser selectByPrimaryKey(Long id);

    @Override
    int updateByPrimaryKeySelective(TUser record);

    @Override
    int updateByPrimaryKey(TUser record);

    int checkActive(@Param("id") Long id, @Param("v_key") String v_key);

    int checkUserEsxist(@Param("userName") String userName);
}