package io.github.alxiw.hello.data;

import io.github.alxiw.hello.model.Account;
import io.github.alxiw.hello.service.AccountDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static io.github.alxiw.hello.data.Const.DB_URL;

public class AccountDaoImpl implements AccountDao {

    public AccountDaoImpl() {
        // do nothing
    }

    @Override
    public int addAccount(String uin, String name) {
        String sql = "INSERT INTO accounts (uin, name) VALUES (?, ?)";
        try (
                Connection connection = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = connection.prepareStatement(sql)
        ) {
            pstmt.setString(1, uin);
            pstmt.setString(2, name);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    @Override
    public Account getAccountById(int id) {
        String sql = "SELECT * FROM accounts WHERE id = ?";
        try (
                Connection connection = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = connection.prepareStatement(sql)
        ) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Account(
                        rs.getInt("id"),
                        rs.getString("uin"),
                        rs.getString("name")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Account> getAllAccounts() {
        List<Account> users = new ArrayList<>();
        String sql = "SELECT * FROM accounts";
        try (
                Connection connection = DriverManager.getConnection(DB_URL);
                Statement stmt = connection.createStatement()
        ) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Account account = new Account(
                        rs.getInt("id"),
                        rs.getString("uin"),
                        rs.getString("name")
                );
                users.add(account);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    @Override
    public int updateAccount(Account account) {
        String sql = "UPDATE accounts SET uin = ?, name = ? WHERE id = ?";
        try (
                Connection connection = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = connection.prepareStatement(sql)
        ) {
            pstmt.setString(1, account.getUin());
            pstmt.setString(2, account.getName());
            pstmt.setInt(3, account.getId());

            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    @Override
    public int deleteAccount(int id) {
        String sql = "DELETE FROM accounts WHERE id = ?";
        try (
                Connection connection = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = connection.prepareStatement(sql)
        ) {
            pstmt.setInt(1, id);

            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }
}
