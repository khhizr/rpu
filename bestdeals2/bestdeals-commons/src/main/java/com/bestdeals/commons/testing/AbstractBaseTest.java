package com.bestdeals.commons.testing;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.containers.localstack.LocalStackContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractBaseTest {
    private static DockerImageName localstackImage = DockerImageName.parse("localstack/localstack:0.11.3");
    private static DockerImageName mongoDbImage = DockerImageName.parse("mongo:5.0.6");

    public static MongoDBContainer mongoDBContainer;
    public static LocalStackContainer localStackContainer;

    @Autowired
    private MongoTemplate mongoTemplate;

    private RestTemplate restTemplate;

    @LocalServerPort
    private int serverPort;

    @Value("${server.servlet.context-path}")
    private String basePrefixForService;

    public String baseUrl;

    @Autowired
    public final void setRestTemplate(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    static {
        mongoDBContainer = new MongoDBContainer(mongoDbImage);
        localStackContainer = new LocalStackContainer(localstackImage).withServices(LocalStackContainer.Service.SNS);
        mongoDBContainer.start();
        localStackContainer.start();
    }

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.autoconfigure.exclude",
                () -> "org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration");
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @BeforeAll
    public void setup() {
        setupBaseUrl();
        setupDB();
    }

    private void setupBaseUrl() {
        baseUrl = "http://localhost:" + serverPort + "/" + basePrefixForService;
    }

    private void setupDB() {
        Set<String> collectionNames = mongoTemplate.getCollectionNames();
        for (String collectionName : collectionNames) {
            mongoTemplate.dropCollection(collectionName);
        }
        // TODO: create required collections
    }

    public static LocalStackContainer getLocalStackContainer() {
        return localStackContainer;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }
}
