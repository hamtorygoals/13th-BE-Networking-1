package cotato.backend.domain.like.entity;

import cotato.backend.domain.application.entity.Application;
import cotato.backend.domain.staff.entity.Staff;
import cotato.backend.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
// 한 운영진이 같은 서류에 중복 좋아요를 누르지 못하도록 복합 유니크 제약
@Table(
	name = "application_like",
	uniqueConstraints = @UniqueConstraint(
		name = "uk_application_staff",
		columnNames = {"application_id", "staff_id"}
	)
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApplicationLike extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "like_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "application_id", nullable = false)
	private Application application;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "staff_id", nullable = false)
	private Staff staff;

	@Builder
	public ApplicationLike(Application application, Staff staff) {
		this.application = application;
		this.staff = staff;
	}
}
