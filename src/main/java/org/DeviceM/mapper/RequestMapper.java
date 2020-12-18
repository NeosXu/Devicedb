package org.DeviceM.mapper;

import org.DeviceM.dao.Request;

import java.util.List;

public interface RequestMapper {
    public List<Request> getAllRequest();

    public List<Request> getStudentAllRequestById(Integer id);

    public List<Request> getTeacherAllRequestById(Integer id);
}
