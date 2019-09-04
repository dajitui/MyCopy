package com.example.demo.util;

import com.example.demo.entity.User;
import com.example.demo.entity.UserVO;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author M
 *
 * 对象复制以及队列复制
 */
@Component
public class MyBeanUtils {

    private static Map<String, BeanCopier> map = new HashMap<>();

    /**
     * 对象复制
     * @param obj1 被复制对象，为空会抛出异常
     * @param classz 复制类型
     * @param <T>
     * @return
     */
    public static <T> T copyObject(Object obj1, Class<T> classz) {
        if (obj1 == null || classz == null) {
            throw new IllegalArgumentException("复制对象或者被复制类型为空!");
        }
        Object obj2 = null;
        try {
            obj2 = classz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        String name = getClassName(obj1.getClass(), classz);
        BeanCopier beanCopier;
        if (map.containsKey(name)) {
            beanCopier = map.get(name);
        } else {
            beanCopier = BeanCopier.create(obj1.getClass(), classz, false);
            map.put(name, beanCopier);
        }
        beanCopier.copy(obj1, obj2, null);
        return (T) obj2;
    }

    /**
     * 复制队列
     * @param list 被复制队列
     * @param classz 复制类型
     * @param <T>
     * @return
     */
    public static <T> List<T> copyList(List<?> list, Class<T> classz) {
        if (CollectionUtils.isEmpty(list)) {
            throw new IllegalArgumentException("被复制的队列为空!");
        }
        List<Object> resultList = new LinkedList<>();
        for (Object obj1 : list) {
            resultList.add(copyObject(obj1, classz));
        }
        return (List<T>) resultList;
    }

    private static String getClassName(Class<?> class1, Class<?> class2) {
        return class1.getName() + class2.getName();
    }

    public static void main(String[] args) {
        User user = new User();
        user.setId(1L);
        user.setName("大鸡腿");
        User user1 = new User();
        user1.setId(2L);
        user1.setName("小鸡腿");
        UserVO vo = MyBeanUtils.copyObject(user, UserVO.class);
        System.out.println(vo);
        List<User> list = new ArrayList<>();
        list.add(user);
        list.add(user1);
        List<UserVO> list1 = MyBeanUtils.copyList(list, UserVO.class);
        System.out.println(list1);
    }


}
