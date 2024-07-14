package com.damao.interceptor;

import com.damao.context.BaseContext;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthorizeInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        String id = request.getHeader("id");
        if (id == null) {
            response.setStatus(500);
            return false;
        }
        Long uid = Long.valueOf(id);
        BaseContext.setCurrentId(uid);
        return true;
    }

    @Override
    public void postHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, ModelAndView modelAndView) {
        BaseContext.removeCurrentId();
    }
}
