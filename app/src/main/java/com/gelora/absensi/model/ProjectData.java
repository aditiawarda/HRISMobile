package com.gelora.absensi.model;

public class ProjectData {

    private String projectId;
    private String projectName;
    private String categoryId;
    private String categoryName;
    private String progressPercent;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getProgressPercent() {
        return progressPercent;
    }

    public void setProgressPercent(String progressPercent) {
        this.progressPercent = progressPercent;
    }
}
