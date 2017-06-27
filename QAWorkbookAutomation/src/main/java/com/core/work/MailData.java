package com.core.work;

/**
 * Created by SunilDeP on 6/21/2017.
 */
public class MailData {

    String Population;
    String Cost_Basis;
    String Period;
    String	Trend;
    double Staging_Data;
    double Production_Data;
    double Perc;
    double Critical;
    double Trivial;
    String result;


    public MailData(String population, String cost_Basis, String period, String trend, double staging_Data, double production_Data, double perc, double critical, double trivial, String result) {
        Population = population;
        Cost_Basis = cost_Basis;
        Period = period;
        Trend = trend;
        Staging_Data = staging_Data;
        Production_Data = production_Data;
        Perc = perc;
        Critical = critical;
        Trivial = trivial;
        this.result = result;
    }

    public String getPopulation() {
        return Population;
    }

    public void setPopulation(String population) {
        Population = population;
    }

    public String getCost_Basis() {
        return Cost_Basis;
    }

    public void setCost_Basis(String cost_Basis) {
        Cost_Basis = cost_Basis;
    }

    public String getPeriod() {
        return Period;
    }

    public void setPeriod(String period) {
        Period = period;
    }

    public String getTrend() {
        return Trend;
    }

    public void setTrend(String trend) {
        Trend = trend;
    }

    public double getStaging_Data() {
        return Staging_Data;
    }

    public void setStaging_Data(double staging_Data) {
        Staging_Data = staging_Data;
    }

    public double getProduction_Data() {
        return Production_Data;
    }

    public void setProduction_Data(double production_Data) {
        Production_Data = production_Data;
    }

    public double getPerc() {
        return Perc;
    }

    public void setPerc(double perc) {
        Perc = perc;
    }

    public double getCritical() {
        return Critical;
    }

    public void setCritical(double critical) {
        Critical = critical;
    }

    public double getTrivial() {
        return Trivial;
    }

    public void setTrivial(double trivial) {
        Trivial = trivial;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }


    @Override
    public String toString() {
        return "MailData{" +
                "Population='" + Population + '\'' +
                ", Cost_Basis='" + Cost_Basis + '\'' +
                ", Period='" + Period + '\'' +
                ", Trend='" + Trend + '\'' +
                ", Staging_Data=" + Staging_Data +
                ", Production_Data=" + Production_Data +
                ", Perc=" + Perc +
                ", Critical=" + Critical +
                ", Trivial=" + Trivial +
                ", result='" + result + '\'' +
                '}';
    }
}
