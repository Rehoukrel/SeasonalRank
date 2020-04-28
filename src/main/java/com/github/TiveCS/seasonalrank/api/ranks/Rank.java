package com.github.TiveCS.seasonalrank.api.ranks;

import com.github.TiveCS.seasonalrank.SeasonalRank;
import com.github.TiveCS.seasonalrank.api.seasonal.SeasonalHandler;

public class Rank {

    private static SeasonalRank plugin = SeasonalRank.getPlugin(SeasonalRank.class);

    private SeasonalHandler handler;
    private String rankName;
    private String nextRank = null;
    private int requiredPoints;

    public Rank(SeasonalHandler handler, String rankName){
        this.handler = handler;
        this.rankName = rankName;
    }

    //------------------------------------

    public boolean hasNextRank(){
        return nextRank != null;
    }

    //------------------------------------

    public void setNextRank(String nextRank) {
        this.nextRank = nextRank;
    }

    public void setRequiredPoints(int requiredPoints) {
        this.requiredPoints = requiredPoints;
    }

    public String getNextRank() {
        return nextRank;
    }

    public int getRequiredPoints() {
        return requiredPoints;
    }

    public SeasonalHandler getHandler() {
        return handler;
    }

    public String getRankName() {
        return rankName;
    }
}
