package org.davidgarcia.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
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
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.davidgarcia.bd.Conexion;
import org.davidgarcia.system.MainApp;
import org.davidgarcia.bean.ServiciosHasEmpleados;
import org.davidgarcia.bean.Empleado;
import org.davidgarcia.bean.Servicio;

public class ServiciosHasEmpleadosController implements Initializable{
    private enum Operacion{NINGUNO, ACTUALIZAR, GUARDAR};
    private Operacion tipoOperacion = Operacion.NINGUNO;
            
    private MainApp escenarioPrincipal;
    private DatePicker fecha;
    
    private ObservableList<ServiciosHasEmpleados> listaServiciosHasEmpleados;
    private ObservableList<Empleado> listaEmpleados;
    private ObservableList<Servicio> listaServicio;
    
    @FXML private TextField txtCodigoEvento;
    @FXML private TextField txtLugarEvento;
    @FXML private TextField txtHoraEvento;
    @FXML private ComboBox cmbCodigoServicio;
    @FXML private ComboBox cmbCodigoEmpleado;
    @FXML private GridPane grpFechaEvento;
    @FXML private TableColumn colCodigoEvento;
    @FXML private TableColumn colCodigoServicio;
    @FXML private TableColumn colCodigoEmpleado;
    @FXML private TableColumn colLugarEvento;
    @FXML private TableColumn colFechaEvento;
    @FXML private TableColumn colHoraEvento;
    @FXML private Button btnNuevo;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;
    @FXML private TableView tblServiciosHasEmpleados;
    
