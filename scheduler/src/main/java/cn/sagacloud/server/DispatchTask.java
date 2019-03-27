package cn.sagacloud.server;

import cn.sagacloud.guice.InjectorInit;
import cn.sagacloud.mybatis.service.TaskService;

public class DispatchTask implements Runnable {
    TaskService service = InjectorInit.getInjector().getInstance(TaskService.class);
    @Override
    public void run() {

    }
}
