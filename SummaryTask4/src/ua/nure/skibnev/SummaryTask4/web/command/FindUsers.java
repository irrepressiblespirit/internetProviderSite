package ua.nure.skibnev.SummaryTask4.web.command;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ua.nure.skibnev.SummaryTask4.Path;
import ua.nure.skibnev.SummaryTask4.db.entity.FullUser;
import ua.nure.skibnev.SummaryTask4.exception.AppException;

public class FindUsers extends Command {
	private static final long serialVersionUID = 2423353715955164601L;

	private static final Logger LOG = Logger.getLogger(FindUsers.class);
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException {
		
		List<FullUser> list=new LinkedList<>();
		Object obj=request.getSession().getAttribute("users");
		Object ob=request.getSession().getAttribute("userCount");
		if(obj instanceof FullUser){
			request.setAttribute("users", (FullUser)obj);
		}else{
			list.addAll((List<FullUser>)obj);
			request.setAttribute("users", list);
		}
		request.setAttribute("userCount",(int)ob);
	
		return Path.PAGE_SETTINGS_USERS;
	}

}