    @Override
    public void initialize(URL url, ResourceBundle resources){
        cargarDatos();
        DatePicker();
        cmbCodigoEmpleado.setItems(getEmpleado());
        cmbCodigoServicio.setItems(getServicio());
    }
    //Método para ejecutar la ventana en escenarioPrincipal
    public MainApp getEscenarioPrincipal(){
        return escenarioPrincipal;
    }
    public void setEscenarioPrincipal(MainApp escenarioPrincipal){
        this.escenarioPrincipal = escenarioPrincipal;
    }
    //Método para retornar a la ventana Entidades de unión
    public void entidadesDeUnion(){
         if(tipoOperacion == Operacion.NINGUNO){
            escenarioPrincipal.EntidadesDeUnion();
        }else{
            int respuesta = JOptionPane.showConfirmDialog(null, "   ¿Está seguro que quiere regresar?\nSi regresa perderá los datos ingresados","Aviso:",
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(respuesta == JOptionPane.OK_OPTION){
                escenarioPrincipal.EntidadesDeUnion();
            }
        }
    }
    //Método para retornar al menu principal
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
    //Método para cargar los datos en la TableView
    public void cargarDatos(){
        tblServiciosHasEmpleados.setItems(getServiciosHasEmpleados());
        colCodigoEvento.setCellValueFactory(new PropertyValueFactory<ServiciosHasEmpleados, Integer>("codigoServicios_has_Empleado"));
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory<ServiciosHasEmpleados, Integer>("Servicios_codigoServicio"));
        colCodigoEmpleado.setCellValueFactory(new PropertyValueFactory<ServiciosHasEmpleados, Integer>("Empleado_codigoEmpleado"));
        colLugarEvento.setCellValueFactory(new PropertyValueFactory<ServiciosHasEmpleados, Date>("lugarEvento"));
        colFechaEvento.setCellValueFactory(new PropertyValueFactory<ServiciosHasEmpleados, String>("fechaEvento"));
        colHoraEvento.setCellValueFactory(new PropertyValueFactory<ServiciosHasEmpleados, String>("horaEvento"));
    }
    //Método para obtener y llenar el query con los datos de serviciosHasEmpleados
    public ObservableList<ServiciosHasEmpleados> getServiciosHasEmpleados(){
        ArrayList<ServiciosHasEmpleados> lista = new ArrayList<ServiciosHasEmpleados>();
            try{
                PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarServicios_has_Empleado()}");
                ResultSet resultado = sp.executeQuery();
                    while(resultado.next()){
                        lista.add(new ServiciosHasEmpleados(resultado.getInt("codigoServicios_has_Empleado"),
                                                            resultado.getInt("Servicios_codigoServicio"),
                                                            resultado.getInt("Empleado_codigoEmpleado"),
                                                            resultado.getDate("fechaEvento"),
                                                            resultado.getString("horaEvento"),
                                                            resultado.getString("lugarEvento")));
                    }     
            }catch(Exception e){
                e.printStackTrace();
            }
        return listaServiciosHasEmpleados = FXCollections.observableArrayList(lista);
    }
    //Método para mostrar y obtener de los datos de Servicio
    public ObservableList<Servicio> getServicio(){
        ArrayList<Servicio> lista = new ArrayList<Servicio>();
            try{
                PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarServicio()}");
                ResultSet resultado = sp.executeQuery();
                    while(resultado.next()){
                        lista.add(new Servicio(resultado.getInt("codigoServicio"),
                                               resultado.getDate("fechaServicio"),
                                               resultado.getString("tipoServicio"),
                                               resultado.getString("horaServicio"),
                                               resultado.getString("lugarServicio"),
                                               resultado.getString("telefonoContacto"),
                                               resultado.getInt("codigoEmpresa")));
                    }
            }catch(Exception e){
                e.printStackTrace();
            }
        return listaServicio = FXCollections.observableArrayList(lista);
    }
    //Método para mostrar y obtener de los datos de Empleado
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
        return listaEmpleados = FXCollections.observableArrayList(lista);
    }
    //Método para buscar el servicio en el comboBox y guardarlo
    public Servicio buscarServicio(int codigoServicio){
        Servicio resultado = null;
            try{
                PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarServicio(?)}");
                sp.setInt(1, codigoServicio);
                ResultSet registro = sp.executeQuery();
                    while(registro.next()){
                        resultado = new Servicio(registro.getInt("codigoServicio"),
                                                 registro.getDate("fechaServicio"),
                                                 registro.getString("tipoServicio"),
                                                 registro.getString("horaServicio"),
                                                 registro.getString("lugarServicio"),
                                                 registro.getString("telefonoContacto"),
                                                 registro.getInt("codigoEmpresa"));
                    }
            }catch(Exception e){
                e.printStackTrace();
            }
        return resultado;
    }
    //Método para buscar el empleado en el comboBox y guardarlo
    public Empleado buscarEmpleado(int codigoEmpleado){
        Empleado resultado = null;
            try{
                PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarEmpleado(?)}");
                sp.setInt(1, codigoEmpleado);
                ResultSet registro = sp.executeQuery();
                    while(registro.next()){
                        resultado = new Empleado(registro.getInt("codigoEmpleado"),
                                                 registro.getInt("numeroEmpleado"),
                                                 registro.getString("apellidosEmpleado"),
                                                 registro.getString("nombresEmpleado"),
                                                 registro.getString("direccionEmpleado"),
                                                 registro.getString("telefonoContacto"),
                                                 registro.getString("gradoCocinero"),
                                                 registro.getInt("codigoTipoEmpleado"));
                    }
            }catch(Exception e){
                e.printStackTrace();
            }
        return resultado;
    }
    //Método para el date Piker
    public void DatePicker(){
        fecha = new DatePicker(Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyy-MM-dd"));
        fecha.getCalendarView().todayButtonTextProperty().set("Today");
        fecha.getCalendarView().setShowWeeks(false);
        fecha.getStylesheets().add("/org/davidgarcia/resource/DatePicker.css");
        grpFechaEvento.add(fecha,0,0);
    }
    //Método para activar controles
    public void activarControles(){
        txtLugarEvento.setEditable(true);
        txtHoraEvento.setEditable(true);
        cmbCodigoServicio.setDisable(false);
        cmbCodigoEmpleado.setDisable(false);
        grpFechaEvento.setDisable(false);
    }
    //Método para desactivar controles
    public void desactivarControles(){
        txtCodigoEvento.setEditable(false);
        txtLugarEvento.setEditable(false);
        txtHoraEvento.setEditable(false);
        cmbCodigoServicio.setDisable(true);
        cmbCodigoEmpleado.setDisable(true);
        grpFechaEvento.setDisable(true);
    }
    //Método para limpiar controles
    public void limpiarControles(){
        txtCodigoEvento.setText(null);
        txtLugarEvento.setText(null);
        txtHoraEvento.setText(null);
        cmbCodigoServicio.getSelectionModel().select(null);
        cmbCodigoEmpleado.getSelectionModel().select(null);
        fecha.selectedDateProperty().set(null);
        tblServiciosHasEmpleados.getSelectionModel().clearSelection();
    }
    //Método para seleccionar elementros 
    public void seleccionarElementos(){
        if(tipoOperacion == Operacion.NINGUNO){
            if(tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()!=null){
                txtCodigoEvento.setText(String.valueOf(((ServiciosHasEmpleados)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getCodigoServicios_has_Empleado()));
                txtLugarEvento.setText(((ServiciosHasEmpleados)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getLugarEvento());
                txtHoraEvento.setText(((ServiciosHasEmpleados)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getHoraEvento()); 
                cmbCodigoEmpleado.getSelectionModel().select(buscarEmpleado(((ServiciosHasEmpleados)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getEmpleado_codigoEmpleado()));
                cmbCodigoServicio.getSelectionModel().select(buscarServicio(((ServiciosHasEmpleados)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getServicios_codigoServicio()));
                fecha.selectedDateProperty().set(((ServiciosHasEmpleados)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getFechaEvento());
            }else if(tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()!=null){
                tblServiciosHasEmpleados.getSelectionModel().clearSelection();
                txtCodigoEvento.setText(null);
                txtLugarEvento.setText(null);
                txtHoraEvento.setText(null);
                cmbCodigoEmpleado.getSelectionModel().select(null);
                cmbCodigoServicio.getSelectionModel().select(null);
                fecha.selectedDateProperty().set(null);
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
                txtCodigoEvento.setDisable(true);
                tipoOperacion = Operacion.GUARDAR;
                break;
            case GUARDAR:
                if(CamposVacios()== true){
                    guardar();
                    desactivarControles();
                    limpiarControles();
                    btnNuevo.setText("Nuevo");
                    btnEliminar.setText("Eliminar");
                    btnEditar.setVisible(true);
                    btnReporte.setVisible(true);
                    txtCodigoEvento.setDisable(false);
                    tipoOperacion = Operacion.NINGUNO;
                    cargarDatos();
                }else{
                    JOptionPane.showMessageDialog(null, "Debe llenar todos los campos");
                }
                break;
        }
    }
    //Método para Guardar en Mysql y Mostrar en tableView
    public void guardar(){
        ServiciosHasEmpleados registro = new ServiciosHasEmpleados();
        registro.setServicios_codigoServicio(((Servicio)cmbCodigoServicio.getSelectionModel().getSelectedItem()).getCodigoServicio());
        registro.setEmpleado_codigoEmpleado(((Empleado)cmbCodigoEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
        registro.setFechaEvento(fecha.getSelectedDate());
        registro.setHoraEvento(txtHoraEvento.getText());
        registro.setLugarEvento(txtLugarEvento.getText());
        try{
            PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarServicios_has_Empleado(?,?,?,?,?)}");
            sp.setInt(1, registro.getServicios_codigoServicio());
            sp.setInt(2, registro.getEmpleado_codigoEmpleado());
            sp.setDate(3, new java.sql.Date(registro.getFechaEvento().getTime()));
            sp.setString(4, registro.getHoraEvento());
            sp.setString(5, registro.getLugarEvento());
            sp.execute();
            listaServiciosHasEmpleados.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    //Método para botón eliminar y cancelar
    public void eliminar(){
        switch(tipoOperacion){
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setVisible(true);
                btnReporte.setVisible(true);
                txtCodigoEvento.setDisable(false);
                tipoOperacion = Operacion.NINGUNO;
                break;
            default:
                if(tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()!=null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro que quiere eliminar el registro","Eliminar Evento", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                        if(respuesta== JOptionPane.YES_OPTION){
                            try{
                                PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarServicios_has_Empleado(?)}");
                                sp.setInt(1, ((ServiciosHasEmpleados)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getCodigoServicios_has_Empleado());
                                sp.execute();
                                listaServiciosHasEmpleados.remove(tblServiciosHasEmpleados.getSelectionModel().getSelectedIndex());
                                limpiarControles();
                                JOptionPane.showMessageDialog(null, "Registro Eliminado");
                                tblServiciosHasEmpleados.getSelectionModel().clearSelection();
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                }
        }
    }
    //Método para el botón editar
    public void editar(){
        switch(tipoOperacion){
            case NINGUNO:
                if(tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()!=null){
                    activarControles();
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setVisible(false);
                    btnEliminar.setVisible(false);
                    txtCodigoEvento.setDisable(true);
                    tipoOperacion = Operacion.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un registro");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                desactivarControles();
                btnEditar.setText("Editar");
                btnReporte.setText("Cancelar");
                btnNuevo.setVisible(true);
                btnEliminar.setVisible(true);
                tipoOperacion = Operacion.NINGUNO;
                cargarDatos();
                break;
        }
    }
    //Método para actualizar en Mysql y actualizar en TableView
    public void actualizar(){
        try{
            PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarServicios_has_Empleado(?,?,?,?,?,?)}");
            ServiciosHasEmpleados registroActualizado = ((ServiciosHasEmpleados)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem());
            registroActualizado.setCodigoServicios_has_Empleado(Integer.parseInt(txtCodigoEvento.getText()));
            registroActualizado.setServicios_codigoServicio(((Servicio)cmbCodigoServicio.getSelectionModel().getSelectedItem()).getCodigoServicio());
            registroActualizado.setEmpleado_codigoEmpleado(((Empleado)cmbCodigoEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
            registroActualizado.setFechaEvento(fecha.getSelectedDate());
            registroActualizado.setHoraEvento(txtHoraEvento.getText());
            registroActualizado.setLugarEvento(txtLugarEvento.getText());
            sp.setInt(1, registroActualizado.getCodigoServicios_has_Empleado());
            sp.setInt(2, registroActualizado.getServicios_codigoServicio());
            sp.setInt(3, registroActualizado.getEmpleado_codigoEmpleado());
            sp.setDate(4,new java.sql.Date(registroActualizado.getFechaEvento().getTime()));
            sp.setString(5, registroActualizado.getHoraEvento());
            sp.setString(6, registroActualizado.getLugarEvento());
            sp.execute();
            JOptionPane.showMessageDialog(null, "Registro Actualizado");
            listaServiciosHasEmpleados.add(registroActualizado);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    //Método para el botón reporte y cancelar
    public void reporte(){
        switch(tipoOperacion){
            case ACTUALIZAR:
                desactivarControles();
                btnEditar.setText("Editar");
                btnReporte.setText("Cancelar");
                btnNuevo.setVisible(true);
                btnEliminar.setVisible(true);
                txtCodigoEvento.setDisable(false);
                tipoOperacion=Operacion.NINGUNO;
                break;
            case NINGUNO:
                break;
        }
    }
    //Método para imprimir reporte
    public void imprimirReporte(){
    
    }
    //Método para no dejar Campos vacíos
    public boolean CamposVacios(){
        boolean validacion = false;
            if(cmbCodigoEmpleado.getSelectionModel().getSelectedItem()!=null && cmbCodigoServicio.getSelectionModel().getSelectedItem()!=null &&
                fecha.getSelectedDate()!=null && txtHoraEvento.getText()!=null && txtLugarEvento.getText()!=null){
                validacion = true;
            }
        return validacion;
    }
}
