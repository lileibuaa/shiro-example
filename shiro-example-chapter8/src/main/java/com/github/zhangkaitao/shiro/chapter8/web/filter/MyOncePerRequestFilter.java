package com.github.zhangkaitao.shiro.chapter8.web.filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.apache.shiro.web.servlet.OncePerRequestFilter;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 14-2-3
 * <p>Version: 1.0
 */
public class MyOncePerRequestFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        System.out.println("=========once per request filter and context path is " + ((ShiroHttpServletRequest) request).getContextPath() + ".");
        chain.doFilter(request, response);
    }
}
