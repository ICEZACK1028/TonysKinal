package org.davidgarcia.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.davidgarcia.bean.TipoEmpleado;
import org.davidgarcia.system.MainApp;
import org.davidgarcia.bd.Conexion;

public class TipoEmpleadoController implements Initializable {
 private enum Operacion{GUARDAR, ACTUALIZAR, NINGUNO}
    private Operacion tipoOperacion = Operacion.NINGUNO;// ponemos default ninguno
    
    private MainApp escenarioPrincipal;
    private ObservableList <TipoEmpleado> listaTipoEmpleado;
    
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtCodigoTipoEmpleado;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private TableColumn colCodigoTipoEmpleado;
    @FXML private TableColumn colDescripcion;
    @FXML private TableView tblTipoEmpleado;
    @FXML private ImageView imgEmpleados;
    
    public void insertarImagen(){
        imgEmpleados.setImage(new Image("/org/davidgarcia/img/empleadoBrillo.png"));
    }
    public void devolverImagen(){
        imgEmpleados.setImage(new Image("/org/davidgarcia/img/empleado.png"));
    }
    
   @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
    }        
    
    public MainApp getEscenarioPrincipal() {
    return escenarioPrincipal;
}

public void setEscenarioPrincipal(MainApp escenarioPrincipal) {
    this.escenarioPrincipal = escenarioPrincipal;
}

