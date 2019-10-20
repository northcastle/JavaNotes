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
	
		//ftp����
		private static FTPClient ftp;
		
		private static InputStream is = null;
		private static OutputStream os  = null;
		private static FileOutputStream fos = null;
		private static FileInputStream fis = null;
		
		
		/**
		 * ��֤��¼
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
					System.out.println("�û���������֤ʧ�ܣ�");
					return false;
				}
				System.out.println("��½��֤�ɹ���");
				System.out.println("��½��Ĺ���Ŀ¼�� �� "+ftp.printWorkingDirectory());
				
				ftp.enterLocalPassiveMode();//����Ϊ����ģʽ(���ϴ��ļ��гɹ��������ϴ��ļ���ע�����У����򱨴�refused:connect  )
				ftp.setFileType(FTP.BINARY_FILE_TYPE);//�޸��ϴ��ļ���ʽ
				ftp.setCharset(Charset.forName("UTF-8"));
				ftp.setControlEncoding("UTF-8");

			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("��½�쳣��");
				return false;
			}
			return true;
		}
		
		 
		/**
		 * ��ȡftpĳһ�ļ���·�����µ��ļ�����,���ڲ鿴�ļ��б�
		 * @param ip
		 * @param port
		 * @param name
		 * @param pwd
		 * @param remotedir Զ�̵�ַĿ¼
		 * @return
		 */
	    public static boolean getFilesName(String ip,int port, String name, String pwd, String remotedir) {
	        try {
	        	if(!login(ip, port, name, pwd)){
					return false;
				}
	        	
	            //��ȡftp���棬ָ���ļ��� ������ļ����֣�����������
	            FTPFile[] files = ftp.listFiles(remotedir);
	            System.out.println(remotedir+" Ŀ¼�µ��ļ������� �� "+files.length);
	            //��ӡ��ftp���棬ָ���ļ��� ������ļ�����
	            for (int i = 0; i < files.length; i++) {
	                System.out.println(files[i].getName());
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	            return false;
	        }finally{
	        	//�����Լ��Ĺر���Դ�ķ���
	        	close();
	        }
	        return true;
	    }
	    
	    
	    /**
	     * �ϴ��ļ� ����һ
	     * @param ip
	     * @param port
	     * @param name
	     * @param pwd
	     * @param remotepath Զ�̵�ַ�ļ�Ŀ¼
	     * @param remotename Զ���ļ���
	     * @param localpath �����ļ�·��
	     * @return
	     */
	    public static boolean putFileOne(String ip,int port, String name, String pwd,String remotepath,String remotename ,String localpath) {
	    	System.out.println("FTP��ַ��Ϣ��-----ip:"+ip+"----port:"+port+"---name:"+name+"---pwd:"+pwd+"---remotepath:"+remotepath+"---remotename:"+remotename+"---localpath:"+localpath);
	        try {
	        	if(!login(ip, port, name, pwd)){
					return false;
				}
	            //�����ص� localpath �ļ��ϴ���ftp�ĸ�Ŀ¼�ļ������棬��������Ϊ remotepath�е�����
	        	ftp.changeWorkingDirectory(remotepath);//�����ϴ�·�� -- FTP�������ļ�Ŀ¼
	        	boolean res = ftp.storeFile(remotename, new FileInputStream(new File(localpath)));
	        	if(res) {
	        		System.out.println("�ļ��ϴ��ɹ�!");
	        	}else {
	        		System.out.println("�ļ��ϴ�ʧ�ܣ�");
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
	     * �ϴ��ļ��ĵڶ��ַ������Ż��˴����ٶ�
	     * @param ip
	     * @param port
	     * @param name
	     * @param pwd
	     * @param remotepath Զ�̵�ַ�ļ�·��
	     * @param localpath �����ļ�·��
	     * @return
	     */
	    public static boolean putFileTwo(String ip,int port, String name, String pwd,String remotepath,String localpath) {
	    	System.out.println("-----ip:"+ip+"----port:"+port+"---name:"+name+"---pwd:"+pwd+"---remotepath:"+remotepath+"---localpath:"+localpath);
	        try {
	        	if(!login(ip, port, name, pwd)){
					return false;
				}
	        	
//	        	 //�O���ς���·��
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
	     * �����ļ� ����һ
	     * @param ip
	     * @param port
	     * @param name
	     * @param pwd
	     * @param remotepath Զ�̵�ַ�ļ�·��
	     * @param localpath �����ļ�·��
	     * @return
	     */
	    public static boolean getFileOne(String ip,int port, String name, String pwd,String remotepath,String localpath) {
	        try {
	        	if(!login(ip, port, name, pwd)){
					return false;
				}
	        	
	            //��ftp��Դ�� remotepath �ļ����ص�����Ŀ¼�ļ������棬��������Ϊ localpath �е�����
	        	boolean res = ftp.retrieveFile(remotepath, new FileOutputStream(new File(localpath)));
	        	if(res) {
	        		System.out.println("�ļ����سɹ���");
	        	}else {
	        		System.out.println("�ļ�����ʧ�ܣ�");
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
	     * �����ļ��ĵڶ��ַ������Ż��˴����ٶ�
	     * @param ip
	     * @param port
	     * @param name
	     * @param pwd
	     * @param remotepath Զ�̵�ַ�ļ�·��
	     * @param localpath  �����ļ�·��
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
		 * �ر���Դ�ķ���
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
