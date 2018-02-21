package ua.nure.skibnev.SummaryTask4.db.entity;

/**
 * User entity.
 * 
 * 
 */
public class User extends Entity {

	private static final long serialVersionUID = -6889036256149495388L;
	
	private String login;
	
	private String password;
	
	private int fullInformationId;
	
	private int count;
	
	private int statusesId;
	
	private int ratesId;

	private int roleId;

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

public void setCount(int count) {
	this.count = count;
}
public int getCount() {
	return count;
}
public void setStatusesId(int statusesId) {
	this.statusesId = statusesId;
}
public int getStatusesId() {
	return statusesId;
}
public void setRatesId(int ratesId) {
	this.ratesId = ratesId;
}
public int getRatesId() {
	return ratesId;
}
public void setFullInformationId(int fullInformationId) {
	this.fullInformationId = fullInformationId;
}
public int getFullInformationId() {
	return fullInformationId;
}
	@Override
	public String toString() {
		return "User [login=" + login  
				+ ", password=" + password 
				+ ", fullInformationId=" + fullInformationId
				+ ", count= "+count
				+ ", statusesId= "+ statusesId
				+ ", ratesId= "+ ratesId
				+ ", roleId=" + roleId + "]";
	}
	
}
