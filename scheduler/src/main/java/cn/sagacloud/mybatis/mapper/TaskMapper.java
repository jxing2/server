package cn.sagacloud.mybatis.mapper;

import cn.sagacloud.mybatis.model.DownloadTaskModel;
import cn.sagacloud.mybatis.model.TaskModel;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface TaskMapper {
    int addTask(TaskModel task);

    int addDownload(@Param("list")List<DownloadTaskModel> downloadTaskModelList, @Param("taskId")int id);

    ArrayList<TaskModel> getTasksByIds(List<Integer> idList);

    ArrayList<TaskModel> getTasksByStatus(List<Integer> statusList);

    int updateSingleTask(TaskModel task);
    @MapKey("id")
    Map<Integer, TaskModel> getTasksMapByIds(List<Integer> idList);
    @MapKey("id")
    Map<Integer, TaskModel> getTasksMapByStatus(List<Integer> statusList);
}
