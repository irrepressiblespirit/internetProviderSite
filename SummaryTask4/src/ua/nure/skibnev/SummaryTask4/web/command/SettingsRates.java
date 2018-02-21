package ua.nure.skibnev.SummaryTask4.web.command;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ua.nure.skibnev.SummaryTask4.Path;
import ua.nure.skibnev.SummaryTask4.db.DBManager;
import ua.nure.skibnev.SummaryTask4.db.Services;
import ua.nure.skibnev.SummaryTask4.db.entity.FullUser;
import ua.nure.skibnev.SummaryTask4.db.entity.Tariff;
import ua.nure.skibnev.SummaryTask4.db.entity.User;
import ua.nure.skibnev.SummaryTask4.exception.AppException;

public class SettingsRates extends Command {
	private static final long serialVersionUID = 2423353715955164001L;

	private static final Logger LOG = Logger.getLogger(SettingsRates.class);
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException {
		if(request.getSession().getAttribute("usr")==null){
			throw new AppException("Unknow user");
		}
		String createbtn=request.getParameter("create");
		String name=request.getParameter("name");
		String price=request.getParameter("price");
		String[] check=request.getParameterValues("check");
		String del=request.getParameter("button");
		String edt=request.getParameter("edt");
		boolean error=false;
		
		String forward=Path.COMMAND_SELECT_RATES;
		
		
		if(createbtn!=null){
			Tariff tariff=new Tariff();
			if(!name.equals("")){
				tariff.setName(name);
			}else{
				throw new AppException("Wrong name");
			}
			LOG.debug("name ==>"+name);
			if(!price.equals("")){
				Pattern pr=Pattern.compile("[0-9]+");
				Matcher mat=pr.matcher(price);
				if(mat.matches()){
					tariff.setPrice(Integer.parseInt(price));
				}else{
					throw new AppException("Wrong price");
				}
			}else{
				throw new AppException("Wrong price");
			}
			
			LOG.debug("price ==>"+price);
			
			if(check==null){
				throw new AppException("Not selec services");
			}
			
			for(int i=0;i<check.length;i++){
				LOG.debug("check ==>["+i+"]"+check[i]);
				if(check[i].toUpperCase().equals(Services.TELEPHONE.name())){
					tariff.getServices().add(Services.TELEPHONE.name());
				}
				if(check[i].toUpperCase().equals(Services.INTERNET.name())){
					tariff.getServices().add(Services.INTERNET.name());
				}
				if(check[i].toUpperCase().equals(Services.CABLE_TV.name())){
					tariff.getServices().add(Services.CABLE_TV.name());
				}
				if(check[i].toUpperCase().equals(Services.IPTV.name())){
					tariff.getServices().add(Services.IPTV.name());
				}
			}
			LOG.debug("bean tariff ==>"+tariff.toString());
			LOG.debug("services list size ==>"+tariff.getServices().size());
			DBManager.getInstance().insertNewRate(tariff);
			int id=DBManager.getInstance().getRateId(tariff);
			for(int j=0;j<tariff.getServices().size();j++){
				LOG.debug("service ==>"+tariff.getServices().get(j));
				DBManager.getInstance().setRatesAndServices(id, extractServices(tariff.getServices().get(j)));
			}
			ServletContext sc=request.getServletContext();
			ArrayList<String> list=new ArrayList<>();
			List<String> col=(List<String>) sc.getAttribute("rates");
			list.addAll(col);
			list.add(tariff.getName());
			sc.setAttribute("rates", list);
			try {
				new DOMParser().addToXML(tariff,true);
			} catch (ParserConfigurationException | TransformerException e) {
				throw new AppException(e.getMessage());
			} catch (SAXException e) {
				throw new AppException(e.getMessage());
			}
				
		}
		if(del!=null){
			List<String> names=DBManager.getInstance().selectRatesName();
			String find="non";
			for(String nm:names){
				if(del.equals(nm)){
					find=nm;
				}
			}
			Tariff tar=new Tariff();
			tar.setName(find);
			LOG.debug("Value of button ==>"+find);
			int id=DBManager.getInstance().getRateId(tar);
			LOG.debug("id in SettingsRates: "+id);
			User us=new User();
			us.setRatesId(id);
			FullUser fus=DBManager.getInstance().getFullUserRate(us, new FullUser());
			String[] rat=fus.getRates().split(" ");
			StringBuffer sb=new StringBuffer();
			for(int i=0;i<rat.length;i++){
				if(!rat[i].equals("price:")){
					sb.append(rat[i]+" ");
				}
			}
			String[] arr=sb.toString().split(" ");
			Tariff deleteTariff=new Tariff();
			deleteTariff.setName(arr[0]);
			deleteTariff.setPrice(Integer.parseInt(arr[1]));
			LOG.debug("Delete tariff is: "+deleteTariff.toString());
			TreeSet<Tariff> tarCol=new TreeSet<>(new SortByPrice());
			sb.delete(0, sb.length());
			for(int i=0;i<names.size();i++){
				Tariff tmp=new Tariff();
				tmp.setName(names.get(i));
				int iden=DBManager.getInstance().getRateId(tmp);
				User usr=new User();
				usr.setRatesId(iden);
				FullUser fusr=new FullUser();
				fusr=DBManager.getInstance().getFullUserRate(usr, new FullUser());
				LOG.debug("FullUser is: "+fusr);
				String[] rats=fusr.getRates().split(" ");
				for(int j=0;j<rats.length;j++){
					if(!rats[j].equals("price:")){
						sb.append(rats[j]+" ");
						LOG.debug("Append to sb ==> "+sb.toString());
					}
				}
				String[] ar=sb.toString().split(" ");
				Tariff tr=new Tariff();
				tr.setName(ar[0]);
				tr.setPrice(Integer.parseInt(ar[1]));
				tarCol.add(tr);
				sb.delete(0, sb.length());
			}
			LOG.debug("tariff collections is"+tarCol.toString());
			int ind=0;
			for(Tariff rate:tarCol){
				if(deleteTariff.equals(rate)){
					if(ind>0){
						ind--;
						break;
					}else{
						ind++;
						break;
					}
				}else{
					ind++;
				}
			}
			LOG.debug("index "+ind);
			Object[] t=tarCol.toArray();
			//LOG.debug("Collection of objects "+t.toString());
			Tariff nw=(Tariff)t[ind];
		    LOG.debug("new tariff "+nw.toString());
		    int ident=DBManager.getInstance().getRateId(nw);
		    DBManager.getInstance().updateRatesIdInUsers(id, ident);
			DBManager.getInstance().deleteTariffAndServices(id);
			DBManager.getInstance().deleteTariff(id);
			
			ServletContext sc=request.getServletContext();
			ArrayList<String> list=new ArrayList<>();
			List<String> col=(List<String>) sc.getAttribute("rates");
			list.addAll(col);
			list.remove(find);
			sc.setAttribute("rates", list);
			try {
				new DOMParser().deleteFromXML(tar);
			} catch (ParserConfigurationException | TransformerException e) {
				throw new AppException(e.getMessage());
			} catch (SAXException e) {
				throw new AppException(e.getMessage());
			}
			
		}
		/*
		if(edit!=null){
			List<String> names=DBManager.getInstance().selectRatesName();
			String find="non";
			for(String nm:names){
				if(edit.equals(nm)){
					find=nm;
				}
			}
			Tariff tar=new Tariff();
			tar.setName(find);
			LOG.debug("Value of button ==>"+find);
			int id=DBManager.getInstance().getRateId(tar);
			User user=new User();
			user.setRatesId(id); 
			FullUser fuser=new FullUser();
			FullUser fus=DBManager.getInstance().getFullUserRate(user, fuser);
			String rates=fus.getRates();
			String[] rat=rates.split(" ");
			StringBuffer sb=new StringBuffer();
			for(int i=0;i<rat.length;i++){
				if(!rat[i].equals("price:")){
					sb.append(rat[i]+" ");
				}
			}
			String[] arr=sb.toString().split(" ");
			for(int j=0;j<arr.length;j++){
				LOG.debug("result: "+arr[j]);
			}
		
		}
		*/
		if(edt!=null){
			Tariff tariff=new Tariff();
			if(!name.equals("")){
				ArrayList<String> tmp=new ArrayList<>();
				List<String> names=DBManager.getInstance().selectRatesName();
				tmp.addAll(names);
				if(!tmp.contains(name)){
					throw new AppException("Wrong name");
				}
				tariff.setName(name);
			}else{
				throw new AppException("Wrong name");
			}
			LOG.debug("name ==>"+name);
			if(!price.equals("")){
				Pattern pr=Pattern.compile("[0-9]+");
				Matcher mat=pr.matcher(price);
				if(mat.matches()){
					tariff.setPrice(Integer.parseInt(price));
				}else{
					throw new AppException("Wrong price");
				}
			}else{
				throw new AppException("Wrong price");
			}
			
			LOG.debug("price ==>"+price);
			
			if(check==null){
				throw new AppException("Not selec services");
			}
			
			for(int i=0;i<check.length;i++){
				LOG.debug("check ==>["+i+"]"+check[i]);
				if(check[i].toUpperCase().equals(Services.TELEPHONE.name())){
					tariff.getServices().add(Services.TELEPHONE.name());
				}
				if(check[i].toUpperCase().equals(Services.INTERNET.name())){
					tariff.getServices().add(Services.INTERNET.name());
				}
				if(check[i].toUpperCase().equals(Services.CABLE_TV.name())){
					tariff.getServices().add(Services.CABLE_TV.name());
				}
				if(check[i].toUpperCase().equals(Services.IPTV.name())){
					tariff.getServices().add(Services.IPTV.name());
				}
			}
			LOG.debug("name in edit ==>"+name);
			LOG.debug("price in edit ==>"+price);
			LOG.debug("tariff in edit ==>"+tariff.toString());
			//Tariff tmp=new Tariff();
			//tmp.setName((String)request.getSession().getAttribute("OldName"));
			int id=DBManager.getInstance().getRateId(tariff);
			LOG.debug("id in SettingsRates: "+id);
			DBManager.getInstance().updateRates(id, tariff);
			DBManager.getInstance().deleteTariffAndServices(id);
			for(int j=0;j<tariff.getServices().size();j++){
				LOG.debug("service ==>"+tariff.getServices().get(j));
				DBManager.getInstance().setRatesAndServices(id, extractServices(tariff.getServices().get(j)));
			}
			/*
			}else{
				if(lst.size()>tariff.getServices().size()){
					boolean flag;
					for(int k=0;k<lst.size();k++){
						flag=false;
						int tmp=lst.get(k);
						for(int m=0;m<tariff.getServices().size();m++){
							if(tmp==extractServices(tariff.getServices().get(m))){
								DBManager.getInstance().updateRatesServices(id,tmp);
								flag=true;
							}
						}
						if(!flag){
							DBManager.getInstance().setRatesAndServices(id, tmp);
						}
					}
				}
				if(lst.size()<tariff.getServices().size()){
					boolean flag;
					for(int k=0;k<tariff.getServices().size();k++){
						flag=false;
						int tmp=extractServices(tariff.getServices().get(k));
						LOG.debug("tmp ++>"+tmp);
						for(int m=0;m<lst.size();m++){
							if(tmp==lst.get(m)){
								LOG.debug("tmp =="+lst.get(m));
								DBManager.getInstance().updateRatesServices(id,tmp);
								flag=true;
							}
							LOG.debug("tmp !=lst.size"+tmp);
						}
						if(!flag){
							LOG.debug("tmp !="+tmp);
							DBManager.getInstance().setRatesAndServices(id, tmp);
						}
					}
				}
			}
			*/
			/*
			if(!tmp.getName().equals(tariff.getName())){
				LOG.debug("in XML setting block");
				try {
					new DOMParser().deleteFromXML(tmp);
					new DOMParser().addToXML(tariff,false);
				} catch (ParserConfigurationException | TransformerException e) {
					throw new AppException(e.getMessage());
				} catch (SAXException e) {
					throw new AppException(e.getMessage());
				}
			}
			*/
		}
		return forward;
	}
	
