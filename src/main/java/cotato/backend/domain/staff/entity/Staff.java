package cotato.backend.domain.staff.entity;

import cotato.backend.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "staff")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Staff extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "staff_id")
	private Long id;

	@Column(nullable = false, length = 30)
	private String name;

	@Column(nullable = false)
	private int age;

	@Column(nullable = false, length = 11)
	private String phoneNumber;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 30)
	private StaffRole role;

	@Builder
	public Staff(String name, int age, String phoneNumber, StaffRole role) {
		this.name = name;
		this.age = age;
		this.phoneNumber = phoneNumber;
		this.role = role;
	}

	public void update(String name, int age, String phoneNumber, StaffRole role) {
		this.name = name;
		this.age = age;
		this.phoneNumber = phoneNumber;
		this.role = role;
	}
}
