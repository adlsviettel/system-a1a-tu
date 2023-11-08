package com.allianceoneapparel.core.helper;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Locale;

public class LocaleResolverHelper extends AcceptHeaderLocaleResolver {
    @Override
    @NonNull
    public Locale resolveLocale(HttpServletRequest request) {
        final String lang = request.getHeader("Accept-Language");
        if (lang == null || lang.isEmpty()) {
            return Locale.of("vi", Locale.getDefault().getCountry());
        }
        return Locale.of(lang, Locale.getDefault().getCountry());
    }
}
