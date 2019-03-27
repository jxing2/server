package cn.sagacloud.mybatis.model;


import java.util.ArrayList;
import java.util.List;

public class TaskModel {
    private int id;
    private String task_name;
    private String task_cmd;
    private String task_param;
    private int task_status;
    private String task_result_json;
    private String task_last_mac;
    private long task_expected_finish_time;

    private List<DownloadTaskModel> downloadTaskModelList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public String getTask_cmd() {
        return task_cmd;
    }

    public void setTask_cmd(String task_cmd) {
        this.task_cmd = task_cmd;
    }

    public String getTask_param() {
        return task_param;
    }

    public void setTask_param(String task_param) {
        this.task_param = task_param;
    }

    public int getTask_status() {
        return task_status;
    }

    public void setTask_status(int task_status) {
        this.task_status = task_status;
    }

    public String getTask_result_json() {
        return task_result_json;
    }

    public void setTask_result_json(String task_result_json) {
        this.task_result_json = task_result_json;
    }

    public String getTask_last_mac() {
        return task_last_mac;
    }

    public void setTask_last_mac(String task_last_mac) {
        this.task_last_mac = task_last_mac;
    }

    public long getTask_expected_finish_time() {
        return task_expected_finish_time;
    }

    public void setTask_expected_finish_time(long task_expected_finish_time) {
        this.task_expected_finish_time = task_expected_finish_time;
    }

    public List<DownloadTaskModel> getDownloadTaskModelList() {
        if(downloadTaskModelList == null )
            downloadTaskModelList = new ArrayList<>();
        return downloadTaskModelList;
    }

    public void setDownloadTaskModelList(List<DownloadTaskModel> downloadTaskModelList) {
        this.downloadTaskModelList = downloadTaskModelList;
    }
}
