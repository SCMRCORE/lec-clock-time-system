package com.clockcommon.utils;

import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class BeanCopyUtils {
    private BeanCopyUtils(){};

    public static <T> T copyBean(Object source,Class<T> target){
        T result = null;

        try {
            result = target.newInstance();
            BeanUtils.copyProperties(source, result);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static <T> List<T> copyBeanList(List<? extends Object> source, Class<T> target){
        return source.stream()
                .map(o -> copyBean(o, target))
                .collect(Collectors.toList());
    }
}
