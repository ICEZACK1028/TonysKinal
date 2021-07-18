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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.davidgarcia.bd.Conexion;
import org.davidgarcia.system.MainApp;
import org.davidgarcia.bean.Empleado;
import org.davidgarcia.bean.TipoEmpleado;

public class EmpleadoController implements Initializable {
    private enum Operacion{GUARDAR, ACTUALIZAR, NINGUNO}
    private Operacion tipoOperacion = Operacion.NINGUNO;// ponemos default ninguno
    private MainApp escenarioPrincipal;
    private ObservableList <Empleado> listaEmpleado;
    private ObservableList<TipoEmpleado> listaTipoEmpleado;
   
    @FXML private TextField txtNumeroEmpleado;
    @FXML private TextField txtApellidosEmpleado;
    @FXML private TextField txtNombresEmpleado;
    @FXML private TextField txtDireccionEmpleado;
    @FXML private TextField txtTelefonoContacto;
    @FXML private TextField txtGradoCocinero;
    @FXML private TextField txtCodigoEmpleado;
    @FXML private ComboBox cmbCodigoTipoEmpleado;
    @FXML private Button btnNuevo;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private Button btnEliminar;
    @FXML private TableColumn colCodigoEmpleado;
    @FXML private TableColumn colNumeroEmpleado;
    @FXML private TableColumn colApellidosEmpleado;
    @FXML private TableColumn colNombresEmpleado;
    @FXML private TableColumn colDireccionEmpleado;
    @FXML private TableColumn colTelefonoContacto;
    @FXML private TableColumn colGradoCocinero;
    @FXML private TableColumn colCodigoTipoEmpleado;
    @FXML private TableView tblEmpleados;
    
@Override
public void initialize(URL url, ResourceBundle rb) {
    cargarDatos();
    cmbCodigoTipoEmpleado.setItems(getTipoEmpleado());
}  

//Método para que pueda generar la ventana en el menú principal
public MainApp getEscenarioPrincipal() {
    return escenarioPrincipal;
}

public void setEscenarioPrincipal(MainApp escenarioPrincipal) {
    this.escenarioPrincipal = escenarioPrincipal;
}

//Método para volver al menú principal
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

//Método para volver una ventana atrás
public void tipoEmpleado(){
    if(tipoOperacion == Operacion.NINGUNO){
            escenarioPrincipal.TipoEmpleado();
        }else{
            int respuesta = JOptionPane.showConfirmDialog(null, "   ¿Está seguro que quiere regresar?\nSi regresa perderá los datos ingresados","Aviso:",
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(respuesta == JOptionPane.OK_OPTION){
                escenarioPrincipal.TipoEmpleado();
            }
        }
}

//Método para cargar datos
    public void cargarDatos(){
        tblEmpleados.setItems(getEmpleado());
        colCodigoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("codigoEmpleado"));
        colNumeroEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("numeroEmpleado"));
        colApellidosEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, String>("apellidosEmpleado"));
        colNombresEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, String>("nombresEmpleado"));
        colDireccionEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("direccionEmpleado"));
        colTelefonoContacto.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("telefonoContacto"));
        colGradoCocinero.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("gradoCocinero"));
        colCodigoTipoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("codigoTipoEmpleado"));
    }
    
