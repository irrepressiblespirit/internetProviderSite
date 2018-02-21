package ua.nure.skibnev.SummaryTask4.loc;

import java.util.Locale;

public enum LocEnum {
	      RU{
		       {
		            this.locale = RU_LOCALE;
		       }
		     },
		  EN{
		        {
		           this.locale = EN_LOCALE;
		        }
		   };
		    private final static String RU_LOCALE = "ru_RU";
		    private final static String EN_LOCALE = "en_US";
		    String locale;
		    public String getLocale(){
		        switch (locale){
		            case RU_LOCALE:
		                Locale.setDefault(new Locale("ru", "RU"));
		                break;
		            case EN_LOCALE:
		                Locale.setDefault(new Locale("en", "US"));
		                break;
		        }
		        return locale;
		    }
}
