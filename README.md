# YesSir
## A Kotlin Autologger

![Image of YesSir](http://www.quickmeme.com/img/ea/ea716f8278f2c0aa9044513ef3b8acc2362567dc1a4456a27343f21a9f0b519f.jpg)

This library provides an annotation, `@LogMe` that will log your functions in order to monitor their execution and duration.
When a function is annotated, it will create a log before and after it's execution, showing it's execution time.

For Example:

```
class Foo {
    private val log = LoggerFactory.getLogger(this::class.java)

    @LogMe
    fun foo(){
        Thread.sleep(1000) // I.E.
        log.info("Hello World!")
    }
}
```

When foo() is executed will result in:

```
23:29:26.969 [main] INFO com.mfalcier.yessir.Foo - com.mfalcier.yessir.Foo.foo has started its execution
23:29:27.971 [main] INFO com.mfalcier.yessir.Foo - Hello World!
23:29:27.972 [main] INFO com.mfalcier.yessir.Foo - com.mfalcier.yessir.Foo.foo has ended its execution after 1000ms
```

The `@LogMe` annotation can also be used on classes, in order to automatically log each of its method:

```
@LogMe
class Foo {
    private val log = LoggerFactory.getLogger(this::class.java)

    fun foo(){
        Thread.sleep(1000) // I.E.
        log.info("Hello World!")
    }
}
```

In order to weave, your POM.xml plugins section must at least contains:

```
<plugins>
    <!-- KAPT EXECUTION -->
    <plugin>
        <artifactId>kotlin-maven-plugin</artifactId>
        <configuration>
            <jvmTarget>1.8</jvmTarget>
        </configuration>
        <groupId>org.jetbrains.kotlin</groupId>
        <version>${kotlin.version}</version>
        <executions>
            <execution>
                <id>kapt</id>
                <goals>
                    <goal>kapt</goal>
                </goals>
            </execution>
            <execution>
                <id>compile</id>
                <phase>compile</phase>
                <goals> <goal>compile</goal> </goals>
            </execution>
            <execution>
                <id>test-compile</id>
                <phase>test-compile</phase>
                <goals> <goal>test-compile</goal> </goals>
            </execution>
        </executions>
    </plugin>
    <!-- WEAVER -->
    <plugin>
        <groupId>com.jcabi</groupId>
        <artifactId>jcabi-maven-plugin</artifactId>
        <version>0.14.1</version>
        <configuration>
            <source>1.8</source>
            <target>1.8</target>
        </configuration>
        <executions>
            <execution>
                <goals>
                    <goal>ajc</goal>
                </goals>
            </execution>
        </executions>
        <dependencies>
            <dependency>
                <groupId>org.aspectj</groupId>
                <artifactId>aspectjrt</artifactId>
                <version>${aspectj.version}</version>
            </dependency>
            <dependency>
                <groupId>org.aspectj</groupId>
                <artifactId>aspectjtools</artifactId>
                <version>${aspectj.version}</version>
            </dependency>
        </dependencies>
    </plugin>
    <!-- FAT JAR -->
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-shade-plugin</artifactId>
        <version>2.4.3</version>
        <executions>
            <execution>
                <phase>package</phase>
                <goals>
                    <goal>shade</goal>
                </goals>
            </execution>
        </executions>
    </plugin>
</plugins>
```

## COMING SOON
Some way to directly include this library as a dependency to your project :sweat_smile:
