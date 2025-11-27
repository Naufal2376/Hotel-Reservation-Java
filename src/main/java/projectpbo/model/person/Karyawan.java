/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectpbo.model.person;

/**
 *
 * @author Naufal
 */
public class Karyawan extends Person {
    private String jabatan;

    public Karyawan(String id, String nama, String username, String password, String jabatan) {
        super(id, nama, username, password);
        this.jabatan = jabatan;
    }

    public String getJabatan() { return jabatan; }

    @Override
    public void displayInfo() {
        System.out.println("Karyawan (" + jabatan + "): " + getNama());
    }
}
