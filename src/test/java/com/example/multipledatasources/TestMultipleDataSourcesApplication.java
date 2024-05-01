package com.example.multipledatasources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.PostgreSQLContainer;

@TestConfiguration(proxyBeanMethods = false)
class TestMultipleDataSourcesApplication {

    @Autowired private DynamicPropertyRegistry dynamicPropertyRegistry;

    @Bean
    MySQLContainer<?> mySQLContainer() {
        MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.4");
        dynamicPropertyRegistry.add("app.datasource.cardholder.url", mySQLContainer::getJdbcUrl);
        dynamicPropertyRegistry.add(
                "app.datasource.cardholder.username", mySQLContainer::getUsername);
        dynamicPropertyRegistry.add(
                "app.datasource.cardholder.password", mySQLContainer::getPassword);
        return mySQLContainer;
    }

    @Bean
    PostgreSQLContainer<?> postgreSQLContainer() {
        PostgreSQLContainer<?> postgreSQLContainer =
                new PostgreSQLContainer<>("postgres:16.2-alpine");
        dynamicPropertyRegistry.add("app.datasource.member.url", postgreSQLContainer::getJdbcUrl);
        dynamicPropertyRegistry.add(
                "app.datasource.member.username", postgreSQLContainer::getUsername);
        dynamicPropertyRegistry.add(
                "app.datasource.member.password", postgreSQLContainer::getPassword);
        return postgreSQLContainer;
    }

    public static void main(String[] args) {
        SpringApplication.from(MultipleDataSourcesApplication::main)
                .with(TestMultipleDataSourcesApplication.class)
                .run(args);
    }
}
