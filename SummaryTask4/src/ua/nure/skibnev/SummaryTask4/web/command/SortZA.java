package ua.nure.skibnev.SummaryTask4.web.command;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.nure.skibnev.SummaryTask4.Path;
import ua.nure.skibnev.SummaryTask4.db.DBManager;
import ua.nure.skibnev.SummaryTask4.db.Services;
import ua.nure.skibnev.SummaryTask4.db.entity.FullUser;
import ua.nure.skibnev.SummaryTask4.db.entity.Tariff;
import ua.nure.skibnev.SummaryTask4.db.entity.User;
import ua.nure.skibnev.SummaryTask4.exception.AppException;

public class SortZA extends Command {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException {
		
		FullUser fr=(FullUser)request.getSession().getAttribute("usr");
		request.setAttribute("rate",fr.getRates());
		
		List<Integer> rat=DBManager.getInstance().getRateId();
		User user;
		StringBuilder sb=new StringBuilder();
		List<Tariff> tar=new ArrayList<>();
		for(int ind:rat){
			user=new User();
			user.setRatesId(ind);
			FullUser fus=DBManager.getInstance().getFullUserRate(user, new FullUser());
			String[] res=fus.getRates().split(" ");
			for(int i=0;i<res.length;i++){
				if(!res[i].equals("price:")){
					sb.append(res[i]+" ");
				}
			}
			String[] arr=sb.toString().split(" ");
			Tariff trf=new Tariff();
			trf.setName(arr[0]);
			trf.setPrice(Integer.parseInt(arr[1]));
			tar.add(trf);
			sb.delete(0, sb.length());
		}
		
		List<Integer> ids=DBManager.getInstance().getServicesId();
		List<String> services=new ArrayList<>();
		for(Integer num:ids){
			if(num==Services.TELEPHONE.ordinal()){
				services.add(Services.TELEPHONE.name());
			}
			if(num==Services.INTERNET.ordinal()){
				services.add(Services.INTERNET.name());
			}
			if(num==Services.CABLE_TV.ordinal()){
				services.add(Services.CABLE_TV.name());
			}
			if(num==Services.IPTV.ordinal()){
				services.add(Services.IPTV.name());
			}
		}
		for(int i=0;i<tar.size();i++){
			tar.get(i).getServices().add(services.get(i));
		}
	
		List<Tariff> tariff=new ArrayList<>();
		while(tar.size()>0){
			Tariff tr=tar.remove(0);
			for(Tariff one:tar){
				if(tr.equals(one)){
					one.getServices().addAll(tr.getServices());
					tariff.add(one);
					break;
				}
			}
		}
		Collections.sort(tariff, new Comp());
	    
		Map<String,Tariff> res=new HashMap<>();
		/*
		for(int i=0;i<tariff.size();i++){
			res.put(tariff.get(i).getName(), tariff.get(i));
		}
	   */
		for(int i=tariff.size()-1;i>=0;i--){
			res.put(tariff.get(i).getName(), tariff.get(i));
		}
		
		request.setAttribute("tariffs", res.values());
		
		return Path.PAGE_CLIENT_RATES;
	}
     private class Comp implements Comparator<Tariff>,Serializable{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public int compare(Tariff o1, Tariff o2) {
			int res=0;
				if(o2.getServices().size()>o1.getServices().size()){
				      res=1;
				}
				if(o2.getServices().size()<o1.getServices().size()){
					res=-1;
				}
			return res;
		}
    	 
     }
	

}
