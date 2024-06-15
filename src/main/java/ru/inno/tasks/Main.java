package ru.inno.tasks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.inno.tasks.service.DataMaker;

import java.io.IOException;

@SpringBootApplication(scanBasePackages = "ru.inno.tasks")
public class Main {

    public static void main(String[] args) throws IOException {
        ApplicationContext ctx = SpringApplication.run(Main.class);
        DataMaker mk = ctx.getBean(DataMaker.class);
        mk.make();

    }

}
