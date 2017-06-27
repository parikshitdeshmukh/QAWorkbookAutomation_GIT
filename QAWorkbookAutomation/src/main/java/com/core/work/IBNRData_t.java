package com.core.work;

/**
 * Created by SunilDeP on 5/30/2017.
 */
public class IBNRData_t {
    String population;
    String displayName;
    String cost_Basis;
    String period;
    Double incurrred_claims;
    Double paid_claims;

    public IBNRData_t(String population, String displayName, String cost_Basis, String period, Double incurrred_claims, Double paid_claims) {
        this.population = population;
        this.displayName = displayName;
        this.cost_Basis = cost_Basis;
        this.period = period;
        this.incurrred_claims = incurrred_claims;
        this.paid_claims = paid_claims;
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

    public Double getIncurrred_claims() {
        return incurrred_claims;
    }

    public void setIncurrred_claims(Double incurrred_claims) {
        this.incurrred_claims = incurrred_claims;
    }

    public Double getPaid_claims() {
        return paid_claims;
    }

    public void setPaid_claims(Double paid_claims) {
        this.paid_claims = paid_claims;
    }
}
