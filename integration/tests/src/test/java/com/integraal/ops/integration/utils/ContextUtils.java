package com.integraal.ops.integration.utils;

import com.integraal.ops.integration.utils.applications.components.ApplicationComponents;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ContextUtils {

    public static ApplicationComponents setupAppComponents(ApplicationContext applicationContext) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Field[] appComponentFields = ApplicationComponents.class.getDeclaredFields();
        var builder =  ApplicationComponents.builder();
        for (Field appComponent : appComponentFields) {
            Class<?> fieldDeclaredClass = appComponent.getType();
            Method builderSetterMethod = builder.getClass().getMethod(appComponent.getName(), fieldDeclaredClass);
            Object bean = applicationContext.getBean(fieldDeclaredClass);
            builderSetterMethod.invoke(builder, bean);
        }
        return builder.build();
    }
}
