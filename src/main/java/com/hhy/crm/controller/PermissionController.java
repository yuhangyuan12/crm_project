package com.hhy.crm.controller;

import com.hhy.crm.base.BaseController;
import com.hhy.crm.service.PermissionService;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

@Controller
public class PermissionController extends BaseController{

    @Resource
    private PermissionService permissionService;
}
