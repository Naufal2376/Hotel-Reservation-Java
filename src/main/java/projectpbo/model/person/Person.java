/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectpbo.model.person;

/**
 *
 * @author Naufal
 */
public abstract class Person {
    private String id;
    private String nama;
    private String username;
    private String password;

    public Person(String id, String nama, String username, String password) {
        this.id = id;
        this.nama = nama;
        this.username = username;
        this.password = password;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getNama() { return nama; }
    public String getId() { return id; }

    public abstract void displayInfo();
}
