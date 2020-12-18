package org.DeviceM.mapper;

import org.DeviceM.dao.Lending;

import java.util.List;

public interface LendingMapper {
    public List<Lending> getAllLending();

    public List<Lending> getStudentAllLendingById(Integer id);

    public List<Lending> getTeacherAllLendingById(Integer id);
}
