package com.hhy.crm.controller;

import com.hhy.crm.base.BaseController;
import com.hhy.crm.service.UserRoleService;
import com.hhy.crm.vo.UserRole;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

@Controller
public class UserRoleController extends BaseController {

    @Resource
    private UserRoleService userRoleService;
}
