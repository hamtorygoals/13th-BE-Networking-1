package cotato.backend.domain.application.dto.response;

import cotato.backend.domain.application.dao.ApplicationListProjection;
import cotato.backend.domain.application.entity.Part;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "서류 목록 항목")
public record ApplicationListItemResponse(

	@Schema(description = "서류 ID", example = "1")
	Long applicationId,

	@Schema(description = "지원자 이름", example = "임준서")
	String name,

	@Schema(description = "지원 기수", example = "13")
	int period,

	@Schema(description = "지원 파트", example = "백엔드")
	Part part,

	@Schema(description = "좋아요 수", example = "5")
	long likeCount

) {
	public static ApplicationListItemResponse from(ApplicationListProjection projection) {
		return new ApplicationListItemResponse(
			projection.getApplicationId(),
			projection.getName(),
			projection.getPeriod(),
			// DB에 문자열로 저장된 Part enum 값을 역변환
			Part.valueOf(projection.getPart()),
			projection.getLikeCount()
		);
	}
}
