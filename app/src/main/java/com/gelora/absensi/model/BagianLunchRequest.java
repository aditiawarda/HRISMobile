package com.gelora.absensi.model;


public class BagianLunchRequest {

    private String IdDept;
    private String IdHeadDept;
    private String KdDept;
    private String NmDept;
    private String status;

    public String getIdDept() {
        return IdDept;
    }

    public void setIdDept(String idDept) {
        IdDept = idDept;
    }

    public String getIdHeadDept() {
        return IdHeadDept;
    }

    public void setIdHeadDept(String idHeadDept) {
        IdHeadDept = idHeadDept;
    }

    public String getKdDept() {
        return KdDept;
    }

    public void setKdDept(String kdDept) {
        KdDept = kdDept;
    }

    public String getNmDept() {
        return NmDept;
    }

    public void setNmDept(String nmDept) {
        NmDept = nmDept;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
