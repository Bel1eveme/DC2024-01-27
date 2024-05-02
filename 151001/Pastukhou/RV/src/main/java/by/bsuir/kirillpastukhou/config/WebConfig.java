package by.bsuir.kirillpastukhou.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@ComponentScan("by.bsuir.kirillpastukhou")
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    public  WebConfig(ApplicationContext applicationContext) {
    }
}
