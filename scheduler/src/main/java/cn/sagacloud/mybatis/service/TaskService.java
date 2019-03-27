package cn.sagacloud.mybatis.service;

import cn.sagacloud.mybatis.dao.TaskDAO;
import cn.sagacloud.mybatis.model.TaskModel;
import com.google.inject.*;
import org.mybatis.guice.transactional.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class TaskService {
    public TaskDAO taskDAO;

    @Inject
    public TaskService(TaskDAO taskDAO){
        this.taskDAO = taskDAO;
    }
    @Transactional
    public boolean addTask(TaskModel task) throws Exception {
        if(task.getDownloadTaskModelList().size() == 0)
            throw new Exception("不能添加没有下载文件的任务");
        if(taskDAO.addTask(task) && taskDAO.addDownload(task))
            return true;
        return false;
    }
    @Transactional
    public boolean updateTask(TaskModel task) throws Exception {
        if(task.getDownloadTaskModelList().size() == 0)
            throw new Exception("不能添加没有下载文件的任务");
        taskDAO.addTask(task);
        taskDAO.addDownload(task);
        return false;
    }


    public ArrayList<TaskModel> getAllTaskByStatus(List<Integer> statusList) throws Exception {

        return null;
    }

    public TaskModel getTaskById(int id) throws Exception {

        return null;
    }
}
