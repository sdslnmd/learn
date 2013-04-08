package com.engineer.sun.serviceregister;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * User: sunluning
 * Date: 13-4-8 下午9:32
 */
public class RegisterAndFindService {
    private static Map<Class<?>, Object[]> instance = new LinkedHashMap<Class<?>, Object[]>();

    public void registerClass(Class<?> impl) {
        instance.put(impl, new Object[1]);
    }

    public static <T> T find(Class<T> whatType) {
        for (Map.Entry<Class<?>, Object[]> entry : instance.entrySet()) {
            if (whatType.isAssignableFrom(entry.getKey())) {
                if (entry.getValue()[0] == null) {
                    try {
                        entry.getValue()[0] = entry.getKey().newInstance();
                    } catch (Exception e) {
                        throw new IllegalStateException(e);
                    }
                }
                return whatType.cast(entry.getValue()[0]);
            }
        }
        return null;
    }
}
