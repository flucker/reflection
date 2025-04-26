package org.example;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;

class Sample {
    String sampleString = "xxx";
}
class Person {
    String name = "Eugene";
    int age = 100;
    int[][] arr = {{1, 2}, {3, 4}, {5, 6}};
    Sample sample = new Sample();
}

public class App2 {
    public static void main(String[] args) throws IllegalAccessException {
        Person person = new Person();

        System.out.println(objToJSON(person));
    }

    public static String objToJSON(Object obj) throws IllegalAccessException {
        Class<?> cls = obj.getClass();
        StringBuilder sb = new StringBuilder();
        Field[] fields = cls.getDeclaredFields();

        sb.append("{");

        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(obj);
            if (field.getType().isArray()) {
                value = objToJSONArray(value);
            }  else if (value.getClass().getPackage().getName().equals(App2.class.getPackage().getName())) {
                value = objToJSON(value);
            } else {
                value = "\"" + value + "\"";
            }

            sb.append('"')
                    .append(field.getName())
                    .append('"')
                    .append(": ")
                    .append(value)
                    .append(',');

        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("}");

        return sb.toString();
    }

    public static String objToJSONArray(Object obj) throws IllegalAccessException {
        StringBuilder sb = new StringBuilder();
        sb.append("[");

        for (int i = 0; i < Array.getLength(obj); i++) {
            Object value = Array.get(obj, i);
            if (value.getClass().isArray()) {
                value = objToJSONArray(value);
            } else if (value.getClass().getPackage().getName().equals(App2.class.getPackage().getName())) {
                value = objToJSON(value);
            } else {
                value = "\"" + value + "\"";
            }
            sb.append(value).append(',');
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("]");

        return sb.toString();
    }
}
