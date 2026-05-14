package cotato.backend.domain.staff.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import cotato.backend.domain.staff.entity.Staff;

public interface StaffRepository extends JpaRepository<Staff, Long> {
}
