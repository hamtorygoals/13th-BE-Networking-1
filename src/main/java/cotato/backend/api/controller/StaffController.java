package cotato.backend.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cotato.backend.api.dto.response.DefaultIdResponse;
import cotato.backend.common.dto.DataResponse;
import cotato.backend.domain.staff.application.StaffService;
import cotato.backend.domain.staff.dto.request.StaffCreateRequest;
import cotato.backend.domain.staff.dto.request.StaffUpdateRequest;
import cotato.backend.domain.staff.dto.response.StaffDetailResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Tag(name = "운영진 API", description = "운영진 등록, 조회 및 정보 수정")
@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/api/staff")
public class StaffController {

	private final StaffService staffService;

	@Operation(summary = "운영진 등록", description = "새로운 운영진을 등록합니다.")
	@PostMapping
	public ResponseEntity<DataResponse<DefaultIdResponse>> createStaff(
		@RequestBody @Valid StaffCreateRequest request) {

		return ResponseEntity.ok(DataResponse.created(DefaultIdResponse.of(staffService.createStaff(request))));
	}

	@Operation(summary = "운영진 조회", description = "운영진 ID로 운영진 정보를 조회합니다.")
	@GetMapping("/{staffId}")
	public ResponseEntity<DataResponse<StaffDetailResponse>> getStaff(@PathVariable Long staffId) {
		return ResponseEntity.ok(DataResponse.from(staffService.getStaff(staffId)));
	}

	@Operation(summary = "운영진 정보 수정", description = "운영진의 이름, 나이, 연락처, 역할을 수정합니다.")
	@PutMapping("/{staffId}")
	public ResponseEntity<DataResponse<StaffDetailResponse>> updateStaff(
		@PathVariable Long staffId,
		@RequestBody @Valid StaffUpdateRequest request) {

		return ResponseEntity.ok(DataResponse.from(staffService.updateStaff(staffId, request)));
	}
}
