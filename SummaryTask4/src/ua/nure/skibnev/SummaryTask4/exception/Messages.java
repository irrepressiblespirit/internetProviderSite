package ua.nure.skibnev.SummaryTask4.exception;

import java.sql.SQLException;

/**
 * Holder for messages of exceptions.
 * 
 *
 */
public class Messages {

	private Messages() {
		// no op
	}
	public static final String ERR_NOT_FIND_ID="Not find id";
	public static final String ERR_CANNOT_FIND_DATAS_IN_DB="Cannot find datas in database";
	public static final String ERR_CANNOT_OBTAIN_USER_BY_LASTNAME="Cannot obtain user by its last name";
	
	public static final String ERR_CANNOT_OBTAIN_USER_ORDER_BEANS = "Cannot obtain user order beans";

	public static final String ERR_CANNOT_OBTAIN_CONNECTION = "Cannot obtain a connection from the pool";

	public static final String ERR_CANNOT_OBTAIN_CATEGORIES = "Cannot obtain categories";

	public static final String ERR_CANNOT_OBTAIN_MENU_ITEMS = "Cannot obtain menu items";

	public static final String ERR_CANNOT_OBTAIN_MENU_ITEMS_BY_ORDER = "Cannot obtain menu items by order";

	public static final String ERR_CANNOT_OBTAIN_MENU_ITEMS_BY_IDENTIFIERS = "Cannot obtain menu items by its identifiers";

	public static final String ERR_CANNOT_OBTAIN_ORDERS = "Cannot obtain orders";

	public static final String ERR_CANNOT_OBTAIN_ORDERS_BY_STATUS_ID = "Cannot obtain orders by status id";

	public static final String ERR_CANNOT_OBTAIN_ORDERS_BY_IDENTIFIERS = "Cannot obtain orders by its identifiers";

	public static final String ERR_CANNOT_OBTAIN_ORDERS_BY_USER_AND_STATUS_ID = "Cannot obtain orders by user and status id";

	public static final String ERR_CANNOT_OBTAIN_USER_BY_ID = "Cannot obtain a user by its id";

	public static final String ERR_CANNOT_OBTAIN_USER_BY_LOGIN = "Cannot obtain a user by its login";

	public static final String ERR_CANNOT_UPDATE_USER = "Cannot update a user";

	public static final String ERR_CANNOT_CLOSE_CONNECTION = "Cannot close a connection";

	public static final String ERR_CANNOT_CLOSE_RESULTSET = "Cannot close a result set";

	public static final String ERR_CANNOT_CLOSE_STATEMENT = "Cannot close a statement";

	public static final String ERR_CANNOT_OBTAIN_DATA_SOURCE = "Cannot obtain the data source";
	
}