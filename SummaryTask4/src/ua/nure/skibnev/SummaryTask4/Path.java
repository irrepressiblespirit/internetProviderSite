package ua.nure.skibnev.SummaryTask4;

/**
 * Path holder (jsp pages, controller commands).
 * 
 */
public final class Path {
	
	// pages
	public static final String PAGE_LOGIN = "/login.jsp";
	public static final String PAGE_ERROR_PAGE = "/WEB-INF/jsp/error_page.jsp";
	public static final String ADMIN_START_PAGE = "/WEB-INF/jsp/admin/settings_users.jsp";
	public static final String CLIENT_START_PAGE = "/WEB-INF/jsp/client/client_page.jsp";
	public static final String ADMIN_SETTINGS_RATES="/WEB-INF/jsp/admin/settings_rates.jsp";
	public static final String PAGE_SETTINGS_USERS="/WEB-INF/jsp/admin/block_unblock_users.jsp";
	public static final String PAGE_CLIENT_RATES="/WEB-INF/jsp/client/client_rates.jsp";
	
	public static final String PAGE_SETTINGS = "/WEB-INF/jsp/settings.jsp";

	// commands
	public static final String COMMAND_ERROR_PAGE="controller?command=errorPage";
	public static final String COMMAND_FIND_USERS="controller?command=findUsers";
	public static final String COMMAND_LIST_USERS="controller?command=listAllUsers";
	public static final String COMMAND_CLIENT_PAGE="controller?command=clientPage";
	public static final String COMMAND_SELECT_RATES="controller?command=selectRates";
	public static final String COMMAND_LIST_SETTINGS_USERS="controller?command=listSettingsUsers";
	public static final String COMMAND_USER_RATE="controller?command=userRate";
	public static final String COMMAND_LOGIN="controller?command=login";
}