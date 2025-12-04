/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectpbo.dao;

import projectpbo.model.Reservasi;
import projectpbo.model.person.Pelanggan;
import projectpbo.model.room.Room;
import projectpbo.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Naufal
 */
public class ReservasiDAO {
    private UserDAO userDAO = new UserDAO();
    private RoomDAO roomDAO = new RoomDAO();

    public void addReservasi(Reservasi res) {
        String sql = "INSERT INTO reservasi (id_reservasi, id_pelanggan, nomor_kamar, check_in, check_out, total_biaya) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, res.getIdReservasi());
            stmt.setString(2, res.getPelanggan().getId());
            stmt.setString(3, res.getRoom().getNomorKamar());
            stmt.setDate(4, Date.valueOf(res.getCheckIn()));
            stmt.setDate(5, Date.valueOf(res.getCheckOut()));
            stmt.setDouble(6, res.getTotalBiaya());
            stmt.executeUpdate();
            
            String updateRoom = "UPDATE room SET status = 'Dipesan' WHERE nomor_kamar = ?";
            try (PreparedStatement stmt2 = conn.prepareStatement(updateRoom)) {
                stmt2.setString(1, res.getRoom().getNomorKamar());
                stmt2.executeUpdate();
            }
            
            System.out.println("[SUKSES] Reservasi " + res.getIdReservasi() + " berhasil dibuat.");
            
        } catch (SQLException e) {
            System.err.println("[ERROR] Gagal reservasi: " + e.getMessage());
        }
    }

    public List<Reservasi> getReservasiByPelanggan(String idPelanggan) {
        List<Reservasi> list = new ArrayList<>();
        String sql = "SELECT * FROM reservasi WHERE id_pelanggan = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idPelanggan);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                String idRes = rs.getString("id_reservasi");
                String noKamar = rs.getString("nomor_kamar");
                Date checkIn = rs.getDate("check_in");
                Date checkOut = rs.getDate("check_out");
                Pelanggan p = (Pelanggan) userDAO.getByUsername(idPelanggan);
                if (p == null) p = new Pelanggan(idPelanggan, "Loading...", "user", "pass");

                Room r = null;
                List<Room> allRooms = roomDAO.getAllRooms();
                for(Room temp : allRooms) {
                    if(temp.getNomorKamar().equals(noKamar)) {
                        r = temp;
                        break;
                    }
                }
                if (r != null) {
                    Reservasi res = new Reservasi(idRes, p, r, checkIn.toLocalDate(), checkOut.toLocalDate());
                    list.add(res);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
