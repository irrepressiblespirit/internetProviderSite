package ua.nure.skibnev.SummaryTask4.db.entity;

import java.util.ArrayList;
import java.util.List;

import ua.nure.skibnev.SummaryTask4.exception.AppException;

public class Tariff implements Comparable<Tariff> {
	private String name;
	private int price;
	private List<String> services;
	
public Tariff() {
	services=new ArrayList<>();
}
public void setName(String name) {
	this.name = name;
}
public String getName() {
	return name;
}
public void setPrice(int price) {
	this.price = price;
}
public int getPrice() {
	return price;
}
public void setServices(List<String> services) {
	this.services = services;
}
public List<String> getServices() {
	return services;
}
@Override
public boolean equals(Object obj) {
	if(obj==null){
		return false;
	}
	if(!Tariff.class.isAssignableFrom(obj.getClass())){
		return false;
	}
	final Tariff tar=(Tariff)obj;
	return tar.name.equals(this.name) && tar.price==this.price;
}
@Override
public int hashCode() {
	final int p = 31;
    int r = 1;
    r=p*r+name.hashCode();
    r=p*r+price;
    r=p*r+((services==null)?0:services.hashCode());
	return r;
}
@Override
public String toString() {
	StringBuilder sb=new StringBuilder();
	for(String res:services){
		sb.append(","+res);
    }
	return "Tariff "+" name: "+name+" price: "+price+" list of services: "+sb.toString();
}
@Override
public int compareTo(Tariff o) {
	if(this.getPrice()>o.getPrice()){
		return 1;
	}
	if(this.getPrice()<o.getPrice()){
		return -1;
	}
	return 0;
}

}
