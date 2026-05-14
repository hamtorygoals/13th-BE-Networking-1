package cotato.backend.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cotato.backend.common.dto.DataResponse;
import cotato.backend.domain.like.application.LikeService;
import cotato.backend.domain.like.dto.request.LikeRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Tag(name = "좋아요 API", description = "운영진의 지원 서류 좋아요 추가 및 취소")
@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/api/applications/{applicationId}/likes")
public class LikeController {

	private final LikeService likeService;

	@Operation(summary = "좋아요 추가", description = "운영진이 지원 서류에 좋아요를 추가합니다.")
	@PostMapping
	public ResponseEntity<DataResponse<Long>> addLike(
		@PathVariable Long applicationId,
		@RequestBody @Valid LikeRequest request) {

		long likeCount = likeService.addLike(applicationId, request.staffId());
		return ResponseEntity.ok(DataResponse.from(likeCount));
	}

	@Operation(summary = "좋아요 취소", description = "운영진이 지원 서류에 누른 좋아요를 취소합니다.")
	@DeleteMapping
	public ResponseEntity<DataResponse<Long>> cancelLike(
		@PathVariable Long applicationId,
		@Parameter(description = "좋아요를 취소하는 운영진 ID", example = "1")
		@RequestParam Long staffId) {

		long likeCount = likeService.cancelLike(applicationId, staffId);
		return ResponseEntity.ok(DataResponse.from(likeCount));
	}
}
