package cotato.backend.domain.application.dao;

// Native Query 결과를 받는 Spring Data Projection 인터페이스.
// 컬럼 alias가 getter 이름과 1:1 매핑된다 (applicationId → getApplicationId()).
public interface ApplicationListProjection {

	Long getApplicationId();

	String getName();

	Integer getPeriod();

	String getPart();

	Long getLikeCount();
}
