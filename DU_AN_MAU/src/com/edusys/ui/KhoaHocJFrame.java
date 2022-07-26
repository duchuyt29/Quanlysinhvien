/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.edusys.ui;

import com.edusys.dao.ChuyenDeDAO;
import com.edusys.dao.KhoaHocDAO;
import com.edusys.entity.ChuyenDe;
import com.edusys.entity.KhoaHoc;
import com.edusys.utils.Auth;
import com.edusys.utils.MsgBox;
import com.edusys.utils.XDate;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;

public class KhoaHocJFrame extends javax.swing.JInternalFrame {

    /**
     * Creates new form KhoaHocJFrame
     */
    KhoaHocDAO khdao = new KhoaHocDAO();
    ChuyenDeDAO cddao = new ChuyenDeDAO();
    int row = -1;

    public KhoaHocJFrame() {
        initComponents();
        init();
    }

    void init() {
        this.fillComboBoxChuyenDe();
    }

    void insert() {
        KhoaHoc kh = getForm();
        kh.setNgayTao(new Date());

        try {
            khdao.insert(kh);
            this.fillTable();
            this.clearForm();
            MsgBox.alert(this, "Thêm mới thành công!");
        } catch (Exception e) {
            MsgBox.alert(this, "Thêm mới thất bại!" + e);
        }

    }

    void update() {
//        KhoaHoc kh = getForm();
//        Integer makh = (Integer) tblKhoaHoc.getValueAt(tblKhoaHoc.getSelectedRow(), 0);
//        kh.setMaKH(makh);
//
//        try {
//            khdao.update(kh);
//            this.fillTable();
//            MsgBox.alert(this, "Cập nhật thành công!");
//        } catch (Exception e) {
//            MsgBox.alert(this, "Cập nhật thất bại!");
//        }
        KhoaHoc kh = this.getForm();
        if (kh == null) {
            return;
        }
        try {
            kh.setMaKH((Integer) tblKhoaHoc.getValueAt(row, 0));
            khdao.update(kh);
            this.fillTable();
            MsgBox.alert(this, "Cập nhật thành công !");
        } catch (Exception e) {
            MsgBox.alert(this, "Cập nhật thất bại !");
        }
    }

    void delete() {
        if (!Auth.isManager()) {
            MsgBox.alert(this, "Bạn không có quyền xóa chuyên đề!");
        } else {
            if (MsgBox.confirm(this, "Bạn có thực sự muốn xóa khóa học này?")) {
                try {
                    int makh = (int) tblKhoaHoc.getValueAt(this.row, 0);
                    khdao.delete(makh);
                    this.fillTable();
                    this.clearForm();
                    MsgBox.alert(this, "Xóa thành công!");
                } catch (Exception e) {
                    MsgBox.alert(this, "Xóa thát bại!");
                }
            }
        }
    }

    void clearForm() {
        KhoaHoc kh = new KhoaHoc();
        ChuyenDe cd = (ChuyenDe) cboChuyenDe.getSelectedItem();

        kh.setMaCD(cd.getMaCD());
        kh.setMaNV(Auth.user.getMaNV());
        kh.setNgayTao(XDate.now());
        kh.setNgayKG(XDate.addDays(XDate.now(), 30));

        this.setForm(kh);
        this.row = -1;
        this.updateStatus();
    }

    void edit() {
        try {
            Integer makh = (Integer) tblKhoaHoc.getValueAt(this.row, 0);
            KhoaHoc kh = khdao.selectByID(makh);
            if (kh != null) {
                this.setForm(kh);
                this.updateStatus();
            }
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vẫn dữ liệu!");
        }
    }

    void first() {
        this.row = 0;
        this.edit();
    }

    void prev() {
        if (this.row > 0) {
            this.row--;
            this.edit();
        }
    }

    void next() {
        if (this.row < tblKhoaHoc.getRowCount() - 1) {
            this.row++;
            this.edit();
        }
    }

    void last() {
        this.row = tblKhoaHoc.getRowCount() - 1;
        this.edit();
    }

    void fillComboBoxChuyenDe() {
        DefaultComboBoxModel model = (DefaultComboBoxModel) cboChuyenDe.getModel();
        model.removeAllElements();
        List<ChuyenDe> list = cddao.selectAll();
        for (ChuyenDe cd : list) {
            model.addElement(cd);
        }

    }

