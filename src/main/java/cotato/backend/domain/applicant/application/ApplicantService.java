package cotato.backend.domain.applicant.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cotato.backend.common.exception.AppException;
import cotato.backend.common.exception.ErrorCode;
import cotato.backend.domain.applicant.dao.ApplicantRepository;
import cotato.backend.domain.applicant.dto.request.ApplicantUpdateRequest;
import cotato.backend.domain.applicant.dto.response.ApplicantDetailResponse;
import cotato.backend.domain.applicant.entity.Applicant;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ApplicantService {

	private final ApplicantRepository applicantRepository;

	public ApplicantDetailResponse getApplicant(Long applicantId) {
		Applicant applicant = findApplicantById(applicantId);
		return ApplicantDetailResponse.from(applicant);
	}

	@Transactional
	public ApplicantDetailResponse updateApplicant(Long applicantId, ApplicantUpdateRequest request) {
		Applicant applicant = findApplicantById(applicantId);

		// 변경하려는 번호가 이미 다른 지원자에게 사용 중인지 확인
		if (applicantRepository.existsByPhoneNumberAndIdNot(request.phoneNumber(), applicantId)) {
			throw new AppException(ErrorCode.PHONE_NUMBER_ALREADY_EXISTS);
		}

		applicant.update(request.name(), request.age(), request.phoneNumber());
		return ApplicantDetailResponse.from(applicant);
		// @Transactional이므로 dirty checking으로 자동 UPDATE 실행
	}

	private Applicant findApplicantById(Long applicantId) {
		return applicantRepository.findById(applicantId)
			.orElseThrow(() -> new AppException(ErrorCode.APPLICANT_NOT_FOUND));
	}
}
