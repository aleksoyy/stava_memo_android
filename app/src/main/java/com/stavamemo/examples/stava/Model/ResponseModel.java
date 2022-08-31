package com.stavamemo.examples.stava.Model;

import java.util.List;

public class ResponseModel {
    private int code;
    private String message;

    private List<MemoModel> data;

    private int uid;
    private int mid;

    private String mtitle;
    private String mdate;
    private String mcont;

    public int getMid() {
        return mid;
    }
    public void setMid(int mid) {
        this.mid = mid;
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

    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public List<MemoModel> getData() {
        return data;
    }
    public void setData(List<MemoModel> data) {
        this.data = data;
    }

    public int getUid() {
        return uid;
    }
    public void setUid(int userId) {
        this.uid = uid;
    }
}
