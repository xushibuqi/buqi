package com.example.demo;

import io.ebean.Model;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Entity class representing tenants.
 */
@Entity
@Table(name = "tenants")
@Data
public class oldt extends Model {
    @Column(name = "id")  // Database field: id
    private String id;

    @Column(name = "enabled")  // Database field: enabled
    private Integer enabled;

    @Column(name = "initFlag")  // Database field: init_flag
    private Integer init_flag;

    @Column(name = "address")  // Database field: address
    private String address;

    @Column(name = "deviceLimitNumbers")  // Database field: device_limit_numbers
    private Integer deviceLimitNumbers;

    @Column(name = "companyname")  // Database field: companyname
    private String companyName;

    @Column(name = "email")  // Database field: email
    private String email;

    @Column(name = "years")  // Database field: years
    private Integer years;

    @Column(name = "state")  // Database field: state
    private Integer state;

    @Column(name = "code")  // Database field: code
    private String code;

    @Column(name = "telephone")  // Database field: telephone
    private String telephone;

    @Column(name = "authorizeRequestId")  // Database field: authorize_request_id
    private String authorizeRequestId;

    @Column(name = "authorizedStartTime")  // Database field: authorized_start_time
    private java.util.Date authorizedStartTime;

    @Column(name = "authorizedEndTime")  // Database field: authorized_end_time
    private java.util.Date authorizedEndTime;

    @Column(name = "random")  // Database field: random
    private String random;

    @Column(name = "period")  // Database field: period
    private Integer period;

    @Column(name = "unit")  // Database field: unit
    private String unit;

    @Column(name = "ftpIp")  // Database field: ftp_ip
    private String ftpIp;


    @Column(name = "ftpPassword")  // Database field: ftp_password
    private String ftpPassword;

    @Column(name = "acsURL")  // Database field: acs_url
    private String acsURL;

    @Column(name = "cloudOltURL")  // Database field: cloud_olt_url
    private String cloudOltURL;

    @Column(name = "cloudOltUsername")  // Database field: cloud_olt_username
    private String cloudOltUsername;

    @Column(name = "cloudOltPassword")  // Database field: cloud_olt_password
    private String cloudOltPassword;

    @Column(name = "cloudOltState")  // Database field: cloud_olt_state
    private Integer cloudOltState;

    @Column(name = "created_at")  // Database field: created_at
    private java.util.Date created_at;

    @Column(name = "updated_at")  // Database field: updated_at
    private java.util.Date updated_at;
}
