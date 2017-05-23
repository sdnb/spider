package com.ct;

/**
 * Created by chentao on 2017/5/23 0023.
 */
public class SpiderConfig {
    private String              domain;
    private String              uri;
    private int                 timeOutMillSec;
    private String              storeFilePath;

//    private Map<String, String> params;
//
//    public Map<String, String> getParams() {
//        return params;
//    }
//
//    public void setParams(Map<String, String> params) {
//        this.params = params;
//    }

    public String getUrl() {
        return domain + uri;
    }

    public String getStoreFilePath() {
        return storeFilePath;
    }

    public void setStoreFilePath(String storeFilePath) {
        this.storeFilePath = storeFilePath;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getTimeOutMillSec() {
        return timeOutMillSec;
    }

    public void setTimeOutMillSec(int timeOutMillSec) {
        this.timeOutMillSec = timeOutMillSec;
    }
}
