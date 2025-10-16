package com.example.todolistsf;

public class TableInfo {

    String title, priority, status, duedate;
    Integer id;

    public TableInfo(Integer id, String title, String priority, String status, String duedate) {
        this.id = id;
        this.title = title;
        this.priority = priority;
        this.status = status;
        this.duedate = duedate;
    }
    public Integer getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }

    public String getPriority() {
        return priority;
    }

    public String getStatus() {
        return status;
    }

    public String getDuedate() {
        return duedate;
    }

}

