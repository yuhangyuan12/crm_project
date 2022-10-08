package com.hhy.crm;

import com.alibaba.fastjson.JSON;
import com.hhy.crm.base.ResultInfo;
import com.hhy.crm.exceptions.AuthException;
import com.hhy.crm.exceptions.ParamsException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


//全局异常处理
@Component
public class GlobalExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        if(ex instanceof AuthException){
            ModelAndView mv = new ModelAndView("redirect:/index");
            return mv;
      }


        /*
      *     1，返回视图
      *     2，返回json数据
      * */
        //默认异常处理
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("code",500);
        modelAndView.addObject("msg","系统异常，请重试。。");

        //判断HandleMethod
        if(handler instanceof HandlerMethod){
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            //获取方法上面声明的@responseBody对象
            ResponseBody responseBody = handlerMethod.getMethod().getDeclaredAnnotation(ResponseBody.class);
            //方法返回视图
            if(responseBody == null){
                if(ex instanceof Exception){
                    ParamsException p = (ParamsException) ex;
                    //设置异常信息
                    modelAndView.addObject("code",p.getCode());
                    modelAndView.addObject("msg",p.getMsg());
                }else if(ex instanceof AuthException){
                    AuthException a = (AuthException) ex;
                    //设置异常信息
                    modelAndView.addObject("code",a.getCode());
                    modelAndView.addObject("msg",a.getMsg());
                }
                return modelAndView;
            }else {
                //方法返回数据

                //默认异常
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setCode(500);
                resultInfo.setMsg("系统异常，请重试!");

                //判断异常是否是自定义异常
                if(ex instanceof ParamsException){
                    ParamsException p = (ParamsException) ex;
                    resultInfo.setCode(p.getCode());
                    resultInfo.setMsg(p.getMsg());
                }else  if(ex instanceof AuthException){
                    AuthException a = (AuthException) ex;
                    resultInfo.setCode(a.getCode());
                    resultInfo.setMsg(a.getMsg());
                }

                //响应json格式数据
                response.setContentType("application/json;charset=utf-8");
                PrintWriter out = null;
                try {
                    out = response.getWriter();
                    String json = JSON.toJSONString(resultInfo);
                    out.write(json);
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if(out != null){
                        out.close();
                    }
                }
                return null;
            }
        }else {
            return modelAndView;
        }
    }
}
