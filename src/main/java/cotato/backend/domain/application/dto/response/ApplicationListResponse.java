package cotato.backend.domain.application.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "서류 목록 조회 응답")
public record ApplicationListResponse(

	@Schema(description = "서류 목록")
	List<ApplicationListItemResponse> applications,

	@Schema(description = "현재 페이지 (1-indexed)", example = "1")
	int page,

	@Schema(description = "페이지 당 건수", example = "10")
	int pageSize,

	@Schema(description = "전체 건수", example = "42")
	long totalCount,

	@Schema(description = "전체 페이지 수", example = "5")
	int totalPages

) {
	public static ApplicationListResponse of(
		List<ApplicationListItemResponse> applications,
		int page, int pageSize, long totalCount, int totalPages) {
		return new ApplicationListResponse(applications, page, pageSize, totalCount, totalPages);
	}
}
