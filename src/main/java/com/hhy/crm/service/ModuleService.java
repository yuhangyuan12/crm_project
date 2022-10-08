package com.hhy.crm.service;

import com.hhy.crm.base.BaseService;
import com.hhy.crm.dao.ModuleMapper;
import com.hhy.crm.dao.PermissionMapper;
import com.hhy.crm.model.TreeModel;
import com.hhy.crm.utils.AssertUtil;
import com.hhy.crm.vo.Module;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ModuleService extends BaseService<Module,Integer> {

    @Resource
    private ModuleMapper moduleMapper;

    @Resource
    private PermissionMapper permissionMapper;
    //查询所有的资源列表
    public List<TreeModel> queryAllModules(Integer roleId){

        List<TreeModel> treeModelList = moduleMapper.queryAllModules();
        //查询指定角色已经授权过的资源列表
        List<Integer> permissionIds = permissionMapper.queryRoleHasModuleIdByRoleId(roleId);
        //判断角色是否拥有角色Id
        if(permissionIds != null&& permissionIds.size()>0){
            treeModelList.forEach(treeModel -> {
                if(permissionIds.contains(treeModel.getId())){
                    treeModel.setChecked(true);
                }
            });
        }
        return treeModelList;
    }


    //查询资源数据
    public Map<String,Object> queryModuleList(){
        Map<String,Object> map = new HashMap<>();
        List<Module> moduleList = moduleMapper.queryModuleList();
        map.put("code",0);
        map.put("msg","");
        map.put("count",moduleList.size());
        map.put("data",moduleList);
        return map;
    }

    //添加资源
    @Transactional(propagation = Propagation.REQUIRED)
    public void addModule(Module module){
        AssertUtil.isTrue(StringUtils.isBlank(module.getModuleName()),"请输入菜单名!");
        Integer grade =module.getGrade();
        AssertUtil.isTrue(null==grade || !(grade==0||grade==1||grade==2),"菜单层级非法!");
        Module module1 = moduleMapper.queryModuleByGradeAndModuleName(grade,module.getModuleName());
        AssertUtil.isTrue(null !=module1,"该层级下菜单名已存在!");
        if(grade==1){
            AssertUtil.isTrue(StringUtils.isBlank(module.getUrl()),"请输入二级菜单url地址!");
            module1=moduleMapper.queryModuleByGradeAndUrl(grade,module.getUrl());
            AssertUtil.isTrue(null !=module1,"二级菜单下url不可重复!");
        }

        // 二级 三级菜单 必须指定上级菜单id
        if(grade!=0){
            AssertUtil.isTrue(null==module.getParentId() ||null== selectByPrimaryKey(module.getParentId()),"请指定上级菜单!");
        }

        AssertUtil.isTrue(StringUtils.isBlank(module.getOptValue()),"请输入菜单权限码!");
        module1 = moduleMapper.queryModuleByOptValue(module.getOptValue());
        AssertUtil.isTrue(null !=module1,"权限码重复!");

        module.setIsValid((byte)1);
        module.setCreateDate(new Date());
        module.setUpdateDate(new Date());
        AssertUtil.isTrue(insertSelective(module)<1,"菜单添加失败!");
    }

    //修改资源
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateModule(Module module){
        Module temp =selectByPrimaryKey(module.getId());
        AssertUtil.isTrue(null ==temp,"待修改的菜单记录不存在!");
        AssertUtil.isTrue(StringUtils.isBlank(module.getModuleName()),"请输入菜单名!");
        //层级校验
        Integer grade =module.getGrade();
        AssertUtil.isTrue(null==grade || !(grade==0||grade==1||grade==2),"菜单层级非法!");
        //模块名称
        temp  = moduleMapper.queryModuleByGradeAndModuleName(grade,module.getModuleName());
        AssertUtil.isTrue(null !=temp && !(temp.getId().equals(module.getId())),"该层级下菜单名已存在!");
        //地址url
        if(grade==1){
            AssertUtil.isTrue(StringUtils.isBlank(module.getUrl()),"请输入二级菜单url地址!");
            temp=moduleMapper.queryModuleByGradeAndUrl(grade,module.getUrl());
            AssertUtil.isTrue(null !=temp && !(temp.getId().equals(module.getId())),"二级菜单下url不可重复!");
        }

        // 二级 三级菜单 必须指定上级菜单id
        if(grade!=0){
            AssertUtil.isTrue(null==module.getParentId() ||null== selectByPrimaryKey(module.getParentId()),"请指定上级菜单!");
        }

        AssertUtil.isTrue(StringUtils.isBlank(module.getOptValue()),"请输入菜单权限码!");
        temp = moduleMapper.queryModuleByOptValue(module.getOptValue());
        AssertUtil.isTrue(null !=temp && !(temp.getId().equals(module.getId())),"权限码重复!");

        module.setUpdateDate(new Date());
        AssertUtil.isTrue(updateByPrimaryKeySelective(module)<1,"菜单记录更新失败!");
    }


    //删除资源
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteModule(Integer id) {
        Module temp =selectByPrimaryKey(id);
        AssertUtil.isTrue(null==temp,"待删除的记录不存在!");
        Integer total = moduleMapper.countSubModuleByParentId(id);
        AssertUtil.isTrue(total>0,"存在子菜单，暂不支持删除操作!");

        // 删除权限表对应记录
        total = permissionMapper.countPermissionByModuleId(id);
        if(total>0){
            AssertUtil.isTrue(permissionMapper.deletePermissionByModuleId(id) !=total,"菜单记录删除失败!");
        }

        temp.setIsValid((byte)0);
        AssertUtil.isTrue(updateByPrimaryKeySelective(temp)<1,"菜单记录删除失败!");
    }
}

