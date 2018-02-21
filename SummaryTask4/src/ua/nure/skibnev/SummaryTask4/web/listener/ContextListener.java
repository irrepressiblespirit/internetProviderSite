package ua.nure.skibnev.SummaryTask4.web.listener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ua.nure.skibnev.SummaryTask4.db.DBManager;
import ua.nure.skibnev.SummaryTask4.db.entity.FullUser;
import ua.nure.skibnev.SummaryTask4.db.entity.Statuses;
import ua.nure.skibnev.SummaryTask4.db.entity.User;
import ua.nure.skibnev.SummaryTask4.exception.DBException;

/**
 * Context listener.
 * 
 * 
 */
public class ContextListener implements ServletContextListener {

	private static final Logger LOG = Logger.getLogger(ContextListener.class);
	
    private Timer tmr;
    
	public void contextDestroyed(ServletContextEvent event) {
		log("Servlet context destruction starts");
		// no op
		//tmr.cancel();
		log("Servlet context destruction finished");
	}

	public void contextInitialized(ServletContextEvent event) {
		log("Servlet context initialization starts");

		ServletContext servletContext = event.getServletContext();
		initLog4J(servletContext);
		initCommandContainer();
		java.util.List<String> find_user=(java.util.List) Arrays.asList(servletContext.getInitParameter("find_user").split(" "));
		LOG.debug("find user ==> "+find_user);
		servletContext.setAttribute("find_user", find_user);
		java.util.List<String> role=(java.util.List) Arrays.asList(servletContext.getInitParameter("role").split(" "));
		LOG.debug("role ==> "+role);
		servletContext.setAttribute("role", role);
		java.util.List<String> rates=(java.util.List) Arrays.asList(servletContext.getInitParameter("rates").split(" "));
		LOG.debug("rates ==> "+rates);
		servletContext.setAttribute("rates", rates);
		java.util.List<String> statuses=(java.util.List) Arrays.asList(servletContext.getInitParameter("statuses").split(" "));
		LOG.debug("statuses ==> "+statuses);
		servletContext.setAttribute("statuses", statuses);
		java.util.List<String> payment_date=(java.util.List) Arrays.asList(servletContext.getInitParameter("payment_date").split(" "));
		LOG.debug("payment date==> "+payment_date);
		//tmr=new Timer();
	    //tmr.schedule(new PaymentControl(payment_date), 0, 86400000);
		//tmr.schedule(new PaymentControl(payment_date), 0);
		LOG.debug("servlet context initialization finished");
	}

	/**
	 * Initializes log4j framework.
	 * 
	 * @param servletContext
	 */
	private void initLog4J(ServletContext servletContext) {
		log("Log4J initialization started");
		try {
			PropertyConfigurator.configure(
				servletContext.getRealPath("WEB-INF/log4j.properties"));
			LOG.debug("Log4j has been initialized");
		} catch (Exception ex) {
			log("Cannot configure Log4j");
			ex.printStackTrace();
		}		
		log("Log4J initialization finished");
	}
	
	/**
	 * Initializes CommandContainer.
	 * 
	 * @param servletContext
	 */
	private void initCommandContainer() {
		
		// initialize commands container
		// just load class to JVM
		try {
			Class.forName("ua.nure.skibnev.SummaryTask4.web.command.CommandContainer");
		} catch (ClassNotFoundException ex) {
			throw new IllegalStateException("Cannot initialize Command Container");
		}
	}
	
	private void log(String msg) {
		System.out.println("[ContextListener] " + msg);
	}
	class PaymentControl extends TimerTask{
        List<String> dates;
        
        public PaymentControl(List<String> col) {
			dates=new ArrayList<>();
			dates.addAll(col);
		}
        
