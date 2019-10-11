package org.thrift.logging.aggregation.domain;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "log_event")
public class LogEvent {

    @PartitionKey()
    @Column(name = "date_id")
    private int dateId;

    @PartitionKey(1)
    @Column(name = "app_name")
    private String appName;
    
    @PartitionKey(2)
    @Column(name = "log_level")
    private String logLevel;
    
    @ClusteringColumn
    private String uuid;

    @Column(name = "host_ip")
    private String hostIp;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "creation_date_utc")
    private Date creationDateUtc;
    
    private String message;
    
    private int version;

    public int getDateId() {
        return dateId;
    }

    public void setDateId(int dateId) {
        this.dateId = dateId;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getHostIp() {
        return hostIp;
    }

    public void setHostIp(String hostIp) {
        this.hostIp = hostIp;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getCreationDateUtc() {
        return creationDateUtc;
    }

    public void setCreationDateUtc(Date creationDateUtc) {
        this.creationDateUtc = creationDateUtc;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}