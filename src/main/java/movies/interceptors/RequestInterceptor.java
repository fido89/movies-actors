package movies.interceptors;

import movies.annotations.Stats;
import movies.beans.StatisticBean;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.ws.rs.*;
import java.lang.reflect.Method;

@Stats
@Interceptor
public class RequestInterceptor {

    @Inject
    StatisticBean statService;

    @AroundInvoke
    public Object processRequest(InvocationContext invocationContext) throws Exception {
        var method = invocationContext.getMethod();
        var clazz = method.getDeclaringClass();
        var clazzPathAnnotation = clazz.getAnnotation(Path.class);
        var methodPathAnnotation = method.getAnnotation(Path.class);
        String key = getHttpMethodAsString(method);

        if (clazzPathAnnotation != null && key != null) {
            key += " " + clazzPathAnnotation.value();
            if (methodPathAnnotation != null) {
                key += "/" + methodPathAnnotation.value();
            }
            statService.putToStatistic(key);
        }
        return invocationContext.proceed();
    }

    private String getHttpMethodAsString(Method method) {
        if (method.getAnnotation(GET.class) != null) {
            return "GET";
        } else if (method.getAnnotation(POST.class) != null) {
            return "POST";
        } else if (method.getAnnotation(DELETE.class) != null) {
            return "DELETE";
        } else if (method.getAnnotation(PUT.class) != null) {
            return "PUT";
        }
        return null;
    }
}
