package cn.sagacloud.mybatis;

import cn.sagacloud.guice.InjectorInit;
import cn.sagacloud.mybatis.dao.TaskDAO;
import cn.sagacloud.mybatis.model.DownloadTaskModel;
import cn.sagacloud.mybatis.model.TaskModel;
import cn.sagacloud.mybatis.service.TaskService;
import com.google.inject.Inject;
import org.junit.Test;

public class TaskTest {
    TaskService service = InjectorInit.getInjector().getInstance(TaskService.class);
    @Test
    public void addTask() throws Exception {
        //service = new TaskService(new TaskDAO());
        TaskModel task = new TaskModel();
        task.setTask_cmd("1");
        task.setTask_name("a");
        task.setTask_status(0);
        task.setTask_expected_finish_time(60);
        DownloadTaskModel down = new DownloadTaskModel();
        down.setTask_url("http://down.xitongwanjia.com/windows10_64_201903.iso");
        down.setTask_md5("fagdasfasdsfsdff");
        task.getDownloadTaskModelList().add(down);
        System.out.println(service.addTask(task));
    }
}
