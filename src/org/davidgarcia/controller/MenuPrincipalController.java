package org.davidgarcia.controller;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.davidgarcia.system.MainApp;

public class MenuPrincipalController implements Initializable{
    private MainApp escenarioPrincipal;
   
    @FXML private ImageView imgModulos;
    @FXML private ImageView imgEntidadesDeUnion;    
    @FXML private ImageView imgAyuda;
    @FXML private ImageView imgAcercaDe;
    
    private enum Imagen{imgModulos, imgReportes,imgEntidadesDeUnion,imgAcercaDe};
    
    public void insertarImagen(){
        imgModulos.setImage(new Image("/org/davidgarcia/img/MODULOSBRILLO.png"));    
        } 
    public void devolverImagen(){
        imgModulos.setImage(new Image("/org/davidgarcia/img/MODULOS.png"));
    }
    public void insertarImagen2(){
        imgAyuda.setImage(new Image("/org/davidgarcia/img/preguntaBrillo.png"));
    }
    public void devolverImagen2(){
        imgAyuda.setImage(new Image("/org/davidgarcia/img/pregunta.png"));
    }
    public void insertarImagen3(){
        imgEntidadesDeUnion.setImage(new Image("/org/davidgarcia/img/ENTIDADESDEUNIONBRILLO.png"));
    }
    public void devolverImagen3(){
        imgEntidadesDeUnion.setImage(new Image("/org/davidgarcia/img/ENTIDADESDEUNION.png"));
    }
    public void insertarImagen4(){
        imgAcercaDe.setImage(new Image("/org/davidgarcia/img/acerca de brillo.png"));
    }
    public void devolverImagen4(){
        imgAcercaDe.setImage(new Image("/org/davidgarcia/img/acerca de.png"));
    }
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
    //Método para la vista de Entidades de Union...
    public void EntidadesDeUnion(){
    escenarioPrincipal.EntidadesDeUnion();
    }
    //Método para direccionar el link del manual de usuario...
    public void ManualDeUsuario(){
       try {
            Runtime.getRuntime().exec("C:\\Windows\\System32\\cmd.exe /K start https://drive.google.com/file/d/1q3EKTPX7plkqsnGct3jMPeDctOOKXC4I/view?usp=sharing");
        } catch (IOException ex) {
            Logger.getLogger(MenuPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
}