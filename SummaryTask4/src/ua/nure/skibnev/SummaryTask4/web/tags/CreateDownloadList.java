package ua.nure.skibnev.SummaryTask4.web.tags;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import ua.nure.skibnev.SummaryTask4.db.entity.Tariff;
import ua.nure.skibnev.SummaryTask4.exception.DBException;


public class CreateDownloadList extends TagSupport {
	private static final long serialVersionUID = 2423353715955164771L;
	
	private String FILEPATH=System.getProperty("user.dir")+File.separator+"SummaryTask4"+File.separator+"tariff.txt";
	
@Override
public int doStartTag() throws JspException {
	File file=new File(FILEPATH);
	PrintWriter pw=null;
	TreeSet<Tariff> tmp=new TreeSet<>();
	try {
		pw=new PrintWriter(file, "utf-8");
		SettingRateList list=new SettingRateList();
		Set<Tariff>set=list.getAllRates();
		tmp.addAll(set);
		list.printRateList(pw, tmp);
		JspWriter out=pageContext.getOut();
		out.write("<a href="+System.getProperty("user.dir")+FILEPATH+" download>download tariffs</a>");
	} catch (IOException e) {
		throw new JspException(e.getMessage());
	} catch (DBException e) {
		throw new JspException(e.getMessage());
	}finally{
		pw.close();
	}
	return SKIP_BODY;
}
}
