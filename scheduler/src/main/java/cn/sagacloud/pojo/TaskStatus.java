package cn.sagacloud.pojo;
/*
 * Author: Jxing
 * Create Time: 2019/3/11
 */

import java.util.ArrayList;
import java.util.Arrays;

public enum TaskStatus {
    // 顺序不可打乱, 添加要在后面添加
    Waiting, Sending, Sent, FileDownloadException, CommandExecuteException, Finished;
    private static ArrayList<TaskStatus> statusList;
    static{
        statusList = new ArrayList<>();
        statusList.addAll(Arrays.asList(TaskStatus.values()));
    }
    public static TaskStatus getTaskStatusById(int index) {
        if(index > -1 && index < statusList.size()){
            return statusList.get(index);
        }
        return null;
    }

    public static int getIdByTaskStatus(TaskStatus status) {
        if(status == null)
            return -1;
        for(int i = 0; i < statusList.size(); ++i){
            if(statusList.get(i).equals(status))
                return i;
        }
        return -1;
    }
}
