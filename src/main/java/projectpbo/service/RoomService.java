/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectpbo.service;

import projectpbo.dao.RoomDAO;
import projectpbo.model.room.Room;
import java.util.List;
import java.util.stream.Collectors;
/**
 *
 * @author Naufal
 */
public class RoomService {
    private RoomDAO roomDAO = new RoomDAO();

    public List<Room> getAllRooms() {
        return roomDAO.getAllRooms();
    }

    public List<Room> getAvailableRooms() {
        List<Room> all = roomDAO.getAllRooms();
        return all.stream()
                .filter(r -> r.getStatus().equalsIgnoreCase("Tersedia"))
                .collect(Collectors.toList());
    }
    
    public void saveRoom(Room room) {
        if (room.getNomorKamar().isEmpty()) {
            throw new IllegalArgumentException("Nomor kamar tidak boleh kosong!");
        }
        roomDAO.addRoom(room);
    }
    
    public void deleteRoom(String nomorKamar) {
        roomDAO.deleteRoom(nomorKamar);
    }
}
