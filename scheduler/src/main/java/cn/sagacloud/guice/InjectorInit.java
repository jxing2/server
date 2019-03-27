package cn.sagacloud.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.mybatis.guice.XMLMyBatisModule;

public class InjectorInit {

    private static Injector injector;
    static {


        injector = Guice.createInjector(new XMLMyBatisModule() {
            @Override
            protected void initialize() {
                //setEnvironmentId("dev");
                setClassPathResource("mybatis-config.xml");
            }
        });
    }

    public static Injector getInjector() {
        return injector;
    }
}
