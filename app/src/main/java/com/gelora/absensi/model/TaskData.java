package com.gelora.absensi.model;

public class TaskData {

    private String taskname;
    private String pic;
    private String status;
    private String date;
    private String progressDate;
    private String timeline;

    public String getTaskName() {
        return taskname;
    }

    public void setTaskName(String taskname) {
        this.taskname = taskname;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProgressDate() {
        return progressDate;
    }

    public void setProgressDate(String progressDate) {
        this.progressDate = progressDate;
    }

    public String getTimeline() {
        return timeline;
    }

    public void setTimeline(String timeline) {
        this.timeline = timeline;
    }

}
