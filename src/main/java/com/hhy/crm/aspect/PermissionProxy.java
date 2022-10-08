package com.hhy.crm.aspect;


import com.hhy.crm.annoation.RequiredPermission;
import com.hhy.crm.exceptions.AuthException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@Component
@Aspect
public class PermissionProxy {
    @Resource
    private HttpSession session;

    @Around(value = "@annotation(com.hhy.crm.annoation.RequiredPermission)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object result = null;
        List<String> permissions = (List<String>) session.getAttribute("permissions");
        if(null == permissions||permissions.size()<1){
            //抛出认证异常
            throw new AuthException();
        }

        //得到对应的目标
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        RequiredPermission requiredPermission = methodSignature.getMethod().getDeclaredAnnotation(RequiredPermission.class);
        //判断注解上的状态码
        if(!(permissions.contains(requiredPermission.code()))){
            throw new AuthException();
        }
        result = pjp.proceed();
        return result;
    }
}
