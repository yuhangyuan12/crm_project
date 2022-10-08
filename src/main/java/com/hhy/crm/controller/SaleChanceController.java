package com.hhy.crm.controller;

import com.hhy.crm.annoation.RequiredPermission;
import com.hhy.crm.base.BaseController;
import com.hhy.crm.base.ResultInfo;
import com.hhy.crm.enums.StateStatus;
import com.hhy.crm.query.SaleChanceQuery;
import com.hhy.crm.service.SaleChanceService;
import com.hhy.crm.utils.CookieUtil;
import com.hhy.crm.utils.LoginUserUtil;
import com.hhy.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("sale_chance")
public class SaleChanceController extends BaseController {

    @Resource
    private SaleChanceService saleChanceService;

    //营销机会分页多条件查询
    @RequiredPermission(code = "101001")
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> querySaleChanceByParams(SaleChanceQuery saleChanceQuery,
                                                      Integer flag,
                                                      HttpServletRequest request
                                                      ){

        if(null != flag && flag == 1){
            saleChanceQuery.setState(StateStatus.STATED.getType());
            //从cookie中获取当前登录用户ID
            Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
            saleChanceQuery.setAssignMan(userId);
        }


        return saleChanceService.querySaleChanceByParams(saleChanceQuery);
    }

    //进入营销机会页面
    @RequiredPermission(code = "1010")
    @RequestMapping("index")
    public String index(){
        return "saleChance/sale_chance";
    }


    //添加营销机会
    @RequiredPermission(code = "101002")
    @RequestMapping("add")
    @ResponseBody
    public ResultInfo addSaleChance(SaleChance saleChance, HttpServletRequest request){
        String userName = CookieUtil.getCookieValue(request,"userName");
        saleChance.setCreateMan(userName);
        saleChanceService.addSaleChance(saleChance);
        return success("营销机会数据添加成功！");
    }

    //更新营销机会
    @RequiredPermission(code = "101004")
    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateSaleChance(SaleChance saleChance){
        saleChanceService.updateSaleChance(saleChance);
        return success("营销机会数据更新成功！");
    }

    //进入添加页面

    @RequestMapping("toSaleChancePage")
    public String toSaleChancePage(Integer id,HttpServletRequest request){
        if(null != id){
            SaleChance saleChance = saleChanceService.selectByPrimaryKey(id);
            request.setAttribute("saleChance",saleChance);
        }
        return "saleChance/add_update";
    }


    //批量删除
    @RequiredPermission(code = "101003")
    @PostMapping("delete")
    @ResponseBody
    public ResultInfo deleteSaleChance(Integer[] ids){
        saleChanceService.deleteSaleChance(ids);
        return success("营销机会删除成功！");
    }

    //更新营销机会的状态
    @RequestMapping("updateSaleChanceDevResult")
    @ResponseBody
    public ResultInfo updateSaleChanceDevResult(Integer id,Integer devResult){
        saleChanceService.updateSaleChanceDevResult(id,devResult);
        return success("营销机会的开发状态更新成功！");
    }
}
