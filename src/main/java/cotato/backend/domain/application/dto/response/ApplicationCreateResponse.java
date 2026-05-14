package cotato.backend.domain.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "서류 등록 응답")
public record ApplicationCreateResponse(

	@Schema(description = "생성된 서류 ID", example = "1")
	Long applicationId,

	@Schema(description = "지원자 ID", example = "1")
	Long applicantId

) {
	public static ApplicationCreateResponse of(Long applicationId, Long applicantId) {
		return new ApplicationCreateResponse(applicationId, applicantId);
	}
}
