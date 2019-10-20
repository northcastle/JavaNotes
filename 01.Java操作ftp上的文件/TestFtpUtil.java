package com.whc.ftp;

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

public class TestFtpUtil {

	public static void main(String[] args) {
		
		//1.��֤��½����Ϣ
		boolean rv = FtpUtil.login("10.90.247.1",21,"d1fabadm","adm1214");
		System.out.println("=============================");
		
		//2.��ȡ���幤��Ŀ¼�µ��ļ����Ƽ�����
		boolean hasfile = FtpUtil.getFilesName("10.90.247.1",21,"d1fabadm","adm1214", "/usr01/d1fabadm");
		System.out.println("===========================");
		
		//3.�ļ��ϴ��Ĳ���
		boolean putok = FtpUtil.putFileOne("10.90.247.1",21,"d1fabadm","adm1214", "/usr01/d1fabadm", "cfwhctext2.mdb", "C:\\testmdb\\CFtest.mdb");
		System.out.println("============================");
		
		//4.�ļ����صĲ���
		boolean downok = FtpUtil.getFileOne("10.90.247.1",21,"d1fabadm","adm1214", "/usr01/d1fabadm/cfwhctext2.mdb", "C:\\testmdb\\CFtestfrom127_2.mdb");
		System.out.println("============================");
		
	}
	

}
