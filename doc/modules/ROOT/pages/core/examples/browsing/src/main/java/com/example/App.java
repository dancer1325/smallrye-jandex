package com.example;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;
import org.jboss.jandex.FieldInfo;
import org.jboss.jandex.Index;
import org.jboss.jandex.Indexer;
import org.jboss.jandex.MethodInfo;
import org.jboss.jandex.Type;


public class App
{
    public static void main( String[] args ) throws IOException {
        browseAClass();
        searchForAnnotations();
    }

    private static void browseAClass() throws IOException {
        browseMap();
        browseCollections();
        browseTyeAnnotations();
    }

    private static void browseCollections() throws IOException {
        // 2. Collections
        // index a class
        Index indexCollections = Index.of(Collections.class);

        ClassInfo clazzCollections = indexCollections.getClassByName(DotName.createSimple("java.util.Collections"));
        Type listType = Type.create(DotName.createSimple("java.util.List"), Type.Kind.CLASS);
        MethodInfo sort = clazzCollections.method("sort", listType);

        Type t = sort.parameterTypes().get(0).asParameterizedType() // List<T extends Comparable<? super T>>
                .arguments().get(0).asTypeVariable()           // T extends Comparable<? super T>
                .bounds().get(0);                              // Comparable<? super T>

        System.out.println("browseAClass - Collections - List.sort's argument's base " + t);

        Type b = t.asParameterizedType()               // Comparable<? super T>
                .arguments().get(0).asWildcardType() // ? super T
                .superBound();                       // T

        System.out.println("browseAClass - Collections - List.sort's argument's base's argument base " + b);
    }

    private static void browseMap() throws IOException {
        // 1. Map
        // index a class
        Index indexMap = Index.of(Map.class);

        // browse class' methods
        ClassInfo clazzMap = indexMap.getClassByName(DotName.createSimple("java.util.Map"));

        for (MethodInfo method : clazzMap.methods()) {
            System.out.println("browseAClass - Map - " + method);
        }
    }

    private static void browseTyeAnnotations() throws IOException {
        Indexer indexer = new Indexer();
        indexer.indexClass(Test.class);
        indexer.indexClass(Test.Label.class);
        Index index = indexer.complete();

        FieldInfo field = index.getClassByName(Test.class).field("names");
        System.out.println( "browseTyeAnnotations " +
                field.type().asParameterizedType()                // Map<Integer, List<@Label("Name") String>>
                        .arguments().get(1).asParameterizedType() // List<@Label("Name") String>
                        .arguments().get(0)                       // @Label("Name") String
                        .annotations().get(0)                     // @Label("Name")
                        .value().asString()                       // "Name"
        );
    }

    private static void searchForAnnotations() throws IOException {
        // index classes
        Indexer indexer = new Indexer();
        indexer.indexClass(Thread.class);
        indexer.indexClass(String.class);
        Index index = indexer.complete();

        // search for methods / marked -- with -- `@Deprecated`
        DotName deprecated = DotName.createSimple("java.lang.Deprecated");
        List<AnnotationInstance> annotations = index.getAnnotations(deprecated);

        for (AnnotationInstance annotation : annotations) {
            switch (annotation.target().kind()) {
                case METHOD:
                    System.out.println("searchForAnnotations - " + annotation.target());
                    break;
            }
        }
    }
}
