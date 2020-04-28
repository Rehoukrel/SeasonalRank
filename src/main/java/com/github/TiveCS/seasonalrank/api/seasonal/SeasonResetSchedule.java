package com.github.TiveCS.seasonalrank.api.seasonal;

import java.time.Month;

public class SeasonResetSchedule {

    private Month month;
    private int resetDay;
    private SeasonalHandler handler;

    public SeasonResetSchedule(SeasonalHandler handler, String monthName, int resetDay){
        this.month = Month.valueOf(monthName.toUpperCase());
        this.resetDay = resetDay;
        this.handler = handler;
    }

    public void setResetDay(int resetDay) {
        this.resetDay = resetDay;
    }

    public SeasonalHandler getHandler() {
        return handler;
    }

    public int getResetDay() {
        return resetDay;
    }

    public Month getMonth() {
        return month;
    }
}
