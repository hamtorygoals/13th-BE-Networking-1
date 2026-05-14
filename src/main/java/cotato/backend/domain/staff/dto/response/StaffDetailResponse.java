package cotato.backend.domain.staff.dto.response;

import cotato.backend.domain.staff.entity.Staff;
import cotato.backend.domain.staff.entity.StaffRole;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "운영진 조회 응답")
public record StaffDetailResponse(

	@Schema(description = "운영진 ID", example = "1")
	Long staffId,

	@Schema(description = "이름", example = "정찬민")
	String name,

	@Schema(description = "나이", example = "30")
	int age,

	@Schema(description = "휴대폰 번호", example = "01012345678")
	String phoneNumber,

	@Schema(description = "역할", example = "파트장")
	StaffRole role

) {
	public static StaffDetailResponse from(Staff staff) {
		return new StaffDetailResponse(
			staff.getId(),
			staff.getName(),
			staff.getAge(),
			staff.getPhoneNumber(),
			staff.getRole()
		);
	}
}
