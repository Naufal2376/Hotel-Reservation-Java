/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectpbo;

import projectpbo.dao.RoomDAO;
import projectpbo.model.room.Room;
import projectpbo.model.room.StandardRoom;
import projectpbo.model.room.SuiteRoom;
import java.util.List;
/**
 *
 * @author Naufal
 */
public class MainTest {
    public static void main(String[] args) {
        RoomDAO dao = new RoomDAO();

        System.out.println("=== 1. TEST CREATE (Tambah Kamar) ===");
        Room k1 = new StandardRoom("505");
        Room k2 = new SuiteRoom("909");
        
        dao.addRoom(k1);
        dao.addRoom(k2);

        System.out.println("\n=== 2. TEST READ (Lihat Semua Kamar) ===");
        List<Room> semuaKamar = dao.getAllRooms();
        for (Room r : semuaKamar) {
            System.out.println("- Kamar: " + r.getNomorKamar() 
                    + " | Tipe: " + r.getClass().getSimpleName() 
                    + " | Harga: " + r.getHargaPerMalam());
        }

        System.out.println("\n=== 3. TEST DELETE (Hapus Kamar 505) ===");
        dao.deleteRoom("505");
        
        System.out.println("\n=== CEK LAGI SETELAH HAPUS ===");
        semuaKamar = dao.getAllRooms();
        for (Room r : semuaKamar) {
            System.out.println("- Kamar: " + r.getNomorKamar());
        }
    }
}
