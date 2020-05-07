package com.scott.eshop.cache.filter;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;

import javax.servlet.*;
import java.io.IOException;

/**
 * Hystrix 请求上下文过滤器
 * @ClassName HystrixRequestContextFilter
 * @Description
 * @Author 47980
 * @Date 2020/5/7 22:06
 * @Version V_1.0
 **/
public class HystrixRequestContextFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HystrixRequestContext context = HystrixRequestContext.initializeContext();
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            context.close();
        }
    }

    @Override
    public void destroy() {

    }
}
