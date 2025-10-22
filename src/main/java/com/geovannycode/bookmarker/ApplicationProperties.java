package com.geovannycode.bookmarker;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

@ConfigMapping(prefix = "app")
public interface ApplicationProperties {
    @WithDefault("10")
    int pageSize();
}
