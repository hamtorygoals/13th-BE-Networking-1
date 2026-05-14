package cotato.backend.common.exception;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import cotato.backend.common.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	// Bean Validation 실패 (@Valid): 모든 필드 에러 메시지를 모아서 반환
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationException(
		MethodArgumentNotValidException e, HttpServletRequest request) {

		String errorMessage = e.getBindingResult().getFieldErrors().stream()
			.map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
			.collect(Collectors.joining(", "));

		log.warn("Validation 실패: {}", errorMessage);
		ErrorResponse errorResponse = ErrorResponse.of(
			ErrorCode.INVALID_PARAMETER, request, errorMessage);
		return ResponseEntity.badRequest().body(errorResponse);
	}

	// JSON 파싱 실패 (잘못된 타입, 잘못된 enum 값 등)
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(
		HttpMessageNotReadableException e, HttpServletRequest request) {

		log.warn("JSON 파싱 오류: {}", e.getMessage());
		ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_JSON, request);
		return ResponseEntity.badRequest().body(errorResponse);
	}

	// 필수 쿼리 파라미터 누락
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<ErrorResponse> handleMissingParam(
		MissingServletRequestParameterException e, HttpServletRequest request) {

		log.warn("필수 파라미터 누락: {}", e.getParameterName());
		ErrorResponse errorResponse = ErrorResponse.of(
			ErrorCode.INVALID_PARAMETER, request, "필수 파라미터가 누락되었습니다: " + e.getParameterName());
		return ResponseEntity.badRequest().body(errorResponse);
	}

	// 지원하지 않는 HTTP 메서드
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ErrorResponse> handleMethodNotSupported(
		HttpRequestMethodNotSupportedException e, HttpServletRequest request) {

		log.warn("지원하지 않는 HTTP 메서드: {}", e.getMethod());
		ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.METHOD_NOT_ALLOWED, request);
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
	}

	// 비즈니스 예외 (AppException)
	@ExceptionHandler(AppException.class)
	public ResponseEntity<ErrorResponse> handleAppCustomException(AppException e, HttpServletRequest request) {
		log.error("AppException 발생: {}", e.getErrorCode().getMessage());
		ErrorResponse errorResponse = ErrorResponse.of(e.getErrorCode(), request);
		return ResponseEntity
			.status(e.getErrorCode().getHttpStatus())
			.body(errorResponse);
	}

	// 처리되지 않은 모든 예외
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleAllException(Exception e, HttpServletRequest request) {
		log.error("처리되지 않은 예외 발생: ", e);
		ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR, request);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	}
}
