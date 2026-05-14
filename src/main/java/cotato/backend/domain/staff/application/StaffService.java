package cotato.backend.domain.staff.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cotato.backend.common.exception.AppException;
import cotato.backend.common.exception.ErrorCode;
import cotato.backend.domain.staff.dao.StaffRepository;
import cotato.backend.domain.staff.dto.request.StaffCreateRequest;
import cotato.backend.domain.staff.dto.request.StaffUpdateRequest;
import cotato.backend.domain.staff.dto.response.StaffDetailResponse;
import cotato.backend.domain.staff.entity.Staff;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class StaffService {

	private final StaffRepository staffRepository;

	@Transactional
	public Long createStaff(StaffCreateRequest request) {
		Staff staff = Staff.builder()
			.name(request.name())
			.age(request.age())
			.phoneNumber(request.phoneNumber())
			.role(request.role())
			.build();
		return staffRepository.save(staff).getId();
	}

	public StaffDetailResponse getStaff(Long staffId) {
		return StaffDetailResponse.from(findStaffById(staffId));
	}

	@Transactional
	public StaffDetailResponse updateStaff(Long staffId, StaffUpdateRequest request) {
		Staff staff = findStaffById(staffId);
		staff.update(request.name(), request.age(), request.phoneNumber(), request.role());
		return StaffDetailResponse.from(staff);
	}

	private Staff findStaffById(Long staffId) {
		return staffRepository.findById(staffId)
			.orElseThrow(() -> new AppException(ErrorCode.STAFF_NOT_FOUND));
	}
}
