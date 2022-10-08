package com.hhy.crm.service;


import com.hhy.crm.base.BaseService;
import com.hhy.crm.dao.UserMapper;
import com.hhy.crm.dao.UserRoleMapper;
import com.hhy.crm.model.UserModel;
import com.hhy.crm.utils.AssertUtil;
import com.hhy.crm.utils.Md5Util;
import com.hhy.crm.utils.PhoneUtil;
import com.hhy.crm.utils.UserIDBase64;
import com.hhy.crm.vo.User;
import com.hhy.crm.vo.UserRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class UserService extends BaseService<User,Integer> {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserRoleMapper userRoleMapper;
    /*
    *   用户登录
    *   1，判断参数是否为空（判断用户姓名，密码是否为空）
    *   2, 调用Dao层 根据=用户名查询用户对象
    *   3，判断用户对象是否为空
    *   4，判读用户密码是否正确
    *   5，密码正确，登录成功
    * */
    public UserModel userLogin(String userName,String userPwd){
        //参数判断
        checkLogin(userName,userPwd);
        //调用dao层。返回用户对象
        User user = userMapper.queryByUserName(userName);
        //判断对象是否为空
        AssertUtil.isTrue(null == user,"用户不存在！");
        //判断用户密码是否正确
        AssertUtil.isTrue(!(user.getUserPwd().equals(Md5Util.encode(userPwd))),"用户密码不正确，请重新输入!");
        //返回构建用户对象
       return buildUserInfo(user);
    }


    //修改密码
    @Transactional(propagation = Propagation.REQUIRED)
    public void updatePassword(Integer userId,String oldPwd,String newPwd,String repeatPwd){
        User user = userMapper.selectByPrimaryKey(userId);
        //查询用户对象是否存在
        AssertUtil.isTrue(null == user,"待更新用户不存在！");
        //调用更新方法
        checkPasswordParams(user,oldPwd,newPwd,repeatPwd);

        user.setUserPwd(Md5Util.encode(newPwd));

        //执行更新 返回受影响的行数
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user)<1,"用户修改密码失败！");
    }

    private void checkPasswordParams(User user, String oldPwd, String newPwd, String repeatPwd) {
        //判断原始密码是否存在
        AssertUtil.isTrue(StringUtils.isBlank(oldPwd),"原始密码不能为空");
        AssertUtil.isTrue(!(user.getUserPwd().equals(Md5Util.encode(oldPwd))),"原始密码不正确！");
        AssertUtil.isTrue(StringUtils.isBlank(newPwd),"新密码不能为空!");
        AssertUtil.isTrue(oldPwd.equals(newPwd),"新密码不能与原始密码保持一致！");
        AssertUtil.isTrue(StringUtils.isBlank(repeatPwd),"校验密码不能为空！");
        AssertUtil.isTrue(!(newPwd.equals(repeatPwd)),"校验密码要与新密码保持一致！");
    }


    //构建返回给客户端的用户对象
    private UserModel buildUserInfo(User user) {
        UserModel userModel = new UserModel();
        userModel.setUserIdStr(UserIDBase64.encoderUserID(user.getId()));
        userModel.setUserName(user.getUserName());
        userModel.setTrueName(user.getTrueName());
        return userModel;
    }

    //密码判断
    private void checkLogin(String userName, String userPwd) {
        //判断姓名密码是否为空
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户姓名不能为空!");
        AssertUtil.isTrue(StringUtils.isBlank(userPwd),"用户密码不能为空!");
    }

    //分页多条件查询
    public List<Map<String,Object>> queryAllSales(){
        return userMapper.queryAllSales();
    }

    //添加用户
    @Transactional(propagation = Propagation.REQUIRED)
    public void addUser(User user){
        //参数校验
        checkUserParams(user.getUserName(),user.getEmail(),user.getPhone(),null);
        //设置默认值
        user.setIsValid(1);
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        user.setUserPwd(Md5Util.encode("123"));
        //执行添加
        AssertUtil.isTrue(userMapper.insertSelective(user) < 1,"添加用户失败！");

        //用户角色关联
        relationUserRole(user.getId(),user.getRoleIds());
    }

    private void relationUserRole(Integer userId, String roleIds) {
        Integer count = userRoleMapper.countUserRoleByUserId(userId);
        if(count>0){
            AssertUtil.isTrue(userRoleMapper.deleteUserByUserId(userId)!=count,"用户角色分配失败！");
        }
        if(StringUtils.isNotBlank(roleIds)){
            List<UserRole> userRoleList = new ArrayList<>();
            String[] roleIdsArray = roleIds.split(",");
            for (String roleId :roleIdsArray) {
                UserRole userRole = new UserRole();
                userRole.setRoleId(Integer.parseInt(roleId));
                userRole.setUserId(userId);
                userRole.setCreateDate(new Date());
                userRole.setUpdateDate(new Date());

                userRoleList.add(userRole);
            }
            //批量添加用户数据
            AssertUtil.isTrue(userRoleMapper.insertBatch(userRoleList)!=userRoleList.size(),"用户角色分配失败！");
        }
    }


    private void checkUserParams(String userName, String email, String phone, Integer userId) {
        //参数校验
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空！");
        User temp = userMapper.queryByUserName(userName);
        AssertUtil.isTrue(null != temp && !(temp.getId().equals(userId)),"用户名已存在！");
        AssertUtil.isTrue(StringUtils.isBlank(email),"用户邮箱不能为空！");
        AssertUtil.isTrue(StringUtils.isBlank(phone),"用户号码不能为空!");
     //   AssertUtil.isTrue(PhoneUtil.isMobile(phone),"手机号格式不正确！");
    }

    //更新用户
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUser(User user){
        //判断id是否存在
        AssertUtil.isTrue(null == user.getId(),"待更新记录不存在！");
        User temp = userMapper.selectByPrimaryKey(user.getId());
        AssertUtil.isTrue(null == temp,"待更新记录不存在！");
        //参数校验
        checkUserParams(user.getUserName(),user.getEmail(),user.getPhone(),temp.getId());

        user.setUpdateDate(new Date());



        //执行更新
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user)!=1,"更新失败！");
        //与角色关联
        relationUserRole(user.getId(),user.getRoleIds());
    }

    //删除用户
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteByIds(Integer[] ids) {
        //判断id是否为空
        AssertUtil.isTrue(null == ids||ids.length==0,"待删除记录不存在!");
        AssertUtil.isTrue(userMapper.deleteBatch(ids)!=ids.length,"用户删除失败!");

        //遍历用户id的数组
        for (Integer userId :ids) {
            Integer count = userRoleMapper.countUserRoleByUserId(userId);
            if(count>0){
                AssertUtil.isTrue(userRoleMapper.deleteUserByUserId(userId)!=count,"用户删除失败！");
            }
        }

    }
}
