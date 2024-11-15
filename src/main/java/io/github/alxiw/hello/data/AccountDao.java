package io.github.alxiw.hello.data;

import io.github.alxiw.hello.model.Account;

import java.util.List;

interface AccountDao {

    int addAccount(String uin, String name);

    Account getAccountById(int id);

    List<Account> getAllAccounts();

    int updateAccount(Account account);

    int deleteAccount(int id);
}
