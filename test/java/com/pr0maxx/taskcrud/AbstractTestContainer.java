package com.pr0maxx.taskcrud;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@SpringBootTest
public abstract class AbstractTestContainer {

    /*
    @Container
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:11.13")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");
    */
    @Container
    protected static final KafkaContainer kafkaContainer =
            new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:5.5.0"));

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        // PostgreSQL
        /*
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
        */
        // Kafka — исправлены ключи с точек на дефисы
        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
        registry.add("spring.kafka.consumer.group-id", () -> "test-group");
        registry.add("spring.kafka.producer.enable", () -> "true");
        registry.add("spring.kafka.session.timeout.ms", () -> "15000");
        registry.add("spring.kafka.max.partition.fetch.bytes", () -> "300000");
        registry.add("spring.kafka.max.poll.records", () -> "1");
        registry.add("spring.kafka.max.poll.interval.ms", () -> "3000");

        // Mail — заглушка на локальный "фейковый" SMTP
        registry.add("spring.mail.host", () -> "localhost");
        registry.add("spring.mail.port", () -> "2525");
        registry.add("spring.mail.test-connection", () -> "true");
    }
}
