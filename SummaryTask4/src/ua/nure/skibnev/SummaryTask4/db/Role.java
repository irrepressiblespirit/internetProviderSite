package ua.nure.skibnev.SummaryTask4.db;


import ua.nure.skibnev.SummaryTask4.db.entity.User;

public enum Role {
ADMIN,SUBSCRIBER;
	
	public static Role getRole(User user) {
		int roleId = user.getRoleId();
		return Role.values()[roleId];
	}
	
	public String getName() {
		return name().toLowerCase();
	}
}
