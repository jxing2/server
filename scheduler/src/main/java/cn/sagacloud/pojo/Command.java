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
    public static Command getCmdById(int index) {
        if(index > -1 && index < cmdList.size()){
            return cmdList.get(index);
        }
        return null;
    }

    public static int getIdByCmd(Command cmd) {
        for(int i = 0; i < cmdList.size(); ++i){
            if(cmdList.get(i).equals(cmd))
                return i;
        }
        return -1;
    }
}