    void fillTable() {
        DefaultTableModel model = (DefaultTableModel) tblKhoaHoc.getModel();
        model.setRowCount(0); // xoa tat ca cac hang
        try {
            ChuyenDe cd = (ChuyenDe) cboChuyenDe.getSelectedItem();
            List<KhoaHoc> list = khdao.selectByChuyenDe(cd.getMaCD());
            for (KhoaHoc kh : list) {
                Object[] row = {
                    kh.getMaKH(),
                    kh.getThoiLuong(),
                    kh.getHocPhi(),
                    XDate.toString(kh.getNgayKG()),
                    kh.getMaNV(),
                    XDate.toString(kh.getNgayTao())

                };
                model.addRow(row); //them 1 hang vao table
            }
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu!");
        }
//        DefaultTableModel dtm = (DefaultTableModel) tblKhoaHoc.getModel();
//        dtm.setRowCount(0);
//        try {
//            ChuyenDe cd = (ChuyenDe) cboChuyenDe.getSelectedItem();
//            List<KhoaHoc> lstkh = khdao.selectByChuyenDe(cd.getMaCD());//lấy ds khoá học phụ thuộc vào chuyên đề
//            for (KhoaHoc kh : lstkh) {
//                Object[] row = new Object[]{
//                    kh.getMaKH(),
//                    kh.getMaCD(),
//                    kh.getThoiLuong(),
//                    kh.getHocPhi(),
//                    kh.getNgayKG(),
//                    kh.getMaNV(),
//                    kh.getNgayTao()
//                };
//                dtm.addRow(row);
//            }
//        } catch (Exception e) {
//            MsgBox.alert(this, "Lỗi truy xuất dữ liệu");
//            e.printStackTrace();
//        }
    }

