package org.davidgarcia.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.davidgarcia.system.MainApp;

public class ReportesController implements Initializable {
    private MainApp escenarioPrincipal;
    
    //Método para que ejecute en la ventana principal
    public void setEscenarioPrincipal(MainApp escenarioPrincipal){
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public MainApp getEscenarioPrincipal(){
        return escenarioPrincipal;
    }
    
    //Método para regresar a menu Principal
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
