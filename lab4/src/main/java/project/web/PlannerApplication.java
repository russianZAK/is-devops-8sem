package project.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableJpaRepositories("project.web.repositories")
public class PlannerApplication {
  public static void main(String[] args) {
    SpringApplication.run(PlannerApplication.class);
  }
}
