package com.sunshine.app.http.base;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class BaseCommonServlet
 */
public abstract class BaseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
    public void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
        request.setCharacterEncoding("utf-8");
        // 1、获得执行的方法名
        String methodName = request.getParameter("method");
        String path = request.getServletPath();
        // 默认方法
        if (methodName == null) {
            methodName = "execute";
        }
        System.out.println("BaseServlet : 本次执行的方法是 :  " + methodName);
        System.out.println("BaseServlet : servlet path :  " + path);
        try {
            // 2、通过反射获得当前运行类中指定方法,形式参数
            Method executeMethod = this.getClass().getMethod(methodName,
                    HttpServletRequest.class, HttpServletResponse.class);
            // 3、反射执行方法
            String result = (String) executeMethod.invoke(this, request,
                    response);
            // 4、将json数据返回
            Writer out = response.getWriter();
            out.write(result);
            out.flush();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("未知方法  : " + methodName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("服务器异常", e);
        }
	}
	
	/**
     * 默认执行方法
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }
	
}