//Método para volver al Menú principal
public void menuPrincipal(){
    if(tipoOperacion == Operacion.NINGUNO){
            escenarioPrincipal.menuPrincipal();
        }else{
            int respuesta = JOptionPane.showConfirmDialog(null, "   ¿Está seguro que quiere ir al menú?\nSi regresa perderá los datos ingresados","Aviso:",
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(respuesta == JOptionPane.OK_OPTION){
                escenarioPrincipal.menuPrincipal();
            }
        }
}

//Método para la vista de Empleado...
public void empleado(){
     if(tipoOperacion == Operacion.NINGUNO){
            escenarioPrincipal.Empleado();
        }else{
            int respuesta = JOptionPane.showConfirmDialog(null, "   ¿Está seguro que quiere ingresar?\nSi ingresa perderá los datos ingresados","Aviso:",
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(respuesta == JOptionPane.OK_OPTION){
                escenarioPrincipal.Empleado();
            }
        }
}

//Método para regresar a módulos...
public void modulos(){
     if(tipoOperacion == Operacion.NINGUNO){
            escenarioPrincipal.Modulos();
        }else{
            int respuesta = JOptionPane.showConfirmDialog(null, "   ¿Está seguro que quiere regresar?\nSi regresa perderá los datos ingresados","Aviso:",
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(respuesta == JOptionPane.OK_OPTION){
                escenarioPrincipal.Modulos();
            }
        }
}
    
//Método para activar los controles
public void activarControles(){
    txtDescripcion.setEditable(true);
}

//Método para desactivar los Controles
public void desactivarControles(){
    txtDescripcion.setEditable(false);
    txtCodigoTipoEmpleado.setEditable(false);      
}

//Método para limpiar los controles
public void limpiarControles(){
    txtDescripcion.setText(null);
    txtCodigoTipoEmpleado.setText(null);      
    tblTipoEmpleado.getSelectionModel().clearSelection();
}

//Método para seleccionar elementos de la tableView
public void seleccionarElemento(){
    if(tipoOperacion == Operacion.NINGUNO){
        if(tblTipoEmpleado.getSelectionModel().getSelectedItem()!=null){
            txtCodigoTipoEmpleado.setText(String.valueOf(((TipoEmpleado)tblTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado()));
            txtDescripcion.setText(((TipoEmpleado)tblTipoEmpleado.getSelectionModel().getSelectedItem()).getDescripcion());
        }
    }
}

//Método para el botón nuevo
public void nuevo(){
    switch(tipoOperacion){
        case NINGUNO:
            activarControles();
            limpiarControles();
            btnNuevo.setText("Guardar");
            btnEliminar.setText("Cancelar");
            btnEditar.setVisible(false);
            btnReporte.setVisible(false);
            txtCodigoTipoEmpleado.setDisable(true);
            tipoOperacion = Operacion.GUARDAR;
            break;
        case GUARDAR:
            if(CamposVacios() == true){
                guardar();
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setVisible(true);
                btnReporte.setVisible(true);
                txtCodigoTipoEmpleado.setDisable(false);
                tipoOperacion = Operacion.NINGUNO;
                cargarDatos();
            }else{
                JOptionPane.showMessageDialog(null, "Debe ingresar la Descripción");
            }
            break;
    }
}

//Método para el botón eliminar
public void eliminar(){
    switch(tipoOperacion){
        case GUARDAR:
            activarControles();
            limpiarControles();
            btnNuevo.setText("Nuevo");
            btnEliminar.setText("Eliminar");
            btnEditar.setVisible(true);
            btnReporte.setVisible(true);
            txtCodigoTipoEmpleado.setDisable(false);
            tipoOperacion = Operacion.NINGUNO;
        break;
        default:
            if(tblTipoEmpleado.getSelectionModel().getSelectedItem()!=null){
            int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro que quiere eliminar el registro?","Eliminar Tipo Empleado" ,JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if(respuesta == JOptionPane.YES_OPTION){
                    try{
                    PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarTipo_Empleado(?)}");
                    sp.setInt(1,((TipoEmpleado)tblTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
                    sp.execute();
                    if(sp.execute()==false){
                    listaTipoEmpleado.remove(tblTipoEmpleado.getSelectionModel().getSelectedIndex());
                    limpiarControles();
                    JOptionPane.showMessageDialog(null, "Registro Eliminado");
                    }
                    }catch(Exception e){
                        JOptionPane.showMessageDialog(null,"        No se puede eliminar el registro \n porque está ligado a registro(s) de Empleados","Nota:",JOptionPane.CLOSED_OPTION);
                        e.printStackTrace();
                    }
                }
            }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un registro");
            }   
    }
}

//Método para el botón Guardar
public void guardar(){
    TipoEmpleado registro = new TipoEmpleado();
    registro.setDescripcion(txtDescripcion.getText());
    try{
    PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarTipo_Empleado(?)}");
    sp.setString(1,registro.getDescripcion());
    sp.execute();
    listaTipoEmpleado.add(registro);
    }catch(Exception e){
    e.printStackTrace();
    }
}

public void editar(){
    switch(tipoOperacion){
        case NINGUNO:
            if(tblTipoEmpleado.getSelectionModel().getSelectedItem()!=null){
            activarControles();
            btnEditar.setText("Actualizar");
            btnReporte.setText("Cancelar");
            btnNuevo.setVisible(false);
            btnEliminar.setVisible(false);
            txtCodigoTipoEmpleado.setDisable(true);
            tipoOperacion = Operacion.ACTUALIZAR;
            }else{
                JOptionPane.showMessageDialog(null, "Debe seleccionar un registro");
            }
        break;
        case ACTUALIZAR:
            actualizar();
            desactivarControles();
            btnEditar.setText("Editar");
            btnReporte.setText("Reporte");
            btnNuevo.setVisible(true);
            btnEliminar.setVisible(true);
            txtCodigoTipoEmpleado.setDisable(false);
            tipoOperacion = Operacion.NINGUNO;
            cargarDatos();
            break;
    }
}

//Método para el botón reporte y cancelar...
public void reporte(){
    switch(tipoOperacion){
        case ACTUALIZAR:
            desactivarControles();
            btnEditar.setText("Editar");
            btnReporte.setText("Reporte");
            btnNuevo.setVisible(true);
            btnEliminar.setVisible(true);
            txtCodigoTipoEmpleado.setDisable(false);
            tipoOperacion = Operacion.NINGUNO;
            break;
        case NINGUNO:
            JOptionPane.showMessageDialog(null, "Funcionalidad no implementada");
            break;
    }
}

//Método para el botón actualizar y cancelar
public void actualizar(){
    try{
        PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarTipo_Empleado(?,?)}");
        TipoEmpleado tipoActualizado = ((TipoEmpleado)tblTipoEmpleado.getSelectionModel().getSelectedItem());
        tipoActualizado.setDescripcion(txtDescripcion.getText());
        sp.setInt(1, tipoActualizado.getCodigoTipoEmpleado());
        sp.setString(2, tipoActualizado.getDescripcion());
        sp.execute();
        JOptionPane.showMessageDialog(null, "Datos Actualizados");
        listaTipoEmpleado.add(tipoActualizado);
        }catch(Exception e){
            e.printStackTrace();
        }
}

//Método para cargar los datos a la vista
public void cargarDatos(){
  tblTipoEmpleado.setItems(getTipoEmpleado());
  colCodigoTipoEmpleado.setCellValueFactory(new PropertyValueFactory<TipoEmpleado, Integer>("codigoTipoEmpleado"));
  colDescripcion.setCellValueFactory(new PropertyValueFactory<TipoEmpleado, String>("descripcion"));
}

//Método para ejecutar el procedimiento almacenado y llenar una lista del resultado
public ObservableList<TipoEmpleado> getTipoEmpleado(){
ArrayList<TipoEmpleado> lista = new ArrayList<TipoEmpleado>();
    try{
        PreparedStatement procedimiento= Conexion.getInstance().getConexion().prepareCall("{call sp_ListarTipo_Empleado}");
        ResultSet resultado= procedimiento.executeQuery();
            while(resultado.next()){
            lista.add(new TipoEmpleado(resultado.getInt("codigoTipoEmpleado"), 
                    resultado.getString("descripcion")));            
            }
    }catch(Exception e){
        e.printStackTrace();
    }
    return listaTipoEmpleado= FXCollections.observableArrayList(lista);
}
//Mètodo para no poder dejar campos vacíos
public boolean CamposVacios(){
    boolean validacion = false;
        if(txtDescripcion.getText()!=null){
            validacion = true;
        }
    return validacion;
}
 
}
