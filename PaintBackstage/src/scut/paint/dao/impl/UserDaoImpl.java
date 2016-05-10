package scut.paint.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import scut.paint.dao.UserDao;
import scut.paint.entity.User;
import scut.paint.util.DbUtil;

/**
 * 用户持久层操作的实现
 * 
 * @author chaolin
 *
 */
public class UserDaoImpl implements UserDao {

	private DbUtil dbUtil = new DbUtil();
	private Connection con;

	@Override
	public boolean save(User user) {
		// TODO Auto-generated method stub
		String sql = "insert into t_user(username,password) values(?,md5(?))";
		try {
			con = dbUtil.getCon();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, user.getUsername());
			ps.setString(2, user.getPassword());
			ps.executeUpdate();
			return true;
		} catch (Exception e) {
			System.out.println("保存user失败:" + e.getMessage());
			return false;
		} finally {
			try {
				con.close();
			} catch (Exception e) {
				System.out.println("关闭con失败: " + e.getMessage());
			}
		}
	}

	@Override
	public List<User> find(String sql) {
		// TODO Auto-generated method stub
		return this.find(sql, null);
	}

	@Override
	public List<User> find(String sql, Object[] param) {
		// TODO Auto-generated method stub
		try {
			con = dbUtil.getCon();
			PreparedStatement ps = con.prepareStatement(sql);
			if (param != null) {
				for (int i = 0; i < param.length; i++) {
					ps.setObject(i + 1, param[i]);
				}
			}
			ResultSet rs = ps.executeQuery();
			List<User> list = new ArrayList<>();
			while (rs.next()) {
				User user = new User();
				user.setId(rs.getInt("uid"));
				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				user.setGrade(rs.getInt("grade"));
				user.setSex(rs.getBoolean("sex"));
				user.setPetname(rs.getString("petname"));
				user.setHead(rs.getString("head"));
				list.add(user);
			}
			return list;
		} catch (Exception e) {
			System.out.println("查询user出错：" + e.getMessage());
			return null;
		} finally {
			try {
				con.close();
			} catch (Exception e) {
				System.out.println("关闭con失败: " + e.getMessage());
			}
		}
	}

	@Override
	public Integer execute(String sql) {
		// TODO Auto-generated method stub
		return this.execute(sql, null);
	}

	@Override
	public Integer execute(String sql, Object[] param) {
		// TODO Auto-generated method stub
		try {
			con = dbUtil.getCon();
			PreparedStatement ps = con.prepareStatement(sql);
			if (param != null) {
				for (int i = 0; i < param.length; i++) {
					ps.setObject(i + 1, param[i]);
				}
			}
			return ps.executeUpdate();
		} catch (Exception e) {
			System.out.println("修改user出错：" + e.getMessage());
			return null;
		} finally {
			try {
				con.close();
			} catch (Exception e) {
				System.out.println("关闭con失败: " + e.getMessage());
			}
		}
	}

}
