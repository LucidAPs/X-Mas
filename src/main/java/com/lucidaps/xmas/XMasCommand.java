package com.lucidaps.xmas;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.bukkit.ChatColor.*;

public class XMasCommand implements CommandExecutor, TabCompleter {

    private final Main plugin;

    private XMasCommand(Main plugin) {
        this.plugin = plugin;
    }

    public static void register(Main plugin) {
        plugin.getCommand("xmas").setExecutor(new XMasCommand(plugin));
        plugin.getCommand("xmas").setTabCompleter(new XMasCommand(plugin));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length > 0) {
            String action = args[0].toLowerCase();
            switch (action) {
                case "help": {
                    for (String line : LocaleManager.COMMAND_HELP) {
                        sender.sendMessage(GREEN + line);
                    }
                    break;
                }
                case "give": {
                    if (args.length > 1) {
                        String name = args[1];
                        Player player = Bukkit.getPlayer(name);
                        if (player != null) {
                            player.getInventory().addItem(XMas.XMAS_CRYSTAL);
                        } else {
                            sender.sendMessage(LocaleManager.COMMAND_PLAYER_OFFLINE);
                        }
                    } else {
                        sender.sendMessage(LocaleManager.COMMAND_NO_PLAYER_NAME);
                    }
                    break;
                }
                case "end": {
                    plugin.end();
                    break;
                }
                case "gifts": {
                    Random random = new Random();
                    for (MagicTree magicTree : XMas.getAllTrees()) {
                        for (int i = 0; i < 3 + random.nextInt(4); i++) {
                            magicTree.spawnPresent();
                        }
                    }
                    Bukkit.broadcastMessage(LocaleManager.COMMAND_GIVEAWAY);
                    break;
                }
                case "reload": {
                    if (sender.hasPermission("xmas.admin")) { // Optional: Add permission check
                        plugin.reloadPluginConfig();
                        sender.sendMessage(ChatColor.GREEN + "X-Mas configuration reloaded!");
                    } else {
                        sender.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                    }
                    break;
                }
                case "addhand": {
                    if (!(sender instanceof Player)) {
                        sender.sendMessage(ChatColor.RED + "This command can only be executed by a player.");
                        return true;
                    }
                    if (!sender.hasPermission("xmas.admin")) {
                        sender.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                        return true;
                    }
                    Player player = (Player) sender;
                    ItemStack item = player.getInventory().getItemInMainHand();

                    if (item == null || item.getType() == Material.AIR) {
                        player.sendMessage(ChatColor.RED + "You must hold an item in your main hand to add it as a gift.");
                        return true;
                    }

                    // Add the item to the gift list
                    plugin.addGiftItem(item);
                    player.sendMessage(ChatColor.GREEN + "The item in your hand has been added to the gift list!");
                    break;
                }


                default:
                    return false;
            }
        } else {
            sendStatus(sender);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> subCommands = Arrays.asList("help", "give", "end", "gifts", "reload", "addhand");

        if (args.length == 1) {
            // Suggest subcommands
            for (String subCommand : subCommands) {
                if (subCommand.startsWith(args[0].toLowerCase())) {
                    completions.add(subCommand);
                }
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("give")) {
            // Suggest online player names for the "give" command
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
                    completions.add(player.getName());
                }
            }
        }

        return completions;
    }

    private void sendStatus(CommandSender sender) {

        int treeCount = XMas.getAllTrees().size();
        Set<UUID> owners = new HashSet<>();
        for (MagicTree magicTree : XMas.getAllTrees()) {
            owners.add(magicTree.getOwner());
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy kk-mm-ss");

        sender.sendMessage(DARK_GREEN + LocaleManager.PLUGIN_NAME + " " + plugin.getDescription().getVersion() + " Plugin Status");
        sender.sendMessage("");
        sender.sendMessage(GRAY + "Event Status: " + (Main.inProgress ? DARK_GREEN + "In Progress" : RED + "Holidays End"));
        if (Main.inProgress) {
            sender.sendMessage(DARK_GREEN + "Current Time: " + GREEN + sdf.format(System.currentTimeMillis()));
            sender.sendMessage(DARK_GREEN + "Holidays end: " + RED + sdf.format(Main.endTime));
        }
        sender.sendMessage(GREEN + "Auto-End: " + (Main.autoEnd ? DARK_GREEN + "Yes" : RED + "No") + GREEN + "    |    " + "Resource Back: " + (Main.resourceBack ? DARK_GREEN + "Yes" : "No"));
        sender.sendMessage("");
        sender.sendMessage(DARK_GREEN + "There are " + GREEN + treeCount + DARK_GREEN + " magic trees owned by " + RED + owners.size() + DARK_GREEN + " players");
        sender.sendMessage(DARK_GREEN + "Use " + RED + "/xmas help" + DARK_GREEN + " for command list");

    }

}
