package ua.nure.skibnev.SummaryTask4.web.command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ua.nure.skibnev.SummaryTask4.Path;
import ua.nure.skibnev.SummaryTask4.db.DBManager;
import ua.nure.skibnev.SummaryTask4.db.Role;
import ua.nure.skibnev.SummaryTask4.db.entity.FullUser;
import ua.nure.skibnev.SummaryTask4.db.entity.Statuses;
import ua.nure.skibnev.SummaryTask4.db.entity.Tariff;
import ua.nure.skibnev.SummaryTask4.db.entity.User;
import ua.nure.skibnev.SummaryTask4.db.entity.UserFullInformation;
import ua.nure.skibnev.SummaryTask4.exception.AppException;
import ua.nure.skibnev.SummaryTask4.web.Controller;

public class SettingsUsers extends Command {

	private static final long serialVersionUID = 2423353715955164331L;

	private static final Logger LOG = Logger.getLogger(SettingsUsers.class);
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException {
		
		LOG.debug("in settingsUsers");
		
		if(request.getSession().getAttribute("usr")==null){
			throw new AppException("Unknow user");
		}
		
		String reg=request.getParameter("registr");
		String del=request.getParameter("delete");
		
		String forward=Path.PAGE_ERROR_PAGE;
		
		if(reg!=null & del==null){
			LOG.debug("Button registr is click !!!");
			boolean error=false;
			StringBuffer sb=new StringBuffer();
			ServletContext sc=request.getServletContext();
			User user=new User();
			String log=request.getParameter("log");
			if(!log.equals("")){
				user.setLogin(log);
			}else{
				throw new AppException("Wrong login");
			}
			LOG.debug("login ==>"+user.getLogin());
			String password=request.getParameter("pass");
			if(!password.equals("")){
				user.setPassword(password);
			}else{
				throw new AppException("Wrong password");
			}
			LOG.debug("password ==>"+user.getPassword());
			UserFullInformation fullinf=new UserFullInformation();
			String firstName=request.getParameter("firstName");
			if(firstName.equals("")){
				throw new AppException("Wrong first name");
			}
			Pattern pat=Pattern.compile("[a-zA-Z[à-ÿÀ-ß]]+");
			Matcher mat=pat.matcher(firstName);
			if(mat.matches() & firstName!=null){
				fullinf.setFirstName(firstName);
			}else{
				throw new AppException("Wrong first name");
			}
			LOG.debug("first name ==>"+fullinf.getFirstName());
			String lastName=request.getParameter("lastName");
			if(lastName.equals("")){
				throw new AppException("Wrong last name");
			}
			Matcher mt=pat.matcher(lastName);
			if(mt.matches() & lastName!=null){
				fullinf.setLastName(lastName);
			}else{
				throw new AppException("Wrong last name");
			}
			LOG.debug("last name ==>"+fullinf.getLastName());
			String telephone=request.getParameter("tel");
			if(!telephone.equals("")){
				Pattern pt=Pattern.compile("[0-9]{10}");
				Matcher matc=pt.matcher(telephone);
				if(matc.matches()){
					fullinf.setTelephone(telephone);
				}else{
					throw new AppException("Wrong number of telephone");
				}
			}else{
				throw new AppException("Wrong number of telephone");
			}
			LOG.debug("telephone ==>"+fullinf.getTelephone());
			String address=request.getParameter("address");
			if(!address.equals("")){
				fullinf.setAddress(address);
			}else{
				throw new AppException("Wrong address");
			}
			LOG.debug("address==>"+fullinf.getAddress());
			String email=request.getParameter("email");
			if(email.equals("")){
				throw new AppException("Wrong e-mail");
			}
			Pattern ptn=Pattern.compile("[a-zA-Z]+@[a-z]+\\.[a-z]+");
			Matcher mtc=ptn.matcher(email);
			if(mtc.matches()){
				fullinf.setEmail(email);
			}else{
				throw new AppException("Wrong e-mail");
			}
			LOG.debug("email ==>"+fullinf.getEmail());
			DBManager.getInstance().addUserFullInf(fullinf);
			int id=DBManager.getInstance().getIdFromFullInf(fullinf);
			LOG.debug("id in fullInformation table ==>"+id);
			user.setFullInformationId(id);
			String count=request.getParameter("balance");
			if(count.equals("")){
				throw new AppException("Wrong balance");
			}
			Pattern pr=Pattern.compile("[0-9]+");
			Matcher mtr=pr.matcher(count);
			if(mtr.matches()){
				user.setCount(Integer.parseInt(count));
			}else{
				throw new AppException("Wrong balance");
			}
			LOG.debug("Balance ==>"+user.getCount());
			java.util.List<String> statuse=(java.util.List<String>)sc.getAttribute("statuses");
			String stat=request.getParameter("stat");
			for(String st:statuse){
				if(st.equals(stat)){
					user.setStatusesId(Statuses.UNBLOCKED.ordinal());
				}else{
					user.setStatusesId(Statuses.BLOCKED.ordinal());
				}
			}
			ArrayList<String> list=new ArrayList<>();
			java.util.List<String> rates=(java.util.List<String>)sc.getAttribute("rates");
			String rt=request.getParameter("rates");
			list.addAll(rates);
			for(String one:list){
				if(one.equals(rt)){
					Tariff trf=new Tariff();
					trf.setName(one);
					user.setRatesId(DBManager.getInstance().getRateId(trf));
					break;
				}
			}
			
			java.util.List<String> role=(java.util.List<String>)sc.getAttribute("role");
			String rl=request.getParameter("role");
			for(String rol:role){
				if(rol.equals(rl)){
					user.setRoleId(Role.SUBSCRIBER.ordinal());
				}else{
					user.setRoleId(Role.ADMIN.ordinal());
				}
			}
			
			DBManager.getInstance().addUser(user);
			FullUser us=DBManager.getInstance().getFullUserStatus(user, fullinf);
			FullUser usr=DBManager.getInstance().getFullUserRate(user, us);
			FullUser fusr=DBManager.getInstance().getFullUserRole(user, usr);
			
			request.getSession().setAttribute("user", fusr);
			
			forward=Path.COMMAND_LIST_SETTINGS_USERS;
			
		}
		if(del!=null & reg==null){
			LOG.debug("Button delete is click !!!");
			forward=Path.COMMAND_LIST_USERS;
		}
		
		return forward;
	}
     
}
