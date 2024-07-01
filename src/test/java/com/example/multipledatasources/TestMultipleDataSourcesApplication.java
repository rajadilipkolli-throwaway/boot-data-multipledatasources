package com.example.multipledatasources;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.lifecycle.Startables;

@TestConfiguration(proxyBeanMethods = false)
class TestMultipleDataSourcesApplication {

    static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.4");

    static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:16.3-alpine");

    static {
        Startables.deepStart(mySQLContainer, postgreSQLContainer).join();
    }

    @DynamicPropertySource
    void addApplicationProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {

        dynamicPropertyRegistry.add("app.datasource.cardholder.url", mySQLContainer::getJdbcUrl);
        dynamicPropertyRegistry.add(
                "app.datasource.cardholder.username", mySQLContainer::getUsername);
        dynamicPropertyRegistry.add(
                "app.datasource.cardholder.password", mySQLContainer::getPassword);
        dynamicPropertyRegistry.add("app.datasource.member.url", postgreSQLContainer::getJdbcUrl);
        dynamicPropertyRegistry.add(
                "app.datasource.member.username", postgreSQLContainer::getUsername);
        dynamicPropertyRegistry.add(
                "app.datasource.member.password", postgreSQLContainer::getPassword);
    }

    public static void main(String[] args) {
        SpringApplication.from(MultipleDataSourcesApplication::main)
                .with(TestMultipleDataSourcesApplication.class)
                .run(args);
    }
}
