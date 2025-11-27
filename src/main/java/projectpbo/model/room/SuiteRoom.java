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
    private double hargaDasar = 500000;
    private double fasilitasTambahan = 200000;

    public SuiteRoom(String nomorKamar) {
        super(nomorKamar);
    }

    @Override
    public double getHargaPerMalam() {
        return hargaDasar + fasilitasTambahan;
    }
}