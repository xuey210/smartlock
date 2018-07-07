package com.sectong.infrared;

/**
 * Created by xueyong on 16/9/9.
 * mobileeasy.
 */
public class FormulaParam {

    public FormulaParam() {

    }

    public FormulaParam(int onoff) {
        this.onoff = onoff;
    }

    public FormulaParam(String dvdCode) {
        this.dvdCode = dvdCode;
    }

    public static FormulaParam onoffInstance(int onoff) {
        FormulaParam formulaParam = new FormulaParam();
        formulaParam.setOnoff(onoff);
        return formulaParam;
    }

    public static FormulaParam modelInstance(int model) {
        FormulaParam formulaParam = new FormulaParam();
        formulaParam.setMode(model);
        return formulaParam;
    }

    public static FormulaParam tempInstance(int temp) {
        FormulaParam formulaParam = new FormulaParam();
        formulaParam.setTemp(temp);
        return formulaParam;
    }

    public static FormulaParam windInstance(int wind) {
        FormulaParam formulaParam = new FormulaParam();
        formulaParam.setWind(wind);
        return formulaParam;
    }

    public static FormulaParam winddirInstance(int winddir) {
        FormulaParam formulaParam = new FormulaParam();
        formulaParam.setWinddir(winddir);
        return formulaParam;
    }

    public static FormulaParam keyInstance(int key) {
        FormulaParam formulaParam = new FormulaParam();
        formulaParam.setKey(key);
        return formulaParam;
    }

    public static FormulaParam tvCodeInstance(String tvcode) {
        FormulaParam formulaParam = new FormulaParam();
        formulaParam.setTvCode(tvcode);
        return formulaParam;
    }

    public static FormulaParam stbCodeInstance(String stbCode) {
        FormulaParam formulaParam = new FormulaParam();
        formulaParam.setStbCode(stbCode);
        return formulaParam;
    }

    public static FormulaParam dvdCodeInstance(String dvdCode) {
        FormulaParam formulaParam = new FormulaParam();
        formulaParam.setDvdCode(dvdCode);
        return formulaParam;
    }

    public static FormulaParam fanCodeInstance(String fanCode) {
        FormulaParam formulaParam = new FormulaParam();
        formulaParam.setFanCode(fanCode);
        return formulaParam;
    }

    public static FormulaParam iptvCodeInstance(String iptvCode) {
        FormulaParam formulaParam = new FormulaParam();
        formulaParam.setIptvCode(iptvCode);
        return formulaParam;
    }

    public static FormulaParam aclCodeInstance(String aclCode) {
        FormulaParam formulaParam = new FormulaParam();
        formulaParam.setAclCode(aclCode);
        return formulaParam;
    }

    private int onoff = 0;
    private int mode = 0;
    private int temp = 0;
    private int wind = 0;
    private int winddir = 0;
    private int key = 0;
    private String _15key = "ON/OFF";

    private String tvCode;
    private String stbCode;
    private String dvdCode;
    private String fanCode;
    private String aclCode;
    private String iptvCode;

    public String getTvCode() {
        return tvCode;
    }

    public void setTvCode(String tvCode) {
        this.tvCode = tvCode;
    }

    public String getStbCode() {
        return stbCode;
    }

    public void setStbCode(String stbCode) {
        this.stbCode = stbCode;
    }

    public String getDvdCode() {
        return dvdCode;
    }

    public void setDvdCode(String dvdCode) {
        this.dvdCode = dvdCode;
    }

    public String getFanCode() {
        return fanCode;
    }

    public void setFanCode(String fanCode) {
        this.fanCode = fanCode;
    }

    public String getAclCode() {
        return aclCode;
    }

    public void setAclCode(String aclCode) {
        this.aclCode = aclCode;
    }

    public String getIptvCode() {
        return iptvCode;
    }

    public void setIptvCode(String iptvCode) {
        this.iptvCode = iptvCode;
    }

    public String get_15key() {
        return _15key;
    }

    public void set_15key(String _15key) {
        this._15key = _15key;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getOnoff() {
        return onoff;
    }

    public void setOnoff(int onoff) {
        this.onoff = onoff;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public int getWind() {
        return wind;
    }

    public void setWind(int wind) {
        this.wind = wind;
    }

    public int getWinddir() {
        return winddir;
    }

    public void setWinddir(int winddir) {
        this.winddir = winddir;
    }
}
