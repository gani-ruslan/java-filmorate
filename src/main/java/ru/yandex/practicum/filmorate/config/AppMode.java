package ru.yandex.practicum.filmorate.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppMode {

    @Value("${app.mode:prod}")
    private String mode;

    @Getter
    @Value("${film.max-popular-count}")
    private int maxPopularCount;

    public boolean isDev() {
        return "dev".equalsIgnoreCase(mode);
    }

    public boolean isProd() {
        return "prod".equalsIgnoreCase(mode);
    }
}
