package org.sg01.example.lms.config;

import org.sg01.example.lms.helper.FooGroupTestHelper;
import org.sg01.example.lms.service.GroupService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class HelperTestConfig {

    @Bean
    public FooGroupTestHelper fooGroupTestHelper(GroupService groupService) {
        return new FooGroupTestHelper(groupService);
    }

}