		@Override
		public void run() {
			
			int current_month=0;
			int current_day=0;
			int current_year=0;
			int day=0;
			int month=0;
			int year=0;
			boolean event=true;
			
			while(event){
			Calendar gcalend=Calendar.getInstance();
			current_month=gcalend.get(Calendar.MONTH);
			current_day=gcalend.get(Calendar.DATE);
			current_year=gcalend.get(Calendar.YEAR);
			day=Integer.parseInt(dates.get(0));
			month=Integer.parseInt(dates.get(1));
		    year=Integer.parseInt(dates.get(2));
		    
			if(year==current_year & month!=current_month){
				event=false;
				ArrayList<User> lst=new ArrayList<>();
				StringBuilder sb=new StringBuilder();
				try {
					lst.addAll(DBManager.getInstance().getUsersByRole(1));
					for(User one:lst){
					   FullUser fus=DBManager.getInstance().getFullUserRate(one, new FullUser());
					   String[] res=fus.getRates().split(" ");
						for(int i=0;i<res.length;i++){
							if(!res[i].equals("price:")){
								sb.append(res[i]+" ");
							}
						}
						String[] arr=sb.toString().split(" ");
						sb.delete(0,sb.length());
						int rs=one.getCount()-Integer.parseInt(arr[1]);
						if(rs<0){
							//DBManager.getInstance().setUserStatus(one, Statuses.BLOCKED.ordinal());
							log("user "+one.getLogin()+" is blocked");
							DBManager.getInstance().updateUserCount(rs, one.getLogin());
							Timer timer=new Timer();
							timer.schedule(new TimerTask(){

								@Override
								public void run() {
									try {
										User usr=DBManager.getInstance().findUserByLogin(one.getLogin());
										int cnt=usr.getCount()-Integer.parseInt(arr[1]);
										if(cnt>=0 | usr.getCount()==0){
											DBManager.getInstance().updateUserCount(cnt, usr.getLogin());
											//DBManager.getInstance().setUserStatus(one, Statuses.UNBLOCKED.ordinal());
											log("user "+one.getLogin()+" is unblocked");
											timer.cancel();
										}
									} catch (DBException e) {
										e.printStackTrace();
									}
										
								}}, 0, 5000);
						}else{
							DBManager.getInstance().updateUserCount(rs, one.getLogin());
							log("user pay and his balance "+rs);
						}
					}
					addToXML(current_day, current_month, current_year);
				} catch (DBException |TransformerException |IOException |SAXException |ParserConfigurationException e) {
					LOG.error(e.getMessage());
				}
			
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				LOG.error(e.getMessage());
			}
			}
		}
		public void addToXML(int day,int month,int year) throws ParserConfigurationException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			final String filepath = System.getProperty("user.dir") + File.separator+"SummaryTask4"+File.separator+"WebContent"+File.separator+"WEB-INF"+File.separator+"web.xml";
			LOG.debug("file path ==>"+filepath);
			final File xmlFile = new File(filepath);
			// parse XML document
			Document document =db.parse(xmlFile);
			// get root element
			Element root = document.getDocumentElement();
			// obtain questions nodes
			NodeList componentNodes = root
					.getElementsByTagName("context-param");
			String str="";
			// process questions nodes
			for (int j = 0; j < componentNodes.getLength(); j++) {
				Node node=componentNodes.item(j);
				// add question to container
				LOG.debug("Node ==>"+node.getNodeName());
				Element el=(Element)node;
				NodeList nl=el.getElementsByTagName("param-name");
				Element elem=(Element)nl.item(0);
				NodeList list=elem.getChildNodes();
				if(((Node)list.item(0)).getNodeValue().equals("payment_date")){
					NodeList ndl=el.getElementsByTagName("param-value");
					Element elm=(Element)ndl.item(0);
					NodeList lst=elm.getChildNodes();
					((Node)lst.item(0)).setNodeValue(String.valueOf(day)+" "+String.valueOf(month)+" "+String.valueOf(year));
					break;
				}
			}
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(filepath));
            transformer.transform(source, result);
		}
	}
}