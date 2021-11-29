package adminController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import persistence.Entity.Department;

import java.io.IOException;


public class DepartmentListItemController {

    @FXML Label name;


    private Department department;
    private DepartmentListController parent;

    public void setDepartmentListController(DepartmentListController con) {
        this.parent = con;
    }

    public void setDepartment(Department department) { this.department = department; }
    public void setText() {
        name.setText(department.getDepartmentName());
    }

    @FXML
    private void delete() {

        // studentListController.vBox.getChildren().remove(index);
        // System.out.println(event.getSource().);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/admin/departmentDeleteBox.fxml"));
        AnchorPane dialogBox = null;
        try {
            dialogBox = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Stage dialogStage = new Stage();
        Scene scene = new Scene(dialogBox);
        dialogStage.setScene(scene);
        dialogStage.initOwner(name.getScene().getWindow());
        dialogStage.initModality(Modality.WINDOW_MODAL);

        dialogStage.show();

        DepartmentDeleteBoxController con = fxmlLoader.getController();
        con.setParentController(parent);



    }



}
