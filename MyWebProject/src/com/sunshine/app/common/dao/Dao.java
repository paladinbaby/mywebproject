package com.sunshine.app.common.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;

import com.sunshine.common.data.IData;
import com.sunshine.common.data.IDataset;
import com.sunshine.common.data.impl.DataMap;
import com.sunshine.common.data.impl.DatasetList;
import com.sunshine.app.common.pool.DbPools;

public class Dao {
	
	public static IDataset qryByCode(String tableName, String sqlRef, IData param, String dbName) throws Exception 
	{
		IDataset dataset = qryByCodeCode(tableName, sqlRef, DaoConsts.sys);
		String sql = dataset.first().getString("SQL_STMT");
		IDataset result = qryBySql(sql, param, dbName);
		return result;
	}

	public static IDataset qryByCodeCode(String tableName, String sqlRef, String dbName) throws Exception 
	{
		// TODO Auto-generated method stub
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT SQL_STMT FROM CODE_CODE WHERE TABLE_NAME=? AND SQL_REF=?";
		IDataset result = new DatasetList();
		
		int i = 0;
		try {
			DbPools.init(dbName);
			DbPools.beginTransaction();
			conn = DbPools.getConnetion(dbName);
			ps = conn.prepareStatement(sql);
			ps.setObject(1, tableName);
			ps.setObject(2, sqlRef);
            rs = ps.executeQuery();
            if(rs != null)
            	result = rsToIDataset(rs);
            DbPools.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if(conn != null)
			{
				DbPools.rollback();
			}
			
			throw new RuntimeException(e);
		}
		finally
		{
			DbPools.closeConnection(conn);
			ps.close();
		}
		
		return result;
	}
	
	public static IDataset qryBySql(String sql, IData param, String dbName) throws Exception 
	{
		// TODO Auto-generated method stub
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		IDataset result = new DatasetList();
		
		int i = 0;
		try {
			DbPools.init(dbName);
			
			DbPools.beginTransaction();
			
			conn = DbPools.getConnetion(dbName);
			
			ps = conn.prepareStatement(sql);
			
			for(Object key:param.keySet()){
	            ps.setObject(i+1, param.get(key));
			}
			
            rs = ps.executeQuery();
            
            if(rs != null)
            	result = rsToIDataset(rs);
            
            DbPools.commit();
            
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if(conn != null)
			{
				DbPools.rollback();
			}
			
			throw new RuntimeException(e);
		}
		finally
		{
			DbPools.closeConnection(conn);
			ps.close();
		}
		
		return result;
	}

	public static int excuteUpdate(String sql, IData param, String dbName) throws Exception 
	{
		// TODO Auto-generated method stub
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		int i = 0;
		int result = 0;
		try {
			DbPools.init(dbName);
			DbPools.beginTransaction();
			conn = DbPools.getConnetion(dbName);
			ps = conn.prepareStatement(sql);
			for(Object key:param.keySet()){
	            System.out.println("key="+key+"and value=" +param.get(key));
	            ps.setObject(i+1, param.get(key));
			}
            result = ps.executeUpdate();
            DbPools.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if(conn != null)
			{
				DbPools.rollback();
			}
			
			throw new RuntimeException(e);
		}
		finally
		{
			DbPools.closeConnection(conn);
			ps.close();
		}
		
		return result;
	}
	
	private static IDataset rsToIDataset(ResultSet rs) throws SQLException 
	{
		// TODO Auto-generated method stub
		IDataset dataset = new DatasetList();
		if(rs.next())
		{
			ResultSetMetaData rsmd = rs.getMetaData();
			do
			{
				IData data = new DataMap();
				for (int i = 1; i <= rsmd.getColumnCount(); i++)
				{
					String name = rsmd.getColumnName(i).toUpperCase();
					String value = getValueByResultSet(rs, rsmd.getColumnType(i), name);
					data.put(name, value);
				}
				dataset.add(data);
				data = null;
			}
			while(rs.next());
			
			rsmd = null;
		}
		return dataset;
	}
	
	private static String getValueByResultSet(ResultSet rs, int type, String name) throws SQLException
	{
		if (type == Types.BLOB)
		{
			return rs.getString(name);
		} else if (type == Types.DATE)
		{
			Date date = rs.getDate(name);
			if (date == null)
			{
				return null;
			}

			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);// format
			// date
		} else if (type == Types.TIMESTAMP)
		{
			Timestamp timestamp = rs.getTimestamp(name);
			if (timestamp == null)
			{
				return null;
			}
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(timestamp.getTime()));// format
			// date
		} else
		{
			return rs.getString(name);
		}
	}
}
