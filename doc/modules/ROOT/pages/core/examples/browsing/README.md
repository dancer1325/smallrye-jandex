# how has it been created?
* `mvn archetype:generate -DgroupId=com.example -DartifactId=indexing -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false`
* | [pom.xml](pom.xml), add
    ```
    <dependency>
      <groupId>io.smallrye</groupId>
      <artifactId>jandex</artifactId>
      <version>3.5.1</version>
    </dependency>
    ```

# how to run?
* `mvn clean compile`
* `mvn exec:java -Dexec.mainClass="com.example.App"`

# ways
## browse a class
* [`browseAClass()`](src/main/java/com/example/App.java)
## search for annotations
* [`searchForAnnotations()`](src/main/java/com/example/App.java)
## search for type annotations
* A type annotation can also be located by searching for the annotation.
  The target for a found type annotation is represented as a `TypeTarget`.
  The `TypeTarget` provides a reference to the annotated type, as well as the enclosing target that contains the type.
  The target itself can be a method, a class, or a field.
  The usage of that target can be a number of places, including parameters, return types, type parameters, type arguments, class extends values, type bounds and receiver types.
  Subclasses of `TypeTarget` provide the necessary information to locate the starting point of the usage.
  
  Since the particular type use can occur at any depth, the relevant branch of the type tree constrained by the above starting point must be traversed to understand the context of the use.
  
  Consider a declaration with complex nested generic type which contains a `@Label` annotation:
  
  [source,java]
  ----
  Map<Integer, List<@Label("Name") String>> names;
  ----
  
  The following code locates a type annotation and then inspects its location:
  
  [source,java]
  ----
  Indexer indexer = new Indexer();
  indexer.indexClass(Test.class);
  indexer.indexClass(Test.Label.class);
  Index index = indexer.complete();
  
  DotName label = DotName.createSimple("Test$Label");
  List<AnnotationInstance> annotations = index.getAnnotations(label);
  for (AnnotationInstance annotation : annotations) {
      if (annotation.target().kind() == AnnotationTarget.Kind.TYPE) {
          TypeTarget typeTarget = annotation.target().asType();
          System.out.println("Type usage is located within: " + typeTarget.enclosingTarget());
          System.out.println("Usage type: " + typeTarget.usage());
          System.out.println("Target type: " + typeTarget.target());
          System.out.println("Expected target? " + (typeTarget.enclosingTarget().asField().type()
                  .asParameterizedType().arguments().get(1)
                  .asParameterizedType().arguments().get(0)
                  == typeTarget.target()));
      }
  }
  ----
  
  The output is:
  
  [source]
  ----
  Type usage is located within: java.util.Map<java.lang.Integer, java.util.List<java.lang.@Label("Name") String>> Test.names
  Usage type: EMPTY
  Target type: java.lang.@Label("Name") String
  Expected target? true
  ----

