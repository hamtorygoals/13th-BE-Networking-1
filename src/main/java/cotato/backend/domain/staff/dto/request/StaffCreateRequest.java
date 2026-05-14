package cotato.backend.domain.staff.dto.request;

import cotato.backend.domain.staff.entity.StaffRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

@Schema(description = "운영진 등록 요청")
public record StaffCreateRequest(

	@Schema(description = "이름", example = "정찬민")
	@NotBlank(message = "이름은 필수입니다")
	String name,

	@Schema(description = "나이", example = "30")
	@Positive(message = "나이는 양수여야 합니다")
	int age,

	@Schema(description = "휴대폰 번호 (010으로 시작하는 11자리)", example = "01098765432")
	@NotBlank(message = "휴대폰 번호는 필수입니다")
	@Pattern(regexp = "^010\\d{8}$", message = "휴대폰 번호는 010으로 시작하는 11자리 숫자여야 합니다")
	String phoneNumber,

	@Schema(description = "역할 (파트장 / 기획팀장 / 홍보팀장 / 부회장 / 회장 / 교육팀장)", example = "파트장")
	@NotNull(message = "역할은 필수입니다")
	StaffRole role

) {}
