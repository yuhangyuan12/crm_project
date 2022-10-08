package com.hhy.crm.controller;


import com.hhy.crm.base.BaseController;
import com.hhy.crm.base.ResultInfo;
import com.hhy.crm.exceptions.ParamsException;
import com.hhy.crm.model.UserModel;
import com.hhy.crm.query.UserQuery;
import com.hhy.crm.service.UserService;
import com.hhy.crm.utils.LoginUserUtil;

import com.hhy.crm.vo.User;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("user")
public class UserController extends BaseController{
    @Resource
    private UserService userService;

    //登录功能
    @PostMapping("login")
    @ResponseBody
    public ResultInfo userLogin(String userName,String userPwd){
        ResultInfo resultInfo = new ResultInfo();
        UserModel userModel =  userService.userLogin(userName,userPwd);
        //将数据返回给请求
        resultInfo.setResult(userModel);
        return resultInfo;
        /*try {
           UserModel userModel =  userService.userLogin(userName,userPwd);
            //将数据返回给请求
            resultInfo.setResult(userModel);
            return resultInfo;
        }catch (ParamsException p){
            resultInfo.setCode(p.getCode());
            resultInfo.setMsg(p.getMessage());
            p.printStackTrace();
        }catch (Exception e){
             resultInfo.setCode(500);
             resultInfo.setMsg("用户登录失败！");
             e.printStackTrace();
        }*/
    }

    //修改密码
    @PostMapping("updatePwd")
    @ResponseBody
    public ResultInfo updateUserPassword(HttpServletRequest request,
                                   String oldPwd,
                                   String newPwd,
                                   String repeatPwd){
        ResultInfo resultInfo = new ResultInfo();
        //获取cookie中的userId
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        //调用service层中的修改方法
        userService.updatePassword(userId,oldPwd,newPwd,repeatPwd);
        /*try {


        }catch (ParamsException p){
            resultInfo.setCode(p.getCode());
            resultInfo.setMsg(p.getMsg());
            p.printStackTrace();
        }catch (Exception e){
            resultInfo.setCode(500);
            resultInfo.setMsg("修改密码失败 ！");
            e.printStackTrace();
        }*/
        return resultInfo;
    }

    //进入修改密码页面
    @RequestMapping("toPasswordPage")
    public String toPasswordPage(){
        return "user/password";
    }



    //查询所有销售人员
    @RequestMapping("queryAllSales")
    @ResponseBody
    public List<Map<String,Object>> queryAllSales(){
        return userService.queryAllSales();
    }



    //分页多添加查询
    @ResponseBody
    @RequestMapping("list")
    public Map<String,Object> selectByParams(UserQuery userQuery){
        return userService.queryByParamsForTable(userQuery);
    }


    //进入用户管理页面
    @RequestMapping("index")
    public String index(){
        return "user/user";
    }


    //添加用户
    @PostMapping("add")
    @ResponseBody
    public ResultInfo addUser(User user){
        userService.addUser(user);
        return success("用户添加成功!");
    }

    //更新用户
    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateUser(User user){
        userService.updateUser(user);
        return success("用户更新成功!");
    }


    @RequestMapping("addOrUpdateUserPage")
    public String addOrUpdateUserPage(Integer id,HttpServletRequest request){
        if(id != null){
            User user = userService.selectByPrimaryKey(id);
            request.setAttribute("userInfo",user);
        }
        return "user/add_update";
    }


    //删除用户
    @PostMapping("delete")
    @ResponseBody
    public ResultInfo deleteUser(Integer[] ids){
        userService.deleteByIds(ids);
        return success("删除成功!");
    }
}
