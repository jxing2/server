package cn.sagacloud.mybatis.model;


import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.List;

public class TaskModel {
    private int id;
    private String task_name;
    private String task_cmd;
    private String task_param;
    @JSONField(serialize=false)
    private int task_status;
    private String task_result_json;
    @JSONField(serialize=false)
    private String task_last_client;
    @JSONField(serialize=false)
    private long task_sent_time;
    private long task_expected_finish_time;

    private List<DownloadTaskModel> downloadTaskModelList;

    public long getTask_sent_time() {
        return task_sent_time;
    }

    public void setTask_sent_time(long task_sent_time) {
        this.task_sent_time = task_sent_time;
    }

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

    public String getTask_last_client() {
        return task_last_client;
    }

    public void setTask_last_client(String task_last_client) {
        this.task_last_client = task_last_client;
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
