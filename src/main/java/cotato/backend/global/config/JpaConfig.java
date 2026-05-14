package cotato.backend.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// @EnableJpaAuditing을 메인 클래스가 아닌 별도 설정 클래스에 두는 이유:
// 메인 클래스에 두면 슬라이스 테스트(@WebMvcTest 등)에서 Auditing 관련 빈이 없어 오류가 발생한다.
@Configuration
@EnableJpaAuditing
public class JpaConfig {
}
