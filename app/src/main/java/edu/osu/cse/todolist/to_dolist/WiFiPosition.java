package edu.osu.cse.todolist.to_dolist;

import java.util.List;

/**
 * Created by Sniper on 2015/11/3.
 */
public class WiFiPosition extends Model {

    private List<AccessPoint> APs;
    private ConfigType mConfig;

    /**
     * Config for how the WiFiPosition works
     */
    public enum ConfigType {
        /**
         * Basic mode, only check one AccessPoint is connected or not
         */
        BASIC
    }

    public ConfigType getConfig() {
        return mConfig;
    }

    public void setConfig(ConfigType config) {
        mConfig = config;
    }

    public void addAP(AccessPoint AP) {

    }

    // TODO: modify the way of remove AP, maybe by SSID or MAC
    public AccessPoint removeAP(AccessPoint AP) {
        return null;
    }

    public List<AccessPoint> removeAllAP() {
        return null;
    }

    /**
     * Check if it is in the specific location defined by WiFi
     *
     * @return <code>true</code> if it's in the position it defines, otherwise <code>false</code>
     */
    public boolean isInRange() {
        return false;
    }

}
