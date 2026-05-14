package cotato.backend.domain.staff.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StaffRole {
	PART_LEADER("파트장"),
	PLANNING_LEADER("기획팀장"),
	PR_LEADER("홍보팀장"),
	VICE_PRESIDENT("부회장"),
	PRESIDENT("회장"),
	EDUCATION_LEADER("교육팀장");

	private final String displayName;

	@JsonValue
	public String getDisplayName() {
		return displayName;
	}

	@JsonCreator
	public static StaffRole from(String value) {
		for (StaffRole role : values()) {
			if (role.displayName.equals(value) || role.name().equalsIgnoreCase(value)) {
				return role;
			}
		}
		throw new IllegalArgumentException("유효하지 않은 역할입니다: " + value
			+ " (파트장 / 기획팀장 / 홍보팀장 / 부회장 / 회장 / 교육팀장)");
	}
}
