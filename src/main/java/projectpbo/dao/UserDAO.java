/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectpbo.dao;

import projectpbo.model.person.Karyawan;
import projectpbo.model.person.Pelanggan;
import projectpbo.model.person.Person;
import projectpbo.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Naufal
 */
public class UserDAO {
    public void addUser(Person person) throws SQLException {
        String sql = "INSERT INTO person (id, nama, username, password, role, jabatan) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, person.getId());
            stmt.setString(2, person.getNama());
            stmt.setString(3, person.getUsername());
            stmt.setString(4, person.getPassword());
            
            if (person instanceof Karyawan) {
                stmt.setString(5, "ADMIN");
                stmt.setString(6, ((Karyawan) person).getJabatan());
            } else {
                stmt.setString(5, "GUEST");
                stmt.setString(6, null);
            }
            
            stmt.executeUpdate();
        }
    }

    public Person getByUsername(String username) {
        String sql = "SELECT * FROM person WHERE username = ?";
        Person user = null;
        
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String role = rs.getString("role");
                
                if ("ADMIN".equalsIgnoreCase(role)) {
                    user = new Karyawan(
                        rs.getString("id"),
                        rs.getString("nama"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("jabatan")
                    );
                } else {
                    user = new Pelanggan(
                        rs.getString("id"),
                        rs.getString("nama"),
                        rs.getString("username"),
                        rs.getString("password")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Gagal cari user: " + e.getMessage());
        }
        return user;
    }
    
    public List<Person> getAllUsers() {
        List<Person> listUser = new ArrayList<>();
        String sql = "SELECT * FROM person";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String role = rs.getString("role");
                Person p;
                
                if ("ADMIN".equalsIgnoreCase(role)) {
                    p = new Karyawan(
                        rs.getString("id"),
                        rs.getString("nama"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("jabatan")
                    );
                } else {
                    p = new Pelanggan(
                        rs.getString("id"),
                        rs.getString("nama"),
                        rs.getString("username"),
                        rs.getString("password")
                    );
                }
                listUser.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Gagal ambil data user: " + e.getMessage());
        }
        
        return listUser; 
    }
    
    public void updateUser(Person person) throws SQLException {
        String sql = "UPDATE person SET nama=?, username=?, password=?, role=?, jabatan=? WHERE id=?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, person.getNama());
            stmt.setString(2, person.getUsername());
            stmt.setString(3, person.getPassword());
            
            if (person instanceof Karyawan) {
                stmt.setString(4, "ADMIN");
                stmt.setString(5, ((Karyawan) person).getJabatan());
            } else {
                stmt.setString(4, "GUEST");
                stmt.setString(5, null);
            }
            
            stmt.setString(6, person.getId());
            
            stmt.executeUpdate();
            System.out.println("[SUKSES] User " + person.getUsername() + " berhasil diupdate.");
        }
    }

    public void deleteUser(String id) {
        String sql = "DELETE FROM person WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Gagal hapus user: " + e.getMessage());
        }
    }
}
