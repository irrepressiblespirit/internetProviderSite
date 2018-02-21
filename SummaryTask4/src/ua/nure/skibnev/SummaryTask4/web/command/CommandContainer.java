package ua.nure.skibnev.SummaryTask4.web.command;

import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

/**
 * Holder for all commands.<br/>
 * 
 * 
 */
public class CommandContainer {
	
	private static final Logger LOG = Logger.getLogger(CommandContainer.class);
	
	private static Map<String, Command> commands = new TreeMap<String, Command>();
	
	static {
		// common commands
		commands.put("login", new LoginCommand());
		commands.put("logout", new LogoutCommand());
		commands.put("settingsUsers",new SettingsUsers());
		commands.put("listSettingsUsers", new ListSettingsUsers());
		commands.put("listAllUsers", new ListAllUsers());
		commands.put("selectRates", new SelectRates());
		commands.put("blockUnblockUsers", new BlockUnblockUsers());
		commands.put("settingsRates", new SettingsRates());
		commands.put("findUsers", new FindUsers());
		commands.put("errorPage", new ErrorPage());
		commands.put("payment", new Payment());
		commands.put("clientPage", new ClientPage());
		commands.put("userRate", new UserRate());
		commands.put("sortZA", new SortZA());
		commands.put("sortPrice", new SortPrice());
		commands.put("userNewRate", new UserNewRate());
		commands.put("enterCommand", new EnterCommand());
		commands.put("sortPriceDown", new SortPriceDown());
		
		LOG.debug("Command container was successfully initialized");
		LOG.trace("Number of commands --> " + commands.size());
	}

	/**
	 * Returns command object with the given name.
	 * 
	 * @param commandName
	 *            Name of the command.
	 * @return Command object.
	 */
	public static Command get(String commandName) {
		if (commandName == null || !commands.containsKey(commandName)) {
			LOG.trace("Command not found, name --> " + commandName);
			return commands.get("noCommand"); 
		}
		
		return commands.get(commandName);
	}
	
}