package io.github.alxiw.hello.data;

import io.github.alxiw.hello.model.Sticker;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static io.github.alxiw.hello.data.Constants.DB_URL;

class StickerDaoImpl implements StickerDao {

    StickerDaoImpl() {
        // do nothing
    }

    @Override
    public int addSticker(String fileId, String emoji) {
        String sql = "INSERT INTO stickers (file_id, emoji) VALUES (?, ?)";
        try (
                Connection connection = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = connection.prepareStatement(sql)
        ) {
            pstmt.setString(1, fileId);
            pstmt.setString(2, emoji);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    @Override
    public Sticker getStickerById(int id) {
        String sql = "SELECT * FROM stickers WHERE id = ?";
        try (
                Connection connection = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = connection.prepareStatement(sql)
        ) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Sticker(
                        rs.getInt("id"),
                        rs.getString("file_id"),
                        rs.getString("emoji")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Sticker> getAllStickers() {
        List<Sticker> stickers = new ArrayList<>();
        String sql = "SELECT * FROM stickers";
        try (
                Connection connection = DriverManager.getConnection(DB_URL);
                Statement stmt = connection.createStatement()
        ) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Sticker sticker = new Sticker(
                        rs.getInt("id"),
                        rs.getString("file_id"),
                        rs.getString("emoji")
                );
                stickers.add(sticker);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stickers;
    }

    @Override
    public int updateSticker(Sticker sticker) {
        String sql = "UPDATE stickers SET file_id = ?, emoji = ? WHERE id = ?";
        try (
                Connection connection = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = connection.prepareStatement(sql)
        ) {
            pstmt.setString(1, sticker.getFileId());
            pstmt.setString(2, sticker.getEmoji());
            pstmt.setInt(3, sticker.getId());

            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    @Override
    public int deleteSticker(int id) {
        String sql = "DELETE FROM stickers WHERE id = ?";
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
