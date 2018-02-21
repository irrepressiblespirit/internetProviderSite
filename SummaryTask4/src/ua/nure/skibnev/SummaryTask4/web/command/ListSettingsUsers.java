package ua.nure.skibnev.SummaryTask4.web.command;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ua.nure.skibnev.SummaryTask4.Path;
import ua.nure.skibnev.SummaryTask4.db.entity.FullUser;
import ua.nure.skibnev.SummaryTask4.db.entity.User;
import ua.nure.skibnev.SummaryTask4.exception.AppException;

public class ListSettingsUsers extends Command {
	private static final long serialVersionUID = 2423353715955164144L;

	private static final Logger LOG = Logger.getLogger(ListSettingsUsers.class);
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException {
		
		LOG.debug("return to settings_users.jsp page !!!");
		FullUser usr=(FullUser)request.getSession().getAttribute("user");
		request.setAttribute("user", usr);
		return Path.ADMIN_START_PAGE;
	}

}
