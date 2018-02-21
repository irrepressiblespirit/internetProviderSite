package ua.nure.skibnev.SummaryTask4.web.command;

import java.io.IOException;

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

public class ClientPage extends Command {
	private static final long serialVersionUID = 2423353715955164666L;

	private static final Logger LOG = Logger.getLogger(ClientPage.class);
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException {
		
		FullUser fr=(FullUser)request.getSession().getAttribute("usr");
		User user=DBManager.getInstance().findUserByLogin(fr.getLogin());
		UserFullInformation fus=DBManager.getInstance().getFullUserFromUser(user);
		FullUser us=DBManager.getInstance().getFullUserStatus(user, fus);
		FullUser use=DBManager.getInstance().getFullUserRate(user, us);
		FullUser fusr=DBManager.getInstance().getFullUserRole(user, use);
		
		request.getSession().setAttribute("usr", fusr);
		
		return Path.CLIENT_START_PAGE;
	}

}
