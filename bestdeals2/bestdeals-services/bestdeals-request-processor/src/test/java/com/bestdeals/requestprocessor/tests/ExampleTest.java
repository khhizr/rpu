package com.bestdeals.requestprocessor.tests;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.PublishResult;
import com.bestdeals.commons.testing.AbstractBaseTest;

import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class ExampleTest extends AbstractBaseTest {
    private static Logger LOGGER = LoggerFactory.getLogger(ExampleTest.class);

    @Autowired
    private AmazonSNS snsClient;

    @Test
    public void snsTest() {
        CreateTopicResult createTopicResult = snsClient.createTopic("otpNumbers");
        PublishResult publishResult = snsClient.publish(createTopicResult.getTopicArn(), "989898989");
        assertNotNull(publishResult);
        LOGGER.info(publishResult.toString());
    }
}