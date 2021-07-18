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
import org.davidgarcia.bean.TipoPlato;
import org.davidgarcia.system.MainApp;
import org.davidgarcia.bd.Conexion;

public class TipoPlatoController implements Initializable {
    private enum Operacion{GUARDAR, ACTUALIZAR, NINGUNO};
    private Operacion tipoOperacion = Operacion.NINGUNO;
    
    private MainApp escenarioPrincipal;
    private ObservableList <TipoPlato> listaTipoPlato;
     
    @FXML private ImageView imgPlatos;
    @FXML private TextField txtCodigoTipoPlato;
    @FXML private TextField txtDescripcion;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private TableColumn colCodigoTipoPlato;
    @FXML private TableColumn colDescripcion;
    @FXML private TableView tblTipoPlatos;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    public void insertarImagen(){
        imgPlatos.setImage(new Image("/org/davidgarcia/img/PlatosBrillo.png"));
    }
    public void devolverImagen(){
        imgPlatos.setImage(new Image("/org/davidgarcia/img/platos.png"));
    }
    //getters and setters para que se ejecute la escena en el escenario principal
    public MainApp getEscenarioPrincipal(){
        return escenarioPrincipal;
    }
    
    public void setEscenarioPrincipal (MainApp escenarioPrincipal){
        this.escenarioPrincipal = escenarioPrincipal;
    }
    //Método para retornar al menú principal...
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
    //Método para retornar a modulos...
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
    //Método para ingresar a la entidad Platos...
    public void plato(){
         if(tipoOperacion == Operacion.NINGUNO){
            escenarioPrincipal.Plato();
        }else{
            int respuesta = JOptionPane.showConfirmDialog(null, "   ¿Está seguro que quiere ingresar?\nSi ingresa perderá los datos ingresados","Aviso:",
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(respuesta == JOptionPane.OK_OPTION){
                escenarioPrincipal.Plato();
            }
        }
    }
    //Método para cargar los datos en la TableView
    public void cargarDatos(){
        tblTipoPlatos.setItems(getTipoPlato());
        colCodigoTipoPlato.setCellValueFactory(new PropertyValueFactory<TipoPlato, Integer>("codigoTipoPlato"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<TipoPlato, String>("descripcion"));
    }
    //Método para asignar las columnas de TipoPlatos
    public ObservableList<TipoPlato> getTipoPlato(){
        ArrayList<TipoPlato> lista = new ArrayList<TipoPlato>();
        try{
            PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarTipo_Plato()}");
            ResultSet resultado = sp.executeQuery();
                while(resultado.next()){
                lista.add(new TipoPlato(resultado.getInt("codigoTipoPlato"),
                                        resultado.getString("descripcion")));
                
                }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaTipoPlato = FXCollections.observableArrayList(lista);
    }
    //Método para activar controles
    public void activarControles(){
        txtDescripcion.setEditable(true);
    }
    //Método para desactivar controles
    public void desactivarControles(){
        txtCodigoTipoPlato.setEditable(false);
        txtDescripcion.setEditable(false);
    }
    //Método para limpiar controles
    public void limpiarControles(){
        txtCodigoTipoPlato.setText(null);
        txtDescripcion.setText(null);
        tblTipoPlatos.getSelectionModel().clearSelection();
    }
    //Método para seleccionar y mostrar en los textfields
    public void seleccionarElementos(){
        if(tipoOperacion == Operacion.NINGUNO){
            if(tblTipoPlatos.getSelectionModel().getSelectedItem()!= null){
                txtCodigoTipoPlato.setText(String.valueOf(((TipoPlato)tblTipoPlatos.getSelectionModel().getSelectedItem()).getCodigoTipoPlato()));
                txtDescripcion.setText(((TipoPlato)tblTipoPlatos.getSelectionModel().getSelectedItem()).getDescripcion());
            }
        }
    }
    //Método para el botón nuevo y guardar
    public void nuevo(){
        switch(tipoOperacion){
            case NINGUNO:
                activarControles();
                limpiarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setVisible(false);
                btnReporte.setVisible(false);
                txtCodigoTipoPlato.setDisable(true);
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
                    txtCodigoTipoPlato.setDisable(false);
                    tipoOperacion = Operacion.NINGUNO; 
                    cargarDatos();
                }else{
                    JOptionPane.showMessageDialog(null, "Debe agregar la Descripción");
                }
                break;
        }
    }
    //Método para guardar nuevos datos en la DB y en la TableView
    public void guardar(){
        TipoPlato registro = new TipoPlato();
        registro.setDescripcion(txtDescripcion.getText());
        try{
            PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarTipo_Plato(?)}");
            sp.setString(1, registro.getDescripcion());
            sp.execute();
            listaTipoPlato.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    //Método para el botón eliminar y cancelar
    public void eliminar(){
        switch(tipoOperacion){
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setVisible(true);
                btnReporte.setVisible(true);
                txtCodigoTipoPlato.setDisable(false);
                tipoOperacion = Operacion.NINGUNO;
                break;
        default:
            if(tblTipoPlatos.getSelectionModel().getSelectedItem()!= null){
            int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro que quiere eliminar el registro?", "Eliminar Tipo Plato",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if(respuesta== JOptionPane.YES_OPTION){
                    try{
                    PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarTipo_Plato(?)}");
                    sp.setInt(1,((TipoPlato)tblTipoPlatos.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());
                    sp.execute();
                    if(sp.execute() == false){
                    listaTipoPlato.remove(tblTipoPlatos.getSelectionModel().getSelectedIndex());
                    limpiarControles();
                    JOptionPane.showMessageDialog(null, "Registro Eliminado");
                    }
                    }catch(Exception e){
                        JOptionPane.showMessageDialog(null,"        No se puede eliminar el registro \n porque está ligado a registro(s) de Platos","Nota:",JOptionPane.CLOSED_OPTION);
                        e.printStackTrace();
                    }
                }
            }else{
                JOptionPane.showMessageDialog(null, "Debe seleccionar un registro");
            }
        }
    }
    //Método para el botón editar y actualizar
    public void editar(){
        switch(tipoOperacion){
            case NINGUNO:
                if(tblTipoPlatos.getSelectionModel().getSelectedItem()!=null){
                activarControles();
                btnEditar.setText("Actualizar");
                btnReporte.setText("Cancelar");
                btnNuevo.setVisible(false);
                btnEliminar.setVisible(false);
                txtCodigoTipoPlato.setDisable(true);
                tipoOperacion= Operacion.ACTUALIZAR;
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
                txtCodigoTipoPlato.setDisable(false);
                tipoOperacion = Operacion.NINGUNO;
                cargarDatos();
                break;
        }
    }
    //Método para actualizar los datos del TableView y de la DB
    public void actualizar(){
        try{
            PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarTipo_Plato(?,?)}");
            TipoPlato tipoActualizado = ((TipoPlato)tblTipoPlatos.getSelectionModel().getSelectedItem());
            tipoActualizado.setDescripcion(txtDescripcion.getText());
            sp.setInt(1, tipoActualizado.getCodigoTipoPlato());
            sp.setString(2, tipoActualizado.getDescripcion());
            sp.execute();
            listaTipoPlato.add(tipoActualizado);
            JOptionPane.showMessageDialog(null, "Datos Actualizados");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    //Método para el botón reporte y cancelar lo editado
    public void reporte(){
        switch(tipoOperacion){
            case ACTUALIZAR:
                desactivarControles();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setVisible(true);
                btnEliminar.setVisible(true);
                txtCodigoTipoPlato.setDisable(false);
                tipoOperacion = Operacion.NINGUNO;
                break;
            case NINGUNO:
                JOptionPane.showMessageDialog(null, "Aún esta función está en desarrollo");
                break;
        }
    }
    //Método para no permitir dejar campos vacíos
    public boolean CamposVacios(){
        boolean validacion = false;
            if(txtDescripcion.getText()!=null){
                validacion= true;
            }
        return validacion;  
    }
}
