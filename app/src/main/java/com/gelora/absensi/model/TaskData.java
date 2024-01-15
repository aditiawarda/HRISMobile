package com.gelora.absensi.model;

public class TaskData {

    private String taksId;
    private String taskName;
    private String projectId;
    private String pic;
    private String progressPercent;

    public String getTaksId() {
        return taksId;
    }

    public void setTaksId(String taksId) {
        this.taksId = taksId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getProgressPercent() {
        return progressPercent;
    }

    public void setProgressPercent(String progressPercent) {
        this.progressPercent = progressPercent;
    }

}
