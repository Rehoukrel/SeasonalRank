package com.github.TiveCS.seasonalrank;

import com.github.TiveCS.seasonalrank.api.player.PlayerData;
import com.github.TiveCS.seasonalrank.api.player.PlayerDataHandler;
import com.github.TiveCS.seasonalrank.api.seasonal.SeasonalHandler;
import com.github.TiveCS.seasonalrank.commands.CmdSeasonalRank;
import com.github.TiveCS.seasonalrank.menu.PlayerInfoMenu;
import com.github.TiveCS.tivecore.lab.menu.MenuListener;
import org.bukkit.plugin.java.JavaPlugin;

public class SeasonalRank extends JavaPlugin {

    private SeasonalHandler seasonalHandler;
    private PlayerDataHandler playerDataHandler;
    private MenuListener menuListener;

    public void onEnable() {
        loadConfig();
        loadMenu();

        this.seasonalHandler = new SeasonalHandler();
        this.playerDataHandler = new PlayerDataHandler();

        loadEvents();
        loadCommands();
    }

    public void onDisable() {
        getConfig().set("last-seasonal-reset-month", seasonalHandler.getLastSeasonalResetMonth());
        saveConfig();

        for (PlayerData pd : playerDataHandler.getLoadedPlayerData().values()){
            pd.getConfig().saveConfig();
        }
    }

    //-----------------------

    public void loadMenu(){
        this.menuListener = new MenuListener(this);

        menuListener.registerMenu(
                new PlayerInfoMenu()
        );
    }

    public void loadConfig(){
        getConfig().options().copyDefaults(true);
        saveConfig();

    }

    public void loadCommands(){
        getCommand("seasonalrank").setExecutor(new CmdSeasonalRank());
        getCommand("seasonalrank").setTabCompleter(new CmdSeasonalRank());
    }

    public void loadEvents(){
    }

    public MenuListener getMenuListener() {
        return menuListener;
    }

    public SeasonalHandler getSeasonalHandler() {
        return seasonalHandler;
    }

    public PlayerDataHandler getPlayerDataHandler() {
        return playerDataHandler;
    }
}
