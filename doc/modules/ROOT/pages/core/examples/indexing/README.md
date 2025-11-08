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
## -- via -- API
### Creating a Persistent Index 
* [`createPersistentIndexViaAPI()`](src/main/java/com/example/App.java)

### Loading a Persistent Index
* [`loadPersistentIndexViaAPI()`](src/main/java/com/example/App.java)

## -- via -- CLI
* `wget https://repo1.maven.org/maven2/io/smallrye/jandex/3.5.1/jandex-3.5.1.jar`
* `wget https://repo1.maven.org/maven2/org/hibernate/hibernate-core/5.6.15.Final/hibernate-core-5.6.15.Final.jar`
* `java -jar jandex-3.5.1.jar hibernate-core-5.6.15.Final.jar`
  * creates [hibernate-core-5.6.15.Final-jar.idx](hibernate-core-5.6.15.Final-jar.idx)
    * 's size << [hibernate-core-5.6.15.Final.jar](hibernate-core-5.6.15.Final.jar)

## -- via -- Ant Task
* TODO:
* The following Ant task can be used with either the `maven-antrun-plugin` or an Ant build to build an index for your project:
  
  [source,xml]
  ----
  <taskdef name="jandex" classname="org.jboss.jandex.JandexAntTask"/>
  <jandex run="@{jandex}">
      <fileset dir="${location.to.index.dir}"/>
  </jandex>
  ----

## -- via -- Maven Plugin
* TODO:
