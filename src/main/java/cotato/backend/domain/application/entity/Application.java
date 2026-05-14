package cotato.backend.domain.application.entity;

import java.time.LocalDateTime;

import cotato.backend.domain.applicant.entity.Applicant;
import cotato.backend.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "application")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Application extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "application_id")
	private Long id;

	// 지연 로딩: 서류 목록 조회 시 Applicant 전체를 불러오지 않기 위해 LAZY 설정
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "applicant_id", nullable = false)
	private Applicant applicant;

	// 제출 당시의 스냅샷 저장 → 지원자가 이름/나이를 나중에 바꿔도 서류 원본이 보존됨
	@Column(nullable = false, length = 30)
	private String name;

	@Column(nullable = false)
	private int period;

	@Column(nullable = false)
	private int age;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private Part part;

	@Column(nullable = false)
	private int ability;

	@Column(nullable = false)
	private int passion;

	@Column(nullable = false, length = 11)
	private String phoneNumber;

	@Column(nullable = false)
	private LocalDateTime applicationTime;

	@Builder
	public Application(Applicant applicant, String name, int period, int age,
		Part part, int ability, int passion, String phoneNumber, LocalDateTime applicationTime) {
		this.applicant = applicant;
		this.name = name;
		this.period = period;
		this.age = age;
		this.part = part;
		this.ability = ability;
		this.passion = passion;
		this.phoneNumber = phoneNumber;
		this.applicationTime = applicationTime;
	}
}
