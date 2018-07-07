package com.sunshine.app.common.pool;

import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DataSourceUtils {

	/**
     * 初始化连接池（单库实例）
     * 
     * @author zhangby2
     * @param clcle
     * @throws Exception
     */
	class SigleDataSource{
		
		private DataSource pool = null;
		
        public SigleDataSource(String dbName) throws SQLException, NamingException
        {
        	
        	//执行命名操作的初始上下文,解析URL
        	Context ctx = new InitialContext();
        	
        	//lookup检索配置。
            pool = (DataSource)ctx.lookup("java:comp/env/sysdb");
            
        }
        
        public DataSource getDataSource()
        {
            return pool;
        }
		
	}
	
	public DataSourceUtils(){}
	
	/**
     * 根据传入的数据源生成连接池
     * 
     * @author zhangby2
     * @param clcle
     * @throws Exception
     */
	public DataSource getCurrentDataSource(String dbName)
	{
		try
		{
			SigleDataSource dataSource = new SigleDataSource(dbName);
			return dataSource.getDataSource();
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException("初始化连接池失败：", e);
		}
		
	}
}
