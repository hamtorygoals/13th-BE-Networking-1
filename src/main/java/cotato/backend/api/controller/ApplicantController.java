package cotato.backend.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cotato.backend.common.dto.DataResponse;
import cotato.backend.domain.applicant.application.ApplicantService;
import cotato.backend.domain.applicant.dto.request.ApplicantUpdateRequest;
import cotato.backend.domain.applicant.dto.response.ApplicantDetailResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Tag(name = "지원자 API", description = "지원자 조회 및 정보 수정")
@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/api/applicants")
public class ApplicantController {

	private final ApplicantService applicantService;

	@Operation(summary = "지원자 조회", description = "지원자 ID로 지원자 정보를 조회합니다.")
	@GetMapping("/{applicantId}")
	public ResponseEntity<DataResponse<ApplicantDetailResponse>> getApplicant(
		@PathVariable Long applicantId) {

		return ResponseEntity.ok(DataResponse.from(applicantService.getApplicant(applicantId)));
	}

	@Operation(summary = "지원자 정보 수정", description = "지원자의 이름, 나이, 연락처를 수정합니다.")
	@PutMapping("/{applicantId}")
	public ResponseEntity<DataResponse<ApplicantDetailResponse>> updateApplicant(
		@PathVariable Long applicantId,
		@RequestBody @Valid ApplicantUpdateRequest request) {

		return ResponseEntity.ok(DataResponse.from(applicantService.updateApplicant(applicantId, request)));
	}
}
