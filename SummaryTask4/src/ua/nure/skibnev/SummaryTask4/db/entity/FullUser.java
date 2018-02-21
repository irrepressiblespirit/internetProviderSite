package ua.nure.skibnev.SummaryTask4.db.entity;

public class FullUser {
	
	private static final long serialVersionUID = -6889036256149495743L;
	
    private String login;
	
	private String password;
	
	private String firstName;
	
	private String lastName;
	 
	private String telephone;
	 
	private String address;
	 
	private String email;
	
    private int count;
	
	private String status;
	
	private String rates;

	private String role;
	
	public String getFirstName() {
		return firstName;
	}
	 public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	 public String getLastName() {
		return lastName;
	}
	 public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	 public String getTelephone() {
		return telephone;
	}
	 public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	 public String getAddress() {
		return address;
	}
	 public void setAddress(String address) {
		this.address = address;
	}
	 public String getEmail() {
		return email;
	}
	 public void setEmail(String email) {
		this.email = email;
	}
public void setLogin(String login) {
	this.login = login;
}
public String getLogin() {
	return login;
}
public void setPassword(String password) {
	this.password = password;
}
public String getPassword() {
	return password;
}
public void setCount(int count) {
	this.count = count;
}
public int getCount() {
	return count;
}
public void setStatus(String status) {
	this.status = status;
}
public String getStatus() {
	return status;
}
public void setRates(String rates) {
	this.rates = rates;
}
public String getRates() {
	return rates;
}
public void setRole(String role) {
	this.role = role;
}
public String getRole() {
	return role;
}
@Override
public String toString() {
	
	return "Full information about user: \n login: "+login+"\n"+" password: "+password+"\n"+" firstName: "+firstName+"\n"+" lastName: "+lastName+"\n"+" telephone: "+telephone+"\n"+" address: "+address+"\n"+" email: "+email+"\n"+" count: "+count+"\n"+" rate: "+rates+"\n"+" role: "+role;
}
}
