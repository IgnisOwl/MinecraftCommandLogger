package michalec.connor.sugunioncommandlogger.bungee;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class Main extends Plugin implements Listener {
	DataHandler dataHandler = new DataHandler(this);
	LogfileHandler logger = new LogfileHandler();
	
	public void onEnable() {
		logger.init();
		
		
		dataHandler.createDirectoryIfMissing("plugins/SUGUnionCommandLogger");
		dataHandler.copyTemplateIfMissing("staffConf.yml", "plugins/SUGUnionCommandLogger/staffConf.yml");
		
		dataHandler.addFile("conf", "plugins/SUGUnionCommandLogger/staffConf.yml");
		dataHandler.loadFileYAML("conf");
		
		getProxy().getPluginManager().registerListener(this, this);
		getProxy().getPluginManager().registerCommand(this, (Command) new ToggleCommandHandler());
	}
	
	@EventHandler(priority = 0)
	public void onPlayerChat(ChatEvent event) {
		System.out.println(event.getMessage());
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
		return(null);
	}
	
	public void setPlayerNotificationSetting(ProxiedPlayer player, Boolean setting) {
		String UUID = player.getUniqueId().toString();
		
		dataHandler.setYAMLBooleanField("conf", UUID, setting);
	}
}