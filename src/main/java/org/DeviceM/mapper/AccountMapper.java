package org.DeviceM.mapper;

import org.DeviceM.dao.Account;

import java.util.List;

public interface AccountMapper {
    public List<Account> getAllAccount();

    public Account getAccountById(Integer id);

    public String getPassWordById(Integer id);
}
