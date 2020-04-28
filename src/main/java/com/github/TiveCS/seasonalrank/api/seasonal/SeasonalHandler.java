package com.github.TiveCS.seasonalrank.api.seasonal;

import com.github.TiveCS.seasonalrank.SeasonalRank;
import com.github.TiveCS.seasonalrank.api.player.PlayerData;
import com.github.TiveCS.seasonalrank.api.ranks.Rank;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitTask;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;

public class SeasonalHandler {

    private static SeasonalRank plugin = SeasonalRank.getPlugin(SeasonalRank.class);

    private HashMap<String, Rank> registeredRanks = new HashMap<>();
    private HashMap<String, SeasonResetSchedule> resetSchedules = new HashMap<>();
    private FileConfiguration config;

    private int lastSeasonalResetMonth;
    private LocalDate currentDate;
    private Rank defaultRank;
    private BukkitTask taskTimer;

    public SeasonalHandler(){
        this.config = plugin.getConfig();
        this.currentDate = LocalDate.now();

        loadData();
        loadRanks();
        loadResetDate();

        startTimer();
    }

    //---------------------------------

    public boolean isNewSeason(){
        String presetMonth = currentDate.getMonth().name().toLowerCase();
        SeasonResetSchedule schedule = resetSchedules.getOrDefault(presetMonth, null);
        if (schedule != null){
            boolean isSameWithLastMonth = currentDate.getMonthValue() == lastSeasonalResetMonth;
            boolean isSameMonth = currentDate.getMonthValue() == schedule.getMonth().getValue();
            boolean isSameDay = currentDate.getDayOfMonth() == schedule.getResetDay();

            return !isSameWithLastMonth && isSameMonth && isSameDay;
        }
        return false;
    }

    @SuppressWarnings("deprecated")
    public void resetSeason(){
        this.lastSeasonalResetMonth = currentDate.getMonthValue();
        for (PlayerData pd : plugin.getPlayerDataHandler().getLoadedPlayerData().values()){
            pd.setPoints(0);
        }

        OfflinePlayer p = Bukkit.getOfflinePlayer("Rhk_Co");
        if (p.isOnline()) {
            Bukkit.broadcastMessage("Season has been reset");
        }
    }

    //---------------------------------

    public void startTimer(){
        if (taskTimer != null){
            taskTimer.cancel();
        }
        taskTimer = Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
            @Override
            public void run() {
                resetSeason();
            }
        }, 0, 20*60);
    }

    public void loadData(){
        this.lastSeasonalResetMonth = config.getInt("last-seasonal-reset-month");
    }

    public void loadRanks(){
        defaultRank = null;
        registeredRanks.clear();

        for (String s : config.getConfigurationSection("ranks").getKeys(false)){
            String path = new StringBuilder().append("ranks.").append(s).toString();
            Rank r = new Rank(this, s);

            if (config.contains(path + ".default-rank")){
                if (config.getBoolean(path + ".default-rank")){
                    defaultRank = r;
                }
            }
            if (config.contains(path + ".next-rank")) {
                r.setNextRank(config.getString(path + ".next-rank"));
            }
            r.setRequiredPoints(config.getInt(path + ".required-points"));

            registeredRanks.put(s.toLowerCase(), r);
        }
    }

    public void loadResetDate(){
        resetSchedules.clear();

        for (String m : config.getConfigurationSection("seasons-ends-date").getKeys(false)){
            SeasonResetSchedule schedule = new SeasonResetSchedule(this, m, config.getInt("season-ends-date." + m));

            resetSchedules.put(m.toLowerCase(), schedule);
        }
    }

    //---------------------------------

    public Rank getDefaultRank() {
        return defaultRank;
    }

    public BukkitTask getTaskTimer() {
        return taskTimer;
    }

    public HashMap<String, SeasonResetSchedule> getResetSchedules() {
        return resetSchedules;
    }

    public HashMap<String, Rank> getRegisteredRanks() {
        return registeredRanks;
    }

    public int getLastSeasonalResetMonth() {
        return lastSeasonalResetMonth;
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public LocalDate getCurrentDate() {
        return currentDate;
    }
}
