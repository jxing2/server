package cn.sagacloud.mybatis.dao;

import cn.sagacloud.mybatis.MyBatisSqlSessionFactory;
import cn.sagacloud.mybatis.mapper.TaskMapper;
import cn.sagacloud.mybatis.model.TaskModel;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.guice.transactional.Transactional;

import java.util.ArrayList;
import java.util.List;

public class TaskDAO {


    public boolean addTask(TaskModel task) {
        SqlSession session = null;
        try {
            session = MyBatisSqlSessionFactory.openSession();
            TaskMapper taskMapper = session.getMapper(TaskMapper.class);
            int affectedRow = taskMapper.addTask(task);
            boolean result = addDownload(task);
            if(!result){
                session.rollback();
            }
            return result;
        } catch (Exception e) {
            return false;
        } finally {
            if(session != null)
                session.commit();
            if (session != null)
                session.close();
        }
    }

    private boolean addDownload(TaskModel task) {
        SqlSession session = null;
        try {
            session = MyBatisSqlSessionFactory.openSession();
            TaskMapper taskMapper = session.getMapper(TaskMapper.class);
            if(task.getId() == 0)
                return false;
            if(task.getDownloadTaskModelList().size() == 0)
                return true;
            int infectedRow = taskMapper.addDownload(task.getDownloadTaskModelList(), task.getId());

            if(infectedRow == task.getDownloadTaskModelList().size())
                return true;
            return true;
        } catch (Exception e) {
            session.rollback();
            return false;
        } finally {
            if(session != null)
                session.commit();
            if (session != null)
                session.close();
        }
    }

    public ArrayList<TaskModel> getTasksByIds(List<Integer> idList) {
        SqlSession session = null;
        ArrayList<TaskModel> result = new ArrayList<>();
        try {
            session = MyBatisSqlSessionFactory.openSession();
            TaskMapper taskMapper = session.getMapper(TaskMapper.class);
            result = taskMapper.getTasksByIds(idList);
            session.commit();
        } catch (Exception ignore) {
        } finally {
            if (session != null)
                session.close();
        }
        return result;
    }

    public ArrayList<TaskModel> getTasksByStatus(List<Integer> statusList) {
        SqlSession session = null;
        ArrayList<TaskModel> result = new ArrayList<>();
        try {
            session = MyBatisSqlSessionFactory.openSession();
            TaskMapper taskMapper = session.getMapper(TaskMapper.class);
            result = taskMapper.getTasksByStatus(statusList);
            session.commit();
        } catch (Exception ignore) {
        } finally {
            if (session != null)
                session.close();
        }
        return result;
    }
}
