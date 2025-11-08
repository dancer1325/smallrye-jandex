package com.example;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;
import org.jboss.jandex.Index;
import org.jboss.jandex.IndexReader;
import org.jboss.jandex.IndexWriter;
import org.jboss.jandex.Indexer;
import org.jboss.jandex.MethodInfo;


public class App
{
    public static void main( String[] args ) throws IOException {
        createPersistentIndexViaAPI();
        loadPersistentIndexViaAPI();
    }

    private static void createPersistentIndexViaAPI() throws IOException {
        // index a class
        Indexer indexer = new Indexer();
        indexer.indexClass(Map.class);
        Index index = indexer.complete();

        // persist the index | disk
        //      file can be used LATER
        try (FileOutputStream out = new FileOutputStream("/tmp/index.idx")) {
            IndexWriter writer = new IndexWriter(out);
            writer.write(index);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static  void loadPersistentIndexViaAPI() throws IOException {
        // load the index -- from the -- PREVIOUS example
        Index index;
        try (FileInputStream input = new FileInputStream("/tmp/index.idx")) {
            IndexReader reader = new IndexReader(input);
            index = reader.read();
        }

        // print ALL java.util.Map's methods -- via -- this index
        ClassInfo clazz = index.getClassByName(DotName.createSimple("java.util.Map"));

        for (MethodInfo method : clazz.methods()) {
            System.out.println(method);
        }
    }
}
