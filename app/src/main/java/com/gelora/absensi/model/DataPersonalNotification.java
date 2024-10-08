package com.gelora.absensi.model;


public class DataPersonalNotification {

    private String id;
    private String type;
    private String sub_type;
    private String notif_from;
    private String notif_from_name;
    private String notif_for;
    private String direct_id;
    private String data_owner;
    private String data_owner_name;
    private String status_read;
    private String created_at;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubType() {
        return sub_type;
    }

    public void setSubType(String subType) {
        this.sub_type = subType;
    }

    public String getNotif_from() {
        return notif_from;
    }

    public void setNotif_from(String notif_from) {
        this.notif_from = notif_from;
    }

    public String getNotif_from_name() {
        return notif_from_name;
    }

    public void setNotif_from_name(String notif_from_name) {
        this.notif_from_name = notif_from_name;
    }

    public String getNotif_for() {
        return notif_for;
    }

    public void setNotif_for(String notif_for) {
        this.notif_for = notif_for;
    }

    public String getDirect_id() {
        return direct_id;
    }

    public void setDirect_id(String direct_id) {
        this.direct_id = direct_id;
    }

    public String getData_owner() {
        return data_owner;
    }

    public void setData_owner(String data_owner) {
        this.data_owner = data_owner;
    }

    public String getData_owner_name() {
        return data_owner_name;
    }

    public void setData_owner_name(String data_owner_name) {
        this.data_owner_name = data_owner_name;
    }

    public String getStatus_read() {
        return status_read;
    }

    public void setStatus_read(String status_read) {
        this.status_read = status_read;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

}
