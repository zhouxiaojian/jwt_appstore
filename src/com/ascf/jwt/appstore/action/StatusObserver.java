package com.ascf.jwt.appstore.action;

import java.util.Map;

/**
 * update Button 上的下载进度
 * @author XRFB74
 *
 */
public interface StatusObserver {

    void setProgressValue(int value);
    
    void setBtnStatus(int type);
    
    Map<String, String> getData();
}
