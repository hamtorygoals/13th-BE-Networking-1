package cotato.backend.domain.applicant.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cotato.backend.domain.applicant.entity.Applicant;

public interface ApplicantRepository extends JpaRepository<Applicant, Long> {

	Optional<Applicant> findByPhoneNumber(String phoneNumber);

	boolean existsByPhoneNumber(String phoneNumber);

	// 다른 지원자(id가 다른)가 동일 번호를 이미 사용 중인지 확인 (수정 시 중복 검사용)
	boolean existsByPhoneNumberAndIdNot(String phoneNumber, Long id);
}
