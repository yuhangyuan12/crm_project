package com.hhy.crm.Interceptor;

import com.hhy.crm.dao.UserMapper;
import com.hhy.crm.exceptions.AuthException;
import com.hhy.crm.utils.LoginUserUtil;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NoLoginInterceptor extends HandlerInterceptorAdapter {

    @Resource
    private UserMapper userMapper;
    /**
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取用户ID
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        if(null == userId||userMapper.selectByPrimaryKey(userId) == null){
            throw new AuthException();
        }
        return true;
    }
}
