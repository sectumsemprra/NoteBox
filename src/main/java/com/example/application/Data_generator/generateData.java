package com.example.application.Data_generator;

import com.example.application.services.UserRepository;
import com.vaadin.flow.spring.annotation.SpringComponent;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@SpringComponent
public class generateData {

    @Bean
    public CommandLineRunner loadData(UserRepository userRepository) {
        return args -> {
            Logger logger = LoggerFactory.getLogger(getClass());
            int seed = 123;

            logger.info("Generating demo data");


            //userRepository.save(new Userr("moushi", "m", Role.USER));
            //userRepository.save(new Userr("a", "a", Role.ADMIN, "iut"));


            logger.info("Generated demo data");
        };
    }

}