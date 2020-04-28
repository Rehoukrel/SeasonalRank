package com.github.TiveCS.seasonalrank.api.player;

import com.github.TiveCS.seasonalrank.SeasonalRank;
import com.github.TiveCS.seasonalrank.api.seasonal.SeasonalHandler;
import com.github.TiveCS.tivecore.lab.language.Placeholder;
import com.github.TiveCS.tivecore.lab.storage.yaml.ConfigManager;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.UUID;

public class PlayerData {

    private static SeasonalRank plugin = SeasonalRank.getPlugin(SeasonalRank.class);
    private static LuckPerms luckPerms = LuckPermsProvider.get();

    private final static File folder = new File(plugin.getDataFolder(), "playerdata");

    private UUID uniqueId;
    private User userData;
    private ConfigManager config;
    private PlayerDataHandler handler;
    private OfflinePlayer offlinePlayer;

    private Placeholder placeholder;

    private int points = 0, lastActiveMonth = -1;

    public PlayerData(PlayerDataHandler handler, UUID uniqueId){
        this.handler = handler;
        this.uniqueId = uniqueId;
        this.userData = luckPerms.getUserManager().getUser(uniqueId);
        this.config = new ConfigManager(new File(folder, uniqueId.toString() + ".yml"));
        this.offlinePlayer = Bukkit.getOfflinePlayer(uniqueId);
        this.placeholder = new Placeholder();

        initData();
        loadData();
    }

    //-------------------------------

    public void openInfoMenu(Player... viewers){

    }

    //-------------------------------

    public void addPoints(int amount){
        setPoints(this.points + amount);
    }

    public void setPoints(int amount){
        config.getData().put("points", amount);
        loadData();

        if (handler.isAbleToNextRank(userData.getPrimaryGroup(), points)){
            handler.moveToNextRank(this);
        }
    }

    //-------------------------------

    public void initData(){
        config.directSetIfNotExists("points", 0);
        config.directSetIfNotExists("last-active-month", -1);
        config.saveConfig();

        config.readConfig();
    }

    public void loadData(){
        this.points = (int) config.getData().get("points");
        this.lastActiveMonth = (int) config.getData().get("last-active-month");
    }

    public void updatePlaceholder(){
        String rank = userData.getPrimaryGroup();
        String nextRank = handler.getNextRank(rank);

        placeholder.addReplacer("player_name", offlinePlayer.getName());
        placeholder.addReplacer("player_points", String.valueOf(this.points));
        placeholder.addReplacer("player_current_points", String.valueOf(this.points));

        placeholder.addReplacer("player_rank", rank);
        placeholder.addReplacer("player_current_rank", rank);

        placeholder.addReplacer("player_next_rank", nextRank);
        placeholder.addReplacer("player_required_points", String.valueOf(handler.getSeasonalHandler().getRegisteredRanks().get(nextRank).getRequiredPoints()));
    }

    //------------------------------

    public PlayerDataHandler getHandler() {
        return handler;
    }

    public User getUserData() {
        return userData;
    }

    public Placeholder getPlaceholder() {
        return placeholder;
    }

    public int getLastActiveMonth() {
        return lastActiveMonth;
    }

    public int getPoints() {
        return points;
    }

    public OfflinePlayer getOfflinePlayer() {
        return offlinePlayer;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public ConfigManager getConfig() {
        return config;
    }
}

