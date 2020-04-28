package com.github.TiveCS.seasonalrank.commands;

import com.github.TiveCS.seasonalrank.SeasonalRank;
import com.github.TiveCS.seasonalrank.api.player.PlayerData;
import com.github.TiveCS.seasonalrank.api.player.PlayerDataHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class CmdSeasonalRank implements CommandExecutor, TabCompleter {

    private static SeasonalRank plugin = SeasonalRank.getPlugin(SeasonalRank.class);

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (command.getName().equalsIgnoreCase("seasonalrank")){
            if (commandSender instanceof Player){
                Player p = (Player) commandSender;

                if (strings.length == 0){
                    PlayerDataHandler handler = plugin.getPlayerDataHandler();
                    PlayerData pd = handler.getPlayerData(p.getUniqueId());

                    p.sendMessage("Rank: " + pd.getUserData().getPrimaryGroup());
                    p.sendMessage("Points: " + pd.getPoints());
                    p.sendMessage("Required Points: " + handler.getNextRank(pd.getUserData().getPrimaryGroup()));

                    pd.openInfoMenu(p);
                    return true;
                }
            }
            if (strings.length == 3){
                if (strings[0].equalsIgnoreCase("addpoint")){
                    Player target = Bukkit.getPlayer(strings[1]);
                    int addition = Integer.parseInt(strings[2]);

                    if (target != null) {
                        PlayerDataHandler handler = plugin.getPlayerDataHandler();
                        PlayerData pd = handler.getPlayerData(target.getUniqueId());
                        int before = pd.getPoints();

                        handler.addPoints(pd, addition);
                        commandSender.sendMessage("Point: " + before + " -> " + pd.getPoints());
                    }
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("seasonalrank")){
            if (strings.length == 1){
                return Arrays.asList("addpoint", "setpoint", "resetseason");
            }
        }
        return null;
    }
}