package cn.sagacloud.mybatis.dao;

import cn.sagacloud.mybatis.MyBatisSqlSessionFactory;
import cn.sagacloud.mybatis.mapper.TaskMapper;
import cn.sagacloud.mybatis.model.TaskModel;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.guice.transactional.Transactional;

public class TaskDAO {


    public boolean addTask(TaskModel task) {
        SqlSession session = null;
        try {
            session = MyBatisSqlSessionFactory.openSession();
            TaskMapper taskMapper = session.getMapper(TaskMapper.class);
            int id = taskMapper.addTask(task);
            session.commit();
            //task.setId(id);
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (session != null)
                session.close();
        }
    }

    public boolean addDownload(TaskModel task) {
        SqlSession session = null;
        try {
            session = MyBatisSqlSessionFactory.openSession();
            TaskMapper taskMapper = session.getMapper(TaskMapper.class);
            if(task.getId() == 0)
                return false;
            if(task.getDownloadTaskModelList().size() == 0)
                return true;
            int infectedRow = taskMapper.addDownload(task.getDownloadTaskModelList(), task.getId());
            session.commit();
            if(infectedRow == task.getDownloadTaskModelList().size())
                return true;
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (session != null)
                session.close();
        }
    }
}
