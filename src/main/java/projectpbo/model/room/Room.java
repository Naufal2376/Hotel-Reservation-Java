/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectpbo.model.room;

/**
 *
 * @author Naufal
 */
public abstract class Room {
    private String nomorKamar;
    private String status;
    protected double harga;

    public Room(String nomorKamar) {
        this.nomorKamar = nomorKamar;
        this.status = "Tersedia";
    }

    public abstract double getHargaPerMalam();
    
    public void setHarga(double harga) {
        this.harga = harga;
    }

    public String getNomorKamar() { return nomorKamar; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