    private int extractServices(String name){
    	int id=-1;
    	if(name.equals(Services.TELEPHONE.name())){
			id=0;
		}
		if(name.equals(Services.INTERNET.name())){
			id=1;
		}
		if(name.equals(Services.CABLE_TV.name())){
			id=2;
		}
		if(name.equals(Services.IPTV.name())){
			id=3;
		}
		LOG.debug("id value ==>"+id);
		return id;
    }
    class DOMParser{
    	
    	private final String FILENAME = File.separator+"SummaryTask4"+File.separator+"WebContent"+File.separator+"WEB-INF"+File.separator+"web.xml";
    	
    	public void addToXML(Tariff tariff,boolean singleton) throws ParserConfigurationException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException{
    		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			final String filepath = System.getProperty("user.dir") + FILENAME;
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
				if(((Node)list.item(0)).getNodeValue().equals("rates")){
					NodeList ndl=el.getElementsByTagName("param-value");
					Element elm=(Element)ndl.item(0);
					NodeList lst=elm.getChildNodes();
					String res=((Node)lst.item(0)).getNodeValue();
					if(singleton){
					lst.item(0).setNodeValue(res.concat(" "+tariff.getName()));
					}else{
						lst.item(0).setNodeValue(res.concat(tariff.getName()));
					}
					LOG.debug("Node value in rate ==>"+lst.item(0).getNodeValue());
					break;
				}
				
			}
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(filepath));
            transformer.transform(source, result);
    	}
    	
    	public void deleteFromXML(Tariff tariff) throws ParserConfigurationException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException{
    		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			final String filepath = System.getProperty("user.dir") + FILENAME;
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
			StringBuilder sb=new StringBuilder();
			// process questions nodes
			for (int j = 0; j < componentNodes.getLength(); j++) {
				Node node=componentNodes.item(j);
				// add question to container
				LOG.debug("Node ==>"+node.getNodeName());
				Element el=(Element)node;
				NodeList nl=el.getElementsByTagName("param-name");
				Element elem=(Element)nl.item(0);
				NodeList list=elem.getChildNodes();
				if(((Node)list.item(0)).getNodeValue().equals("rates")){
					NodeList ndl=el.getElementsByTagName("param-value");
					Element elm=(Element)ndl.item(0);
					NodeList lst=elm.getChildNodes();
					String res=((Node)lst.item(0)).getNodeValue();
					String[] words=res.split(" ");
					for(int i=0;i<words.length;i++){
						if(!words[i].equals(tariff.getName())){
							sb.append(words[i]+" ");
						}
					}
					lst.item(0).setNodeValue(sb.toString());
					LOG.debug("Node value in rate ==>"+lst.item(0).getNodeValue());
					break;
				}
				
			}
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(filepath));
            transformer.transform(source, result);
            
    	}
    }
    class SortByPrice implements Comparator<Tariff>,Serializable{

		/**
		 * 
		 */
		private static final long serialVersionUID = 123L;

		@Override
		public int compare(Tariff o1, Tariff o2) {
			return o2.getPrice()-o1.getPrice();
		}
    	
    }
}
