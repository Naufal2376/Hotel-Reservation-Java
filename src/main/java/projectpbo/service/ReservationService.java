/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectpbo.service;

import projectpbo.dao.ReservasiDAO;
import projectpbo.model.Reservasi;
import projectpbo.model.person.Pelanggan;
import projectpbo.model.room.Room;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
/**
 *
 * @author Naufal
 */
public class ReservationService {
    private ReservasiDAO reservasiDAO = new ReservasiDAO();

    public Reservasi createReservation(Pelanggan pelanggan, Room room, LocalDate checkIn, LocalDate checkOut) {
        if (!room.getStatus().equalsIgnoreCase("Tersedia")) {
            throw new IllegalStateException("Maaf, kamar ini sudah tidak tersedia (Status: " + room.getStatus() + ")");
        }
        
        if (checkOut.isBefore(checkIn)) {
            throw new IllegalArgumentException("Tanggal Check-Out tidak boleh lebih dulu dari Check-In!");
        }

        String idReservasi = "RES-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        Reservasi reservasiBaru = new Reservasi(idReservasi, pelanggan, room, checkIn, checkOut);
        
        reservasiDAO.addReservasi(reservasiBaru);
        
        return reservasiBaru;
    }
    
    public List<Reservasi> getUserHistory(String idPelanggan) {
        return reservasiDAO.getReservasiByPelanggan(idPelanggan);
    }
    
    public void cancelReservation(String idReservasi) {
        reservasiDAO.cancelReservasi(idReservasi);
    }
}
