package ua.nure.skibnev.SummaryTask4.web.command;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ua.nure.skibnev.SummaryTask4.Path;
import ua.nure.skibnev.SummaryTask4.exception.AppException;
import ua.nure.skibnev.SummaryTask4.web.Controller;

public class ErrorPage extends Command {
	
	private static final long serialVersionUID = 2423353715955164772L;

	private static final Logger LOG = Logger.getLogger(ErrorPage.class);
	
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException {
		LOG.error("in error page");
		String message=(String)request.getSession().getAttribute("errorMessage");
		request.setAttribute("errorMessage", message);
		return Path.PAGE_ERROR_PAGE;
	}

}
