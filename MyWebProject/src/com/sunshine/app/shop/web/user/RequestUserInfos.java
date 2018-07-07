package com.sunshine.app.shop.web.user;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sunshine.app.common.dao.Dao;
import com.sunshine.app.common.dao.DaoConsts;
import com.sunshine.app.http.base.BaseServlet;
import com.sunshine.common.data.IData;
import com.sunshine.common.data.IDataset;
import com.sunshine.common.data.impl.DataMap;

/**
 * Servlet implementation class RequestUserInfos
 */
@WebServlet(name="RequestUserInfos", urlPatterns="/RequestUserInfos")
public class RequestUserInfos extends BaseServlet {
	private static final long serialVersionUID = 1L;
       
	public String qryUserInfos(HttpServletRequest request, HttpServletResponse response)
            throws Exception
	{
		// 1.获取到客户端传递过来的用户账号和密码
        String name = request.getParameter("user_name");
        IData data = new DataMap();
        data.put("user_name", name);
		return getCustName(name, data).toJsonString();
	}
	
	private IData getCustName(String name, IData param) throws Exception
	{
		IData data = new DataMap();
		if(name != null)
		{
			IDataset dataset = Dao.qryByCode("UD_CUSTOMER", "SEL_BY_NAME", param, DaoConsts.user);
			data = dataset.first();
		}
		System.out.println("data="+data);
		return data;
	}

}
