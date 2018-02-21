package ua.nure.skibnev.SummaryTask4.web.command;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.nure.skibnev.SummaryTask4.Path;
import ua.nure.skibnev.SummaryTask4.exception.AppException;

public class EnterCommand extends Command {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException {
		String log=request.getParameter("login");
		String pas=request.getParameter("password");
		String Locale = request.getParameter("locale");
		request.getSession().setAttribute("login", log);
		request.getSession().setAttribute("password", pas);
		request.getSession().setAttribute("locale", Locale);
		return Path.COMMAND_LOGIN;
	}
   
}
