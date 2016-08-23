package dynomite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;

@EnableAutoConfiguration
@ComponentScan
@Configuration
@RestController
@SpringBootApplication
public class IntroDynomiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(IntroDynomiteApplication.class, args);
	}
}
