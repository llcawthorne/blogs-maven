package sec;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.access.AccessDeniedHandler;
import sec.error.MyAccessDeniedHandler;

@SpringBootApplication
public class SecurityThymeleafApplication {

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new MyAccessDeniedHandler();
    }

    public static void main(String[] args) {
        SpringApplication.run(SecurityThymeleafApplication.class, args);
    }

}
