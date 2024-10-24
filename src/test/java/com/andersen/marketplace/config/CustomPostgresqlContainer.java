package com.andersen.marketplace.config;

import org.testcontainers.containers.PostgreSQLContainer;

public class CustomPostgresqlContainer extends PostgreSQLContainer<CustomPostgresqlContainer> {
    private static final String IMAGE_NAME = "postgres:15.1";
    private static CustomPostgresqlContainer container;

    public CustomPostgresqlContainer() {
        super(IMAGE_NAME);
    }

    public static CustomPostgresqlContainer getInstance() {
        if (container == null) {
            container = new CustomPostgresqlContainer()
                    .withDatabaseName("postgres")
                    .withUsername("postgres")
                    .withPassword("postgres");
            container.start();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
    }
}
