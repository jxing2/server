package cn.sagacloud.mybatis;

import java.io.IOException;
import java.io.InputStream;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class MyBatisSqlSessionFactory {
    private MyBatisSqlSessionFactory(){}
    private static volatile SqlSessionFactory sqlSessionFactory = null;
    public static SqlSessionFactory getSqlSessionFactory(){
        if(sqlSessionFactory==null){
            InputStream inputStream;
            try {
                synchronized(MyBatisSqlSessionFactory.class) {
                    if (sqlSessionFactory == null) {
                        inputStream = Resources.getResourceAsStream("mybatis-config.xml");
                        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
                    }
                }
//                sqlSessionFactory.getConfiguration().addMapper(UserMapper.class);
//                sqlSessionFactory.getConfiguration().addMapper(ManufacturerMapper.class);
//                sqlSessionFactory.getConfiguration().addMapper(BrandMapper.class);
            }
            catch(IOException e){
                System.out.println(e.getMessage());
                throw new RuntimeException(e.getCause());
            }
        }
        return sqlSessionFactory;
    }
    public static SqlSession openSession(){
        return getSqlSessionFactory().openSession();
    }
}
