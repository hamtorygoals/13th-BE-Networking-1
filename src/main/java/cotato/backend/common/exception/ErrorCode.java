package cotato.backend.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	// ─── Common ─────────────────────────────────────────────────────
	BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.", "COMMON-001"),
	INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "요청 파라미터가 잘못되었습니다.", "COMMON-002"),
	NOT_FOUND(HttpStatus.NOT_FOUND, "찾을 수 없습니다.", "COMMON-003"),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부에서 에러가 발생하였습니다.", "COMMON-004"),
	INVALID_JSON(HttpStatus.BAD_REQUEST, "요청 JSON 형식이 올바르지 않습니다.", "COMMON-005"),
	METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 HTTP 메서드입니다.", "COMMON-006"),

	// ─── Application (지원 서류) ─────────────────────────────────────
	APPLICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "지원 서류를 찾을 수 없습니다.", "APPLICATION-001"),
	INVALID_FILTER_TYPE(HttpStatus.BAD_REQUEST,
		"유효하지 않은 필터 타입입니다. (likes / gisu / gisu+likes)", "APPLICATION-002"),
	PERIOD_REQUIRED(HttpStatus.BAD_REQUEST,
		"gisu 또는 gisu+likes 필터 사용 시 period(지원기수)를 함께 전달해야 합니다.", "APPLICATION-003"),

	// ─── Applicant (지원자) ──────────────────────────────────────────
	APPLICANT_NOT_FOUND(HttpStatus.NOT_FOUND, "지원자를 찾을 수 없습니다.", "APPLICANT-001"),
	PHONE_NUMBER_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 다른 지원자가 사용 중인 휴대폰 번호입니다.", "APPLICANT-002"),

	// ─── Staff (운영진) ─────────────────────────────────────────────
	STAFF_NOT_FOUND(HttpStatus.NOT_FOUND, "운영진을 찾을 수 없습니다.", "STAFF-001"),

	// ─── Like (좋아요) ──────────────────────────────────────────────
	ALREADY_LIKED(HttpStatus.CONFLICT, "이미 좋아요를 누른 서류입니다.", "LIKE-001"),
	LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "좋아요 기록을 찾을 수 없습니다.", "LIKE-002"),
	;

	private final HttpStatus httpStatus;
	private final String message;
	private final String code;
}
