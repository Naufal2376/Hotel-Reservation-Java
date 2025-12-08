/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectpbo.model.room;

/**
 *
 * @author Naufal
 */
public class SuiteRoom extends Room {
    public SuiteRoom(String nomorKamar) {
        super(nomorKamar);
        this.harga = 500000;
    }

    @Override
    public double getHargaPerMalam() {
        return this.harga;
    }
}