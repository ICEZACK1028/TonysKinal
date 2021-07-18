package org.davidgarcia.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import org.davidgarcia.bean.Empresa;
import org.davidgarcia.bean.Servicio;
import org.davidgarcia.system.MainApp;
import eu.schudt.javafx.controls.calendar.DatePicker;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.davidgarcia.bd.Conexion;
import org.davidgarcia.report.GenerarReporte;

public class ServicioController implements Initializable {
    private MainApp escenarioPrincipal;
    private enum Operacion {GUARDAR, ACTUALIZAR,  NINGUNO};
    private Operacion tipoOperacion = Operacion.NINGUNO;
    private ObservableList<Servicio> listaServicio;
    private ObservableList<Empresa> listaEmpresa;
    private DatePicker fecha;
    @FXML private TextField txtCodigoServicio;
    @FXML private TextField txtTelefonoContacto;
    @FXML private TextField txtTipoServicio;
    @FXML private TextField txtLugarServicio;
    @FXML private TextField txtHoraServicio;
    @FXML private Button btnNuevo;
    @FXML private Button btnReporte;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private ComboBox cmbCodigoEmpresa;
    @FXML private TableColumn colCodigoServicio;
    @FXML private TableColumn colFechaServicio;
    @FXML private TableColumn colTipoServicio;
    @FXML private TableColumn colHoraServicio;
    @FXML private TableColumn colLugarServicio;
    @FXML private TableColumn colTelefonoContacto;
    @FXML private TableColumn colCodigoEmpresa;
    @FXML private TableView tblServicios;
    @FXML private GridPane grpFechaServicio;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
        DatePicker();
        cmbCodigoEmpresa.setItems(getEmpresa());
    }  
    
    public MainApp getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(MainApp escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    //Método para regresar a la vista de empresa...
    public void empresa(){
          if(tipoOperacion == Operacion.NINGUNO){
            escenarioPrincipal.Empresa();
        }else{
            int respuesta = JOptionPane.showConfirmDialog(null, "   ¿Está seguro que quiere regresar?\nSi regresa perderá los datos ingresados","Aviso:",
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(respuesta == JOptionPane.OK_OPTION){
                escenarioPrincipal.Empresa();
            }
        }
    }  
    //Método para regresar al menú principal...
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
    //Método para la configuración y selección del DatePicker
    public void DatePicker(){
        fecha = new DatePicker(Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyy-MM-dd"));
        fecha.getCalendarView().todayButtonTextProperty().set("Hoy");
        fecha.getCalendarView().setShowWeeks(false);
        fecha.getStylesheets().add("/org/davidgarcia/resource/DatePicker.css");
        grpFechaServicio.add(fecha,0,0);
    }
    public void cargarDatos(){
        tblServicios.setItems(getServicio());
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory<Servicio, Integer>("codigoServicio"));
        colFechaServicio.setCellValueFactory(new PropertyValueFactory<Servicio, Date>("fechaServicio"));
        colTipoServicio.setCellValueFactory(new PropertyValueFactory<Servicio, String>("tipoServicio"));
        colHoraServicio.setCellValueFactory(new PropertyValueFactory<Servicio, String>("horaServicio"));
        colLugarServicio.setCellValueFactory(new PropertyValueFactory<Servicio, String>("lugarServicio"));
        colTelefonoContacto.setCellValueFactory(new PropertyValueFactory<Servicio, String>("telefonoContacto"));
        colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Servicio, Integer>("codigoEmpresa"));
    }
    
    public ObservableList<Servicio> getServicio(){
        ArrayList<Servicio> lista = new ArrayList<Servicio>();
        try{
            PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarServicio}");
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
    
    public ObservableList<Empresa> getEmpresa(){
        ArrayList<Empresa> lista = new ArrayList<Empresa>();
        try{
            PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEmpresa()}");
            ResultSet resultado = sp.executeQuery();
            while(resultado.next()){
                lista.add(new Empresa(resultado.getInt("codigoEmpresa"),
                                      resultado.getString("nombreEmpresa"),
                                      resultado.getString("direccion"),
                                      resultado.getString("telefono")));
            } 
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaEmpresa = FXCollections.observableArrayList(lista);
    }
    //Método para buscar empresas
      public Empresa buscarEmpresa(int codigoEmpresa){
        Empresa resultado = null;
        try{
            PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarEmpresa(?)}");
            sp.setInt(1, codigoEmpresa);
            ResultSet registro = sp.executeQuery();
            while(registro.next()){
                resultado =  new Empresa(registro.getInt("codigoEmpresa"),
                                         registro.getString("nombreEmpresa"),
                                         registro.getString("direccion"),
                                         registro.getString("telefono"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }  
    //Método para activar controles
    public void activarControles(){
        txtTelefonoContacto.setEditable(true);
        txtTipoServicio.setEditable(true);
        txtLugarServicio.setEditable(true);
        txtHoraServicio.setEditable(true);
        grpFechaServicio.setDisable(false);
        cmbCodigoEmpresa.setDisable(false);
    }
    //Método para desactivar controles
    public void desactivarControles(){
        txtCodigoServicio.setEditable(false);
        txtTelefonoContacto.setEditable(false);
        txtTipoServicio.setEditable(false);
        txtLugarServicio.setEditable(false);
        txtHoraServicio.setEditable(false);
        grpFechaServicio.setDisable(true);
        cmbCodigoEmpresa.setDisable(true);
    }
    //Método limpiar controles
    public void limpiarControles(){
        txtCodigoServicio.setText(null);
        txtTelefonoContacto.setText(null);
        txtTipoServicio.setText(null);
        txtLugarServicio.setText(null);
        txtHoraServicio.setText(null);
        fecha.selectedDateProperty().set(null);
        cmbCodigoEmpresa.getSelectionModel().select(null);
        tblServicios.getSelectionModel().clearSelection();
    }
    //Método para seleccionar elementos
    public void seleccionarElementos(){
        if(tipoOperacion == Operacion.NINGUNO){
            if(tblServicios.getSelectionModel().getSelectedItem()!=null){
                txtCodigoServicio.setText(String.valueOf(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getCodigoServicio()));
                fecha.selectedDateProperty().set(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getFechaServicio());        
                txtTipoServicio.setText(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getTipoServicio());
                txtHoraServicio.setText(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getHoraServicio());
                txtLugarServicio.setText(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getLugarServicio());
                txtTelefonoContacto.setText(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getTelefonoContacto());
                cmbCodigoEmpresa.getSelectionModel().select(buscarEmpresa(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
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
                txtCodigoServicio.setDisable(true);
                cmbCodigoEmpresa.setDisable(false);
                tipoOperacion = Operacion.GUARDAR;
                break;
            case GUARDAR:
                if(CamposVacios()== true){
                    //if(validacionHora(txtHoraServicio.getText()) == true){
                        guardar();
                        desactivarControles();
                        limpiarControles();
                        btnNuevo.setText("Nuevo");
                        btnEliminar.setText("Eliminar");
                        btnEditar.setVisible(true);
                        btnReporte.setVisible(true);
                        txtCodigoServicio.setDisable(true);
                        cmbCodigoEmpresa.setDisable(true);
                        tipoOperacion = Operacion.NINGUNO;
                        cargarDatos();
                    /*}else{
                        JOptionPane.showMessageDialog(null, "Debe ingresar la hora en formato hh:MM:dd");
                    }*/
                }else{
                    JOptionPane.showMessageDialog(null, "Debe llenar todos los campos");
                }
                break;
        }
    }
    //Método para el botón guardar
    public void guardar(){
        Servicio registro = new Servicio();
        registro.setFechaServicio(fecha.getSelectedDate());
        registro.setTipoServicio(txtTipoServicio.getText());
        registro.setHoraServicio(txtHoraServicio.getText());
        registro.setLugarServicio(txtLugarServicio.getText());
        registro.setTelefonoContacto(txtTelefonoContacto.getText());
        registro.setCodigoEmpresa(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        try{
            PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarServicio(?,?,?,?,?,?)}");
            sp.setDate(1, new java.sql.Date(registro.getFechaServicio().getTime()));
            sp.setString(2, registro.getTipoServicio());
            sp.setString(3, registro.getHoraServicio());
            sp.setString(4, registro.getLugarServicio());
            sp.setString(5, registro.getTelefonoContacto());
            sp.setInt(6, registro.getCodigoEmpresa());
            sp.execute();
            listaServicio.add(registro);
        }catch(Exception e){
            e.printStackTrace();
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
                btnEditar.setVisible(true);
                btnReporte.setVisible(true);
                txtCodigoServicio.setDisable(false);
                tipoOperacion = Operacion.NINGUNO;
            break;
            default:
                if(tblServicios.getSelectionModel().getSelectedItem()!= null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro que quiere eliminar el registro?", "Eliminar Servicio",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                    try{
                        PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarServicio(?)}");
                        sp.setInt(1, ((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getCodigoServicio());
                        sp.execute();
                        listaServicio.remove(tblServicios.getSelectionModel().getSelectedIndex());
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
    //Método para el botón editar
    public void editar(){
        switch(tipoOperacion){
            case NINGUNO:   
                if(tblServicios.getSelectionModel().getSelectedItem()!= null){
                    activarControles();
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setVisible(false);
                    btnEliminar.setVisible(false);
                    txtCodigoServicio.setDisable(true);
                    cmbCodigoEmpresa.setDisable(false);
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
                txtCodigoServicio.setDisable(false);
                tipoOperacion = Operacion.NINGUNO;
                cargarDatos();
                break;
        }
    }
    //Método para el botón actualizar
    public void actualizar(){
        try{
        PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarServicio(?,?,?,?,?,?,?)}");
        Servicio servicioActualizado = ((Servicio)tblServicios.getSelectionModel().getSelectedItem());
        servicioActualizado.setCodigoServicio(Integer.parseInt(txtCodigoServicio.getText()));
        servicioActualizado.setFechaServicio(fecha.getSelectedDate());
        servicioActualizado.setTipoServicio(txtTipoServicio.getText());
        servicioActualizado.setHoraServicio(txtHoraServicio.getText());
        servicioActualizado.setLugarServicio(txtLugarServicio.getText());
        servicioActualizado.setTelefonoContacto(txtTelefonoContacto.getText());
        servicioActualizado.setCodigoEmpresa(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        sp.setInt(1,servicioActualizado.getCodigoServicio());
        sp.setDate(2,new java.sql.Date(servicioActualizado.getFechaServicio().getTime()));
        sp.setString(3, servicioActualizado.getTipoServicio());
        sp.setString(4, servicioActualizado.getHoraServicio());
        sp.setString(5, servicioActualizado.getLugarServicio());
        sp.setString(6, servicioActualizado.getTelefonoContacto());
        sp.setInt(7, servicioActualizado.getCodigoEmpresa());
        sp.execute();
        JOptionPane.showMessageDialog(null, "Datos Actualizados");
        listaServicio.add(servicioActualizado);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    //Método para el botón reporte
     public void reporte(){
        switch(tipoOperacion){
            case ACTUALIZAR:
                desactivarControles();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setVisible(true);
                btnEliminar.setVisible(true);
                txtCodigoServicio.setDisable(false);
                cmbCodigoEmpresa.setDisable(true);
                tipoOperacion = Operacion.NINGUNO;
                break;
            case NINGUNO:
                if(tblServicios.getSelectionModel().getSelectedItem()!= null){
                    imprimirReporte();
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un registro");
                }
                break;
        }
    }
     //Método para no dejar campos vacíos
     public boolean CamposVacios(){
        boolean validacion= false;
            if(fecha.getSelectedDate()!=null && txtHoraServicio.getText()!=null && txtLugarServicio.getText()!=null &&
                txtTelefonoContacto.getText()!=null && txtTipoServicio.getText()!=null && cmbCodigoEmpresa.getSelectionModel().getSelectedItem()!=null){
                validacion = true;
            }
        return validacion;
     }
     //Método para imprimir el reporte
     public void imprimirReporte(){
        Map parametros = new HashMap();
        int codigoServicio= ((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getCodigoServicio();
        parametros.put("IDServicio", codigoServicio);
        GenerarReporte.mostrarReporte("ReporteServicios.jasper", "Reporte de Servicios", parametros);
     }
     /*//Método para la validación de la hora
     public boolean validacionHora(String Hora){
            char [] a = Hora.toString().toCharArray();
            String [] c = Hora.split(":");
            if((a[0] == ' ') || (a[1]== ' ') || (a[2]== ' ') || (a[3]== ' ') || (a[4]== ' ') || (a[5]==' ') || (a[6]== ' ') || (a[7]==' ')
                || (Integer.parseInt(c[0]) > 24) || (Integer.parseInt(c[1]) > 59) || (Integer.parseInt(c[2]) > 59) || (Hora.length()!= 7)){
                return false;
            }else{
                return true;
            }
        
     }*/
}
