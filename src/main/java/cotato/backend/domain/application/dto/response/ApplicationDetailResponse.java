package cotato.backend.domain.application.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import cotato.backend.domain.application.entity.Application;
import cotato.backend.domain.application.entity.Part;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "서류 상세 조회 응답")
public record ApplicationDetailResponse(

	@Schema(description = "서류 ID", example = "1")
	Long applicationId,

	@Schema(description = "지원자 이름", example = "임준서")
	String name,

	@Schema(description = "지원 기수", example = "13")
	int period,

	@Schema(description = "나이", example = "24")
	int age,

	@Schema(description = "지원 파트", example = "백엔드")
	Part part,

	@Schema(description = "실력 점수", example = "8")
	int ability,

	@Schema(description = "열정 점수", example = "9")
	int passion,

	@Schema(description = "휴대폰 번호", example = "01012345678")
	String phoneNumber,

	@Schema(description = "서류 제출 시간", example = "2025-02-28 23:30")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	LocalDateTime applicationTime,

	@Schema(description = "좋아요 수", example = "5")
	long likeCount

) {
	public static ApplicationDetailResponse of(Application application, long likeCount) {
		return new ApplicationDetailResponse(
			application.getId(),
			application.getName(),
			application.getPeriod(),
			application.getAge(),
			application.getPart(),
			application.getAbility(),
			application.getPassion(),
			application.getPhoneNumber(),
			application.getApplicationTime(),
			likeCount
		);
	}
}
