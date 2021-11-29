package adminController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import persistence.Entity.Department;


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


    }



}
