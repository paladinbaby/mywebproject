package com.sunshine.app.common.pool;

import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DataSourceUtils {

	/**
     * ��ʼ�����ӳأ�����ʵ����
     * 
     * @author zhangby2
     * @param clcle
     * @throws Exception
     */
	class SigleDataSource{
		
		private DataSource pool = null;
		
        public SigleDataSource(String dbName) throws SQLException, NamingException
        {
        	
        	//ִ�����������ĳ�ʼ������,����URL
        	Context ctx = new InitialContext();
        	
        	//lookup�������á�
            pool = (DataSource)ctx.lookup("java:comp/env/sysdb");
            
        }
        
        public DataSource getDataSource()
        {
            return pool;
        }
		
	}
	
	public DataSourceUtils(){}
	
	/**
     * ���ݴ��������Դ�������ӳ�
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
			throw new RuntimeException("��ʼ�����ӳ�ʧ�ܣ�", e);
		}
		
	}
}
