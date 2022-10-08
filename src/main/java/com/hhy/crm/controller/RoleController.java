package com.hhy.crm.controller;

import com.hhy.crm.base.BaseController;
import com.hhy.crm.base.ResultInfo;
import com.hhy.crm.query.RoleQuery;
import com.hhy.crm.service.RoleService;
import com.hhy.crm.vo.Role;
import org.omg.CORBA.INTERNAL;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.naming.spi.ResolveResult;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("role")
public class RoleController extends BaseController {

    @Resource
    private RoleService roleService;

    //多条件查询
    @ResponseBody
    @RequestMapping("queryAllRoles")
    public List<Map<String,Object>> queryAllSales(Integer userId){
        return roleService.queryAllRoles(userId);
    }


    //分页条件查询
    @GetMapping("list")
    @ResponseBody
    public Map<String,Object> selectByParams(RoleQuery roleQuery){
        return roleService.queryByParamsForTable(roleQuery);
    }


    //进入角色页面
    @RequestMapping("index")
    public String index(){
        return "role/role";
    }

    //添加角色
    @PostMapping("add")
    @ResponseBody
    public ResultInfo addRole(Role role){
        roleService.addRole(role);
        return success("角色添加成功！");
    }


    //更新角色
    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateRole(Role role){
        roleService.updateRole(role);
        return success("更新成功!");
    }


    //删除角色
    @PostMapping("delete")
    @ResponseBody
    public ResultInfo deleteRole(Integer id){
        roleService.deleteRole(id);
        return success("删除成功!");
    }

    //打开添加模态框
    @RequestMapping("addOrUpdateRolePage")
    public String addOrUpdateRolePage(Integer id, HttpServletRequest request){
        if(null != id){
            Role role = roleService.selectByPrimaryKey(id);
            request.setAttribute("role",role);
        }
        return "role/add_update";
    }


    //角色授权操作
    @PostMapping("addGrant")
    @ResponseBody
    public ResultInfo addGrant(Integer roleId,Integer[] mids){
        roleService.addGrant(roleId,mids);
        return success("角色授权成功！");
    }
}
