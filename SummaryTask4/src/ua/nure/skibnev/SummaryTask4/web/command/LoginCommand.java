package ua.nure.skibnev.SummaryTask4.web.command;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.nure.skibnev.SummaryTask4.exception.DBException;
import ua.nure.skibnev.SummaryTask4.loc.LocEnum;
import ua.nure.skibnev.SummaryTask4.Path;
import ua.nure.skibnev.SummaryTask4.db.Role;
import ua.nure.skibnev.SummaryTask4.db.entity.FullUser;
import ua.nure.skibnev.SummaryTask4.db.entity.Statuses;
import ua.nure.skibnev.SummaryTask4.db.entity.User;
import ua.nure.skibnev.SummaryTask4.db.entity.UserFullInformation;
import ua.nure.skibnev.SummaryTask4.db.DBManager;
import ua.nure.skibnev.SummaryTask4.exception.AppException;

/**
 * Login command.
 * 
 * 
 */
public class LoginCommand extends Command {

	private static final long serialVersionUID = -3071536593627692473L;

	private static final Logger LOG = Logger.getLogger(LoginCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) 
			throws IOException, ServletException, ua.nure.skibnev.SummaryTask4.exception.AppException {
		LOG.debug("Command starts");
		
		HttpSession session = request.getSession();
		
		String choosedLocale = String.valueOf(session.getAttribute("locale"));
		// obtain login and password from a request
		DBManager manager = DBManager.getInstance();
		String login =String.valueOf(session.getAttribute("login"));
		LOG.trace("Request parameter: loging --> " + login);

		String password = String.valueOf(session.getAttribute("password"));
		if (login == null || password == null || login.isEmpty() || password.isEmpty()) {
			throw new AppException("Login/password cannot be empty");
		}

		User user = manager.findUserByLogin(login);
		LOG.trace("Found in DB: user --> " + user);

		if (user == null || !password.equals(user.getPassword())) {
			throw new AppException("Cannot find user with such login/password");
		}
		
        if(user.getStatusesId()==Statuses.BLOCKED.ordinal()){
        	throw new AppException("Your blocked in this system. Please contact with Your administrator");
        }
        
		Role userRole = Role.getRole(user);
		LOG.trace("userRole --> " + userRole);
		
		String forward = Path.PAGE_ERROR_PAGE;

		if (userRole == Role.ADMIN) {
			forward = Path.ADMIN_START_PAGE;
		}

		if (userRole == Role.SUBSCRIBER) {
			forward = Path.CLIENT_START_PAGE;
		}
		
		UserFullInformation fus=DBManager.getInstance().getFullUserFromUser(user);
		FullUser us=DBManager.getInstance().getFullUserStatus(user, fus);
		FullUser use=DBManager.getInstance().getFullUserRate(user, us);
		FullUser fusr=DBManager.getInstance().getFullUserRole(user, use);
		
		session.setAttribute("usr", fusr);
		LOG.trace("Set the session attribute: user --> " + user);
		
		LocEnum loc=LocEnum.valueOf(choosedLocale.toUpperCase());
		String locale=loc.getLocale();
		session.setAttribute("lang", locale);
		
		LOG.trace("Set the session attribute: lang --> " + locale);

		session.setAttribute("userRole", userRole);
		LOG.trace("Set the session attribute: userRole --> " + userRole);

		LOG.info("User " + user + " logged as " + userRole.toString().toLowerCase());

		LOG.debug("Command finished");
		
		return forward;

	}

}