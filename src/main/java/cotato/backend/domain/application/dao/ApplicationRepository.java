package cotato.backend.domain.application.dao;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import cotato.backend.domain.application.entity.Application;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

	// 서류 상세 조회: Applicant를 JOIN FETCH하여 추가 쿼리 없이 한 번에 조회
	@Query("SELECT a FROM Application a JOIN FETCH a.applicant WHERE a.id = :id")
	Optional<Application> findByIdWithApplicant(@Param("id") Long id);

	// ─── 서류 목록 조회 (Projection + Native Query) ─────────────────
	// Native Query를 쓰는 이유: LEFT JOIN + GROUP BY + COUNT 조합을 JPQL로 표현하면
	// Hibernate가 서브쿼리를 row마다 실행하는 N+1에 가까운 쿼리를 생성할 수 있다.
	// 서브쿼리로 미리 집계한 뒤 JOIN하는 방식이 대용량에서 훨씬 효율적이다.

	// filterBy=likes: 좋아요 많은 순 정렬, 전체 서류 대상
	@Query(value = """
		SELECT
		    a.application_id  AS applicationId,
		    a.name            AS name,
		    a.period          AS period,
		    a.part            AS part,
		    COALESCE(lc.like_count, 0) AS likeCount
		FROM application a
		LEFT JOIN (
		    SELECT application_id, COUNT(*) AS like_count
		    FROM application_like
		    GROUP BY application_id
		) lc ON lc.application_id = a.application_id
		ORDER BY likeCount DESC, a.application_id ASC
		""",
		countQuery = "SELECT COUNT(*) FROM application",
		nativeQuery = true)
	Page<ApplicationListProjection> findAllOrderByLikeCount(Pageable pageable);

	// filterBy=gisu: 특정 기수 서류만 조회 (등록 순)
	@Query(value = """
		SELECT
		    a.application_id  AS applicationId,
		    a.name            AS name,
		    a.period          AS period,
		    a.part            AS part,
		    COALESCE(lc.like_count, 0) AS likeCount
		FROM application a
		LEFT JOIN (
		    SELECT application_id, COUNT(*) AS like_count
		    FROM application_like
		    GROUP BY application_id
		) lc ON lc.application_id = a.application_id
		WHERE a.period = :period
		ORDER BY a.application_id ASC
		""",
		countQuery = "SELECT COUNT(*) FROM application WHERE period = :period",
		nativeQuery = true)
	Page<ApplicationListProjection> findByPeriod(@Param("period") int period, Pageable pageable);

	// filterBy=gisu+likes: 특정 기수 서류 중 좋아요 많은 순 정렬
	@Query(value = """
		SELECT
		    a.application_id  AS applicationId,
		    a.name            AS name,
		    a.period          AS period,
		    a.part            AS part,
		    COALESCE(lc.like_count, 0) AS likeCount
		FROM application a
		LEFT JOIN (
		    SELECT application_id, COUNT(*) AS like_count
		    FROM application_like
		    GROUP BY application_id
		) lc ON lc.application_id = a.application_id
		WHERE a.period = :period
		ORDER BY likeCount DESC, a.application_id ASC
		""",
		countQuery = "SELECT COUNT(*) FROM application WHERE period = :period",
		nativeQuery = true)
	Page<ApplicationListProjection> findByPeriodOrderByLikeCount(@Param("period") int period, Pageable pageable);
}
