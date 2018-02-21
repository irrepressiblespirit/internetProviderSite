package ua.nure.skibnev.SummaryTask4.db.entity;

public class UserFullInformation {
 private String first_name;
 private String last_name;
 private String telephone;
 private String address;
 private String email;
 
 public String getFirstName() {
	return first_name;
}
 public void setFirstName(String firstName) {
	this.first_name = firstName;
}
 public String getLastName() {
	return last_name;
}
 public void setLastName(String lastName) {
	this.last_name = lastName;
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
 @Override
	public String toString() {
		
		return "UserFullInformation["+" firstName "+first_name+" lastName "+last_name+" telephone "+telephone+" address "+address+" email "+email;
	}
}
