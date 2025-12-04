/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectpbo.dao;

import projectpbo.model.room.Room;
import projectpbo.model.room.StandardRoom;
import projectpbo.model.room.SuiteRoom;
import projectpbo.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Naufal
 */
public class RoomDAO {
    public void addRoom(Room room) {
        String sql = "INSERT INTO room (nomor_kamar, tipe_kamar, status, harga) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, room.getNomorKamar());
            String tipe = (room instanceof SuiteRoom) ? "Suite" : "Standard";
            stmt.setString(2, tipe);
            stmt.setString(3, room.getStatus());
            stmt.setDouble(4, room.getHargaPerMalam());
            
            stmt.executeUpdate();
            System.out.println("[SUKSES] Kamar " + room.getNomorKamar() + " berhasil disimpan.");
            
        } catch (SQLException e) {
            System.err.println("[ERROR] Gagal simpan: " + e.getMessage());
        }
    }

    public List<Room> getAllRooms() {
        List<Room> listKamar = new ArrayList<>();
        String sql = "SELECT * FROM room";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String nomor = rs.getString("nomor_kamar");
                String tipe = rs.getString("tipe_kamar");
                String status = rs.getString("status");
                
                Room r;
                if (tipe.equalsIgnoreCase("Suite")) {
                    r = new SuiteRoom(nomor);
                } else {
                    r = new StandardRoom(nomor);
                }
                r.setStatus(status);
                
                listKamar.add(r);
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Gagal ambil data: " + e.getMessage());
        }
        return listKamar;
    }

    public void deleteRoom(String nomorKamar) {
        String sql = "DELETE FROM room WHERE nomor_kamar = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nomorKamar);
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("[SUKSES] Kamar " + nomorKamar + " berhasil dihapus.");
            } else {
                System.out.println("[INFO] Kamar tidak ditemukan.");
            }
            
        } catch (SQLException e) {
            System.err.println("[ERROR] Gagal hapus: " + e.getMessage());
        }
    }
}
