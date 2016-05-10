package scut.paint.dao;

import java.util.List;

import scut.paint.entity.User;

/**
 *用户持久层操作
 * @author chaolin
 *
 */
public interface UserDao {

	public boolean save(User user);
	
	public List<User> find(String sql);
	
	public List<User> find(String sql,Object[] param);
	
	public Integer execute(String sql);
	
	public Integer execute(String sql,Object[] param);
	
}
