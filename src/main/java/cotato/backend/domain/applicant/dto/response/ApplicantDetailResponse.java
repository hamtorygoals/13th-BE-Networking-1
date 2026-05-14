package cotato.backend.domain.applicant.dto.response;

import cotato.backend.domain.applicant.entity.Applicant;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "지원자 조회 응답")
public record ApplicantDetailResponse(

	@Schema(description = "지원자 ID", example = "1")
	Long applicantId,

	@Schema(description = "이름", example = "임준서")
	String name,

	@Schema(description = "나이", example = "25")
	int age,

	@Schema(description = "휴대폰 번호", example = "01012345678")
	String phoneNumber

) {
	public static ApplicantDetailResponse from(Applicant applicant) {
		return new ApplicantDetailResponse(
			applicant.getId(),
			applicant.getName(),
			applicant.getAge(),
			applicant.getPhoneNumber()
		);
	}
}
