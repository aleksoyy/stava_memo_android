package com.stavamemo.examples.stava.Model;

public class MemoModel {

    private int mid, uid;
    private String mtitle, mdate, mcont;

    public int getMid() {
        return mid;
    }
    public void setMid(int mid) {
        this.mid = mid;
    }

    public int getUid() {
        return uid;
    }
    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getMtitle() {
        return mtitle;
    }
    public void setMtitle(String mtitle) {
        this.mtitle = mtitle;
    }

    public String getMdate() {
        return mdate;
    }
    public void setMdate(String mdate) {
        this.mdate = mdate;
    }

    public String getMcont() {
        return mcont;
    }
    public void setMcont(String mcont) {
        this.mcont = mcont;
    }
}
