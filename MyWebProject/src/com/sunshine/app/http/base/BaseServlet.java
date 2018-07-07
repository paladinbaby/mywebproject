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
        // 1�����ִ�еķ�����
        String methodName = request.getParameter("method");
        String path = request.getServletPath();
        // Ĭ�Ϸ���
        if (methodName == null) {
            methodName = "execute";
        }
        System.out.println("BaseServlet : ����ִ�еķ����� :  " + methodName);
        System.out.println("BaseServlet : servlet path :  " + path);
        try {
            // 2��ͨ�������õ�ǰ��������ָ������,��ʽ����
            Method executeMethod = this.getClass().getMethod(methodName,
                    HttpServletRequest.class, HttpServletResponse.class);
            // 3������ִ�з���
            String result = (String) executeMethod.invoke(this, request,
                    response);
            // 4����json���ݷ���
            Writer out = response.getWriter();
            out.write(result);
            out.flush();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("δ֪����  : " + methodName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("�������쳣", e);
        }
	}
	
	/**
     * Ĭ��ִ�з���
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }
	
}
