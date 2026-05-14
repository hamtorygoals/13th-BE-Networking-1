package cotato.backend.domain.application.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Part {
	PLANNING("기획"),
	DESIGN("디자이너"),
	FRONTEND("프론트엔드"),
	BACKEND("백엔드");

	private final String displayName;

	// JSON 직렬화 시 enum 이름 대신 한글 displayName으로 출력
	@JsonValue
	public String getDisplayName() {
		return displayName;
	}

	// 한글("기획") 또는 영문("BACKEND") 둘 다 역직렬화 허용
	@JsonCreator
	public static Part from(String value) {
		for (Part part : values()) {
			if (part.displayName.equals(value) || part.name().equalsIgnoreCase(value)) {
				return part;
			}
		}
		throw new IllegalArgumentException("유효하지 않은 파트입니다: " + value
			+ " (기획 / 디자이너 / 프론트엔드 / 백엔드)");
	}
}
