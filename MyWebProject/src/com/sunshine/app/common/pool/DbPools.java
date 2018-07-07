package com.sunshine.app.common.pool;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.sunshine.app.common.pool.DataSourceUtils;

/**
 * Copyright: Copyright (c) 2018 sunshine
 * 
 * @ClassName: DbUtils.java
 * @Description: 数据库工具类
 * @version: v1.0.0
 * @author: zhangby2
 */
public class DbPools {
	
	private static DataSource dataSource = null;
	
	private static ThreadLocal<Connection> thd = new ThreadLocal<>();
	
	/**
     * 初始化方法
     * 
     * @author zhangby2
     * @param clcle
     * @throws Exception
     */
	public DbPools() {}
	
	/**
     * 调用DataSourceUtils类生成连接池
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
     * 取出当前数据源
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
     * 从连接池取出一个连接
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
     * 开始一个事务，并存入本地线程
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
			conn.setAutoCommit(false); //开启事务
			thd.set(conn);
		}
		else
		{
			throw new SQLException("事务已经开启，不能重复开启！");
		}
	}
	
	/**
     * 提交一个事务，清除本地线程，将连接归还给连接池
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
			throw new SQLException("事务没有开启，不能提交！");
		}
		else
		{
			conn.commit();
			conn.close();
			thd.set(null);
		}
	}
	
	/**
     * 回滚一个事务，清除本地线程，将连接归还给连接池
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
			throw new SQLException("事务没有开启，不能回滚！");
		}
		else
		{
			conn.rollback();
			conn.close();
			thd.set(null);
		}
	}
	
	/**
     * 释放所有连接
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