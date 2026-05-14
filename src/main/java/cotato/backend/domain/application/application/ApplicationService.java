package cotato.backend.domain.application.application;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cotato.backend.common.exception.AppException;
import cotato.backend.common.exception.ErrorCode;
import cotato.backend.domain.applicant.dao.ApplicantRepository;
import cotato.backend.domain.applicant.entity.Applicant;
import cotato.backend.domain.application.dao.ApplicationListProjection;
import cotato.backend.domain.application.dao.ApplicationRepository;
import cotato.backend.domain.application.dto.request.ApplicationCreateRequest;
import cotato.backend.domain.application.dto.response.ApplicationCreateResponse;
import cotato.backend.domain.application.dto.response.ApplicationDetailResponse;
import cotato.backend.domain.application.dto.response.ApplicationListItemResponse;
import cotato.backend.domain.application.dto.response.ApplicationListResponse;
import cotato.backend.domain.application.entity.Application;
import cotato.backend.domain.like.dao.ApplicationLikeRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ApplicationService {

	private static final int DEFAULT_PAGE_SIZE = 10;

	private final ApplicationRepository applicationRepository;
	private final ApplicantRepository applicantRepository;
	private final ApplicationLikeRepository applicationLikeRepository;

	@Transactional
	public ApplicationCreateResponse createApplication(ApplicationCreateRequest request) {
		// 전화번호로 기존 지원자 조회 → 없으면 새로 생성 (재지원 처리)
		Applicant applicant = applicantRepository.findByPhoneNumber(request.phoneNumber())
			.orElseGet(() -> applicantRepository.save(
				Applicant.builder()
					.name(request.name())
					.age(request.age())
					.phoneNumber(request.phoneNumber())
					.build()
			));

		Application application = Application.builder()
			.applicant(applicant)
			.name(request.name())       // 제출 당시 스냅샷 저장
			.period(request.period())
			.age(request.age())
			.part(request.part())
			.ability(request.ability())
			.passion(request.passion())
			.phoneNumber(request.phoneNumber())
			.applicationTime(request.applicationTime())
			.build();

		Application saved = applicationRepository.save(application);
		return ApplicationCreateResponse.of(saved.getId(), applicant.getId());
	}

	public ApplicationDetailResponse getApplication(Long applicationId) {
		// JOIN FETCH로 Applicant 추가 쿼리 없이 조회
		Application application = applicationRepository.findByIdWithApplicant(applicationId)
			.orElseThrow(() -> new AppException(ErrorCode.APPLICATION_NOT_FOUND));

		long likeCount = applicationLikeRepository.countByApplicationId(applicationId);
		return ApplicationDetailResponse.of(application, likeCount);
	}

	public ApplicationListResponse getApplicationList(String filterBy, Integer period, int page, int pageSize) {
		if (page < 1) {
			throw new AppException(ErrorCode.INVALID_PARAMETER);
		}
		// page는 1-indexed로 받아 0-indexed로 변환
		Pageable pageable = PageRequest.of(page - 1, pageSize);

		Page<ApplicationListProjection> resultPage = switch (filterBy) {
			case "likes" -> applicationRepository.findAllOrderByLikeCount(pageable);
			case "gisu" -> {
				validatePeriod(period);
				yield applicationRepository.findByPeriod(period, pageable);
			}
			case "gisu+likes" -> {
				validatePeriod(period);
				yield applicationRepository.findByPeriodOrderByLikeCount(period, pageable);
			}
			default -> throw new AppException(ErrorCode.INVALID_FILTER_TYPE);
		};

		List<ApplicationListItemResponse> items = resultPage.getContent().stream()
			.map(ApplicationListItemResponse::from)
			.toList();

		return ApplicationListResponse.of(
			items,
			page,
			pageSize,
			resultPage.getTotalElements(),
			resultPage.getTotalPages()
		);
	}

	private void validatePeriod(Integer period) {
		if (period == null) {
			throw new AppException(ErrorCode.PERIOD_REQUIRED);
		}
	}
}
