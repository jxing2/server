package cn.sagacloud.mybatis.service;

import cn.sagacloud.mybatis.dao.TaskDAO;
import cn.sagacloud.mybatis.model.TaskModel;
import com.google.inject.*;
import org.mybatis.guice.transactional.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class TaskService {
    private TaskDAO taskDAO;

    @Inject
    public TaskService(TaskDAO taskDAO){
        this.taskDAO = taskDAO;
    }
    @Transactional
    public boolean addTask(TaskModel task) throws Exception {
        if(task.getDownloadTaskModelList().size() == 0)
            throw new Exception("不能添加没有下载文件的任务");
        if(taskDAO.addTask(task))
            return true;
        return false;
    }
    @Transactional
    public boolean updateTask(TaskModel task) throws Exception {
        ArrayList<TaskModel> tasks = taskDAO.getTasksByIds(Arrays.asList(task.getId()));
        if(tasks == null || tasks.size() == 0){
            return true;
        }
        TaskModel prev = tasks.get(0);
        return false;
    }


    public ArrayList<TaskModel> getAllTaskByStatus(List<Integer> statusList) throws Exception {
        if(statusList == null || statusList.size() == 0)
            return new ArrayList<>();
        ArrayList<TaskModel> tasks = taskDAO.getTasksByStatus(statusList);
        return tasks;
    }

    public ArrayList<TaskModel> getTaskByIds(List<Integer> idList) throws Exception {
        if(idList == null || idList.size() == 0)
            return new ArrayList<>();
        ArrayList<TaskModel> tasks = taskDAO.getTasksByIds(idList);
        return tasks;
    }
}
