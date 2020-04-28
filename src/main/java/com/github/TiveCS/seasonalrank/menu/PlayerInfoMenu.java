package com.github.TiveCS.seasonalrank.menu;

import com.github.TiveCS.seasonalrank.SeasonalRank;
import com.github.TiveCS.seasonalrank.api.player.PlayerData;
import com.github.TiveCS.tivecore.lab.menu.ActiveMenu;
import com.github.TiveCS.tivecore.lab.menu.Menu;
import com.github.TiveCS.tivecore.lab.utils.xseries.XMaterial;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.io.File;
import java.util.Arrays;

public class PlayerInfoMenu extends Menu {

    private static SeasonalRank plugin = SeasonalRank.getPlugin(SeasonalRank.class);
    private final static File folder = new File(plugin.getDataFolder(), "menu");

    private PlayerData playerData = null;

    public PlayerInfoMenu() {
        super(folder, "seasonalrank-playerinfo", "&1%player_name% Stats", 4);

        addItem("border", XMaterial.BLACK_STAINED_GLASS_PANE, " ", 0,0, new int[]{0,1,2,3,5,6,7,8, 9,12,17, 18,21,26, 27,28,29,30,31,32,33,34,35});

        addItem("season-info", XMaterial.OAK_SIGN, "&c&lSeasonal Info", 1, Arrays.asList(" ", "&7- &fSeason ends in &e%seasonal_ends_left% &fday(s)"), 0,0, new int[]{4});

        addItem("current-rank", XMaterial.BOOK, "&eCurrent Rank", 1, Arrays.asList(" ", "&7- &f%player_current_rank_prefix%"), 0,0, new int[]{10});
        addItem("next-rank", XMaterial.KNOWLEDGE_BOOK, "&6Next Rank", 1, Arrays.asList(" ", "&7- &f%player_next_rank_prefix%"), 0,0, new int[]{19});

        addItem("current-points", XMaterial.EMERALD, "&3Current Points", 1, Arrays.asList(" ", "&7- &f%player_points%"), 0,0, new int[]{11});
        addItem("required-points", XMaterial.DIAMOND, "&bRequired Points", 1, Arrays.asList(" ", "&7- &f%player_required_points%"), 0,0, new int[]{20});

        addItem("points-by-raid", XMaterial.IRON_AXE, "&ePoints by Raids", 1, Arrays.asList(" ", "&7- &f%player_points_by_raid%"), 0,0, new int[]{22});
    }

    @Override
    public void action(ActiveMenu active, InventoryClickEvent event) {
        int slot = event.getSlot();
        event.setCancelled(true);
    }

    public PlayerData getPlayerData() {
        return playerData;
    }

    public void setPlayerData(PlayerData playerData) {
        this.playerData = playerData;
    }
}
