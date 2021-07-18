package org.davidgarcia.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.davidgarcia.system.MainApp;

public class EntidadesDeUnionController implements Initializable{
    private MainApp escenarioPrincipal;
    
    @FXML private ImageView imgServiciosHasEmpleados;
    @FXML private ImageView imgServiciosHasPlatos;
    @FXML private ImageView imgProductosHasPlatos;
    
    public void insertarImagen(){
        imgServiciosHasEmpleados.setImage(new Image ("/org/davidgarcia/img/serviciosHasEmpleadosBrillo.png"));
    }
    public void devolverImagen(){
        imgServiciosHasEmpleados.setImage(new Image ("/org/davidgarcia/img/serviciosHasEmpleados.png"));
    }   
    public void insertarImagen2(){
        imgServiciosHasPlatos.setImage(new Image ("/org/davidgarcia/img/serviciosHasPlatosBrillo.png"));
    }
    public void devolverImagen2(){
        imgServiciosHasPlatos.setImage(new Image ("/org/davidgarcia/img/serviciosHasPlatos.png"));
    }
    public void insertarImagen3(){
        imgProductosHasPlatos.setImage(new Image ("/org/davidgarcia/img/productosHasPlatosBrillo.png"));
    }
    public void devolverImagen3(){
        imgProductosHasPlatos.setImage(new Image ("/org/davidgarcia/img/productosHasPlatos.png"));
    }   
    @Override
    public void initialize(URL location, ResourceBundle resources){
    
    }
    //Getters and setters para ejecutar la ventana en el escenarioPrincipal
    public MainApp getEscenarioPrincipal() {
        return escenarioPrincipal;
    }
    public void setEscenarioPrincipal(MainApp escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    //Método para ingresar en servicios has empleados
    public void serviciosHasEmpleados(){
        escenarioPrincipal.ServiciosHasEmpleado();
    }
    //Método para retornar al menú principal
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    //Método para ingresar en servicios Has Platos
    public void serviciosHasPlatos(){
        escenarioPrincipal.ServiciosHasPlatos();
    }
    //Método para ingresar en productos Has Platos
    public void productosHasPlatos(){
        escenarioPrincipal.ProductosHasPlatos();
    }
}
