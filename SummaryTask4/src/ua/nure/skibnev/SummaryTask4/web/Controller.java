package ua.nure.skibnev.SummaryTask4.web;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ua.nure.skibnev.SummaryTask4.web.command.Command;
import ua.nure.skibnev.SummaryTask4.web.command.CommandContainer;
import ua.nure.skibnev.SummaryTask4.Path;
import ua.nure.skibnev.SummaryTask4.db.DBManager;
import ua.nure.skibnev.SummaryTask4.exception.AppException;
import ua.nure.skibnev.SummaryTask4.exception.DBException;

/**
 * Main servlet controller.
 * 
 * 
 */
public class Controller extends HttpServlet {
	
	private static final long serialVersionUID = 2423353715955164816L;

	private static final Logger LOG = Logger.getLogger(Controller.class);

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		LOG.debug("Method get()");
		process(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		LOG.debug("Method post()");
		String commandName = request.getParameter("command");
		LOG.trace("Request parameter: command --> " + commandName);

		// obtain command object by its name
		Command command = CommandContainer.get(commandName);
		LOG.trace("Obtained command --> " + command);

		// execute command and get forward address
		String forward = ua.nure.skibnev.SummaryTask4.Path.COMMAND_ERROR_PAGE;
		try {
			forward = command.execute(request, response);
			
		} catch (AppException e) {
			request.getSession().setAttribute("errorMessage", e.getMessage());
			LOG.error(e.getMessage());
		}
		LOG.trace("Forward address --> " + forward);

		LOG.debug("Controller finished, now go to forward address --> " + forward);
		
		response.sendRedirect(forward);
	}

	/**
	 * Main method of this controller.
	 */
	private void process(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		
		System.out.println("In method process !!!");
		
		LOG.debug("Controller starts");

		// extract command name from the request
		String commandName = request.getParameter("command");
		LOG.trace("Request parameter: command --> " + commandName);

		// obtain command object by its name
		Command command = CommandContainer.get(commandName);
		LOG.trace("Obtained command --> " + command);

		// execute command and get forward address
		String forward = ua.nure.skibnev.SummaryTask4.Path.PAGE_ERROR_PAGE;
		try {
			forward = command.execute(request, response);
			
		} catch (AppException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			System.out.println("DBException#main"+e.getMessage());
			request.setAttribute("errorMessage", e.getMessage());
			LOG.error(e.getMessage());
		}
		LOG.trace("Forward address --> " + forward);

		LOG.debug("Controller finished, now go to forward address --> " + forward);
		
		// go to forward
		request.getRequestDispatcher(forward).forward(request, response);
	}

}