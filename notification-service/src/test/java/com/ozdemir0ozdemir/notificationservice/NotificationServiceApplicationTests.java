package com.ozdemir0ozdemir.notificationservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class NotificationServiceApplicationTests extends AbstractIT {

    @Test
    void contextLoads() {}
}
