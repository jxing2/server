package cn.sagacloud.mybatis;

import cn.sagacloud.guice.InjectorInit;
import cn.sagacloud.mybatis.dao.TaskDAO;
import cn.sagacloud.mybatis.model.DownloadTaskModel;
import cn.sagacloud.mybatis.model.TaskModel;
import cn.sagacloud.mybatis.service.TaskService;
import com.google.inject.Inject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TaskTest {
    TaskService service = InjectorInit.getInjector().getInstance(TaskService.class);
    @Test
    public void addTask() throws Exception {
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

    @Test
    public void getTaskByIds() throws Exception {
        List<Integer> ids = new ArrayList<>();
        ids.add(0);ids.add(9);ids.add(11);
        ArrayList<TaskModel> tasks = service.getTaskByIds(ids);
        System.out.println(tasks);
    }

    @Test
    public void getTaskByStatus() throws Exception {
        List<Integer> status = new ArrayList<>();
        status.add(0);status.add(9);status.add(11);
        ArrayList<TaskModel> tasks = service.getAllTaskByStatus(status);
        System.out.println(tasks);
    }

    @Test
    public void getTaskMapByIds() throws Exception {
        List<Integer> ids = new ArrayList<>();
        ids.add(0);ids.add(9);ids.add(11);
        Map<Integer, TaskModel> tasks = service.getTaskMapByIds(ids);
        System.out.println(tasks);
    }

    @Test
    public void getTaskMapByStatus() throws Exception {
        List<Integer> status = new ArrayList<>();
        status.add(0);status.add(9);status.add(11);
        Map<Integer, TaskModel> tasks = service.getAllTaskMapByStatus(status);
        System.out.println(tasks);
    }

    @Test
    public void updateTask() throws Exception {
        TaskModel task = new TaskModel();
        task.setId(9);
        task.setTask_cmd("1");
        task.setTask_name("a");
        task.setTask_status(1);
        task.setTask_expected_finish_time(60);
        System.out.println(service.updateTask(task));
    }
}
