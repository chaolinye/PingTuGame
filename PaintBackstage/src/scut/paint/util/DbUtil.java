package scut.paint.util;

import java.sql.Connection;
import java.sql.DriverManager;

import com.sina.sae.util.SaeUserInfo;

/**
 * 数据库工具类
 * 
 * @author chaolin
 *
 */
public class DbUtil {
	private String dbUrl = "jdbc:mysql://w.rdc.sae.sina.com.cn:3307/app_paintbackstage?useUnicode=true&amp;characterEncoding=UTF-8";
	private String dbUserName = SaeUserInfo.getAccessKey();
	private String dbPassword = SaeUserInfo.getSecretKey();
	private String jdbcName = "com.mysql.jdbc.Driver";

	/*
	 * private String dbUrl =
	 * "jdbc:mysql://localhost:3306/paint_db?useUnicode=true&amp;characterEncoding=UTF-8";
	 * private String dbUserName = "root"; private String dbPassword = "123456";
	 * private String jdbcName = "com.mysql.jdbc.Driver";
	 */
	/**
	 * 获取数据库连接
	 * 
	 * @return
	 * @throws Exception
	 */
	public Connection getCon() throws Exception {
		Class.forName(jdbcName);
		Connection con = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
		return con;
	}

	/**
	 * 关闭数据库连接
	 * 
	 * @param con
	 * @throws Exception
	 */
	public void closeCon(Connection con) throws Exception {
		if (con != null) {
			con.close();
		}
	}

	public static void main(String[] args) {
		try {

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
