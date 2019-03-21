package com.nucleomesh.spring.springexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(Configuration.class)
public class SpringExampleApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringExampleApplication.class, args);

  }

}
