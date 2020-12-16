package org.DeviceM.util;

import org.DeviceM.App;
import org.DeviceM.mapper.FunctionMapper;
import org.apache.ibatis.session.SqlSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.function.Function;

public class Transaction {
    public static <T> T getMapper(Class<T> clazz) {
        InvocationHandler handler = (proxy, method, args) -> {
            // try结束之后，session自动关闭
            try (SqlSession session = App.sessionFactory.openSession()) {
                Object mapper = session.getMapper(FunctionMapper.class);
                try {
                    Object result = method.invoke(mapper, args);
                    session.commit();
                    return result;
                } catch (Exception e) {
                    session.rollback();
                    throw e;
                }
            }
        };
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, handler);
    }

    public static <R> Object start(Function<SqlSession, R> service) {
        try (SqlSession session = App.sessionFactory.openSession()) {
            try {
                Object result = service.apply(session);
                session.commit();
                return result;
            } catch (Exception e) {
                session.rollback();
                throw e;
            }
        }
    }
}
