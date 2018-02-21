package ua.nure.skibnev.SummaryTask4.web.command;

import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import ua.nure.skibnev.SummaryTask4.exception.AppException;
import ua.nure.skibnev.SummaryTask4.exception.DBException;

public class Payment extends Command{
	
	private static final long serialVersionUID = 2423353715955164009L;

	private static final Logger LOG = Logger.getLogger(Payment.class);
	
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException {
		LOG.debug("in payment class");
		
		if(request.getSession().getAttribute("usr")==null){
			throw new AppException("Unknow user");
		}
		
		String pay=request.getParameter("payment");
		String btn=request.getParameter("pay");
		String test=request.getParameter("test");
		FullUser fuser=(FullUser)request.getSession().getAttribute("usr");
		LOG.debug("sum: "+pay);
		LOG.debug("user login "+fuser.getLogin());
		
		if(btn!=null){
			
		if(pay.equals("")){
			throw new AppException("Empty field");
		}
		Pattern pr=Pattern.compile("[0-9]+");
		Matcher mtr=pr.matcher(pay);
		if(mtr.matches()){
			int newCount=fuser.getCount()+Integer.parseInt(pay);
			DBManager.getInstance().updateUserCount(newCount, fuser.getLogin());
		}else{
			throw new AppException("Wrong balance");
		}
		}
		if(test!=null){
			User us=new User();
			us.setLogin(fuser.getLogin());
			StringBuilder sb=new StringBuilder();
			String[] res=fuser.getRates().split(" ");
			for(int i=0;i<res.length;i++){
				if(!res[i].equals("price:")){
					sb.append(res[i]+" ");
				}
			}
			String[] arr=sb.toString().split(" ");
			sb.delete(0,sb.length());
			int rs=fuser.getCount()-Integer.parseInt(arr[1]);
			if(rs<0){
				DBManager.getInstance().setUserStatus(us, Statuses.BLOCKED.ordinal());
				LOG.debug("user "+fuser.getLogin()+" is blocked");
				DBManager.getInstance().updateUserCount(rs, fuser.getLogin());
				Timer timer=new Timer();
				timer.schedule(new TimerTask(){

					@Override
					public void run() {
						try {
							User ur=DBManager.getInstance().findUserByLogin(fuser.getLogin());
							int cnt=ur.getCount()-Integer.parseInt(arr[1]);
							if(cnt>=0 | ur.getCount()==0){
								DBManager.getInstance().updateUserCount(cnt, ur.getLogin());
								DBManager.getInstance().setUserStatus(ur, Statuses.UNBLOCKED.ordinal());
								LOG.debug("user "+fuser.getLogin()+" is unblocked");
								timer.cancel();
							}
						} catch (DBException e) {
							e.printStackTrace();
						}
							
					}}, 0, 5000);
			}else{
				DBManager.getInstance().updateUserCount(rs, fuser.getLogin());
				LOG.debug("user pay and his balance "+rs);
			}
		}
		return Path.COMMAND_CLIENT_PAGE;
	}

}
