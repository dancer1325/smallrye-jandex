package com.example;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.List;
import java.util.Map;

public class Test {
    public Map<Integer, List<@Label("Name") String>> names;

    @Target(ElementType.TYPE_USE)
    public @interface Label {
        String value();
    }
}
