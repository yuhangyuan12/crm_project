package com.hhy.crm.dao;

import com.hhy.crm.base.BaseMapper;
import com.hhy.crm.vo.Permission;

import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission,Integer> {
    //通过角色id查询记录
    Integer countPermissionByRoleId(Integer roleId);

    //根据id删除角色
    void deletePermissionByRoleId(Integer roleId);

    //查询角色拥有的所有资源的集合
    List<Integer> queryRoleHasModuleIdByRoleId(Integer roleId);

    //查询资源列表
    List<String> queryUserHasRoleHasPermissionByUserId(Integer userId);

    Integer countPermissionByModuleId(Integer id);

    Integer deletePermissionByModuleId(Integer id);
}