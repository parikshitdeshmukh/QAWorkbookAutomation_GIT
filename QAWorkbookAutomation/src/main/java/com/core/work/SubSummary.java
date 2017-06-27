package com.core.work;

/**
 * Created by SunilDeP on 6/22/2017.
 */
public class SubSummary {

    int critical;
    int trivial;
    int cautonary;

    public SubSummary(int critical, int trivial, int cautonary) {
        this.critical = critical;
        this.trivial = trivial;
        this.cautonary = cautonary;
    }

    public int getCritical() {
        return critical;
    }

    public void setCritical(int critical) {
        this.critical = critical;
    }

    public int getTrivial() {
        return trivial;
    }

    public void setTrivial(int trivial) {
        this.trivial = trivial;
    }

    public int getCautonary() {
        return cautonary;
    }

    public void setCautonary(int cautonary) {
        this.cautonary = cautonary;
    }
}

