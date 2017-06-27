package com.core.work;

/**
 * Created by SunilDeP on 5/25/2017.
 */
public class AllTrend_t {

    String population;
    String displayName;
    String cost_Basis;
    String period;
    String isCompleted;
    String trend;
    int trend_Order;
    double data;

    public AllTrend_t(){

    }

    public AllTrend_t(String population, String displayName, String cost_Basis, String period, String isCompleted, String trend, int trend_Order, double data) {
        this.population = population;
        this.displayName = displayName;
        this.cost_Basis = cost_Basis;
        this.period = period;
        this.isCompleted = isCompleted;
        this.trend = trend;
        this.trend_Order = trend_Order;
        this.data = data;
    }

    public String getPopulation() {
        return population;
    }

    public void setPopulation(String population) {
        this.population = population;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getCost_Basis() {
        return cost_Basis;
    }

    public void setCost_Basis(String cost_Basis) {
        this.cost_Basis = cost_Basis;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(String isCompleted) {
        this.isCompleted = isCompleted;
    }

    public String getTrend() {
        return trend;
    }

    public void setTrend(String trend) {
        this.trend = trend;
    }

    public int getTrend_Order() {
        return trend_Order;
    }

    public void setTrend_Order(int trend_Order) {
        this.trend_Order = trend_Order;
    }

    public double getData() {
        return data;
    }

    public void setData(double data) {
        this.data = data;
    }
}
