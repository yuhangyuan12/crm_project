package com.hhy.crm.service;

import com.hhy.crm.base.BaseService;
import com.hhy.crm.dao.PermissionMapper;
import com.hhy.crm.vo.Permission;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PermissionService extends BaseService<Permission,Integer> {

    @Resource
    private PermissionMapper permissionMapper;

    //通过查询用户拥有的角色，查询角色拥有的资源，得到用户拥有的资源
    public List<String> queryUserHasRoleHasPermissionByUserId(Integer userId) {
        return permissionMapper.queryUserHasRoleHasPermissionByUserId(userId);
    }
}
