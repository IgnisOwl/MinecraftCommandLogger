package michalec.connor.sugunioncommandlogger.bungee;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class Main extends Plugin implements Listener {
	DataHandler dataHandler = new DataHandler(this);
	LogfileHandler logger = new LogfileHandler();
	
	@Override
	public void onEnable() {
		
		//manage YAML data:
		dataHandler.createDirectoryIfMissing("plugins/SUGUnionCommandLogger");
		dataHandler.copyTemplateIfMissing("staffConf.yml", "plugins/SUGUnionCommandLogger/staffConf.yml");
		
		dataHandler.addFile("conf", "plugins/SUGUnionCommandLogger/staffConf.yml");
		dataHandler.loadFileYAML("conf");
		
		//register the toggle notif command and listener
		getProxy().getPluginManager().registerListener(this, this);
		getProxy().getPluginManager().registerCommand(this, (Command) new ToggleCommandHandler(this));
		
		logger.init(); //enable the logger
	}
	
	@Override
	public void onDisable() {
		logger.cleanup();
	}
	
	@EventHandler(priority = 0)
	public void onPlayerChat(ChatEvent event) {
		String message = event.getMessage();
		if(message.charAt(0) == '/') {
			//It's a command, so log it and send to staff
			//TODO: Make a configuration and an option whether or not to bypass staff commands or not
			//It will still log the commands regardless of permissions tho
			
			ProxiedPlayer executor = ((ProxiedPlayer) event.getSender());
			
			if(!(executor.hasPermission("sugunioncommandlogger.staff"))) {
				//Notify anyone who has the staff permission
				for(ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
					if(player.hasPermission("sugunioncommandlogger.staff")) {
						//TODO: Make this customizable: 
						player.sendMessage(TextComponent.fromLegacyText("- "+ChatColor.GOLD+executor.getDisplayName()+ChatColor.DARK_GRAY+"["+ChatColor.RED+executor.getServer().getInfo().getName()+ChatColor.DARK_GRAY+"] "+ChatColor.GRAY+message));
					}
				}
			}
			else {
				//If the sender has the permission dont worry about notifying
			}
			
			//Log the commands:
			logger.init(); //reload the file
			//TODO: Make this customizable: 
			logger.append(executor.getUniqueId().toString()+"("+executor.getDisplayName()+") ["+executor.getServer().getInfo().getName()+"]: "+message);
			logger.cleanup(); //close the file
		}
	}
	
	public Boolean playerHasNotificationEnabled(ProxiedPlayer player) {
		String UUID = player.getUniqueId().toString();
		
		if(dataHandler.YAMLPathExists("conf", UUID)) {
			return(dataHandler.getYAMLBooleanField("conf", UUID));
		}
		else {
			//create section and defeault to true
			dataHandler.setYAMLBooleanField("conf", UUID, true);
		}
		dataHandler.setYAMLBooleanField("conf", UUID, true);
		return(true);
	}
	
	public void setPlayerNotificationSetting(ProxiedPlayer player, Boolean setting) {
		String UUID = player.getUniqueId().toString();
		
		dataHandler.setYAMLBooleanField("conf", UUID, setting);
	}
}
