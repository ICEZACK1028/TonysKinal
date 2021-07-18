package org.davidgarcia.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.davidgarcia.system.MainApp;

public class MainAppController implements Initializable{
    private MainApp escenarioPrincipal;
   
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
    
    public MainApp getEscenarioPrincipal(){
    return escenarioPrincipal;
    }
   
    public void setEscenarioPrincipal (MainApp escenarioPrincipal){
    this.escenarioPrincipal = escenarioPrincipal;
    }
    //Método para la vista de AcercaDe...
    public void DatosProgramador(){
    escenarioPrincipal.DatosProgramador();
    }
    //Método para la vista de Empresa...
    public void Empresa(){
    escenarioPrincipal.Empresa();
    }
    //Método para la vista de TipoEmpleado...
    public void TipoEmpleado(){
    escenarioPrincipal.TipoEmpleado();
    }
    //Método para la vista de Modulos...
    public void Modulos(){
    escenarioPrincipal.Modulos();
    }
    /*//Método para la vista de reportes...
    public void Reportes(){
    escenarioPrincipal.Reportes();
    }*/
}