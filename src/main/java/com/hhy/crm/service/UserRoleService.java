package com.hhy.crm.service;

import com.hhy.crm.base.BaseService;
import com.hhy.crm.dao.UserRoleMapper;
import com.hhy.crm.vo.UserRole;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserRoleService extends BaseService<UserRole,Integer> {

    @Resource
    private UserRoleMapper userRoleMapper;
}