    void chonChuyenDe() {
//        ChuyenDe cd = (ChuyenDe) cboChuyenDe.getSelectedItem();
//
//        txtThoiLuong.setText(String.valueOf(cd.getThoiLuong()));
//        txtHocPhi.setText(String.valueOf(cd.getHocPhi()));
//        txtChuyenDe.setText(cd.getTenCD());
//        txtGhiChu.setText(cd.getTenCD());
////        
//        this.fillTable();
//        this.row = -1;
//        tabs.setSelectedIndex(1);
//        this.updateStatus();
        try {
            ChuyenDe cd = (ChuyenDe) cboChuyenDe.getSelectedItem();
            txtThoiLuong.setText(String.valueOf(cd.getThoiLuong()));
            txtHocPhi.setText(String.valueOf(cd.getHocPhi()));
            txtChuyenDe.setText(cd.getTenCD());
            txtGhiChu.setText(cd.getTenCD());
            txtNgayTao.setText(String.valueOf(java.time.LocalDate.now()));
            txtNguoiTao.setText(Auth.user.getMaNV());

            this.fillTable();//đổ lại DL khoahoc lên bảg
            this.row = -1;
            this.updateStatus();
            tabs.setSelectedIndex(1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void setForm(KhoaHoc kh) {
        List<ChuyenDe> list = cddao.selectAll();
        for (ChuyenDe cd : list) {
            if (cd.getMaCD().equals(kh.getMaCD())) {
                txtChuyenDe.setText(cd.getTenCD());
                txtHocPhi.setText(Double.toString(cd.getHocPhi()));
                txtThoiLuong.setText(Integer.toString(cd.getThoiLuong()));
                txtNguoiTao.setText(kh.getMaNV());
                txtNgayTao.setText(XDate.toString(kh.getNgayTao()));
                txtNgayKG.setText(XDate.toString(kh.getNgayKG()));
                txtGhiChu.setText(kh.getGhiChu());
                return;
            }
        }
//        this.txtHocPhi.setText(String.valueOf(kh.getHocPhi()));
//        this.txtNgayKG.setText(String.valueOf(kh.getNgayKG()));
//        this.txtNgayTao.setText(String.valueOf(kh.getNgayTao()));
//        this.txtNguoiTao.setText(kh.getMaNV());
//        this.txtThoiLuong.setText(String.valueOf(kh.getThoiLuong()));
//        this.txtGhiChu.setText(kh.getGhiChu());

    }

    KhoaHoc getForm() {
        KhoaHoc kh = new KhoaHoc();
        ChuyenDe cd = (ChuyenDe) cboChuyenDe.getSelectedItem();

        kh.setMaCD(cd.getMaCD());
        kh.setNgayKG(XDate.toDate(txtNgayKG.getText()));
        kh.setHocPhi(Double.valueOf(txtHocPhi.getText()));
        kh.setThoiLuong(Integer.valueOf(txtThoiLuong.getText()));
        kh.setMaNV(Auth.user.getMaNV());
        kh.setNgayTao(XDate.toDate(txtNgayTao.getText()));
        kh.setGhiChu(txtGhiChu.getText());
        return kh;
//        KhoaHoc kh = new KhoaHoc();
//        ChuyenDe cd = (ChuyenDe) cboChuyenDe.getSelectedItem();
//        kh.setMaCD(cd.getMaCD());
//        kh.setHocPhi(Double.valueOf(txtHocPhi.getText()));
//        kh.setNgayKG(java.sql.Date.valueOf(txtNgayKG.getText()));
//        kh.setMaNV(Auth.user.getMaNV());
//        kh.setNgayTao(java.sql.Date.valueOf(txtNgayTao.getText()));
//        kh.setGhiChu(txtGhiChu.getText());
//        kh.setThoiLuong(Integer.valueOf(txtThoiLuong.getText()));
//        return kh;
    }

    void updateStatus() {
//        boolean add = (this.row < 0);
//        boolean edit = (this.row >= 0);
//        boolean first = (this.row == 0);
//        boolean last = (this.row == tblKhoaHoc.getRowCount() - 1);
//        //Trang thai form
//        txtChuyenDe.setEditable(!edit);
//        txtHocPhi.setEditable(edit);
//        txtThoiLuong.setEditable(edit);
//        btnThem.setEnabled(!edit && add);
//        btnSua.setEnabled(edit);
//        btnXoa.setEnabled(edit);
//        //Trang thai dieu huong
//        btnFirst.setEnabled(edit && !first);
//        btnPrev.setEnabled(edit && !first);
//        btnNext.setEnabled(edit && !last);
//        btnLast.setEnabled(edit && !last);
         boolean edit = (this.row >= 0);//bản ghi bất kì
        boolean first = (this.row == 0);//bản ghi đầu
        boolean last = (this.row == tblKhoaHoc.getRowCount() - 1);//bản ghi cuối

        
        this.btnThem.setEnabled(!edit);
        this.btnSua.setEnabled(edit);
        this.btnXoa.setEnabled(edit);

        btnFirst.setEnabled(edit && !first);
        btnPrev.setEnabled(edit && !first);
        btnNext.setEnabled(edit && !last);
        btnLast.setEnabled(edit && !last);
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
        jPanel6 = new javax.swing.JPanel();
        cboChuyenDe = new javax.swing.JComboBox<>();
        tabs = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtChuyenDe = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtGhiChu = new javax.swing.JTextArea();
        jLabel10 = new javax.swing.JLabel();
        txtHocPhi = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtNguoiTao = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtNgayKG = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtThoiLuong = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtNgayTao = new javax.swing.JTextField();
        btnThem = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnMoi = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        btnFirst = new javax.swing.JButton();
        btnPrev = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnLast = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblKhoaHoc = new javax.swing.JTable();

        setClosable(true);
        setIconifiable(true);
        setTitle("EduSys - Quản Lý Khóa Học");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/com/edusys/utils/fpt.png"))); // NOI18N
        setPreferredSize(new java.awt.Dimension(689, 465));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 51, 255));
        jLabel1.setText("CHUYÊN ĐỀ");

        cboChuyenDe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboChuyenDeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cboChuyenDe, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cboChuyenDe, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setText("Chuyên Đề");

        txtChuyenDe.setEditable(false);
        txtChuyenDe.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtChuyenDe.setForeground(new java.awt.Color(255, 0, 0));
        txtChuyenDe.setEnabled(false);

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setText("Ghi Chú");

        txtGhiChu.setColumns(20);
        txtGhiChu.setRows(5);
        jScrollPane1.setViewportView(txtGhiChu);

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel10.setText("Học Phí");

        txtHocPhi.setEditable(false);

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel11.setText("Người Tạo");

        txtNguoiTao.setEditable(false);

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel12.setText("Khai Giảng");

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel13.setText("Thời Lượng (Giờ)");

        txtThoiLuong.setEditable(false);

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel14.setText("Ngày Tạo");

        txtNgayTao.setEditable(false);

        btnThem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Add.png"))); // NOI18N
        btnThem.setText("Thêm");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        btnSua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Edit.png"))); // NOI18N
        btnSua.setText("Sửa");
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });

        btnMoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Refresh.png"))); // NOI18N
        btnMoi.setText("Mới");
        btnMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoiActionPerformed(evt);
            }
        });

        btnXoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Delete.png"))); // NOI18N
        btnXoa.setText("Xóa");
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        btnFirst.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/first_button.png"))); // NOI18N
        btnFirst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFirstActionPerformed(evt);
            }
        });

        btnPrev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/previous_button.png"))); // NOI18N
        btnPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrevActionPerformed(evt);
            }
        });

        btnNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/next_button.png"))); // NOI18N
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        btnLast.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/last_button.png"))); // NOI18N
        btnLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLastActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnThem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnSua)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnXoa)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(95, 95, 95)
                        .addComponent(btnFirst)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPrev)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnLast, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jScrollPane1)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel10)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel11)
                                        .addComponent(txtNguoiTao, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel7))
                                    .addComponent(txtHocPhi, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtChuyenDe, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jLabel3))
                            .addGap(18, 18, 18)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel12)
                                .addComponent(txtNgayKG, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel13)
                                .addComponent(txtThoiLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtNgayTao, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel14)))))
                .addGap(56, 56, 56))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 19, Short.MAX_VALUE)
                    .addComponent(jLabel12))
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtChuyenDe, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNgayKG, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 19, Short.MAX_VALUE)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtHocPhi, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtThoiLuong))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNgayTao, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNguoiTao, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnFirst, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPrev, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLast, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnThem)
                        .addComponent(btnSua)
                        .addComponent(btnXoa)
                        .addComponent(btnMoi)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabs.addTab("CẬP NHẬT", jPanel1);

        tblKhoaHoc.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã KH", "Thời Lượng", "Học Phí", "Khai Giảng", "Tạo Bởi", "Ngày Tạo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblKhoaHoc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblKhoaHocMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblKhoaHoc);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 839, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE))
        );

        tabs.addTab("DANH SÁCH", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(layout.createSequentialGroup()
                .addComponent(tabs, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tabs)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cboChuyenDeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboChuyenDeActionPerformed
        // TODO add your handling code here:
        chonChuyenDe();
    }//GEN-LAST:event_cboChuyenDeActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        // TODO add your handling code here:
        insert();
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoiActionPerformed
        // TODO add your handling code here:
        clearForm();
    }//GEN-LAST:event_btnMoiActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        // TODO add your handling code here:
        update();
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        // TODO add your handling code here:
        delete();
    }//GEN-LAST:event_btnXoaActionPerformed

    private void btnFirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFirstActionPerformed
        // TODO add your handling code here:
        first();
    }//GEN-LAST:event_btnFirstActionPerformed

    private void btnPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevActionPerformed
        // TODO add your handling code here:
        prev();
    }//GEN-LAST:event_btnPrevActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        // TODO add your handling code here:
        next();
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLastActionPerformed
        // TODO add your handling code here:
        last();
    }//GEN-LAST:event_btnLastActionPerformed

    private void tblKhoaHocMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblKhoaHocMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            this.row = tblKhoaHoc.rowAtPoint(evt.getPoint());
            if (this.row >= 0) {
                this.edit();
                tabs.setSelectedIndex(0);
            }
        }
    }//GEN-LAST:event_tblKhoaHocMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFirst;
    private javax.swing.JButton btnLast;
    private javax.swing.JButton btnMoi;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPrev;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnXoa;
    private javax.swing.JComboBox<String> cboChuyenDe;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane tabs;
    private javax.swing.JTable tblKhoaHoc;
    private javax.swing.JTextField txtChuyenDe;
    private javax.swing.JTextArea txtGhiChu;
    private javax.swing.JTextField txtHocPhi;
    private javax.swing.JTextField txtNgayKG;
    private javax.swing.JTextField txtNgayTao;
    private javax.swing.JTextField txtNguoiTao;
    private javax.swing.JTextField txtThoiLuong;
    // End of variables declaration//GEN-END:variables

}
