package ua.nure.skibnev.SummaryTask4.web.command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ua.nure.skibnev.SummaryTask4.Path;
import ua.nure.skibnev.SummaryTask4.db.DBManager;
import ua.nure.skibnev.SummaryTask4.db.entity.FullUser;
import ua.nure.skibnev.SummaryTask4.db.entity.Statuses;
import ua.nure.skibnev.SummaryTask4.db.entity.User;
import ua.nure.skibnev.SummaryTask4.db.entity.UserFullInformation;
import ua.nure.skibnev.SummaryTask4.exception.AppException;

public class BlockUnblockUsers extends Command {
	private static final long serialVersionUID = 2423353715955164999L;

	private static final Logger LOG = Logger.getLogger(BlockUnblockUsers.class);
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException {
		
		LOG.debug("In  BlockUnblockUsers class");
		
		if(request.getSession().getAttribute("usr")==null){
			throw new AppException("Unknow user");
		}
		
		List<FullUser> lst=new LinkedList<>();
		ServletContext sc=request.getServletContext();
		java.util.List<String> find_user=(java.util.List<String>)sc.getAttribute("find_user");
		String choise=request.getParameter("choise");
		String data=request.getParameter("data");
		String find=request.getParameter("fnd");
		
		String forward="";
		
		if(choise.equals("log") & find!=null){
			if(data.equals("")){
				throw new AppException("Empty field");
			}
			LOG.debug("choise the log");
					User user=DBManager.getInstance().findUserByLogin(data);
					LOG.debug("choise the user by id ==>"+user.toString());
					UserFullInformation fus=DBManager.getInstance().getFullUserFromUser(user);
					FullUser us=DBManager.getInstance().getFullUserStatus(user, fus);
					FullUser use=DBManager.getInstance().getFullUserRate(user, us);
					FullUser fusr=DBManager.getInstance().getFullUserRole(user, use);
					request.getSession().setAttribute("users", fusr);
					request.getSession().setAttribute("userCount", 1);
					forward="controller?command=findUsers";
					
		}
		if(choise.equals("lastName") & find!=null){
			if(data.equals("")){
				throw new AppException("Empty field");
			}
			LOG.debug("choise the last name");
			List<User> users=DBManager.getInstance().findUserByLastName(data);
			for(User user:users){
			UserFullInformation fus=DBManager.getInstance().getFullUserFromUser(user);
			FullUser us=DBManager.getInstance().getFullUserStatus(user, fus);
			FullUser use=DBManager.getInstance().getFullUserRate(user, us);
			FullUser fusr=DBManager.getInstance().getFullUserRole(user, use);
			lst.add(fusr);
			}
			if(lst.size()==1){
				request.getSession().setAttribute("users", lst.get(0));
				request.getSession().setAttribute("userCount", 1);
				forward=Path.COMMAND_FIND_USERS;
			}else{
				request.getSession().setAttribute("users", lst);
				request.getSession().setAttribute("userCount", 0);
				forward=Path.COMMAND_FIND_USERS;
			}
		}
		if(find==null){
			String res=request.getParameter("blk");
			List<String> logs=DBManager.getInstance().getUsersLogins();
			String findLogin="non";
			for(String lgn:logs){
				if(res.equals(lgn)){
					findLogin=lgn;
				}
			}
			LOG.debug("Value of button ==>"+findLogin);
			User user=DBManager.getInstance().findUserByLogin(findLogin);
			if(user.getStatusesId()==Statuses.UNBLOCKED.ordinal()){
				DBManager.getInstance().setUserStatus(user, Statuses.BLOCKED.ordinal());
			}else{
				DBManager.getInstance().setUserStatus(user, Statuses.UNBLOCKED.ordinal());
			}
			
			forward=Path.COMMAND_LIST_USERS;
		}
		return forward;
	}

}
