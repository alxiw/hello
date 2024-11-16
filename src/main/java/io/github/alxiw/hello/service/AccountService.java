package io.github.alxiw.hello.service;

import io.github.alxiw.hello.data.AccountDaoImpl;
import io.github.alxiw.hello.model.Account;

import java.util.List;

public class AccountService {

    private static volatile AccountService instance;

    public static AccountService getInstance() {
        // Первый проверка (не блокирующая)
        if (instance == null) {
            synchronized (AccountService.class) {
                // Второй проверка (блокирующая)
                if (instance == null) {
                    instance = new AccountService();
                }
            }
        }
        return instance;
    }

    private final AccountDao accountDao;

    private AccountService() {
        this.accountDao = new AccountDaoImpl();
    }

    public Response addAccount(String uin, String name) {
        return getResponse(accountDao.addAccount(uin, name));
    }

    public Account getAccountById(int id) {
        return accountDao.getAccountById(id);
    }

    public List<Account> getAllAccounts() {
        return accountDao.getAllAccounts();
    }

    public Response updateAccount(Account account) {
        return getResponse(accountDao.updateAccount(account));
    }

    public Response deleteAccount(int id) {
        return getResponse(accountDao.deleteAccount(id));
    }

    private Response getResponse(int value) {
        if (value <= -1) {
            return Response.ERROR;
        } else if (value == 0) {
            return Response.NO_CHANGES;
        } else {
            return Response.SUCCESS;
        }
    }
}
