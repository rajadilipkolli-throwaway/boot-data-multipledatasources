package com.example.multipledatasources;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.PostgreSQLContainer;

@TestConfiguration(proxyBeanMethods = false)
public class TestMultipleDataSourcesApplication {

    @Bean
    // @DependsOn("mySQLContainer")
    PostgreSQLContainer<?> postgreSQLContainer(DynamicPropertyRegistry dynamicPropertyRegistry) {
        PostgreSQLContainer<?> postgreSQLContainer =
                new PostgreSQLContainer<>("postgres:16.1-alpine").withDatabaseName("memberdb");
        dynamicPropertyRegistry.add("app.datasource.member.url", postgreSQLContainer::getJdbcUrl);
        dynamicPropertyRegistry.add(
                "app.datasource.member.username", postgreSQLContainer::getUsername);
        dynamicPropertyRegistry.add(
                "app.datasource.member.password", postgreSQLContainer::getPassword);
        dynamicPropertyRegistry.add("spring.liquibase.url", postgreSQLContainer::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.liquibase.user", postgreSQLContainer::getUsername);
        dynamicPropertyRegistry.add("spring.liquibase.password", postgreSQLContainer::getPassword);
        return postgreSQLContainer;
    }

    @Bean
    MySQLContainer<?> mySQLContainer(DynamicPropertyRegistry dynamicPropertyRegistry) {
        MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.2");
        dynamicPropertyRegistry.add("app.datasource.cardholder.url", mySQLContainer::getJdbcUrl);
        dynamicPropertyRegistry.add(
                "app.datasource.cardholder.username", mySQLContainer::getUsername);
        dynamicPropertyRegistry.add(
                "app.datasource.cardholder.password", mySQLContainer::getPassword);
        dynamicPropertyRegistry.add("spring.flyway.url", mySQLContainer::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.flyway.user", mySQLContainer::getUsername);
        dynamicPropertyRegistry.add("spring.flyway.password", mySQLContainer::getPassword);
        return mySQLContainer;
    }

    public static void main(String[] args) {
        SpringApplication.from(MultipleDataSourcesApplication::main)
                .with(TestMultipleDataSourcesApplication.class)
                .run(args);
    }
}
