package com.gxd.hystrix.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;


public class ArgumentsFilter extends HttpServletRequestWrapper {

    //参数字节数组
    private byte[] requestBody;

    //http请求对象
    private HttpServletRequest request;


    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request The request to wrap
     * @throws IllegalArgumentException if the request is null
     */
    public ArgumentsFilter(HttpServletRequest request) {
        super(request);
        this.request = request;
    }
}
