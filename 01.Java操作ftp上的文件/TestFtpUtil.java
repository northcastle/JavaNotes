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
		
		//1.验证登陆的信息
		boolean rv = FtpUtil.login("10.90.247.1",21,"d1fabadm","adm1214");
		System.out.println("=============================");
		
		//2.获取具体工作目录下的文件名称及个数
		boolean hasfile = FtpUtil.getFilesName("10.90.247.1",21,"d1fabadm","adm1214", "/usr01/d1fabadm");
		System.out.println("===========================");
		
		//3.文件上传的操作
		boolean putok = FtpUtil.putFileOne("10.90.247.1",21,"d1fabadm","adm1214", "/usr01/d1fabadm", "cfwhctext2.mdb", "C:\\testmdb\\CFtest.mdb");
		System.out.println("============================");
		
		//4.文件下载的操作
		boolean downok = FtpUtil.getFileOne("10.90.247.1",21,"d1fabadm","adm1214", "/usr01/d1fabadm/cfwhctext2.mdb", "C:\\testmdb\\CFtestfrom127_2.mdb");
		System.out.println("============================");
		
	}
	

}
