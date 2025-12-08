/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package projectpbo.view.panels;

import java.awt.Color;
import java.awt.Font;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import projectpbo.dao.UserDAO;
import projectpbo.model.person.Karyawan;
import projectpbo.model.person.Person;
import projectpbo.model.person.Pelanggan;
/**
 *
 * @author Naufal
 */
public class AdminUserPanel extends javax.swing.JPanel {

    /**
     * Creates new form AdminUserPanel
     */
    
    private UserDAO userDAO = new UserDAO();
    private JTable tableUser;
    private DefaultTableModel tableModel;
    
    public AdminUserPanel() {
        initComponentsCustom();
        loadTableData();
    }
    
    private String selectedUserId = null;
    
    private javax.swing.JTextField txtNama, txtUser;
    private javax.swing.JPasswordField txtPass;
    private javax.swing.JComboBox<String> comboRole;

    private void initComponentsCustom() {
        this.setLayout(null); 
        this.setBackground(new Color(0, 204, 255)); 

        JLabel lblTitle = new JLabel("MANAJEMEN PENGGUNA");
        lblTitle.setFont(new Font("Perpetua Titling MT", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(30, 20, 300, 30);
        this.add(lblTitle);

        int y = 70;
        addLabel("Nama:", 30, y); txtNama = addTextField(120, y); y+=40;
        addLabel("Username:", 30, y); txtUser = addTextField(120, y); y+=40;
        addLabel("Password:", 30, y); txtPass = addPasswordField(120, y); y+=40;
        addLabel("Role:", 30, y); 
        comboRole = new javax.swing.JComboBox<>(new String[]{"GUEST", "ADMIN"});
        comboRole.setBounds(120, y, 200, 30);
        this.add(comboRole);
        
        JButton btnSave = new JButton("SIMPAN USER");
        btnSave.setBounds(340, 70, 150, 40);
        btnSave.setBackground(new Color(0, 153, 0));
        btnSave.setForeground(Color.WHITE);
        btnSave.addActionListener(e -> actionSaveOrUpdate());
        this.add(btnSave);

        JButton btnReset = new JButton("RESET FORM");
        btnReset.setBounds(340, 120, 150, 30);
        btnReset.addActionListener(e -> resetForm());
        this.add(btnReset);

        String[] columns = {"ID", "Nama", "Username", "Role", "Jabatan"};
        tableModel = new DefaultTableModel(columns, 0);
        tableUser = new JTable(tableModel);
        tableUser.setRowHeight(25);
        
        tableUser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = tableUser.getSelectedRow();
                if (row != -1) {
                    selectedUserId = tableUser.getValueAt(row, 0).toString();
                    txtNama.setText(tableUser.getValueAt(row, 1).toString());
                    txtUser.setText(tableUser.getValueAt(row, 2).toString());
                    comboRole.setSelectedItem(tableUser.getValueAt(row, 3).toString());
                    
                    txtPass.setText(""); 
                    JOptionPane.showMessageDialog(null, "Mode Edit Aktif: Silakan isi password baru/lama untuk konfirmasi update.");
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tableUser);
        scrollPane.setBounds(30, 250, 600, 250);
        this.add(scrollPane);

        JButton btnHapus = new JButton("HAPUS USER");
        btnHapus.setBounds(30, 510, 120, 30);
        btnHapus.setBackground(Color.RED);
        btnHapus.setForeground(Color.WHITE);
        btnHapus.addActionListener(e -> actionHapus());
        this.add(btnHapus);
        
        JButton btnRefresh = new JButton("REFRESH");
        btnRefresh.setBounds(160, 510, 100, 30);
        btnRefresh.addActionListener(e -> loadTableData());
        this.add(btnRefresh);
    }
    
    private void addLabel(String text, int x, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(Color.WHITE);
        lbl.setBounds(x, y, 80, 30);
        this.add(lbl);
    }
    private javax.swing.JTextField addTextField(int x, int y) {
        javax.swing.JTextField txt = new javax.swing.JTextField();
        txt.setBounds(x, y, 200, 30);
        this.add(txt);
        return txt;
    }
    private javax.swing.JPasswordField addPasswordField(int x, int y) {
        javax.swing.JPasswordField txt = new javax.swing.JPasswordField();
        txt.setBounds(x, y, 200, 30);
        this.add(txt);
        return txt;
    }

    private void loadTableData() {
        DefaultTableModel model = (DefaultTableModel) tableUser.getModel();
        model.setRowCount(0);
        
        try {
            List<Person> users = userDAO.getAllUsers(); 
            for (Person p : users) {
                String role = (p instanceof Karyawan) ? "ADMIN" : "GUEST";
                String jabatan = (p instanceof Karyawan) ? ((Karyawan)p).getJabatan() : "-";
                
                model.addRow(new Object[]{
                    p.getId(), p.getNama(), p.getUsername(), role, jabatan
                });
            }
        } catch (Exception e) {
            System.err.println("Gagal load table: " + e.getMessage());
        }
    }
    
    private void resetForm() {
        txtNama.setText("");
        txtUser.setText("");
        txtPass.setText("");
        comboRole.setSelectedIndex(0);
        selectedUserId = null;
        tableUser.clearSelection();
    }
    
    private void actionSaveOrUpdate() {
        try {
            String nama = txtNama.getText();
            String user = txtUser.getText();
            String pass = new String(txtPass.getPassword());
            String role = comboRole.getSelectedItem().toString();
            
            if (nama.isEmpty() || user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Semua data (termasuk password) harus diisi!");
                return;
            }

            String id;
            if (selectedUserId != null) {
                id = selectedUserId; // Update
            } else {
                id = (role.equals("ADMIN") ? "K-" : "P-") + System.currentTimeMillis(); // Insert
            }
            
            Person p;
            if(role.equals("ADMIN")) {
                p = new Karyawan(id, nama, user, pass, "Staff");
            } else {
                p = new Pelanggan(id, nama, user, pass);
            }
            
            if (selectedUserId != null) {
                userDAO.updateUser(p); 
                JOptionPane.showMessageDialog(this, "Data User Berhasil Diupdate!");
            } else {
                userDAO.addUser(p);
                JOptionPane.showMessageDialog(this, "User Baru Berhasil Ditambah!");
            }
            
            loadTableData();
            resetForm();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal: " + e.getMessage());
        }
    }
    
    private void actionHapus() {
        int row = tableUser.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih user yang ingin dihapus!");
            return;
        }
        
        String id = tableUser.getValueAt(row, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(this, "Yakin hapus user ID: " + id + "?");
        
        if (confirm == JOptionPane.YES_OPTION) {
             userDAO.deleteUser(id);
             loadTableData();
             resetForm();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        button1 = new java.awt.Button();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPasswordField1 = new javax.swing.JPasswordField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        button1.setLabel("button1");

        jPanel1.setBackground(new java.awt.Color(102, 204, 255));

        jLabel1.setText("------------------------------------------------------------------------------------------------------");

        jLabel2.setText("AdminUserPanel");

        jLabel3.setFont(new java.awt.Font("Perpetua", 1, 14)); // NOI18N
        jLabel3.setText("MANAJEMEN PENGGUNA");

        jLabel4.setText("Tambah Karyawan Baru:");

        jLabel5.setText("Nama        :");

        jLabel6.setText("Username  :");

        jLabel7.setText("Password  :");

        jPasswordField1.setText("jPasswordField1");
        jPasswordField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordField1ActionPerformed(evt);
            }
        });

        jLabel8.setText("Jabatan  :");

        jLabel9.setText("+---------------------------+");

        jLabel10.setText("+---------------------------+");

        jLabel11.setFont(new java.awt.Font("Perpetua", 1, 14)); // NOI18N
        jLabel11.setText("+ TAMBAH STAFF");

        jLabel12.setText("Daftar Semua Pengguna:");

        jLabel13.setText("+--------------------------------------------------------------------------------------------+");

        jLabel14.setText("I      ID           I        NAMA      I       ROLE              I               JABATAN                     I");

        jLabel15.setText("+--------------------------------------------------------------------------------------------+");

        jLabel16.setText("I     P-001      I       BUDI          I       GUEST            I                       -                          I");

        jLabel17.setText("I     K-001      I       ADMIN      I       ADMIN           I               MANAGER                   I");

        jLabel18.setText("+--------------------------------------------------------------------------------------------+");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 439, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel4)
                                .addComponent(jLabel3)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel5)
                                    .addGap(211, 211, 211)
                                    .addComponent(jLabel8))
                                .addComponent(jLabel12)
                                .addComponent(jLabel13))
                            .addComponent(jLabel15)
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 439, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 439, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18))
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel8)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addGap(35, 35, 35)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel15)
                .addGap(4, 4, 4)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel18)
                .addContainerGap(27, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jPasswordField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPasswordField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jPasswordField1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private java.awt.Button button1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPasswordField jPasswordField1;
    // End of variables declaration//GEN-END:variables
}
