package com.allianceoneapparel.core.config;

import com.allianceoneapparel.core.extension.MessageSourceExtensions;
import com.allianceoneapparel.core.helper.LocaleResolverHelper;
import com.allianceoneapparel.core.helper.MessageSourceHelper;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

@Configuration
public class LocaleConfig {

    @Bean
    public MessageSourceExtensions localizeMsg() {
        MessageSource messageSource = new MessageSourceHelper();
        return new MessageSourceExtensions(messageSource);
    }

    @Bean
    public AcceptHeaderLocaleResolver localeResolver() {
        return new LocaleResolverHelper();
    }
}
