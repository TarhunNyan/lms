package org.sg01.example.lms.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class FeatureFlagService {

    private final Environment environment;

    public boolean isDev() {
        return Arrays
                .asList(environment.getActiveProfiles())
                .contains("dev");
    }

}
