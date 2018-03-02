package io.example;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MyTest {
  @Autowired
  private Foo foo;
  @Autowired
  @Qualifier("handCraftedFoo")
  private Foo anotherFoo;

  @Test
  public void testPropertyAssignment() {
    Assert.assertEquals("blah", foo.getName());
    Assert.assertEquals("password", foo.getBar().getPassword());
    Assert.assertNull(foo.getEndpoint());

    Assert.assertEquals("boy", anotherFoo.getName());
    Assert.assertEquals("secret", anotherFoo.getBar().getPassword());
  }
}

///////////////////////// Domain objects
@Component
@ConfigurationProperties
class Foo {
  private String name;
  private Bar bar;
  private String endpoint;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Bar getBar() {
    return bar;
  }

  public void setBar(Bar bar) {
    this.bar = bar;
  }

  public String getEndpoint() {
    return endpoint;
  }

  public void setEndpoint(String endpoint) {
    this.endpoint = endpoint;
  }
}

@Component
@ConfigurationProperties(prefix = "bar")
class Bar {
  private String password;

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}

///////////////////////////// Configurations
@Configuration
class MyTestConfig {
  @Bean
  public Foo foo() {
    Foo foo = new Foo();
    foo.setBar(bar());
    return foo;
  }

  @Bean
  public Foo handCraftedFoo() {
    Foo foo = new Foo();
    foo.setName("boy");
    Bar bar = new Bar();
    bar.setPassword("secret");
    foo.setBar(bar);
    return foo;
  }

  @Bean
  public Bar bar() {
    return new Bar();
  }
}


//////////////////////////// Isolated application for running tests
@SpringBootApplication
class MyTestApp {
  public static void main(String[] args) {
    SpringApplication.run(MyTestApp.class, args);
  }
}
