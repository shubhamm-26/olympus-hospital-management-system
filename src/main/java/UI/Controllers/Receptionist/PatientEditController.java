package UI.Controllers.Receptionist;

import UI.Elements.PopUpBox;
import UI.Elements.Report;
import UI.Functions.JumpScene;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import currentsession.CurrentPatientInfo;
import database.DBFetchers.getPatientInfo;
import database.DBFetchers.getReportInfo;
import hospital.Patient.Patient;
import hospital.Patient.PatientReportView;
import hospital.Staff.Reception;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class PatientEditController implements Initializable {

    @FXML
    private TableColumn<Report, Date> date;

    @FXML
    private TableColumn<Report, Long> reportid;

    @FXML
    private TableColumn<Report, String> deptname;

    @FXML
    private TableColumn<Report, String> dic;

    @FXML
    private TableView<Report> table;

    @FXML
    private Label buttonnotif;

    @FXML
    private Label patientNameLabel;

    @FXML
    private TextField patient_contact_no;

    @FXML
    private TextField patient_dob;

    @FXML
    private TextField patient_email;

    @FXML
    private TextField patient_fname;

    @FXML
    private TextField patient_gender;

    @FXML
    private TextField patient_lname;

    @FXML
    private TextField patient_status;

    @FXML
    private TextField patient_uid;

    @FXML
    private BorderPane editPane;

    ObservableList<Report> list = FXCollections.observableArrayList();
    public static Report inheritedReport = new Report();

    @FXML
    void makeFieldsEditable(MouseEvent event) {
        patient_dob.setEditable(true);
        patient_fname.setEditable(true);
        patient_lname.setEditable(true);
        patient_gender.setEditable(true);
        patient_status.setEditable(true);
        patient_email.setEditable(true);
        patient_contact_no.setEditable(true);

        buttonnotif.setText("Patient Details are now editable!");
        buttonnotif.setId("labelnotif");
        buttonnotif.setVisible(true);
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(e -> buttonnotif.setVisible(false));
        pause.play();
    }

    @FXML
    void refresh(MouseEvent event) throws IOException {
        JumpScene.changeScene(editPane,"UI/patientEditForm_PopUp.fxml",event);
    }

    @FXML
    void setValues(MouseEvent event) throws SQLException {
        String fname = patient_fname.getText();
        String lname= patient_lname.getText();
        String gender=patient_gender.getText();
        Long number= Long.valueOf(patient_contact_no.getText());
        String email=patient_email.getText();
        String status=patient_status.getText();
        Date date= Date.valueOf(patient_dob.getText());
        Patient patient=new Patient(PatientReceptionistController.inheritableUser,fname,lname,date,gender,number,email,status);
        Reception.editPatientDetails(patient);

        buttonnotif.setText("New Patient Details Saved Successfully!");
        buttonnotif.setId("labelnotif2");
        buttonnotif.setVisible(true);
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(e -> buttonnotif.setVisible(false));
        pause.play();
    }
    private void setReportList() throws SQLException {
        PatientReportView[] reports = getReportInfo.searchPatientReports(CurrentPatientInfo.getPatient().getPatient_id());
        for (PatientReportView patientReportView : reports) {
            Report report = new Report();
            report.setReportid(patientReportView.getReport_id());
            report.setDate(patientReportView.getStart_date());
            report.setDic(patientReportView.getDoctor_name());
            report.setDeptname(patientReportView.getDepartment_name());
            report.setDic_id(patientReportView.getStaff_id());
            table.getItems().add(report);

        }
    }

    @FXML
    void deleteReport(MouseEvent event) throws SQLException, JSchException, SftpException, IOException {
    Report report = table.getSelectionModel().getSelectedItem();
    long report_id = report.getReportid();
    Reception.closeReport(report_id);
    table.getItems().remove(report);
    }

    @FXML
    void gotoAddReport(MouseEvent event) throws IOException {
        PopUpBox.addReportPopUp("Add Report Details");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buttonnotif.setVisible(false);

        reportid.setCellValueFactory(new PropertyValueFactory<>("reportid"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        dic.setCellValueFactory(new PropertyValueFactory<>("dic"));
        deptname.setCellValueFactory(new PropertyValueFactory<>("deptname"));

        table.setItems(list);

        table.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2){
                inheritedReport = table.getSelectionModel().getSelectedItem();
                try {
                    PopUpBox.addAppointmentPopUp("Add Appointment Details!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            setReportList();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        patient_dob.setEditable(false);
        patient_fname.setEditable(false);
        patient_lname.setEditable(false);
        patient_gender.setEditable(false);
        patient_status.setEditable(false);
        patient_email.setEditable(false);
        patient_contact_no.setEditable(false);

        try {
            Patient[] pat= getPatientInfo.searchPatients(PatientReceptionistController.inheritableUser);
            patient_dob.setText(pat[0].getDOB().toString());
            patient_fname.setText(pat[0].getFname());
            patient_lname.setText(pat[0].getLname());
            patient_email.setText(pat[0].getEmail());
            patient_uid.setText(String.valueOf(pat[0].getPatient_id()));
            patient_contact_no.setText(String.valueOf(pat[0].getContact_no()));
            patient_gender.setText(pat[0].getGender());
            patient_status.setText(pat[0].getStatus());
            patientNameLabel.setText(pat[0].getFname() +" "+ pat[0].getLname());

        } catch (SQLException e) {
            throw new RuntimeException(e);

        }
    }
}
