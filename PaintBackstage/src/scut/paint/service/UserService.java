package scut.paint.service;

import java.util.List;

import scut.paint.entity.User;

/**
 * 用户相关服务
 * @author chaolin
 *
 */
public interface UserService {
	
	public boolean addUser(String username,String password);
	
	public User findUserByUsernameAndPassword(String username,String password);
	
	public boolean updateGrade(Integer grade,Integer id);
	
	public List<User> listUser(Integer num);
	
	public boolean setHead(String head,Integer id);
	
	public String getHead(Integer id);
	
	public boolean setPetName(String petname,Integer id);
	
	public boolean setSex(Boolean sex,Integer id);
	
	public User findUserById(Integer id);
}
