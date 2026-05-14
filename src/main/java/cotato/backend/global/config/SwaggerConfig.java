package cotato.backend.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI()
			.info(new Info()
				.title("코테이토 동아리 지원자 관리 시스템 API")
				.description("코테이토 13기 BE 네트워킹 과제 - 지원자/서류/운영진 관리 API")
				.version("v1.0.0")
			);
	}
}
