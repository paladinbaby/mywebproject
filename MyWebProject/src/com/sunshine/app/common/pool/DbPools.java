package com.sunshine.app.common.pool;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.sunshine.app.common.pool.DataSourceUtils;

/**
 * Copyright: Copyright (c) 2018 sunshine
 * 
 * @ClassName: DbUtils.java
 * @Description: ���ݿ⹤����
 * @version: v1.0.0
 * @author: zhangby2
 */
public class DbPools {
	
	private static DataSource dataSource = null;
	
	private static ThreadLocal<Connection> thd = new ThreadLocal<>();
	
	/**
     * ��ʼ������
     * 
     * @author zhangby2
     * @param clcle
     * @throws Exception
     */
	public DbPools() {}
	
	/**
     * ����DataSourceUtils���������ӳ�
     * 
     * @author zhangby2
     * @param clcle
     * @throws Exception
     */
	public static void init(String dbName) throws Exception
	{
		DataSourceUtils ds = new DataSourceUtils();
		dataSource = ds.getCurrentDataSource(dbName);
	}
	
	/**
     * ȡ����ǰ����Դ
     * 
     * @author zhangby2
     * @param clcle
     * @throws Exception
     */
	public DataSource getDataSource() throws Exception
	{
		return dataSource;
	}
	
	/**
     * �����ӳ�ȡ��һ������
     * 
     * @author zhangby2
     * @param clcle
     * @throws Exception
     */
	public static Connection getConnetion(String dbName) throws Exception
	{
		Connection conn = thd.get();
		if(conn != null)
			return conn;
		return dataSource.getConnection();
	}
	
	/**
     * ��ʼһ�����񣬲����뱾���߳�
     * 
     * @author zhangby2
     * @param clcle
     * @throws Exception
     */
	public static void beginTransaction() throws SQLException
	{
		Connection conn = thd.get();
		if(conn == null )
		{
			conn = dataSource.getConnection();
			conn.setAutoCommit(false); //��������
			thd.set(conn);
		}
		else
		{
			throw new SQLException("�����Ѿ������������ظ�������");
		}
	}
	
	/**
     * �ύһ��������������̣߳������ӹ黹�����ӳ�
     * 
     * @author zhangby2
     * @param clcle
     * @throws Exception
     */
	public static void commit() throws SQLException
	{
		Connection conn = thd.get();
		if(conn == null )
		{
			throw new SQLException("����û�п����������ύ��");
		}
		else
		{
			conn.commit();
			conn.close();
			thd.set(null);
		}
	}
	
	/**
     * �ع�һ��������������̣߳������ӹ黹�����ӳ�
     * 
     * @author zhangby2
     * @param clcle
     * @throws Exception
     */
	public static void rollback() throws SQLException
	{
		Connection conn = thd.get();
		if(conn == null )
		{
			throw new SQLException("����û�п��������ܻع���");
		}
		else
		{
			conn.rollback();
			conn.close();
			thd.set(null);
		}
	}
	
	/**
     * �ͷ���������
     * 
     * @author zhangby2
     * @param clcle
     * @throws Exception
     */
	public static void closeConnection(Connection connection) throws Exception
	{
		try {
			Connection con = thd.get();
			if(con == null )
				connection.close();
			if(con != connection)
				connection.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}