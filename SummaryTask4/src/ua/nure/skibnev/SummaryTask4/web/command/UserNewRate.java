package ua.nure.skibnev.SummaryTask4.web.command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ua.nure.skibnev.SummaryTask4.Path;
import ua.nure.skibnev.SummaryTask4.db.DBManager;
import ua.nure.skibnev.SummaryTask4.db.entity.FullUser;
import ua.nure.skibnev.SummaryTask4.db.entity.User;
import ua.nure.skibnev.SummaryTask4.exception.AppException;

public class UserNewRate extends Command {
	private static final long serialVersionUID = 2423353715955164552L;

	private static final Logger LOG = Logger.getLogger(UserNewRate.class);
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException {
		
		if(request.getSession().getAttribute("usr")==null){
			throw new AppException("Unknow user");
		}
		String sort_1=request.getParameter("sortUp");
		String sort_2=request.getParameter("sortDown");
		String forward="";
		if(sort_1!=null){
			forward="controller?command=sortPrice";
		}
		if(sort_2!=null){
			forward="controller?command=sortPriceDown";
		}
		if(sort_1==null & sort_2==null){
			forward=Path.COMMAND_USER_RATE;
		}
		FullUser fr=(FullUser)request.getSession().getAttribute("usr");
		LOG.debug("user "+fr);
		List<User>lst=DBManager.getInstance().joinByPrice();
		List<User> ar=new ArrayList<>();
		ar.addAll(lst);
		for(User one:ar){
		LOG.debug("login "+one.getLogin()+" tariff price"+one.getRatesId());
		}
		String select=request.getParameter("select");
		if(select!=null){
			List<String> names=DBManager.getInstance().selectRatesName();
			String find="non";
			for(String nm:names){
				if(select.equals(nm)){
					find=nm;
				}
			}
			LOG.debug("tariff name "+find);
			request.getServletContext().setAttribute("newRate", find);
			request.getServletContext().setAttribute("usernewRate", fr.getLogin());
		}
		return forward;
	}

}
