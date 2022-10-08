package com.hhy.crm.dao;

import com.hhy.crm.base.BaseMapper;
import com.hhy.crm.vo.UserRole;

public interface UserRoleMapper extends BaseMapper<UserRole,Integer> {
    //根据id查询
    Integer countUserRoleByUserId(Integer userId);

    //根据id删除
    Integer deleteUserByUserId(Integer userId);
}