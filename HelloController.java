package com.example.pbouas;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.Optional;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HelloController {


    @FXML
    private Pane myPane;

    @FXML
    private Pane Tentang;

    @FXML
    private Pane PaneTambah;


    @FXML
    private TableView<DataBarang> tableView;

    @FXML
    private TableColumn<DataBarang, String> namaBarangColumn;

    @FXML
    private TableColumn<DataBarang, Integer> nomorBarangColumn;

    @FXML
    private TableColumn<DataBarang, Integer> jumlahColumn;

    @FXML
    private TableColumn<DataBarang, String> spesifikasiColumn;

    @FXML
    private TableColumn<DataBarang, String> tanggalColumn;

    @FXML
    private TextField namaBarangField;

    @FXML
    private TextField nomorBarangField;

    @FXML
    private TextField jumlahField;

    @FXML
    private TextField spesifikasiField;


    //daftar
    @FXML
    private TextField TFNIM;

    @FXML
    private TextField TFNama;

    @FXML
    private PasswordField TFPass;

    @FXML
    Button Update;

    @FXML
    Button tambah;

    @FXML
    private Text jumlahDataText;

    @FXML
    private Text jumlahUserText;






    private static final String DB_URL = "jdbc:mysql://localhost/elab";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";





    // Metode bantuan untuk memuat data dari database ke dalam TableView
    public class DataBarang {

        private String namaBarang;
        private int nomorBarang;
        private int jumlah;
        private String spesifikasi;
        private String tanggal;

        // Constructor
        public DataBarang(String namaBarang, int nomorBarang, int jumlah, String spesifikasi, String tanggal) {
            this.namaBarang = namaBarang;
            this.nomorBarang = nomorBarang;
            this.jumlah = jumlah;
            this.spesifikasi = spesifikasi;
            this.tanggal = tanggal;
        }

        // Getters
        public String getNamaBarang() {
            return namaBarang;
        }

        public int getNomorBarang() {
            return nomorBarang;
        }

        public int getJumlah() {
            return jumlah;
        }

        public String getSpesifikasi() {
            return spesifikasi;
        }

        public String getTanggal() {
            return tanggal;
        }

        // Setters
        public void setNamaBarang(String namaBarang) {
            this.namaBarang = namaBarang;
        }

        public void setNomorBarang(int nomorBarang) {
            this.nomorBarang = nomorBarang;
        }

        public void setJumlah(int jumlah) {
            this.jumlah = jumlah;
        }

        public void setSpesifikasi(String spesifikasi) {
            this.spesifikasi = spesifikasi;
        }

        public void setTanggal(String tanggal) {
            this.tanggal = tanggal;
        }
    }

    //LOAD DATA DARI DATABASE BARANG KE TABLEVIEW
    private void loadTableData() {
        tableView.getItems().clear();

        namaBarangColumn.setCellValueFactory(new PropertyValueFactory<>("namaBarang"));
        nomorBarangColumn.setCellValueFactory(new PropertyValueFactory<>("nomorBarang"));
        jumlahColumn.setCellValueFactory(new PropertyValueFactory<>("jumlah"));
        spesifikasiColumn.setCellValueFactory(new PropertyValueFactory<>("spesifikasi"));
        tanggalColumn.setCellValueFactory(new PropertyValueFactory<>("tanggal"));

        List<DataBarang> dataBarangList = getDataFromDatabase();
        tableView.getItems().addAll(dataBarangList);
        updateJumlahData();
        updateJumlahUser();
    }

    private List<DataBarang> getDataFromDatabase() {
        List<DataBarang> dataBarangList = new ArrayList<>();

        String query = "SELECT * FROM barang";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                String namaBarang = resultSet.getString("Nama_Barang");
                int nomorBarang = resultSet.getInt("Nomor_Barang");
                int jumlah = resultSet.getInt("Jumlah");
                String spesifikasi = resultSet.getString("Spesifikasi");
                String tanggal = resultSet.getString("Tanggal");

                DataBarang dataBarang = new DataBarang(namaBarang, nomorBarang, jumlah, spesifikasi, tanggal);
                dataBarangList.add(dataBarang);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dataBarangList;
    }

    //BUAT MENAMPILKAN PANE CRUD
    @FXML
    private void togglePaneVisibility() {
        refreshTableData();
        myPane.setVisible(!myPane.isVisible());
        Tentang.setVisible(false);
    }
    //MENAMPILKAN DASHBOARD
    @FXML
    private void hideBothPanes() {
        // Menyembunyikan kedua pane, 'myPane' dan 'Tentang'
        myPane.setVisible(false);
        Tentang.setVisible(false);
    }

    //MENAMPILKAN PANE TENTANG KAMI
    @FXML
    private void tentangg() {
        // Menampilkan pane 'Tentang' dan menyembunyikan 'myPane'
        myPane.setVisible(false);
        Tentang.setVisible(true);
    }

    //METHOD BUTTON TAMBAH DATA JADI KEETIKA MENEKAN BUTTON TAMBAH DATA AKAN MELAKUKAN INI SEMUA
    @FXML
    private void Tambahhh() {
        namaBarangField.clear();
        nomorBarangField.clear();
        jumlahField.clear();
        spesifikasiField.clear();
        PaneTambah.setVisible(true);
        nomorBarangField.setDisable(false);
        tambah.setVisible(true);
        Update.setVisible(false);
    }


    // METHOD MEMBUKA SCENE BARU
    private void loadNewScene(String fxmlFileName, ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
        Parent newSceneParent = null;
        try {
            newSceneParent = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Membuat scene baru
        Scene newScene = new Scene(newSceneParent);

        // Mendapatkan informasi stage
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Menetapkan scene baru pada stage
        stage.setScene(newScene);

        // Menampilkan stage dengan scene baru
        stage.show();
    }

    //UNTUK MENAMBAHKAN DATA KE DATABASE
    @FXML
    private void handleTambahDataButton(ActionEvent event) {
        String namaBarang = namaBarangField.getText();
        int jumlah = Integer.parseInt(jumlahField.getText());
        String spesifikasi = spesifikasiField.getText();
        String tanggal = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        int nomorBarang = Integer.parseInt(nomorBarangField.getText());


        // PENGECEKAN APAKAH NOMOR BARANG SUDAH ADA APA BELUM
        if (!isNomorBarangExists(nomorBarang)) {
            if (tambahDataBarang(namaBarang, nomorBarang, jumlah, spesifikasi, tanggal)) {
                System.out.println("Data berhasil ditambahkan");
                PaneTambah.setVisible(false);
                namaBarangField.clear();
                nomorBarangField.clear();
                jumlahField.clear();
                spesifikasiField.clear();
                refreshTableData();
            } else {
                System.out.println("Gagal menambahkan data");
            }
        } else {
            showAlert("Peringatan", "Nomor Barang sudah ada. Pilih nomor barang lain.");
        }
    }


    //UNTUK REFRESH DATA
    private void refreshTableData() {
        loadTableData();
    }
    private boolean tambahDataBarang(String namaBarang, int nomorBarang, int jumlah, String spesifikasi, String tanggal) {
        if (isNomorBarangExists(nomorBarang)) {
            showAlert("Peringatan", "Nomor Barang sudah ada. Pilih nomor barang lain.");
            return false;
        }

        String query = "INSERT INTO barang (Nama_Barang, Nomor_Barang, Jumlah, Spesifikasi, Tanggal) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, namaBarang);
            preparedStatement.setInt(2, nomorBarang);
            preparedStatement.setInt(3, jumlah);
            preparedStatement.setString(4, spesifikasi);
            preparedStatement.setString(5, tanggal);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isNomorBarangExists(int nomorBarang) {
        String query = "SELECT COUNT(*) FROM barang WHERE Nomor_Barang = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, nomorBarang);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    //UNTUK SIGNUP ATAU DAFTAR
    @FXML
    private void handleSignUpButton(ActionEvent event) {
        String nim = TFNIM.getText(); String nama = TFNama.getText(); String pass = TFPass.getText();

        if (registerUser(nim, nama, pass)) {
            System.out.println("User registered successfully");
            loadNewScene("Login.fxml", event);

        } else {
            System.out.println("Failed to register user");
        }
    }


    //METHOD HAPUS DATA
    @FXML
    private void handleHapusDataButton(ActionEvent event) {
        // Mendapatkan data terpilih dari tabel
        DataBarang selectedData = tableView.getSelectionModel().getSelectedItem();
        if (selectedData != null) {
            // Menampilkan konfirmasi sebelum menghapus data
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Konfirmasi");
            alert.setHeaderText("Hapus Data");
            alert.setContentText("Apakah Anda yakin ingin menghapus data ini?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Memanggil metode untuk menghapus data dari database
                if (hapusDataBarang(selectedData.getNomorBarang())) {
                    System.out.println("Data berhasil dihapus");
                    refreshTableData();
                } else {
                    System.out.println("Gagal menghapus data");
                }
            }
        } else {
            showAlert("Peringatan", "Pilih data yang akan dihapus");
        }
    }


    private boolean hapusDataBarang(int nomorBarang) {
        String query = "DELETE FROM barang WHERE Nomor_Barang = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, nomorBarang);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0; // Jika ada baris yang terpengaruh, data berhasil dihapus
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    //METHOD UPDATE DATA
    @FXML
    private void UPDATE() {
        DataBarang selectedData = tableView.getSelectionModel().getSelectedItem();

        if (selectedData != null) {
            // Mengatur nilai dari objek DataBarang ke dalam TextField
            namaBarangField.setText(selectedData.getNamaBarang());
            nomorBarangField.setText(String.valueOf(selectedData.getNomorBarang()));
            jumlahField.setText(String.valueOf(selectedData.getJumlah()));
            spesifikasiField.setText(selectedData.getSpesifikasi());

            // Menampilkan pane update
            PaneTambah.setVisible(true);
            nomorBarangField.setDisable(true);
            Update.setVisible(true);
            tambah.setVisible(false);

        } else {
            showAlert("Peringatan", "Pilih data yang akan diupdate");
        }

    }

    //METHOD UNTUK TOMBOL BATAL
    @FXML
    private void Batal(){
        PaneTambah.setVisible(false);
        namaBarangField.clear();
        nomorBarangField.clear();
        jumlahField.clear();
        spesifikasiField.clear();
    }


    @FXML
    private void handleUpdateDataButton(ActionEvent event) {
        // Mendapatkan data terpilih dari tabel
        DataBarang selectedData = tableView.getSelectionModel().getSelectedItem();
        if (selectedData != null) {
            // Mengambil nilai dari input fields
            String namaBarang = namaBarangField.getText();
            int nomorBarang = Integer.parseInt(nomorBarangField.getText());
            int jumlah = Integer.parseInt(jumlahField.getText());
            String spesifikasi = spesifikasiField.getText();
            String tanggal = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            // Memanggil metode untuk mengupdate data ke dalam database
            if (updateDataBarang(namaBarang, nomorBarang, jumlah, spesifikasi, tanggal)) {
                System.out.println("Data berhasil diupdate");
                // Menyembunyikan pane setelah update
                PaneTambah.setVisible(false);
                namaBarangField.clear();
                nomorBarangField.clear();
                jumlahField.clear();
                spesifikasiField.clear();
                // Memuat ulang data setelah penghapusan
                refreshTableData();
            } else {
                System.out.println("Gagal mengupdate data");
            }
        } else {
            showAlert("Peringatan", "Pilih data yang akan diupdate");
        }
    }

    private boolean updateDataBarang(String namaBarang, int nomorBarang, int jumlah, String spesifikasi, String tanggal) {
        String query = "UPDATE `barang` SET "
                + "`Nama_Barang`=?,"
                + "`Nomor_Barang`=?,"
                + "`Jumlah`=?,"
                + "`Spesifikasi`=?,"
                + "`Tanggal`=? WHERE Nomor_Barang = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
             preparedStatement.setString(1, namaBarang);
             preparedStatement.setInt(2, nomorBarang);
             preparedStatement.setInt(3, jumlah);
             preparedStatement.setString(4, spesifikasi);
             preparedStatement.setString(5, tanggal);
             preparedStatement.setInt(6, nomorBarang);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0; // Jika ada baris yang terpengaruh, data berhasil diupdate
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean registerUser(String nim, String nama, String pass) {
        String query = "INSERT INTO user (NIM, Nama, Pass) VALUES (?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, nim);
            preparedStatement.setString(2, nama);
            preparedStatement.setString(3, pass);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    //MENAMPILKAN JUMLAH DATA BARANG
    private void updateJumlahData() {
        String query = "SELECT COUNT(*) FROM barang";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                int jumlahData = resultSet.getInt(1);
                jumlahDataText.setText(String.valueOf(jumlahData));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //MENAMPILKAN JUMLAH USER
    private void updateJumlahUser() {
        String query = "SELECT COUNT(*) FROM user";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                int jumlahUser = resultSet.getInt(1);
                jumlahUserText.setText(String.valueOf(jumlahUser));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}