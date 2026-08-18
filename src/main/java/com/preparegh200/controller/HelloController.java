package com.preparegh200.controller;

import java.io.File;
import java.io.FileWriter;
import java.util.Date;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String hello() {
        return "Hello from prepare_gh200 Spring Boot app!";
    }

    @GetMapping("/add")
    public String add() {
        // write to data.txt located under the container WORKDIR (System property `user.dir`)
        System.out.println("Current working directory: " + System.getProperty("user.dir"));
        File file = new File(System.getProperty("user.dir"), "data.txt");
        try {
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
                System.out.println("Writing to file: " + file.getAbsolutePath());
            }
            try (FileWriter writer = new FileWriter(file, true)) {
                System.out.println("Item added at " + new Date() + "\n");
                writer.write("Item added at " + new Date() + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error adding item: " + e.getMessage();
        }
        return "Item added!";
    }
}