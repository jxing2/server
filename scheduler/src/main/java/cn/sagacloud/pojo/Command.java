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
    TaskSuccess;
    private static ArrayList<Command> cmdList;
    static{
        cmdList = new ArrayList<>();
        cmdList.addAll(Arrays.asList(Command.values()));
    }
    public static Command getCmdById(int index) {
        if(index > -1 && index < cmdList.size()){
            return cmdList.get(index);
        }
        return null;
    }
}
