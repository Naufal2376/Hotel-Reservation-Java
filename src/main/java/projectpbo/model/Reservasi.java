/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectpbo.model;

import projectpbo.model.payment.IPayable;
import projectpbo.model.person.Pelanggan;
import projectpbo.model.room.Room;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 *
 * @author Naufal
 */
public class Reservasi implements IPayable {
    private String idReservasi;
    private Pelanggan pelanggan;
    private Room room;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private double totalBiaya;

    public Reservasi(String idReservasi, Pelanggan pelanggan, Room room, LocalDate checkIn, LocalDate checkOut) {
        this.idReservasi = idReservasi;
        this.pelanggan = pelanggan;
        this.room = room;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.totalBiaya = hitungTotalTagihan();
    }

    @Override
    public double hitungTotalTagihan() {
        long lamaMenginap = ChronoUnit.DAYS.between(checkIn, checkOut);
        if (lamaMenginap < 1) lamaMenginap = 1;
        return room.getHargaPerMalam() * lamaMenginap;
    }

    @Override
    public void prosesPembayaran() {
        System.out.println("Pembayaran sebesar Rp" + totalBiaya + " berhasil diproses.");
        room.setStatus("Dipesan");
    }

    public String getIdReservasi() { return idReservasi; }
    public Pelanggan getPelanggan() { return pelanggan; }
    public Room getRoom() { return room; }
    public double getTotalBiaya() { return totalBiaya; }
    public LocalDate getCheckIn() { return checkIn; }
    public LocalDate getCheckOut() { return checkOut; }
}