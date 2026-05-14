package cotato.backend.domain.application.dto.request;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import cotato.backend.domain.application.entity.Part;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Schema(description = "서류 지원 요청")
public record ApplicationCreateRequest(

	@Schema(description = "이름 (한글 2~10자)", example = "임준서")
	@NotBlank(message = "이름은 필수입니다")
	@Pattern(regexp = "^[가-힣]{2,10}$", message = "이름은 한글 2글자 이상 10글자 이하여야 합니다")
	String name,

	@Schema(description = "지원 기수 (1 이상)", example = "13")
	@Min(value = 1, message = "지원 기수는 1 이상이어야 합니다")
	int period,

	@Schema(description = "나이 (22~30)", example = "24")
	@Min(value = 22, message = "나이는 22살 이상이어야 합니다")
	@Max(value = 30, message = "나이는 30살 이하여야 합니다")
	int age,

	@Schema(description = "지원 파트 (기획 / 디자이너 / 프론트엔드 / 백엔드)", example = "백엔드")
	@NotNull(message = "지원 파트는 필수입니다")
	Part part,

	@Schema(description = "실력 점수 (0~10)", example = "8")
	@Min(value = 0, message = "실력은 0 이상이어야 합니다")
	@Max(value = 10, message = "실력은 10 이하여야 합니다")
	int ability,

	@Schema(description = "열정 점수 (0~10)", example = "9")
	@Min(value = 0, message = "열정은 0 이상이어야 합니다")
	@Max(value = 10, message = "열정은 10 이하여야 합니다")
	int passion,

	@Schema(description = "휴대폰 번호 (010으로 시작하는 11자리)", example = "01012345678")
	@NotBlank(message = "휴대폰 번호는 필수입니다")
	@Pattern(regexp = "^010\\d{8}$", message = "휴대폰 번호는 010으로 시작하는 11자리 숫자여야 합니다")
	String phoneNumber,

	@Schema(description = "서류 제출 시간 (yyyy-MM-dd HH:mm)", example = "2025-02-28 23:30")
	@NotNull(message = "서류 제출 시간은 필수입니다")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	LocalDateTime applicationTime

) {}
