package cn.sagacloud.mybatis.model;

public class DownloadTaskModel {
    private int id;
    private int task_id;
    private String task_url;
    private String task_md5;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public String getTask_url() {
        return task_url;
    }

    public void setTask_url(String task_url) {
        this.task_url = task_url;
    }

    public String getTask_md5() {
        return task_md5;
    }

    public void setTask_md5(String task_md5) {
        this.task_md5 = task_md5;
    }
}
