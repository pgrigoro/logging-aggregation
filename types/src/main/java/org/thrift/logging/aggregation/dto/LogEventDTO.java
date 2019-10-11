package org.thrift.logging.aggregation.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

import static org.thrift.logging.aggregation.util.DateUtil.FULL_DATE_FORMAT;
import static org.thrift.logging.aggregation.util.DateUtil.SHORT_DATE_FORMAT;
import static org.thrift.logging.aggregation.util.DateUtil.formatUTC;
import static org.thrift.logging.aggregation.util.DateUtil.parseUTC;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LogEventDTO {
    private int v;
    private String m;
    private String uuid;
    private String time;
    private LogLevel logLevel;
    private String hostIp;
    private String appName;
    private String userName;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getV() {
        return v;
    }

    public void setV(int version) {
        this.v = version;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getM() {
        return m;
    }

    public void setM(String message) {
        this.m = message;
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
    }

    public String getHostIp() {
        return hostIp;
    }

    public void setHostIp(String hostIp) {
        this.hostIp = hostIp;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @JsonIgnore
    public Date getTimeAsDate() {
        return parseUTC(time, FULL_DATE_FORMAT);
    }

    @JsonIgnore
    public void setTimeFromDate(Date date) {
        this.time = formatUTC(date, FULL_DATE_FORMAT);
    }

    @JsonIgnore
    public Integer getShortTime() {
        String shortTime = formatUTC(getTimeAsDate(), SHORT_DATE_FORMAT);
        return Integer.parseInt(shortTime);
    }
    
    @Override
    public String toString() {
        return "LogEventDTO{" +
                "uuid='" + uuid + '\'' +
                ", v=" + v +
                ", time=" + time +
                ", m='" + m + '\'' +
                ", logLevel=" + logLevel +
                ", hostIp='" + hostIp + '\'' +
                ", appName='" + appName + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
