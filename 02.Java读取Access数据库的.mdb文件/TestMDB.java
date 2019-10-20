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
		// ֱ�ӵ��÷������в��Բ���
		tmdb.readFileACCESS("C://testmdb//CFtestfrom127_2.mdb");
		
		
	}
	
	
	/**
	 * ��ȡ .mdb �ļ��ķ���
	 * @param filePath �� ָ���ļ���·��λ��
	 * @return
	 * ���� �� �˷���ֻ�ܶ�ȡ���ص� .mdb �ļ�
	 * ���� ��
	 *   1.jdk1.8 �Ļ�����
	 *   2.ʹ�� Access_JDBC30.jar
	 */
	public List<Map> readFileACCESS(String filePath) {
		
		List<Map> maplist = new ArrayList();
		
		//1.���û���������
		Properties prop = new Properties();
		prop.put("charSet", "gb2312"); // �����ǽ����������
		prop.put("user", ""); // �����û���
		prop.put("password", "cf1301"); // ��������
		
		//2.���ö�ȡ�ı����ļ���·����ͨ���������ݹ�����
		String url = "jdbc:Access:///"+filePath; 
		
		//3.�����Ǳ�׼��JDBC�Ĳ���
		//PreparedStatement ps = null; 
		Statement stmt = null;
		ResultSet rs = null;
		try {
			//3.1 ��������
			Class.forName("com.hxtt.sql.access.AccessDriver");
			//3.2 ��������,��Ҫʹ�� .mdb �ļ���λ�ã���ʱ��url,��������ļ���λ�ã�prop���������û����������Ϣ
			Connection conn = DriverManager.getConnection(url, prop);
			//3.3 ��ȡstatement����
			stmt = (Statement) conn.createStatement();
			//3.4 ����Ҫִ�е�sql�ַ���,ע�⣬����ط�Ҫ��ȡ��ĸ�ʽ Ϊ ���� �����ƣ��ļ����ƣ�.������ ��
			String sql = "select * from CFtestfrom127_2.mytable";
			//3.5 ִ�в�ѯ����ȡ���صĽ����
			rs = stmt.executeQuery(sql);
			//3.6 ��ȡ���ؽ������Ԫ����
			ResultSetMetaData data = rs.getMetaData();
			//3.7 ����Ԫ���ݼ��ϣ���ȡ�����ֵ
			while (rs.next()) {
				Map map = new HashMap();
				for (int i = 1; i <= data.getColumnCount(); i++) {
					String columnName = data.getColumnName(i); // ����
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
