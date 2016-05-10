package scut.paint.service.impl;

import java.util.List;

import scut.paint.dao.UserDao;
import scut.paint.dao.impl.UserDaoImpl;
import scut.paint.entity.User;
import scut.paint.service.UserService;

public class UserServiceImpl implements UserService{

	UserDao userDao=new UserDaoImpl();
	@Override
	public boolean addUser(String username, String password) {
		// TODO Auto-generated method stub
		User user=new User(username,password);
		return userDao.save(user);
	}

	@Override
	public User findUserByUsernameAndPassword(String username,String password) {
		// TODO Auto-generated method stub
		String sql="select * from t_user where username=? and password=md5(?) ";
		List<User> list=userDao.find(sql, new Object[]{username,password});
		if(list==null||list.size()==0) return null;
		return list.get(0);
	}

	@Override
	public boolean updateGrade(Integer grade,Integer id) {
		// TODO Auto-generated method stub
		String sql="update t_user set grade=? where uid=?";
		Integer result=userDao.execute(sql, new Object[]{grade,id});
		if(result==null||result<=0) return false;
		return true;
	}

	@Override
	public List<User> listUser(Integer num) {
		// TODO Auto-generated method stub
		String sql="select * from t_user order by grade desc limit ?";
		return userDao.find(sql, new Object[]{num});
		
	}

	@Override
	public boolean setHead(String head, Integer id) {
		// TODO Auto-generated method stub
		String sql="update t_user set head=? where uid=?";
		return userDao.execute(sql, new Object[]{head,id})>0?true:false;
	}

	@Override
	public String getHead(Integer id) {
		// TODO Auto-generated method stub
		String sql="select * from t_user where uid=?";
		List<User> users=userDao.find(sql, new Object[]{id});
		if(users==null||users.isEmpty()) return null;
		else{
			return users.get(0).getHead();
		}
	}

	@Override
	public boolean setPetName(String petname, Integer id) {
		// TODO Auto-generated method stub
		String sql="update t_user set petname=? where uid=?";
		return userDao.execute(sql, new Object[]{petname,id})>0?true:false;
	}

	@Override
	public boolean setSex(Boolean sex, Integer id) {
		// TODO Auto-generated method stub
		String sql="update t_user set sex=? where uid=?";
		return userDao.execute(sql, new Object[]{sex,id})>0?true:false;
	}

	@Override
	public User findUserById(Integer id) {
		// TODO Auto-generated method stub
		String sql="select * from t_user where uid=? ";
		List<User> list=userDao.find(sql, new Object[]{id});
		if(list==null||list.size()==0) return null;
		return list.get(0);
	}
	
	
	
}
