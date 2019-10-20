package com.whc.project;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class TestMDB {
	
	public static void main(String ...strings) {
		TestMDB tmdb = new TestMDB();
		// 直接调用方法进行测试操作
		tmdb.readFileACCESS("C://testmdb//CFtestfrom127_2.mdb");
		
		
	}
	
	
	/**
	 * 读取 .mdb 文件的方法
	 * @param filePath ： 指定文件的路径位置
	 * @return
	 * 局限 ： 此方法只能读取本地的 .mdb 文件
	 * 条件 ：
	 *   1.jdk1.8 的环境下
	 *   2.使用 Access_JDBC30.jar
	 */
	public List<Map> readFileACCESS(String filePath) {
		
		List<Map> maplist = new ArrayList();
		
		//1.设置基本的属性
		Properties prop = new Properties();
		prop.put("charSet", "gb2312"); // 这里是解决中文乱码
		prop.put("user", ""); // 放入用户名
		prop.put("password", "cf1301"); // 放入密码
		
		//2.设置读取的本地文件的路径，通过参数传递过来的
		String url = "jdbc:Access:///"+filePath; 
		
		//3.下面是标准的JDBC的操作
		//PreparedStatement ps = null; 
		Statement stmt = null;
		ResultSet rs = null;
		try {
			//3.1 加载驱动
			Class.forName("com.hxtt.sql.access.AccessDriver");
			//3.2 创建连接,需要使用 .mdb 文件的位置，此时的url,就是这个文件的位置，prop参数就是用户名密码等信息
			Connection conn = DriverManager.getConnection(url, prop);
			//3.3 获取statement对象
			stmt = (Statement) conn.createStatement();
			//3.4 创建要执行的sql字符串,注意，这个地方要读取表的格式 为 ：“ 库名称（文件名称）.表名称 ”
			String sql = "select * from CFtestfrom127_2.mytable";
			//3.5 执行查询，获取返回的结果集
			rs = stmt.executeQuery(sql);
			//3.6 获取返回结果集的元数据
			ResultSetMetaData data = rs.getMetaData();
			//3.7 遍历元数据集合，获取具体的值
			while (rs.next()) {
				Map map = new HashMap();
				for (int i = 1; i <= data.getColumnCount(); i++) {
					String columnName = data.getColumnName(i); // 列名
					String columnValue = rs.getString(i);
					System.out.println(columnName + ":" + columnValue);
					map.put(columnName, columnValue);
					
				}
				System.out.println("===================");
				maplist.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return maplist;
	}


}
