package org.davidgarcia.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.davidgarcia.system.MainApp;

public class ModulosController implements Initializable {
    private MainApp escenarioPrincipal;
    
    @FXML private ImageView imgEmpresas;
    @FXML private ImageView imgTipoDeEmpleado;
    @FXML private ImageView imgTipoDePlato;
    @FXML private ImageView imgProductos;
    
    public void insertarImagen(){
        imgEmpresas.setImage(new Image("/org/davidgarcia/img/empresaBrillo.png"));    
    } 
    public void devolverImagen(){
        imgEmpresas.setImage(new Image("/org/davidgarcia/img/empresa.png"));
    }
    public void insertarImagen2(){
        imgTipoDeEmpleado.setImage(new Image("/org/davidgarcia/img/tipoEmpleadoBrillo.png"));
    }
    public void devolverImagen2(){
        imgTipoDeEmpleado.setImage(new Image("/org/davidgarcia/img/tipoEmpleado.png"));
    }
    public void insertarImagen3(){
        imgTipoDePlato.setImage(new Image("/org/davidgarcia/img/tipo de platoBrillo.png"));
    }
    public void devolverImagen3(){
        imgTipoDePlato.setImage(new Image("/org/davidgarcia/img/tipo de plato.png"));
    }
    public void insertarImagen4(){
        imgProductos.setImage(new Image("/org/davidgarcia/img/productoBrillo.png"));
    }
    public void devolverImagen4(){
        imgProductos.setImage(new Image("/org/davidgarcia/img/producto.png"));
    }
  
    //Método para que pueda generar la ventana en el menú principal
    public MainApp getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(MainApp escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    //Método para regresar al menu principal...
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    //Método para la ventana de empresa...
    public void empresa(){
        escenarioPrincipal.Empresa();
    }
    //Método para la ventana de tipo Empleado...
    public void tipoEmpleado(){
        escenarioPrincipal.TipoEmpleado();
    }
    //Método para la ventana de tipo Plato...
    public void tipoPlato(){
        escenarioPrincipal.TipoPlato();
    }
    //Método para la ventana de productos...
    public void producto(){
        escenarioPrincipal.Producto();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
    
}
