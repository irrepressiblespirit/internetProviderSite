package ua.nure.skibnev.SummaryTask4.web.tags;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;

public class PaymentDate {
	private static final long serialVersionUID = 2423353715955164002L;

	private static final Logger LOG = Logger.getLogger(PaymentDate.class);
	
	private final int MONTH_LENGTH[]
	        = {31,28,31,30,31,30,31,31,30,31,30,31}; 
	private final int LEAP_MONTH_LENGTH[]
	        = {31,29,31,30,31,30,31,31,30,31,30,31}; 
	
	private String[] month={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
	
	public String getCurrentdate(){
		Calendar cal=Calendar.getInstance();
		return "Today "+cal.get(Calendar.DATE)+"/"+month[cal.get(Calendar.MONTH)]+"/"+cal.get(Calendar.YEAR);
	}
	public String getPaymentdate(){
		GregorianCalendar gcalen=new GregorianCalendar();
		int current_month=gcalen.get(Calendar.MONTH);
		int current_day=gcalen.get(Calendar.DATE);
		int current_year=gcalen.get(Calendar.YEAR);
		int tmp=0;
		if(gcalen.isLeapYear(current_year)){
			tmp=LEAP_MONTH_LENGTH[current_month];
		}else{
			tmp=MONTH_LENGTH[current_month];
		}
		int date=tmp-current_day;
		return "To make payment in "+date+" days";
	}
}
