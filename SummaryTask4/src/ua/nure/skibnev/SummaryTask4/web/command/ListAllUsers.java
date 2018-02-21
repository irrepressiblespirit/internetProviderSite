package ua.nure.skibnev.SummaryTask4.web.command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ua.nure.skibnev.SummaryTask4.Path;
import ua.nure.skibnev.SummaryTask4.db.DBManager;
import ua.nure.skibnev.SummaryTask4.db.entity.FullUser;
import ua.nure.skibnev.SummaryTask4.db.entity.User;
import ua.nure.skibnev.SummaryTask4.db.entity.UserFullInformation;
import ua.nure.skibnev.SummaryTask4.exception.AppException;
import ua.nure.skibnev.SummaryTask4.web.Controller;

public class ListAllUsers extends Command {
	private static final long serialVersionUID = 2423353715955164671L;

	private static final Logger LOG = Logger.getLogger(ListAllUsers.class);
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException {
		
		
		List<FullUser> users=new ArrayList<>();
		List<UserFullInformation> ufi=DBManager.getInstance().selectAllUserFullInf();
		LOG.debug("Collection ufi is consist of: "+ufi.toString());
		List<User> usr=DBManager.getInstance().selectAllFromUser();
		LOG.debug("Collection users is consist of: "+usr.toString());
		for(int i=0;i<usr.size();i++){
			    User objU=usr.get(i);
			    UserFullInformation objF=ufi.get(i);
				FullUser us=DBManager.getInstance().getFullUserStatus(objU, objF);
				FullUser use=DBManager.getInstance().getFullUserRate(objU, us);
				FullUser fusr=DBManager.getInstance().getFullUserRole(objU, use);
				users.add(fusr);
		}
		request.setAttribute("users", users);
		return Path.PAGE_SETTINGS_USERS;
	}

}