//Método para ejecutar el procedimiento almacenado y llenar una lista del resultado
public ObservableList<Empleado> getEmpleado(){
    ArrayList<Empleado> lista = new ArrayList<Empleado>();
        try{
            PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEmpleado()}");
            ResultSet resultado = sp.executeQuery();
            while(resultado.next()){
            lista.add(new Empleado(resultado.getInt("codigoEmpleado"),
                    resultado.getInt("numeroEmpleado"),
                    resultado.getString("apellidosEmpleado"),
                    resultado.getString("nombresEmpleado"),
                    resultado.getString("direccionEmpleado"),
                    resultado.getString("telefonoContacto"),
                    resultado.getString("gradoCocinero"),
                    resultado.getInt("codigoTipoEmpleado")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaEmpleado= FXCollections.observableArrayList(lista);
 }

//Método para llenar el sp y mostrar la información en el ComboBox
public ObservableList<TipoEmpleado> getTipoEmpleado(){
    ArrayList<TipoEmpleado> lista = new ArrayList<TipoEmpleado>();
    try{
        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarTipo_Empleado()}");
        ResultSet resultado = procedimiento.executeQuery();
        while(resultado.next()){
            lista.add(new TipoEmpleado(resultado.getInt("codigoTipoEmpleado"),
                                        resultado.getString("descripcion")));
        }
    }catch(Exception e){
        e.printStackTrace();
    }
    return listaTipoEmpleado = FXCollections.observableArrayList(lista);
}
//Método para activar los controles
public void activarControles(){
    txtNumeroEmpleado.setEditable(true);
    txtApellidosEmpleado.setEditable(true);     
    txtNombresEmpleado.setEditable(true);
    txtDireccionEmpleado.setEditable(true);       
    txtTelefonoContacto.setEditable(true);        
    txtGradoCocinero.setEditable(true);        
    cmbCodigoTipoEmpleado.setDisable(false);        
}
    
    //Método para desactivar los controles
public void desactivarControles(){
    txtCodigoEmpleado.setEditable(false);
    txtNumeroEmpleado.setEditable(false);
    txtNombresEmpleado.setEditable(false);
    txtApellidosEmpleado.setEditable(false);     
    txtDireccionEmpleado.setEditable(false);       
    txtTelefonoContacto.setEditable(false);
    txtGradoCocinero.setEditable(false);        
    cmbCodigoTipoEmpleado.setDisable(true);            
}
    
//Método para limpiar los controles
public void limpiarControles(){
    txtCodigoEmpleado.setText(null);
    txtNumeroEmpleado.setText(null);
    txtApellidosEmpleado.setText(null);     
    txtNombresEmpleado.setText(null);
    txtDireccionEmpleado.setText(null);       
    txtTelefonoContacto.setText(null);
    txtGradoCocinero.setText(null);        
    cmbCodigoTipoEmpleado.getSelectionModel().select(null);
    tblEmpleados.getSelectionModel().clearSelection();
}
    
    //Método para seleccionar elementos de la tableView
    public void seleccionarElemento(){
        if(tipoOperacion == Operacion.NINGUNO){
            if(tblEmpleados.getSelectionModel().getSelectedItem()!=null){
                txtCodigoEmpleado.setText(String.valueOf(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
                txtNumeroEmpleado.setText(String.valueOf(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getNumeroEmpleado()));
                txtApellidosEmpleado.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getApellidosEmpleado());     
                txtNombresEmpleado.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getNombresEmpleado());
                txtDireccionEmpleado.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getDireccionEmpleado());       
                txtTelefonoContacto.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getTelefonoContacto());
                txtGradoCocinero.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getGradoCocinero());        
                cmbCodigoTipoEmpleado.getSelectionModel().select(buscarTipoEmpleado(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado()));
            }
        }
    }
    
   //Método para buscar Tipo Empleado
   public TipoEmpleado buscarTipoEmpleado(int codigoTipoEmpleado){
        TipoEmpleado resultado = null;
        try{
            PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarTipo_Empleado(?)}");
            sp.setInt(1, codigoTipoEmpleado);
            ResultSet registro  = sp.executeQuery();
            while(registro.next()){
                resultado = new TipoEmpleado(
                                registro.getInt("codigoTipoEmpleado"),
                                registro.getString("descripcion"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
   
    //Método para el botón nuevo
    public void nuevo(){
        switch(tipoOperacion){
            case NINGUNO:
                activarControles();
                limpiarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnReporte.setVisible(false);
                btnEditar.setVisible(false);
                txtCodigoEmpleado.setDisable(true);
                tblEmpleados.getSelectionModel().clearSelection();
                tipoOperacion= Operacion.GUARDAR;
                break;
            case GUARDAR:
                if(CampoVacios()== true){
                    if(validacionEntero() == true){
                        guardar();
                        desactivarControles();
                        limpiarControles();
                        btnNuevo.setText("Nuevo");
                        btnEliminar.setText("Eliminar");
                        btnReporte.setVisible(true);
                        btnEditar.setVisible(true);
                        txtCodigoEmpleado.setDisable(false);
                        tipoOperacion= Operacion.NINGUNO;
                        cargarDatos();
                    }else{
                        JOptionPane.showMessageDialog(null,"    '"+txtNumeroEmpleado.getText()+"'"+" \n No es un Número de Empleado");
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Solo puede dejar vacío el campo Grado Cocinero");
                }
                break;
        }
    }
    
    //Método para el botón guardar
    public void guardar(){
        Empleado registro = new Empleado();
        registro.setNumeroEmpleado(Integer.parseInt(txtNumeroEmpleado.getText()));
        registro.setApellidosEmpleado(txtApellidosEmpleado.getText());
        registro.setNombresEmpleado(txtNombresEmpleado.getText());
        registro.setDireccionEmpleado(txtDireccionEmpleado.getText());
        registro.setTelefonoContacto(txtTelefonoContacto.getText());
        registro.setGradoCocinero(txtGradoCocinero.getText());
        registro.setCodigoTipoEmpleado(((TipoEmpleado)cmbCodigoTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
        try{
            PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarEmpleado(?,?,?,?,?,?,?)}");
            sp.setInt(1, registro.getNumeroEmpleado());
            sp.setString(2,registro.getApellidosEmpleado());
            sp.setString(3,registro.getNombresEmpleado());
            sp.setString(4,registro.getDireccionEmpleado());
            sp.setString(5,registro.getTelefonoContacto());
            sp.setString(6,registro.getGradoCocinero());
            sp.setInt(7,registro.getCodigoTipoEmpleado());
            sp.execute();
            listaEmpleado.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    //Método para el botón editar
    public void editar(){
        switch(tipoOperacion){
            case NINGUNO:
                if(tblEmpleados.getSelectionModel().getSelectedItem()!=null){
                activarControles();
                btnEditar.setText("Actualizar");
                btnReporte.setText("Cancelar");
                btnNuevo.setVisible(false);
                btnEliminar.setVisible(false);
                txtCodigoEmpleado.setDisable(true);
                tipoOperacion = Operacion.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un registro de la tabla");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                desactivarControles();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setVisible(true);
                btnEliminar.setVisible(true);
                txtCodigoEmpleado.setDisable(false);
                tipoOperacion = Operacion.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    //Método para el botón eliminar
    public void eliminar(){
        switch(tipoOperacion){
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnReporte.setVisible(true);
                btnEditar.setVisible(true);
                txtCodigoEmpleado.setDisable(false);
                tipoOperacion= Operacion.NINGUNO;                
            break;
            default:
                if(tblEmpleados.getSelectionModel().getSelectedItem()!=null){
                int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro que quiere eliminar el registro?","Eliminar Empleado", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                    try{
                        PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarEmpleado(?)}");
                        sp.setInt(1,((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
                        sp.execute();
                        listaEmpleado.remove(tblEmpleados.getSelectionModel().getSelectedIndex());
                        limpiarControles();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un registro de la tabla");
                }
        }
    }
    
    //Método para el botón actualizar
    public void actualizar(){
        try{
        PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarEmpleado(?,?,?,?,?,?,?,?)}");
        Empleado empleadoActualizado = ((Empleado)tblEmpleados.getSelectionModel().getSelectedItem());
        //Obteniendo los datos de la vista al modelo en java
        empleadoActualizado.setNumeroEmpleado(Integer.parseInt(txtNumeroEmpleado.getText()));
        empleadoActualizado.setApellidosEmpleado(txtApellidosEmpleado.getText());
        empleadoActualizado.setNombresEmpleado(txtNombresEmpleado.getText());
        empleadoActualizado.setDireccionEmpleado(txtDireccionEmpleado.getText());
        empleadoActualizado.setTelefonoContacto(txtTelefonoContacto.getText());
        empleadoActualizado.setGradoCocinero(txtGradoCocinero.getText());
        empleadoActualizado.setCodigoTipoEmpleado(((TipoEmpleado)cmbCodigoTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
        //Enviando los datos actualizados a ejecutar en el ejecutar en el objeto procedimiento
        sp.setInt(1,empleadoActualizado.getCodigoEmpleado());
        sp.setInt(2,empleadoActualizado.getNumeroEmpleado());
        sp.setString(3,empleadoActualizado.getApellidosEmpleado());
        sp.setString(4,empleadoActualizado.getNombresEmpleado());
        sp.setString(5,empleadoActualizado.getDireccionEmpleado());
        sp.setString(6,empleadoActualizado.getTelefonoContacto());
        sp.setString(7,empleadoActualizado.getGradoCocinero());
        sp.setInt(8,empleadoActualizado.getCodigoTipoEmpleado());
        sp.execute();
        JOptionPane.showMessageDialog(null, "Datos Actualizados");
        listaEmpleado.add(empleadoActualizado);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    //Método para el botón de reporte y cancelar
    public void reporte(){
    switch(tipoOperacion){
        case ACTUALIZAR:
            desactivarControles();
            btnEditar.setText("Editar");
            btnReporte.setText("Reporte");
            btnNuevo.setVisible(true);
            btnEliminar.setVisible(true);
            txtCodigoEmpleado.setDisable(false);
            tipoOperacion = Operacion.NINGUNO;
            break;
        case NINGUNO:
            JOptionPane.showMessageDialog(null, "Funcionalidad aún no implementada");
            break;
    }
}
    //Método para no dejar campos vacios, en esta entidad únicamente puede quedar vacío el grado de cocinero
    public boolean CampoVacios(){
        boolean validacion = false;
            if(txtApellidosEmpleado.getText()!=null && txtDireccionEmpleado.getText()!=null && txtNombresEmpleado.getText()!=null && 
                txtNumeroEmpleado.getText()!=null && cmbCodigoTipoEmpleado.getSelectionModel().getSelectedItem()!=null){
                validacion = true;
            }
        return validacion;
    }
    //Método para validar el texto entero
    public boolean validacionEntero(){
         int entero;
        try{
            entero = Integer.parseInt(txtNumeroEmpleado.getText());
            return true;
        }catch(Exception e){
            e.printStackTrace();
          
            return false;
        }
    }
}
