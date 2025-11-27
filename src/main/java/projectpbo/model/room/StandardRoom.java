/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectpbo.model.room;

/**
 *
 * @author Naufal
 */
public class StandardRoom extends Room {
    private double hargaDasar = 300000;

    public StandardRoom(String nomorKamar) {
        super(nomorKamar);
    }

    @Override
    public double getHargaPerMalam() {
        return hargaDasar;
    }
}