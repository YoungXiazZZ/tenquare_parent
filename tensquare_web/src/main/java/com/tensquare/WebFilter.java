package com.tensquare;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;


@Component
public class WebFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return "pre";// 前置过滤器

        //pre   请求被路由之前被调用
        //route 请求时被调用
        //post  route和error过滤器之后被调用
        //error 处理请求发生错误时调用
    }

    @Override
    public int filterOrder() {
        return 0;// 优先级为0，数字越大，优先级越低
    }

    @Override
    public boolean shouldFilter() {
        return true;// 是否执行该过滤器，此处为true，说明需要过滤
    }

    @Override
    public Object run() throws ZuulException {
        //向heander中添加鉴权令牌
        //获取当前容器对象
        RequestContext requestContext = RequestContext.getCurrentContext();
        //获取request对象,和header
        HttpServletRequest request = requestContext.getRequest();
        String authorization = request.getHeader("Authorization");
        if (authorization != null) {
            requestContext.addZuulRequestHeader("Authorization", authorization);
        }
        //放行
        return null;
    }
}
