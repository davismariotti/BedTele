package me.gomeow.bedtele;

import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class bedtele extends JavaPlugin implements Listener {
	public final Logger logger = Logger.getLogger("Minecraft");

	@Override
	public void onDisable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info(pdfFile.getName() + " has Been Disabled!");
	}

	@Override
	public void onEnable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info(pdfFile.getName() + " Version " + pdfFile.getVersion()
				+ " has Been Enabled!");
		saveDefaultConfig();
		this.getServer().getPluginManager().registerEvents(this, this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {

		

		if (sender instanceof Player) {
			Player player = (Player) sender;
			// check if the command bedtele was typed
			if (commandLabel.equalsIgnoreCase("granoldsbed")) {
				if (args.length == 0) {
					// checks if there was no args
					if (player.isOp()) {
						// checks if player is op
						player.sendMessage(ChatColor.RED + "Correct Usage:");
						player.sendMessage(ChatColor.YELLOW
								+ "/granoldsbed create - Use this to make the bed teleport you.");
						player.sendMessage(ChatColor.YELLOW
								+ "/granoldsbed setbed {name} - Use this to set the bed that will teleport you.");
						player.sendMessage(ChatColor.YELLOW
								+ "/granoldsbed setdestination {name} - Use this to set the destination of the bed.");
						player.sendMessage(ChatColor.YELLOW
								+ "/granoldsbed - Brings up this page.");
						// shows help page
					} else {
						player.sendMessage(ChatColor.RED
								+ "You do not have permission to use this command");
						// if player is not op, send this message
					}
				}

				if (args.length == 1) {
					// checks if there was 1 arg
					if (args[0].equalsIgnoreCase("create")) {
						// checks if you used the create arg
						player.sendMessage(ChatColor.GREEN
								+ "Please point to a bed and type "
								+ ChatColor.ITALIC + "/granoldsbed setbed");
					}
					if (args[0].equalsIgnoreCase("setdestination")) {
						player.sendMessage(ChatColor.RED
								+ "You need to specify a name!");
					}
					if (args[0].equalsIgnoreCase("setbed")) {
						player.sendMessage(ChatColor.RED
								+ "You need to specify a name!");
					}
				}
				if (args.length == 2) {
					if (args[0].equalsIgnoreCase("setbed")) {
						if (player.getTargetBlock(null, 200).getTypeId() == 26) {
							// ckecks if you pointed to a bed

							int bedlocX = player.getTargetBlock(null, 0).getLocation().getBlockX();
							int bedlocY = player.getTargetBlock(null, 0).getLocation().getBlockY();
							int bedlocZ = player.getTargetBlock(null, 0).getLocation().getBlockZ();
							String bedlocWorld = player.getWorld().getName();
							String bedlocName = args[1].toLowerCase();
							// gets the location of the bed

							this.getConfig().set(bedlocName + ".BedLocation.X", bedlocX);
							this.getConfig().set(bedlocName + ".BedLocation.Y", bedlocY);
							this.getConfig().set(bedlocName + ".BedLocation.Z", bedlocZ);
							this.getConfig().set(bedlocName + ".BedLocation.World", bedlocWorld);
							
							
							List<String> names = this.getConfig().getStringList("Names");
							if(!(names.contains(bedlocName))) {
								names.add(bedlocName);
								this.getConfig().set("Names", names);
							}
							
							this.saveConfig();
							// puts the location in the config
							player.sendMessage(ChatColor.GREEN
									+ "You set a Granold's bed with the name "
									+ ChatColor.AQUA + bedlocName
									+ ChatColor.GREEN + " in the world "
									+ ChatColor.DARK_GREEN + ChatColor.ITALIC
									+ bedlocWorld + ChatColor.RESET
									+ ChatColor.GREEN + " at " + bedlocX + " "
									+ bedlocY + " " + bedlocZ + "!");
						} else {
							player.sendMessage(ChatColor.RED
									+ "You need to point to a bed!");
						}
					}

					if (args[0].equalsIgnoreCase("setdestination")) {

						int destX = player.getLocation().getBlockX();
						int destY = player.getLocation().getBlockY();
						int destZ = player.getLocation().getBlockZ();
						String destWorld = player.getWorld().getName();
						String destName = args[1].toLowerCase();
						// gets your location

						List<String> destNames = this.getConfig().getStringList("Names");
						if(destNames.contains(destName)) {
							this.getConfig().set(destName + ".Destination.X", destX);
							this.getConfig().set(destName + ".Destination.Y", destY);
							this.getConfig().set(destName + ".Destination.Z", destZ);
							this.getConfig().set(destName + ".Destination.World", destWorld);
							this.saveConfig();
						}
						else {
							player.sendMessage(ChatColor.RED + "You need to set the bed first!");
						}
						// puts the location in the config
						player.sendMessage(ChatColor.GREEN
								+ "You set a destination with the name  "
								+ ChatColor.AQUA + destName + ChatColor.GREEN
								+ " in the world \"" + ChatColor.DARK_GREEN
								+ ChatColor.ITALIC + destWorld
								+ ChatColor.RESET + ChatColor.GREEN + "\" at "
								+ destX + " " + destY + " " + destZ + "!");
					}
				}
			}
		} else {
			sender.sendMessage("You must be a player to use these commands!");
		}
		return false;
	}

	@EventHandler
	public void onBedLeave(PlayerBedLeaveEvent event) {
	//listens for a bed leave event
		Player teleported = event.getPlayer();
		
		
		for(String string : this.getConfig().getStringList("Names")) {
			Integer locationX = this.getConfig().getInt(string + ".BedLocation.X");
			Integer locationY = this.getConfig().getInt(string + ".BedLocation.Y");
			Integer locationZ = this.getConfig().getInt(string + ".BedLocation.Z");
			String locationWorld = this.getConfig().getString(string + ".BedLocation.World");
			
			if(teleported.getLocation().getBlockX() >= (locationX-2) && teleported.getLocation().getBlockX()<= (locationX+2)) {
				//Found X, Verification Success
				if(teleported.getLocation().getBlockY() == locationY) {
					//Found Y, Verification Success
					if(teleported.getLocation().getBlockZ() >= (locationZ-2) && teleported.getLocation().getBlockZ()<= (locationZ+2)) {
						if(teleported.getWorld().getName().equalsIgnoreCase(locationWorld)) {
							
							Integer teleDestX = this.getConfig().getInt(string + ".Destination.X");
							Integer teleDestY = this.getConfig().getInt(string + ".Destination.Y");
							Integer teleDestZ = this.getConfig().getInt(string + ".Destination.Z");
							String teleDestWorld = this.getConfig().getString(string + ".Destination.World");
							
							if(teleDestX == null 
									|| teleDestY == null 
									|| teleDestZ == null
									|| teleDestWorld == null) {
										teleported.sendMessage(ChatColor.RED + "No one set a destination for this Granold's bed!");
									}
									else {
										String message = this.getConfig().getString("teleport-message");
										teleported.sendMessage(ChatColor.DARK_GREEN + message);
									teleported.teleport(new Location(Bukkit.getWorld(teleDestWorld), teleDestX, teleDestY, teleDestZ));
									//teleports the player
									}
						}
					}	
				}
			} 
		}
	}
}	
