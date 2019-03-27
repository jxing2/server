package cn.sagacloud.mybatis.mapper;

import cn.sagacloud.mybatis.model.DownloadTaskModel;
import cn.sagacloud.mybatis.model.TaskModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TaskMapper {
    public int addTask(TaskModel task);

    public int addDownload(@Param("list")List<DownloadTaskModel> downloadTaskModelList, @Param("taskId")int id) ;
}
