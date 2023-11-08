package com.allianceoneapparel.core.extension;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

public class MessageSourceExtensions {
    MessageSource messageSource;

    public MessageSourceExtensions(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMsg(String localizeMsg, Object[] args) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(localizeMsg, args, locale);
    }

    public String getMsg(String localizeMsg) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(localizeMsg, new Object[]{}, locale);
    }
}