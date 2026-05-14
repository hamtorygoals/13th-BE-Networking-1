package cotato.backend.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cotato.backend.common.dto.DataResponse;
import cotato.backend.domain.application.application.ApplicationService;
import cotato.backend.domain.application.dto.request.ApplicationCreateRequest;
import cotato.backend.domain.application.dto.response.ApplicationCreateResponse;
import cotato.backend.domain.application.dto.response.ApplicationDetailResponse;
import cotato.backend.domain.application.dto.response.ApplicationListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Tag(name = "서류 API", description = "지원 서류 등록 및 조회")
@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/api/applications")
public class ApplicationController {

	private final ApplicationService applicationService;

	@Operation(summary = "서류 등록", description = "지원 서류를 등록합니다. 동일 전화번호 지원자는 재지원으로 처리됩니다.")
	@PostMapping
	public ResponseEntity<DataResponse<ApplicationCreateResponse>> createApplication(
		@RequestBody @Valid ApplicationCreateRequest request) {

		return ResponseEntity.ok(DataResponse.created(applicationService.createApplication(request)));
	}

	@Operation(summary = "서류 단건 조회", description = "서류 ID로 지원 서류 상세 정보를 조회합니다.")
	@GetMapping("/{applicationId}")
	public ResponseEntity<DataResponse<ApplicationDetailResponse>> getApplication(
		@PathVariable Long applicationId) {

		return ResponseEntity.ok(DataResponse.from(applicationService.getApplication(applicationId)));
	}

	@Operation(
		summary = "서류 목록 조회",
		description = """
			filterBy 파라미터에 따라 서류 목록을 필터링·정렬합니다.
			- `likes`: 좋아요 많은 순 정렬
			- `gisu`: 특정 기수(period) 서류만 조회 (period 필수)
			- `gisu+likes`: 특정 기수 내에서 좋아요 많은 순 정렬 (period 필수)
			"""
	)
	@GetMapping
	public ResponseEntity<DataResponse<ApplicationListResponse>> getApplicationList(
		@Parameter(description = "필터 타입 (likes / gisu / gisu+likes)", example = "likes")
		@RequestParam String filterBy,

		@Parameter(description = "지원 기수 (gisu, gisu+likes 필터 시 필수)", example = "13")
		@RequestParam(required = false) Integer period,

		@Parameter(description = "페이지 번호 (1부터 시작)", example = "1")
		@RequestParam(defaultValue = "1") int page,

		@Parameter(description = "페이지 당 건수 (기본값 10)", example = "10")
		@RequestParam(defaultValue = "10") int pageSize
	) {
		return ResponseEntity.ok(
			DataResponse.from(applicationService.getApplicationList(filterBy, period, page, pageSize))
		);
	}
}
