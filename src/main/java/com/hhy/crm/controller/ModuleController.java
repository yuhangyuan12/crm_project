package com.hhy.crm.controller;

import com.hhy.crm.base.BaseController;
import com.hhy.crm.base.ResultInfo;
import com.hhy.crm.model.TreeModel;
import com.hhy.crm.service.ModuleService;
import com.hhy.crm.vo.Module;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("module")
public class ModuleController extends BaseController {

    @Resource
    private ModuleService moduleService;


    @RequestMapping("queryAllModules")
    @ResponseBody
    public List<TreeModel> queryAllModules(Integer roleId){
        return moduleService.queryAllModules(roleId);
    }


    //打开授权页面
    @RequestMapping("toAddGrantPage")
    public String toAddGrantPage(Integer roleId, HttpServletRequest request){

        //将需要授权的角色Id设置到请求域中
        request.setAttribute("roleId",roleId);
        return "role/grant";
    }


    //查询资源列表
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> queryModuleList(){
        return moduleService.queryModuleList();
    }

    //进入菜单管理页面
    @RequestMapping("index")
    public String index(){
        return "module/module";
    }


    //添加资源
    @PostMapping("add")
    @ResponseBody
    public ResultInfo addModule(Module module){
        moduleService.addModule(module);
        return success("添加资源成功！");
    }

    //修改资源
    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateModule(Module module){
         moduleService.updateModule(module);
         return success("修改资源成功！");
    }

    //删除资源
    @PostMapping("delete")
    @ResponseBody
    public ResultInfo deleteModule(Integer id){
        moduleService.deleteModule(id);
        return success("删除资源成功!");
    }


    //打开修改资源的对话框
    @RequestMapping("updateModulePage")
    public String updateModulePage(Integer id, Model model){
        model.addAttribute("module",moduleService.selectByPrimaryKey(id));
        return "module/update";
    }


    //打开添加资源的页面
    @RequestMapping("addModulePage")
    public String addModulePage(Integer grade,Integer parentId,HttpServletRequest request){
        request.setAttribute("grade",grade);
        request.setAttribute("parentId",parentId);
        return "module/add";
    }
}
