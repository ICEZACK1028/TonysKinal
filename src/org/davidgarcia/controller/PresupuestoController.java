package org.davidgarcia.controller;

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
import eu.schudt.javafx.controls.calendar.DatePicker;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.davidgarcia.bd.Conexion;
import org.davidgarcia.bean.Empresa;
import org.davidgarcia.bean.Presupuesto;
import org.davidgarcia.report.GenerarReporte;
import org.davidgarcia.system.MainApp;


public class PresupuestoController implements Initializable {
    private MainApp escenarioPrincipal;
    private enum Operacion {GUARDAR,ACTUALIZAR,NINGUNO};
    private Operacion tipoOperacion = Operacion.NINGUNO;
    private ObservableList<Empresa> listaEmpresa;
    private ObservableList<Presupuesto> listaPresupuesto;
    private DatePicker fecha;
    @FXML private TextField txtCodigoPresupuesto;
    @FXML private TextField txtCantidadPresupuesto;
    @FXML private GridPane grpFechaSolicitud;
    @FXML private ComboBox cmbCodigoEmpresa; 
    @FXML private TableView tblPresupuestos;
    @FXML private TableColumn colCodigoEmpresa;
    @FXML private TableColumn colFechaSolicitud;
    @FXML private TableColumn colCantidadPresupuesto;
    @FXML private TableColumn colCodigoPresupuesto;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {  
        cargarDatos();
        DatePicker();
        cmbCodigoEmpresa.setItems(getEmpresa());
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
    //Método de configuración del DatePicker
    public void DatePicker(){
        fecha = new DatePicker(Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyy-MM-dd"));
        fecha.getCalendarView().todayButtonTextProperty().set("Today");
        fecha.getCalendarView().setShowWeeks(false);
        fecha.getStylesheets().add("/org/davidgarcia/resource/DatePicker.css");
        grpFechaSolicitud.add(fecha,0,0);
    }
    // Método para cargar los datos en la TableView
    public void cargarDatos(){
        tblPresupuestos.setItems(getPresupuesto());
        colCodigoPresupuesto.setCellValueFactory(new PropertyValueFactory<Presupuesto, Integer>("codigoPresupuesto"));
        colFechaSolicitud.setCellValueFactory(new PropertyValueFactory<Presupuesto, Date>("fechaSolicitud"));
        colCantidadPresupuesto.setCellValueFactory(new PropertyValueFactory<Presupuesto, Double>("cantidadPresupuesto"));
        colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Presupuesto, Integer>("codigoEmpresa"));
    }
    //Método para observar los datos de la tabla presupuesto
    public ObservableList<Presupuesto> getPresupuesto(){
        ArrayList<Presupuesto> lista = new ArrayList<Presupuesto>();
        try{
            PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarPresupuesto()}");
            ResultSet resultado = sp.executeQuery();
            while (resultado.next()){
                lista.add(new Presupuesto(resultado.getInt("codigoPresupuesto"),
                                           resultado.getDate("fechaSolicitud"),
                                           resultado.getDouble("cantidadPresupuesto"),
                                           resultado.getInt("codigoEmpresa")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaPresupuesto = FXCollections.observableArrayList(lista);
    }
    //Método para observar los datos de la tabla Empresa
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
    
    //Método para seleccionar elementos
    public void seleccionarElementos(){
        if(tipoOperacion == Operacion.NINGUNO){
            if(tblPresupuestos.getSelectionModel().getSelectedItem()!=null){
                txtCodigoPresupuesto.setText(String.valueOf(((Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem()).getCodigoPresupuesto()));
                txtCantidadPresupuesto.setText(String.valueOf(((Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem()).getCantidadPresupuesto()));
                fecha.selectedDateProperty().set(((Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem()).getFechaSolicitud());
                cmbCodigoEmpresa.getSelectionModel().select(buscarEmpresa(((Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
            }
        }
    }
     
    //Método para activar controles
    public void activarControles(){
        txtCantidadPresupuesto.setEditable(true);
        grpFechaSolicitud.setDisable(false);
        cmbCodigoEmpresa.setDisable(false);
    }
    
    //Método para desactivar controles
    public void desactivarControles(){
        txtCodigoPresupuesto.setEditable(false);
        txtCantidadPresupuesto.setEditable(false);
        grpFechaSolicitud.setDisable(true);
        cmbCodigoEmpresa.setDisable(true);
    }
    
    //Método para limpiar controles
    public void limpiarControles(){
        txtCodigoPresupuesto.setText(null);
        txtCantidadPresupuesto.setText(null);
        fecha.selectedDateProperty().set(null);
        cmbCodigoEmpresa.getSelectionModel().select(null);
        tblPresupuestos.getSelectionModel().clearSelection();
    }
    
    //Método para buscar empresa
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
    //Método para guardar un presupuesto en la base de datos y en la tableview
    public void guardar(){
        Presupuesto registro = new Presupuesto();
        registro.setFechaSolicitud(fecha.getSelectedDate());
        registro.setCantidadPresupuesto(Double.parseDouble(txtCantidadPresupuesto.getText()));
        registro.setCodigoEmpresa(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        try{
         PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarPresupuesto(?,?,?)}");
         sp.setDate(1,new java.sql.Date(registro.getFechaSolicitud().getTime()));
         sp.setDouble(2, registro.getCantidadPresupuesto());
         sp.setInt(3, registro.getCodigoEmpresa());
         sp.execute();
         listaPresupuesto.add(registro);
        }catch(Exception e){
           e.printStackTrace();
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
                txtCodigoPresupuesto.setDisable(true);
                tblPresupuestos.getSelectionModel().clearSelection();
                tipoOperacion = Operacion.GUARDAR;
                break;
            case GUARDAR:
                if(CampoVacio() == true){
                    if(ValidacionEntero() ==true ){
                            guardar();
                            desactivarControles();
                            limpiarControles();
                            btnNuevo.setText("Nuevo");
                            btnEliminar.setText("Eliminar");
                            btnEditar.setVisible(true);
                            btnReporte.setVisible(true);
                            txtCodigoPresupuesto.setDisable(false);
                            tipoOperacion = Operacion.NINGUNO;
                            cargarDatos();
                    }else{
                        JOptionPane.showMessageDialog(null,"'"+txtCantidadPresupuesto.getText()+"'"+"\n No es una Cantidad");
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Debe llenar todos los campos");
                }
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
                btnEditar.setVisible(true);
                btnReporte.setVisible(true);
                txtCodigoPresupuesto.setDisable(false);
                tipoOperacion = Operacion.NINGUNO;
            break;
            default:
                if(tblPresupuestos.getSelectionModel().getSelectedItem()!=null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro que quiere eliminar el registro?", "Eliminar Presupuesto", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarPresupuesto1(?)}");
                            sp.setInt(1, ((Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem()).getCodigoPresupuesto());
                            listaPresupuesto.remove(tblPresupuestos.getSelectionModel().getSelectedIndex());
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
        switch (tipoOperacion){
            case NINGUNO:
                if(tblPresupuestos.getSelectionModel().getSelectedItem()!= null){
                    activarControles();
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setVisible(false);
                    btnEliminar.setVisible(false);
                    txtCodigoPresupuesto.setDisable(true);
                    tipoOperacion = Operacion.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un registro de la tabla");
                }
                break;
            case ACTUALIZAR:
                if(CampoVacio() == true){
                    actualizar();
                    desactivarControles();
                    btnEditar.setText("Editar");
                    btnReporte.setText("Reporte");
                    btnEliminar.setVisible(true);
                    btnNuevo.setVisible(true);
                    txtCodigoPresupuesto.setDisable(false);    
                    tipoOperacion = Operacion.NINGUNO;
                    cargarDatos();
                }else{
                   JOptionPane.showMessageDialog(null, "Debe llenar todos los campos");
                }
                break;
        }
    }
    //Método para el botón actualizar
    public void actualizar(){
        try{
            PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarPresupuesto(?,?,?,?)}");
            Presupuesto presupuestoActualizado = ((Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem());
                presupuestoActualizado.setCodigoPresupuesto(Integer.parseInt(txtCodigoPresupuesto.getText()));
                presupuestoActualizado.setFechaSolicitud(fecha.getSelectedDate());
                presupuestoActualizado.setCantidadPresupuesto(Double.parseDouble(txtCantidadPresupuesto.getText()));
                presupuestoActualizado.setCodigoEmpresa(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
                sp.setInt(1, presupuestoActualizado.getCodigoPresupuesto());
                sp.setDate(2, new java.sql.Date(presupuestoActualizado.getFechaSolicitud().getTime()));
                sp.setDouble(3, presupuestoActualizado.getCantidadPresupuesto());
                sp.setInt(4, presupuestoActualizado.getCodigoEmpresa());
                sp.execute();
                JOptionPane.showMessageDialog(null, "Datos Actualizados");
                listaPresupuesto.add(presupuestoActualizado);
        }catch(Exception e){
            e.printStackTrace();
        }
        
    
    }
    //Método para el botón reporte y poder generar reporte
    public void reporte(){
        switch(tipoOperacion){
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setVisible(true);
                btnEliminar.setVisible(true);
                txtCodigoPresupuesto.setDisable(false);
                tipoOperacion = Operacion.NINGUNO;
            break;
            case NINGUNO:
                if(tblPresupuestos.getSelectionModel().getSelectedItem() != null){
                    imprimirReporte();
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un registro");
                }
            break;
        } 
    }
    //Método para generar el reporte de presupuesto
    public void imprimirReporte(){
        Map parametros = new HashMap();
        int codigoEmpresa = ((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa();
        parametros.put("codigoEmpresa", codigoEmpresa);
        GenerarReporte.mostrarReporte("ReportePresupuesto.jasper", "Reporte de Presupuesto", parametros);
    }
    //Método para no dejar campos vacíos
    public boolean CampoVacio(){
        boolean validacion = false;
            if(fecha.getSelectedDate()!=null && txtCantidadPresupuesto.getText()!=null && 
                cmbCodigoEmpresa.getSelectionModel().getSelectedItem()!=null){
                validacion = true;
            }       
        return validacion;
    }
    //Método para validar enteros
    public boolean ValidacionEntero(){
        Double decimal;
        try{
            decimal = Double.parseDouble(txtCantidadPresupuesto.getText());
            return true;
        }catch(Exception e){
            return false;
        }
    }
}
