# Spring Property Extensions
Provides helper classes that can be used to read various file types into the Spring Environment

## Maven

```xml
<dependencies>
    <dependency>
        <groupId>com.github.hsoj48</groupId>
        <artifactId>spring-property-extensions</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

## Usage

Create a flat file.  The filename will be used as the property key (without file extension) and the value will be the entire contents of the file.

>query_1.sql
>```sql
>SELECT *
>FROM DUAL
>```

Include the flat file in a PropertySource declaration and use the WholeFilePropertySourceFactory provided by this project.
```java
@Configuration
@PropertySource(value = "classpath:query_1.sql", factory = WholeFilePropertySourceFactory.class)
public class TestConfiguration {
    
}
```

Spring bean can now reference this value via the filename (without extension) from either the @Value annotation or from an autowired Environment.
```java
@Component
public class TestClass {
    
    @Value("${query_1}")
    private String query1;
}
```

```java
@Component
public class TestClass {
    
    @Autowired
    private Environment env;
    
    public void something() {
        env.getProperty("query_1");
    }
}
```

Wildcard placeholders are also supported by the WholeFilePropertySourceFactory which will include all matched files in a single PropertySource.  

>Note: PropertySources created in this manner have no particular order within their properties.
```java
@Configuration
@PropertySource(value = "classpath:*.sql", factory = WholeFilePropertySourceFactory.class)
public class TestConfiguration {
    
}
```

## Authors

- Josh Fields
