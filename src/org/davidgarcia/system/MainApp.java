
package org.davidgarcia.system;

import java.io.InputStream;
import javafx.scene.image.Image;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.layout.AnchorPane;
import org.davidgarcia.controller.DatosProgramadorController;
import org.davidgarcia.controller.EmpleadoController;
import org.davidgarcia.controller.EmpresaController;
import org.davidgarcia.controller.EntidadesDeUnionController;
import org.davidgarcia.controller.MenuPrincipalController;
import org.davidgarcia.controller.ModulosController;
import org.davidgarcia.controller.PlatoController;
import org.davidgarcia.controller.PresupuestoController;
import org.davidgarcia.controller.ProductoController;
import org.davidgarcia.controller.ProductosHasPlatosController;
import org.davidgarcia.controller.ServicioController;
import org.davidgarcia.controller.TipoEmpleadoController;
import org.davidgarcia.controller.TipoPlatoController;
import org.davidgarcia.controller.ServiciosHasEmpleadosController;
import org.davidgarcia.controller.ServiciosHasPlatosController;

public class MainApp extends Application {
    private final String Paquete_Vista = "/org/davidgarcia/view/";
    private Stage escenarioPrincipal;
    private Scene escena;
  
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override 
      public void start(Stage escenarioPrincipal) throws Exception{
        this.escenarioPrincipal = escenarioPrincipal;
        this.escenarioPrincipal.setTitle("Tony's Kinal App");
       escenarioPrincipal.getIcons().add(new Image("/org/davidgarcia/img/TK3.PNG")); // Para el icono de la aplicación
//        Parent root = FXMLLoader.load(getClass().getResource("/org/davidgarcia/view/MenuPrincipalView.fxml")); // Para el FXML
//        Scene escena = new Scene(root); 
//        scenarioPrincipal.setScene(escena);
          menuPrincipal ();
          escenarioPrincipal.setResizable(false);
          escenarioPrincipal.show(); 
      }
    //Método del menú principal
      public void menuPrincipal(){
      try{
          MenuPrincipalController menuPrincipal = (MenuPrincipalController)cambiarEscena("MenuPrincipalView.fxml",640,424);
          menuPrincipal.setEscenarioPrincipal(this);
      }catch (Exception e){
        e.printStackTrace();
      }
    }
      //Método para la ejecución del acercaDe...
      public void DatosProgramador(){
      try {
        DatosProgramadorController DatosProgramador = (DatosProgramadorController)cambiarEscena("DatosProgramadorView.fxml",600,400);
        DatosProgramador.setEscenarioPrincipal(this);
      }catch (Exception e){
          e.printStackTrace();
      }
    }
      //Método para la ejecución de la ventana de Empresa...
      public void Empresa(){
      try {
         EmpresaController Empresa = (EmpresaController)cambiarEscena("EmpresaView.fxml",620,413);
         Empresa.setEscenarioPrincipal(this);
      }catch (Exception e){
          e.printStackTrace();
      }
    }
      //Método para la ejecución de la ventana de TipoEmpleado...
      public void TipoEmpleado(){
      try {
          TipoEmpleadoController TipoEmpleado = (TipoEmpleadoController)cambiarEscena("TipoEmpleadoView.fxml",595,400);
          TipoEmpleado.setEscenarioPrincipal(this);
      }catch (Exception e){
          e.printStackTrace();
      }
    }
      //Método para la ejecución de la ventana de Empleado...
      public void Empleado(){
          try{
          EmpleadoController Empleado = (EmpleadoController)cambiarEscena("EmpleadoView.fxml", 760, 530);
          Empleado.setEscenarioPrincipal(this);
          }catch(Exception e){
              e.printStackTrace();
          }   
      }
      //Método para la ejecución de la ventana de presupuesto...
      public void Presupuesto(){
        try{
        PresupuestoController Presupuesto = (PresupuestoController)cambiarEscena("PresupuestoView.fxml", 650, 450);
        Presupuesto.setEscenarioPrincipal(this);
        }catch(Exception e){
          e.printStackTrace();
        }
      }
      //Método para la ejecución de la ventana de servicios...
      public void Servicio(){
          try{
          ServicioController Servicio = (ServicioController)cambiarEscena("ServicioView.fxml", 767, 530);
          Servicio.setEscenarioPrincipal(this);
          }catch(Exception e){
              e.printStackTrace();
          }
      }
      //Método para la ejecución de la ventana de modulos...
      public void Modulos(){
          try{
              ModulosController Modulos = (ModulosController)cambiarEscena("ModulosView.fxml", 640, 425);
              Modulos.setEscenarioPrincipal(this);
          }catch(Exception e){
              e.printStackTrace();
          }
      }
      //Método para la ejecución de la ventana de tipo plato...
      public void TipoPlato(){
            try{
                 TipoPlatoController TipoPlato = (TipoPlatoController)cambiarEscena("TipoPlatoView.fxml", 600, 315 );
                 TipoPlato.setEscenarioPrincipal(this);
            }catch(Exception e){
                e.printStackTrace();
            }
      }
      //Método para la ejecución de la ventana de platos...
      public void Plato(){
          try{
              PlatoController Plato = (PlatoController)cambiarEscena("PlatoView.fxml",700,466);
              Plato.setEscenarioPrincipal(this);
          }catch(Exception e){
              e.printStackTrace();
          }
      }
      //Método para la ejecución de la ventana de productos...
      public void Producto(){
          try{
              ProductoController Producto = (ProductoController)cambiarEscena("ProductoView.fxml", 653, 400);
              Producto.setEscenarioPrincipal(this);
          }catch(Exception e){
              e.printStackTrace();
          }
      }
      //Método para la ejecución de la ventana de Entidades de unión...
      public void EntidadesDeUnion(){
          try{
              EntidadesDeUnionController EntidadesDeUnion = (EntidadesDeUnionController)cambiarEscena("EntidadesDeUnionView.fxml", 640, 425);
              EntidadesDeUnion.setEscenarioPrincipal(this);
          }catch(Exception e){
              e.printStackTrace();
          }
      }
      //Método para la ejecución de la ventana de Servicios Has Empleado...
      public void ServiciosHasEmpleado(){
          try{
              ServiciosHasEmpleadosController ServiciosHasEmpleado =(ServiciosHasEmpleadosController)cambiarEscena("ServiciosHasEmpleadosView.fxml", 700, 500);
              ServiciosHasEmpleado.setEscenarioPrincipal(this);
          }catch(Exception e){
              e.printStackTrace();
          }
      }
      //Método para la ejecución de la ventana de Servicios Has Platos...
      public void ServiciosHasPlatos(){
          try{
              ServiciosHasPlatosController ServiciosHasPlatos = (ServiciosHasPlatosController)cambiarEscena("ServicioHasPlatosView.fxml",613 , 407);
              ServiciosHasPlatos.setEscenarioPrincipal(this);
          }catch(Exception e){
              e.printStackTrace();
          }
      }
      //Método para la ejecución de la ventana de Productos Has Platos...
      public void ProductosHasPlatos(){
          try{
              ProductosHasPlatosController ProductosHasPlatos = (ProductosHasPlatosController)cambiarEscena("ProductosHasPlatosView.fxml", 600, 400);
              ProductosHasPlatos.setEscenarioPrincipal(this);
          }catch(Exception e){
              e.printStackTrace();
          }
      }
      
    public Initializable cambiarEscena(String fxml, int ancho, int alto) throws Exception{
     Initializable resultado = null;
     FXMLLoader cargadorFXML = new FXMLLoader();
     InputStream archivo = MainApp.class.getResourceAsStream(Paquete_Vista+fxml);
     cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
     cargadorFXML.setLocation(MainApp.class.getResource(Paquete_Vista+ fxml));
     escena = new  Scene ((AnchorPane)cargadorFXML.load(archivo));
     escenarioPrincipal.setScene(escena);
     escenarioPrincipal.sizeToScene();
     resultado = (Initializable)cargadorFXML.getController();
     return resultado;
      }
   }

