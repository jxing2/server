package cn.sagacloud.pojo;
/*
 * Author: Jxing
 * Create Time: 2019/3/11
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Command {
    SendTask,
    RefuseTask,
    AcceptTask,
    DownloadError,
    CommandError,
    TaskSuccess,
    ClientInfo;
    private static ArrayList<Command> cmdList;
    static{
        cmdList = new ArrayList<>();
        cmdList.addAll(Arrays.asList(Command.values()));
    }
    // 是否是和任务状态转换相关的命令
    public static boolean isStatusRelated(Command cmd){
        if(ClientInfo.equals(cmd))
            return false;
        return true;
    }
}
