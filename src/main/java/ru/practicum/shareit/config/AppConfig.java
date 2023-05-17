package ru.practicum.shareit.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")     // настройки внутри приложения
public class AppConfig {
}
