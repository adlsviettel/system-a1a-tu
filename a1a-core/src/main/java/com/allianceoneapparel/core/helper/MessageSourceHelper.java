package com.allianceoneapparel.core.helper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.AbstractMessageSource;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;


@Slf4j
public class MessageSourceHelper extends AbstractMessageSource {
    @Override
    protected MessageFormat resolveCode(@NonNull String code, Locale locale) {
        String resName = String.format("msg_%s.json", locale.getLanguage());
        try (InputStream jsonLang = this.getClass().getClassLoader().getResourceAsStream(resName)) {
            Map<String, String> messages = new ObjectMapper().readValue(jsonLang, new TypeReference<>() {});
            var message = messages.get(code);
            if (message != null) {
                return new MessageFormat(message, locale);
            }
        } catch (IOException io) {
            log.error(io.getMessage());
        }
        return null;
    }
}
