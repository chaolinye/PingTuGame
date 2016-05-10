package scut.paint.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import scut.paint.entity.User;
import scut.paint.service.UserService;
import scut.paint.service.impl.UserServiceImpl;
import scut.paint.util.MyFileUtil;
import scut.paint.util.ResponseUtil;

/**
 * 控制器，处理请求
 * 
 * @author chaolin
 *
 */
public class UserController extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	UserService userService = new UserServiceImpl();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		req.setCharacterEncoding("UTF-8");
		if (req.getParameter("action") == null)
			return;
		switch (req.getParameter("action")) {
		case "login":
			this.login(req, resp);
			break;
		case "register":
			this.register(req, resp);
			break;
		case "grade":
			this.grade(req, resp);
			break;
		case "rank":
			this.rank(req, resp);
			break;
		case "uploadHead":
			this.uploadHead(req, resp);
			break;
		case "head":
			this.head(req, resp);
			break;
		case "setPetname":
			this.setPetname(req, resp);
			break;
		case "setSex":
			this.setSex(req, resp);
			break;
		case "getInfo":
			this.getInfo(req, resp);
			break;
		}

	}

	/**
	 * 登录
	 * 
	 * @param request
	 * @param response
	 */
	private void login(HttpServletRequest request, HttpServletResponse response) {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		if (username == null || password == null)
			return;

		User user = userService.findUserByUsernameAndPassword(username, password);
		if (user == null) {
			ResponseUtil.writeObject(response, Integer.valueOf(-1));
		} else {
			ResponseUtil.writeObject(response, user.getId());
		}

	}

	/**
	 * 注册
	 * 
	 * @param request
	 * @param response
	 */
	private void register(HttpServletRequest request, HttpServletResponse response) {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		if (userService.addUser(username, password)) {
			this.login(request, response);
		} else {
			ResponseUtil.writeObject(response, Integer.valueOf(-1));
		}
	}

	/**
	 * 更新分数
	 * 
	 * @param request
	 * @param response
	 */
	private void grade(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		String grade = request.getParameter("grade");
		Boolean result = userService.updateGrade(Integer.parseInt(grade), Integer.parseInt(id));
		ResponseUtil.writeObject(response, result);
	}

	/**
	 * 排行榜
	 * 
	 * @param request
	 * @param response
	 */
	private void rank(HttpServletRequest request, HttpServletResponse response) {
		Integer num = 20;
		ResponseUtil.writeUserJSONResponse(response, userService.listUser(num));
	}

	/**
	 * 上传头像
	 * 
	 * @param request
	 * @param response
	 */
	private void uploadHead(HttpServletRequest request, HttpServletResponse response) {
		String tempDirectory = request.getServletContext().getRealPath("/") + "temp/"; // 临时目录
		System.out.println("temp=" + tempDirectory);
		String realDirectory = request.getServletContext().getRealPath("/") + "head/"; // 上传文件存放目录
		System.out.println("loadpath=" + realDirectory);
		MyFileUtil.mkdir(new File(tempDirectory));
		MyFileUtil.mkdir(new File(realDirectory));;

		int id = Integer.parseInt(request.getParameter("id"));

		FileItemFactory factory = new DiskFileItemFactory(10, new File(tempDirectory));
		ServletFileUpload upload = new ServletFileUpload(factory);

		upload.setSizeMax(500 * 1024 * 1024);// 设置该次上传最大值为500M

		try {
			List<FileItem> list = upload.parseRequest(request);
			Iterator<FileItem> iter = list.iterator();
			while (iter.hasNext()) {
				FileItem item = iter.next();
				// item.isFormField()用来判断当前对象是否是file表单域的数据 如果返回值是true说明不是
				// 就是普通表单域
				if (!item.isFormField()) {
					/*
					 * 只有file表单域才将该对象中的内容写到真实文件夹中
					 */
					String lastpath = item.getName();// 获取上传文件的名称
					lastpath = lastpath.substring(lastpath.lastIndexOf("."));
					String filename = id + lastpath;
					System.out.println("filename: "+filename);
					item.write(new File(realDirectory + filename));
					Boolean result = userService.setHead(realDirectory + filename, id);
					ResponseUtil.writeObject(response, result);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			ResponseUtil.writeObject(response, Boolean.FALSE);
		}
	}

	/**
	 * 获取头像
	 * 
	 * @param request
	 * @param response
	 */
	public void head(HttpServletRequest request, HttpServletResponse response) {
		int id = Integer.parseInt(request.getParameter("id"));
		String head = userService.getHead(id);
		if (head != null) {
			response.setContentType("image/jpeg");
			try (OutputStream outputStream = response.getOutputStream();
					InputStream inputStream = new FileInputStream(head)) {
				byte[] buffer = new byte[1024];
				int i = -1;
				while ((i = inputStream.read(buffer)) != -1) {
					outputStream.write(buffer, 0, i);
				}
				outputStream.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void setPetname(HttpServletRequest request, HttpServletResponse response) {
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			String petname = request.getParameter("petname");
			Boolean result = userService.setPetName(petname, id);
			ResponseUtil.writeObject(response, result);
		} catch (Exception e) {
			ResponseUtil.writeObject(response, Boolean.FALSE);
		}
	}

	public void setSex(HttpServletRequest request, HttpServletResponse response) {
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			boolean sex = request.getParameter("sex").equalsIgnoreCase("female") ? false : true;
			Boolean result = userService.setSex(sex, id);
			ResponseUtil.writeObject(response, result);
		} catch (Exception e) {
			ResponseUtil.writeObject(response, Boolean.FALSE);
		}
	}

	public void getInfo(HttpServletRequest request, HttpServletResponse response) {
		int id = Integer.parseInt(request.getParameter("id"));
		User user = userService.findUserById(id);
		List<User> list = new ArrayList<User>();
		list.add(user);
		ResponseUtil.writeUserJSONResponse(response, list);
	}

}
