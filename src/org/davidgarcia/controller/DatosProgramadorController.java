
package org.davidgarcia.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.davidgarcia.system.MainApp;

  public class DatosProgramadorController implements Initializable {
   private MainApp escenarioPrincipal;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    
    public MainApp getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(MainApp escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal(){
    escenarioPrincipal.menuPrincipal();
    }

}
