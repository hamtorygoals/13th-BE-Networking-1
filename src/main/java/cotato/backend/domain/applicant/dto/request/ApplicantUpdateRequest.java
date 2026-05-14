package cotato.backend.domain.applicant.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

@Schema(description = "지원자 정보 수정 요청")
public record ApplicantUpdateRequest(

	@Schema(description = "이름", example = "임준서")
	@NotBlank(message = "이름은 필수입니다")
	String name,

	@Schema(description = "나이", example = "25")
	@Positive(message = "나이는 양수여야 합니다")
	int age,

	@Schema(description = "휴대폰 번호 (010으로 시작하는 11자리)", example = "01012345678")
	@NotBlank(message = "휴대폰 번호는 필수입니다")
	@Pattern(regexp = "^010\\d{8}$", message = "휴대폰 번호는 010으로 시작하는 11자리 숫자여야 합니다")
	String phoneNumber

) {}
