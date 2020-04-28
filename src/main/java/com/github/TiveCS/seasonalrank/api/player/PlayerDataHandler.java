package com.github.TiveCS.seasonalrank.api.player;

import com.github.TiveCS.seasonalrank.SeasonalRank;
import com.github.TiveCS.seasonalrank.api.ranks.Rank;
import com.github.TiveCS.seasonalrank.api.seasonal.SeasonalHandler;
import com.github.TiveCS.seasonalrank.menu.PlayerInfoMenu;
import com.github.TiveCS.tivecore.lab.menu.MenuListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PlayerDataHandler  {

    private static SeasonalRank plugin = SeasonalRank.getPlugin(SeasonalRank.class);

    private SeasonalHandler seasonalHandler;
    private HashMap<UUID, PlayerData> loadedPlayerData = new HashMap<>();

    public PlayerDataHandler(){
        this.seasonalHandler = plugin.getSeasonalHandler();
    }

    //-----------------------------------

    public void openInfoMenu(UUID player, Player... viewers){
        MenuListener l = plugin.getMenuListener();
        PlayerData pd = getPlayerData(player);

        try {
            PlayerInfoMenu menu = (PlayerInfoMenu) l.getRegisteredMenu().get("seasonalrank-playerinfo").clone();
            menu.setPlayerData(pd);

            l.open(menu, pd.getPlaceholder(), 1, viewers);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    //-----------------------------------

    public void addPoints(UUID player, int amount){
        PlayerData pd = getPlayerData(player);
        pd.addPoints(amount);
    }

    public void addPoints(PlayerData playerData, int amount){
        playerData.addPoints(amount);
    }

    public void moveToNextRank(PlayerData playerData){
        Rank r = seasonalHandler.getRegisteredRanks().get(playerData.getUserData().getPrimaryGroup());
        if (r != null){
            if (r.hasNextRank()) {
                plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "lp user " + playerData.getOfflinePlayer().getName() + " parent set " + r.getNextRank());
                Bukkit.getPlayer(playerData.getUniqueId()).sendMessage("you has been reached rank " + r.getNextRank());
            }
        }
    }

    //-----------------------------------

    public boolean isAbleToNextRank(String currentRank, int points){
        Rank r = seasonalHandler.getRegisteredRanks().getOrDefault(getNextRank(currentRank), null);
        if (r != null){
            return points >= r.getRequiredPoints();
        }
        return false;
    }

    public String getNextRank(String beforeNextRank){
        Rank r = seasonalHandler.getRegisteredRanks().getOrDefault(beforeNextRank, null);
        if (r != null){
            return r.getNextRank();
        }
        return null;
    }

    public PlayerData getPlayerData(UUID playerUniqueId){
        if (!loadedPlayerData.containsKey(playerUniqueId)){
            loadedPlayerData.put(playerUniqueId, new PlayerData(this, playerUniqueId));
        }

        return loadedPlayerData.get(playerUniqueId);
    }

    //-----------------------------------

    public SeasonalHandler getSeasonalHandler() {
        return seasonalHandler;
    }

    public HashMap<UUID, PlayerData> getLoadedPlayerData() {
        return loadedPlayerData;
    }
}
