/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectpbo.model.person;

/**
 *
 * @author Naufal
 */
public class Pelanggan extends Person{
    public Pelanggan(String id, String nama, String username, String password) {
        super(id, nama, username, password);
    }

    @Override
    public void displayInfo() {
        System.out.println("Pelanggan: " + getNama());
    }
}
