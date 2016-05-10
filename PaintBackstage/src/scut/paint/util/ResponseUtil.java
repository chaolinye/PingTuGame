package scut.paint.util;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import scut.paint.entity.User;

public class ResponseUtil {
	public static void writeUserJSONResponse(HttpServletResponse response, List<User> list) {
		if (list == null)
			return;
		JSONArray json = new JSONArray();
		for (User user : list) {
			JSONObject jo = new JSONObject();
			jo.put("id", user.getId());
			jo.put("username", user.getUsername());
			jo.put("password", user.getPassword());
			jo.put("grade", user.getGrade());
			jo.put("petname", user.getPetname());
			jo.put("sex", user.getSex()?"male":"female");
			json.add(jo);
		}
		System.out.println(json.toString());
		try {
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(json.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void writeObject(HttpServletResponse response, Object obj) {
		if(obj==null) return;
		try {
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(obj.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
