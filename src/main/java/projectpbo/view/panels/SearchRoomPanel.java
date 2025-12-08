/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package projectpbo.view.panels;

import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import projectpbo.model.room.Room;
import projectpbo.service.AuthService;
import projectpbo.service.ReservationService;
import projectpbo.service.RoomService;
import projectpbo.view.MainFrame;
/**
 *
 * @author Naufal
 */
public class SearchRoomPanel extends javax.swing.JPanel {

    /**
     * Creates new form SearchRoomPanel
     */
    private RoomService roomService = new RoomService();
    private ReservationService resService = new ReservationService();

    public SearchRoomPanel() {
        initComponentsCustom();
        loadTableData();
    }
    
    public void initComponentsCustom() {
        this.setLayout(new GridBagLayout());
        this.setBackground(new Color(0, 204, 255));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel lblTitle = new JLabel("PENCARIAN KAMAR TERSEDIA");
        lblTitle.setFont(new Font("Perpetua Titling MT", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setHorizontalAlignment(JLabel.CENTER);
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        this.add(lblTitle, gbc);

        gbc.gridwidth = 1; gbc.gridy = 1; gbc.gridx = 0;
        this.add(createLabel("Tipe Kamar:"), gbc);
        
        comboTipe = new JComboBox<>(new String[]{"Semua", "Standard", "Suite"});
        gbc.gridx = 1;
        this.add(comboTipe, gbc);

        gbc.gridy = 2; gbc.gridx = 0;
        this.add(createLabel("Check-In:"), gbc);
        
        dateCheckIn = new JDateChooser();
        dateCheckIn.setDateFormatString("yyyy-MM-dd");
        dateCheckIn.setDate(new Date());
        gbc.gridx = 1;
        this.add(dateCheckIn, gbc);

        gbc.gridy = 3; gbc.gridx = 0;
        this.add(createLabel("Check-Out:"), gbc);
        
        dateCheckOut = new JDateChooser();
        dateCheckOut.setDateFormatString("yyyy-MM-dd");
        Date tomorrow = new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24));
        dateCheckOut.setDate(tomorrow);
        gbc.gridx = 1;
        this.add(dateCheckOut, gbc);

        JButton btnCari = new JButton("CARI KAMAR");
        btnCari.addActionListener(e -> actionCari());
        gbc.gridy = 4; gbc.gridx = 0; gbc.gridwidth = 2;
        this.add(btnCari, gbc);

        String[] cols = {"No Kamar", "Tipe", "Status", "Harga"};
        tableModel = new DefaultTableModel(cols, 0);
        tableRoom = new JTable(tableModel);
        tableRoom.setRowHeight(25);
        
        JScrollPane scroll = new JScrollPane(tableRoom);
        gbc.gridy = 5; gbc.weighty = 1.0; gbc.fill = GridBagConstraints.BOTH;
        this.add(scroll, gbc);

        JPanel panelBtn = new JPanel();
        panelBtn.setOpaque(false);
        
        JButton btnBack = new JButton("KEMBALI");
        btnBack.addActionListener(e -> {
            MainFrame mf = (MainFrame) SwingUtilities.getWindowAncestor(this);
            if(mf != null) mf.showWelcomePanel();
        });
        
        JButton btnPesan = new JButton("PESAN SEKARANG");
        btnPesan.setBackground(Color.ORANGE);
        btnPesan.addActionListener(e -> actionPesan());
        
        panelBtn.add(btnBack);
        panelBtn.add(btnPesan);
        
        gbc.gridy = 6; gbc.weighty = 0;
        this.add(panelBtn, gbc);
    }
    
    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        return lbl;
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        List<Room> list = roomService.getAllRooms();
        for (Room r : list) {
            tableModel.addRow(new Object[]{
                r.getNomorKamar(),
                r.getClass().getSimpleName().replace("Room", ""),
                r.getStatus(),
                String.format("Rp %.0f", r.getHargaPerMalam())
            });
        }
    }
    
    private void actionCari() {
        String filter = comboTipe.getSelectedItem().toString();
        tableModel.setRowCount(0);
        List<Room> list = roomService.getAllRooms();
        
        for (Room r : list) {
            String tipe = r.getClass().getSimpleName();
            if(filter.equals("Semua") || tipe.contains(filter)) {
                tableModel.addRow(new Object[]{
                    r.getNomorKamar(),
                    tipe.replace("Room", ""),
                    r.getStatus(),
                    String.format("Rp %.0f", r.getHargaPerMalam())
                });
            }
        }
    }
    
    private void actionPesan() {
        if (AuthService.currentUser == null) {
            JOptionPane.showMessageDialog(this, "Silakan Login Dulu!"); return;
        }
        if (AuthService.currentUser instanceof projectpbo.model.person.Karyawan) {
            JOptionPane.showMessageDialog(this, "Admin tidak boleh pesan!"); return;
        }
        
        int row = tableRoom.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih kamar di tabel!"); return;
        }
        
        String noKamar = tableRoom.getValueAt(row, 0).toString();
        String status = tableRoom.getValueAt(row, 2).toString();
        
        if (!status.equalsIgnoreCase("Tersedia")) {
            JOptionPane.showMessageDialog(this, "Kamar tidak tersedia!"); return;
        }
        
        Date dIn = dateCheckIn.getDate();
        Date dOut = dateCheckOut.getDate();
        
        if (dIn == null || dOut == null) {
            JOptionPane.showMessageDialog(this, "Pilih tanggal Check-In & Check-Out!"); return;
        }
        
        LocalDate ldIn = dIn.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate ldOut = dOut.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        
        try {
            Room selectedRoom = null;
            for(Room r : roomService.getAllRooms()) {
                if(r.getNomorKamar().equals(noKamar)) { selectedRoom = r; break; }
            }
            
            resService.createReservation(
                (projectpbo.model.person.Pelanggan) AuthService.currentUser, 
                selectedRoom, 
                ldIn, 
                ldOut
            );
            
            JOptionPane.showMessageDialog(this, "Sukses Booking Kamar " + noKamar);
            loadTableData();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal: " + e.getMessage());
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

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        btnCari = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        btnPesanOrder = new javax.swing.JButton();
        btnKembali = new javax.swing.JToggleButton();

        setBackground(new java.awt.Color(0, 204, 255));

        jLabel1.setFont(new java.awt.Font("Perpetua Titling MT", 1, 14)); // NOI18N
        jLabel1.setText("PENCARIAN KAMAR TERSEDIA");

        jLabel2.setText("Tipe Kamar  :");

        jLabel3.setText("Check-In      :");

        jLabel4.setText("Check-Out   :");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        btnCari.setText("CARI KAMAR");
        btnCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Perpetua Titling MT", 1, 14)); // NOI18N
        jLabel5.setText("HASIL PENCARIAN:");

        jTable1.setFont(new java.awt.Font("Perpetua Titling MT", 0, 12)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        btnPesanOrder.setText("PESAN SEKARANG");
        btnPesanOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPesanOrderActionPerformed(evt);
            }
        });

        btnKembali.setText("KEMBALI");
        btnKembali.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKembaliActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(btnKembali, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnPesanOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1)
                        .addComponent(jLabel5)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel4)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jTextField2))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel2)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGap(294, 294, 294)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(25, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCari, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(208, 208, 208))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addComponent(btnCari)
                .addGap(30, 30, 30)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnKembali, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPesanOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(46, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void btnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariActionPerformed
        String tipeDipilih = jComboBox2.getSelectedItem().toString(); 
        
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);
        
        List<Room> listKamar = roomService.getAllRooms();
        for (Room r : listKamar) {
            String tipeKamar = r.getClass().getSimpleName();
            
            if (tipeDipilih.equalsIgnoreCase("Semua") || tipeKamar.contains(tipeDipilih)) { 
                 model.addRow(new Object[]{
                    r.getNomorKamar(), 
                    tipeKamar.replace("Room", ""), 
                    r.getStatus(), 
                    String.format("Rp %.0f", r.getHargaPerMalam())
                });
            }
        }
    }//GEN-LAST:event_btnCariActionPerformed

    private void btnKembaliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKembaliActionPerformed
        MainFrame mainFrame = (MainFrame) SwingUtilities.getWindowAncestor(this);
        if (mainFrame != null) {
            mainFrame.showWelcomePanel();
        }
    }//GEN-LAST:event_btnKembaliActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void btnPesanOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPesanOrderActionPerformed
        if (AuthService.currentUser == null) {
            JOptionPane.showMessageDialog(this, "Sesi habis. Silakan login ulang.");
            return;
        }
        
        if (AuthService.currentUser instanceof projectpbo.model.person.Karyawan) {
            JOptionPane.showMessageDialog(this, "Maaf, akun ADMIN tidak bisa memesan kamar.\nSilakan login sebagai Pelanggan (Guest).");
            return;
        }
        
        if (AuthService.currentUser == null) {
            JOptionPane.showMessageDialog(this, "Sesi habis. Silakan login ulang.");
            return;
        }

        int barisPilih = jTable1.getSelectedRow();
        if (barisPilih == -1) {
            JOptionPane.showMessageDialog(this, "Pilih dulu kamar yang mau dipesan dari tabel!");
            return;
        }
        
        String noKamar = jTable1.getValueAt(barisPilih, 0).toString();
        String status = jTable1.getValueAt(barisPilih, 2).toString(); 
        
        if (!status.equalsIgnoreCase("Tersedia")) {
            JOptionPane.showMessageDialog(this, "Maaf, kamar ini sedang tidak tersedia.");
            return;
        }

        String tglCheckInStr = jTextField1.getText();
        String tglCheckOutStr = jTextField2.getText();
        
        if (tglCheckInStr.equals("YYYY-MM-DD") || tglCheckOutStr.equals("YYYY-MM-DD")) {
            JOptionPane.showMessageDialog(this, "Mohon isi tanggal Check-In dan Check-Out!");
            return;
        }

        try {
            LocalDate checkIn = LocalDate.parse(tglCheckInStr);
            LocalDate checkOut = LocalDate.parse(tglCheckOutStr);
            
            Room roomDipilih = null;
            for(Room r : roomService.getAllRooms()) {
                if(r.getNomorKamar().equals(noKamar)) {
                    roomDipilih = r;
                    break;
                }
            }
            
            resService.createReservation(
                (projectpbo.model.person.Pelanggan) AuthService.currentUser, 
                roomDipilih, 
                checkIn, 
                checkOut
            );
            
            JOptionPane.showMessageDialog(this, "BERHASIL! Kamar " + noKamar + " telah dipesan.");
            
            loadTableData(); 
            
             MainFrame mf = (MainFrame) SwingUtilities.getWindowAncestor(this);
             mf.showHistoryPanel();

        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Format Tanggal Salah!\nGunakan format: YYYY-MM-DD (Contoh: 2023-12-25)");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal Pesan: " + e.getMessage());
        }
    }//GEN-LAST:event_btnPesanOrderActionPerformed


    private JComboBox<String> comboTipe;
    private JDateChooser dateCheckIn;
    private JDateChooser dateCheckOut;
    private JTable tableRoom;
    private DefaultTableModel tableModel;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCari;
    private javax.swing.JToggleButton btnKembali;
    private javax.swing.JButton btnPesanOrder;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
}
