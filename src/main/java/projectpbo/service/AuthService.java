/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectpbo.service;

import projectpbo.dao.UserDAO;
import projectpbo.model.person.Person;
/**
 *
 * @author Naufal
 */
public class AuthService {
    private UserDAO userDAO = new UserDAO();
    
    public static Person currentUser; 

    public boolean login(String username, String password) {
        Person user = userDAO.getByUsername(username);
        
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            return true;
        }
        return false;
    }
    
    public void logout() {
        currentUser = null;
    }
    
    public void register(Person person) throws Exception {
        if (userDAO.getByUsername(person.getUsername()) != null) {
            throw new IllegalArgumentException("Username sudah terpakai!");
        }
        userDAO.addUser(person);
    }
}
