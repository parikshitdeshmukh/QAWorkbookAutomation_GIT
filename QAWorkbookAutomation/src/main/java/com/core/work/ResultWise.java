package com.core.work;

/**
 * Created by SunilDeP on 6/23/2017.
 */
public class ResultWise {


    String A;
    int critical=0;
    int trivial=0;
    int cautionay=0;

    public ResultWise(){};

    public ResultWise(String a, int critical, int trivial, int cautionay) {
        A = a;
        this.critical = critical;
        this.trivial = trivial;
        this.cautionay = cautionay;
    }

    public String getA() {
        return A;
    }

    public void setA(String a) {
        A = a;
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

    public int getCautionay() {
        return cautionay;
    }

    public void setCautionay(int cautionay) {
        this.cautionay = cautionay;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj ==null || obj.getClass() != this.getClass()){
            return false;
        }
        ResultWise anotherObject =(ResultWise) obj;
        return anotherObject.A==null? this.A==null : anotherObject.A.equals(this.A);
    }

    @Override
    public int hashCode() {
        return this.A.hashCode();
    }
}
