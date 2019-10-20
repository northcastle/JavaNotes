package com.whc.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;    
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

public class FtpUtil {
	
		//ftp对象
		private static FTPClient ftp;
		
		private static InputStream is = null;
		private static OutputStream os  = null;
		private static FileOutputStream fos = null;
		private static FileInputStream fis = null;
		
		
		/**
		 * 验证登录
		 * @param ip
		 * @param port
		 * @param name
		 * @param pwd
		 * @return
		 */
		public static boolean login(String ip,int port, String name, String pwd) {
			try {
				ftp = new FTPClient();
				ftp.connect(ip, port);
				
				
				//System.out.println(ftp.login(name, pwd));
				if(!ftp.login(name, pwd)){
					System.out.println("用户名密码验证失败！");
					return false;
				}
				System.out.println("登陆验证成功！");
				System.out.println("登陆后的工作目录是 ： "+ftp.printWorkingDirectory());
				
				ftp.enterLocalPassiveMode();//设置为被动模式(如上传文件夹成功，不能上传文件，注释这行，否则报错refused:connect  )
				ftp.setFileType(FTP.BINARY_FILE_TYPE);//修改上传文件格式
				ftp.setCharset(Charset.forName("UTF-8"));
				ftp.setControlEncoding("UTF-8");

			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("登陆异常！");
				return false;
			}
			return true;
		}
		
		 
		/**
		 * 获取ftp某一文件（路径）下的文件名字,用于查看文件列表
		 * @param ip
		 * @param port
		 * @param name
		 * @param pwd
		 * @param remotedir 远程地址目录
		 * @return
		 */
	    public static boolean getFilesName(String ip,int port, String name, String pwd, String remotedir) {
	        try {
	        	if(!login(ip, port, name, pwd)){
					return false;
				}
	        	
	            //获取ftp里面，指定文件夹 里面的文件名字，存入数组中
	            FTPFile[] files = ftp.listFiles(remotedir);
	            System.out.println(remotedir+" 目录下的文件个数是 ： "+files.length);
	            //打印出ftp里面，指定文件夹 里面的文件名字
	            for (int i = 0; i < files.length; i++) {
	                System.out.println(files[i].getName());
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	            return false;
	        }finally{
	        	//调用自己的关闭资源的方法
	        	close();
	        }
	        return true;
	    }
	    
	    
	    /**
	     * 上传文件 方法一
	     * @param ip
	     * @param port
	     * @param name
	     * @param pwd
	     * @param remotepath 远程地址文件目录
	     * @param remotename 远程文件名
	     * @param localpath 本地文件路径
	     * @return
	     */
	    public static boolean putFileOne(String ip,int port, String name, String pwd,String remotepath,String remotename ,String localpath) {
	    	System.out.println("FTP地址信息：-----ip:"+ip+"----port:"+port+"---name:"+name+"---pwd:"+pwd+"---remotepath:"+remotepath+"---remotename:"+remotename+"---localpath:"+localpath);
	        try {
	        	if(!login(ip, port, name, pwd)){
					return false;
				}
	            //将本地的 localpath 文件上传到ftp的根目录文件夹下面，并重命名为 remotepath中的名字
	        	ftp.changeWorkingDirectory(remotepath);//设置上传路径 -- FTP服务器文件目录
	        	boolean res = ftp.storeFile(remotename, new FileInputStream(new File(localpath)));
	        	if(res) {
	        		System.out.println("文件上传成功!");
	        	}else {
	        		System.out.println("文件上传失败！");
	        	}
	        	return res;
	        } catch (Exception e) {
	            e.printStackTrace();
	            return false;
	        }finally{
	        	close();
	        }
//	        return true;
	    }

	    
	    /**
	     * 上传文件的第二种方法，优化了传输速度
	     * @param ip
	     * @param port
	     * @param name
	     * @param pwd
	     * @param remotepath 远程地址文件路径
	     * @param localpath 本地文件路径
	     * @return
	     */
	    public static boolean putFileTwo(String ip,int port, String name, String pwd,String remotepath,String localpath) {
	    	System.out.println("-----ip:"+ip+"----port:"+port+"---name:"+name+"---pwd:"+pwd+"---remotepath:"+remotepath+"---localpath:"+localpath);
	        try {
	        	if(!login(ip, port, name, pwd)){
					return false;
				}
	        	
//	        	 //設置上傳的路徑
//	            ftp.changeWorkingDirectory(remotepath);
	            os = ftp.storeFileStream(remotepath);
	            fis = new FileInputStream(new File(localpath));
	           

	            byte[] b = new byte[1024];
	            int len = 0;
	            while ((len = fis.read(b)) != -1) {
	                os.write(b,0,len);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            return false;
	        }finally {
	        	close();
			}
	        return true;
	    }

	    
	    /**
	     * 下载文件 方法一
	     * @param ip
	     * @param port
	     * @param name
	     * @param pwd
	     * @param remotepath 远程地址文件路径
	     * @param localpath 本地文件路径
	     * @return
	     */
	    public static boolean getFileOne(String ip,int port, String name, String pwd,String remotepath,String localpath) {
	        try {
	        	if(!login(ip, port, name, pwd)){
					return false;
				}
	        	
	            //将ftp资源中 remotepath 文件下载到本地目录文件夹下面，并重命名为 localpath 中的名字
	        	boolean res = ftp.retrieveFile(remotepath, new FileOutputStream(new File(localpath)));
	        	if(res) {
	        		System.out.println("文件下载成功！");
	        	}else {
	        		System.out.println("文件下载失败！");
	        	}
	        	
	        	return res;
	        } catch (IOException e) {
	            e.printStackTrace();
	            return false;
	        }finally{
	        	close();
	        }
//	        return true;
	    }

		
	    /**
	     * 下载文件的第二种方法，优化了传输速度
	     * @param ip
	     * @param port
	     * @param name
	     * @param pwd
	     * @param remotepath 远程地址文件路径
	     * @param localpath  本地文件路径
	     * @return
	     */
		public static boolean getFileTwo(String ip,int port, String name, String pwd,String remotepath,String localpath) {

			try {
				if(!login(ip, port, name, pwd)){
					return false;
				}

				is = ftp.retrieveFileStream(remotepath);
				fos = new FileOutputStream(new File(localpath));

				byte[] b = new byte[1024];
				int len = 0;
				while ((len = is.read(b)) != -1) {
					fos.write(b,0,len);
				}

			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}finally {
				close();
			}
			return true;
		}

		/**
		 * 关闭资源的方法
		 */
		private static void close(){
			
			if(ftp.isConnected()){
				try {
					ftp.disconnect();
				} catch (IOException e) {
				}
			}
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {
				}
			}
			if(os != null){
				try {
					os.close();
				} catch (IOException e) {
				}
			}
			if(fis != null){
				try {
					fis.close();
				} catch (IOException e) {
				}
			}
			if(fos != null){
				try {
					fos.close();
				} catch (IOException e) {
				}
			}
		}

}
