package cotato.backend.domain.like.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cotato.backend.common.exception.AppException;
import cotato.backend.common.exception.ErrorCode;
import cotato.backend.domain.application.dao.ApplicationRepository;
import cotato.backend.domain.application.entity.Application;
import cotato.backend.domain.like.dao.ApplicationLikeRepository;
import cotato.backend.domain.like.entity.ApplicationLike;
import cotato.backend.domain.staff.dao.StaffRepository;
import cotato.backend.domain.staff.entity.Staff;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeService {

	private final ApplicationLikeRepository applicationLikeRepository;
	private final ApplicationRepository applicationRepository;
	private final StaffRepository staffRepository;

	@Transactional
	public long addLike(Long applicationId, Long staffId) {
		// 이미 좋아요를 눌렀는지 확인 (DB 유니크 제약으로도 보장되지만, 명확한 메시지를 위해 선검사)
		if (applicationLikeRepository.existsByApplicationIdAndStaffId(applicationId, staffId)) {
			throw new AppException(ErrorCode.ALREADY_LIKED);
		}

		Application application = applicationRepository.findById(applicationId)
			.orElseThrow(() -> new AppException(ErrorCode.APPLICATION_NOT_FOUND));

		Staff staff = staffRepository.findById(staffId)
			.orElseThrow(() -> new AppException(ErrorCode.STAFF_NOT_FOUND));

		ApplicationLike like = ApplicationLike.builder()
			.application(application)
			.staff(staff)
			.build();

		applicationLikeRepository.save(like);
		return applicationLikeRepository.countByApplicationId(applicationId);
	}

	@Transactional
	public long cancelLike(Long applicationId, Long staffId) {
		ApplicationLike like = applicationLikeRepository
			.findByApplicationIdAndStaffId(applicationId, staffId)
			.orElseThrow(() -> new AppException(ErrorCode.LIKE_NOT_FOUND));

		applicationLikeRepository.delete(like);
		return applicationLikeRepository.countByApplicationId(applicationId);
	}
}
