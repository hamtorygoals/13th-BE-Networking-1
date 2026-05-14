package cotato.backend.domain.like.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cotato.backend.domain.like.entity.ApplicationLike;

public interface ApplicationLikeRepository extends JpaRepository<ApplicationLike, Long> {

	boolean existsByApplicationIdAndStaffId(Long applicationId, Long staffId);

	Optional<ApplicationLike> findByApplicationIdAndStaffId(Long applicationId, Long staffId);

	long countByApplicationId(Long applicationId);
}
