package cotato.backend.domain.like.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "좋아요 요청")
public record LikeRequest(

	@Schema(description = "좋아요를 누르는 운영진 ID", example = "1")
	@NotNull(message = "운영진 ID는 필수입니다")
	Long staffId

) {}
