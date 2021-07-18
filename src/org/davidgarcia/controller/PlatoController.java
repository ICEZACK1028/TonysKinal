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
import org.davidgarcia.bean.Plato;
import org.davidgarcia.bean.TipoPlato;

public class PlatoController implements Initializable{
    private enum Operacion{GUARDAR,NINGUNO,ACTUALIZAR};
    private Operacion tipoOperacion = Operacion.NINGUNO;
    
    private MainApp escenarioPrincipal;
    private ObservableList<Plato> listaPlato;
    private ObservableList<TipoPlato> listaTipoPlato;
    
    @FXML private TextField txtCodigoPlato;
    @FXML private TextField txtNombrePlato;
    @FXML private TextField txtDescripcionPlato;
    @FXML private TextField txtPrecioPlato;
    @FXML private TextField txtCantidad;
    @FXML private ComboBox cmbCodigoTipoPlato;
    @FXML private TableColumn colCodigoPlato;
    @FXML private TableColumn colNombrePlato;
    @FXML private TableColumn colDescripcionPlato;
    @FXML private TableColumn colCantidad;
    @FXML private TableColumn colPrecioPlato;
    @FXML private TableColumn colCodigoTipoPlato;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private TableView tblPlatos;
    
      @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoTipoPlato.setItems(getTipoPlato());
    }
    
    //getters and setters para ejecutar la ventana en el escenarioPrincipal
    public MainApp getEscenarioPrincipal(){
        return escenarioPrincipal;
    }
    
    public void setEscenarioPrincipal(MainApp escenarioPrincipal){
        this.escenarioPrincipal = escenarioPrincipal;
    }
    //Método para retornar a TipoPlato...
    public void tipoPlato(){
        if(tipoOperacion == Operacion.NINGUNO){
            escenarioPrincipal.TipoPlato();
        }else{
            int respuesta = JOptionPane.showConfirmDialog(null, "   ¿Está seguro que quiere regresar?\nSi regresa perderá los datos ingresados","Aviso:",
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(respuesta == JOptionPane.OK_OPTION){
                escenarioPrincipal.TipoPlato();
            }
        }
    }
    //Método para volver al menú principal...
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
    //Método para cargar los datos de la DB en la tableView
    public void cargarDatos(){
        tblPlatos.setItems(getPlato());
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory<Plato, Integer>("codigoPlato"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<Plato, Integer>("cantidad"));
        colNombrePlato.setCellValueFactory(new PropertyValueFactory<Plato, Integer>("nombrePlato"));
        colDescripcionPlato.setCellValueFactory(new PropertyValueFactory<Plato, Integer>("descripcionPlato"));
        colPrecioPlato.setCellValueFactory(new PropertyValueFactory<Plato, Double>("precioPlato"));
        colCodigoTipoPlato.setCellValueFactory(new PropertyValueFactory<Plato, Integer>("codigoTipoPlato"));
    }
    //Método para ejecutar el query en DB y llenar la tabla de resultado (TableView)
    public ObservableList<Plato> getPlato(){
        ArrayList<Plato> lista = new ArrayList<Plato>();
        try{
            PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarPlato()}");
            ResultSet resultado = sp.executeQuery();
                while(resultado.next()){
                    lista.add(new Plato(resultado.getInt("codigoPlato"),
                                        resultado.getInt("cantidad"),
                                        resultado.getString("nombrePlato"),
                                        resultado.getString("descripcionPlato"),
                                        resultado.getDouble("precioPlato"),
                                        resultado.getInt("codigoTipoPlato")));
                }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaPlato = FXCollections.observableArrayList(lista);
    }
    //Método para ejecutar el sp y mostrar los datos de la entidad TipoPlato en el ComboBox TipoPlato
    public ObservableList<TipoPlato> getTipoPlato(){
        ArrayList<TipoPlato> lista = new ArrayList<>();
            try{
                PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarTipo_Plato}");
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
    //Método para buscar tipoPlato, para poder agregar únicamente un registro al seleccionarlo en la TableView y ComboBox
    public TipoPlato buscarTipoPlato ( int codigoTipoPlato){
        TipoPlato resultado = null;
        try{
            PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarTipo_Plato(?)}");
            sp.setInt(1, codigoTipoPlato);
            ResultSet registro = sp.executeQuery();
                while(registro.next()){
                    resultado = new TipoPlato(registro.getInt("codigoTipoPlato"),
                                            registro.getString("descripcion"));
                }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    //Método para activar controles
    public void activarControles(){
        txtCantidad.setEditable(true);
        txtNombrePlato.setEditable(true);
        txtDescripcionPlato.setEditable(true);
        txtPrecioPlato.setEditable(true);
        cmbCodigoTipoPlato.setDisable(false);
    }
    //Método para desactivar controles
    public void desactivarControles(){
        txtCodigoPlato.setEditable(false);
        txtCantidad.setEditable(false);
        txtNombrePlato.setEditable(false);
        txtDescripcionPlato.setEditable(false);
        txtPrecioPlato.setEditable(false);
        cmbCodigoTipoPlato.setDisable(true);
    }
    //Método para limpiar controles
    public void limpiarControles(){
        txtCodigoPlato.setText(null);
        txtCantidad.setText(null);
        txtNombrePlato.setText(null);
        txtDescripcionPlato.setText(null);
        txtPrecioPlato.setText(null);
        cmbCodigoTipoPlato.getSelectionModel().select(null);
        tblPlatos.getSelectionModel().clearSelection();
    }
    //Método para seleccionar elementos de la TableView
    public void seleccionarElementos(){
        if(tipoOperacion == Operacion.NINGUNO){
            if(tblPlatos.getSelectionModel().getSelectedItem()!=null){
                txtCodigoPlato.setText(String.valueOf(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato()));
                txtCantidad.setText(String.valueOf(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getCantidad()));
                txtNombrePlato.setText(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getNombrePlato());
                txtDescripcionPlato.setText(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getDescripcionPlato());
                txtPrecioPlato.setText(String.valueOf(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getPrecioPlato()));
                cmbCodigoTipoPlato.getSelectionModel().select(buscarTipoPlato(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getCodigoTipoPlato()));
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
                txtCodigoPlato.setDisable(true);
                tblPlatos.getSelectionModel().clearSelection();
                tipoOperacion = Operacion.GUARDAR;
                break;
            case GUARDAR:
                if(CamposVacios()== true){
                    if(validacionEntero() == true){
                        guardar();
                        desactivarControles();
                        limpiarControles();
                        btnNuevo.setText("Nuevo");
                        btnEliminar.setText("Eliminar");
                        btnEditar.setVisible(true);
                        btnReporte.setVisible(true);
                        txtCodigoPlato.setDisable(false);
                        tipoOperacion = Operacion.NINGUNO;
                        cargarDatos();
                    }else{
                        JOptionPane.showMessageDialog(null, "En Cantidad y Precio debe ingresar solo números\n"
                                                            +"Cantidad = Número Entero | Precio = Con centavos");
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Debe llenar todos los campos");
                }
                break; 
        }
    }
    //Método para guardar en la DB y agregar en la TableView
    public void guardar(){
        Plato registro = new Plato();
        registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
        registro.setNombrePlato(txtNombrePlato.getText());
        registro.setDescripcionPlato(txtDescripcionPlato.getText());
        registro.setPrecioPlato(Double.parseDouble(txtPrecioPlato.getText()));
        registro.setCodigoTipoPlato(((TipoPlato)cmbCodigoTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());
        try{
            PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarPlato(?,?,?,?,?)}");
            sp.setInt(1, registro.getCantidad());
            sp.setString(2, registro.getNombrePlato());
            sp.setString(3, registro.getDescripcionPlato());
            sp.setDouble(4, registro.getPrecioPlato());
            sp.setInt(5, registro.getCodigoTipoPlato());
            sp.execute();
            listaPlato.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    //Método para eliminar y para cancelar el botón nuevo
    public void eliminar(){
        switch (tipoOperacion){
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setVisible(true);
                btnReporte.setVisible(true);
                txtCodigoPlato.setDisable(false);
                tipoOperacion = Operacion.NINGUNO;
                break;
            default:
                if(tblPlatos.getSelectionModel().getSelectedItem()!= null){
                int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro que quiere eliminar el registro?", "Eliminar Plato",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarPlato(?)}");
                            sp.setInt(1, ((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato());
                            sp.execute();
                            JOptionPane.showMessageDialog(null, "Registro Eliminado");
                            listaPlato.remove(tblPlatos.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un registro");
                }
        }
    }
    //Método para el botón editar
    public void editar(){
        switch(tipoOperacion){
            case NINGUNO:
                if(tblPlatos.getSelectionModel().getSelectedItem()!= null){
                    activarControles();
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setVisible(false);
                    btnEliminar.setVisible(false);
                    txtCodigoPlato.setDisable(true);
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
                txtCodigoPlato.setDisable(false);
                tipoOperacion = Operacion.NINGUNO;
                cargarDatos();
                break;
        }
    }
    //Método para actualizar el registro en la DB y mostrarlo en la TableView
    public void actualizar(){
        try{
            PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarPlato(?,?,?,?,?,?)}");
            Plato platoActualizado = ((Plato)tblPlatos.getSelectionModel().getSelectedItem());
            //Enviando datos obtenidos de los textFields 
            platoActualizado.setCodigoPlato(Integer.parseInt(txtCodigoPlato.getText()));
            platoActualizado.setCantidad(Integer.parseInt(txtCantidad.getText()));
            platoActualizado.setNombrePlato(txtNombrePlato.getText());
            platoActualizado.setDescripcionPlato(txtDescripcionPlato.getText());
            platoActualizado.setPrecioPlato(Double.parseDouble(txtPrecioPlato.getText()));
            platoActualizado.setCodigoTipoPlato(((TipoPlato)cmbCodigoTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());
            //Enviando los datos actualizados al model MysQl por el sp y al tableView
            sp.setInt(1, platoActualizado.getCodigoPlato());
            sp.setInt(2, platoActualizado.getCantidad());
            sp.setString(3, platoActualizado.getNombrePlato());
            sp.setString(4, platoActualizado.getDescripcionPlato());
            sp.setDouble(5, platoActualizado.getPrecioPlato());
            sp.setInt(6, platoActualizado.getCodigoTipoPlato());
            sp.execute();
            JOptionPane.showMessageDialog(null, "Datos Actualizados");
            listaPlato.add(platoActualizado);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    //Método para el botón reporte y cancelar lo editado
    public void reporte(){
        switch(tipoOperacion){
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setVisible(true);
                btnEliminar.setVisible(true);
                txtCodigoPlato.setDisable(false);
                tipoOperacion = Operacion.NINGUNO;
                break;
        }
    }
    //Método para no dejar campos vacíos
    public boolean CamposVacios(){
        boolean validacion = false;
            if(txtCantidad.getText()!=null && txtNombrePlato.getText()!=null && txtDescripcionPlato.getText()!=null &&
               txtPrecioPlato.getText()!=null && cmbCodigoTipoPlato.getSelectionModel().getSelectedItem()!=null){
                validacion = true;
            }
        return validacion;
    }
    //Método para validar el texto entero
    public boolean validacionEntero(){
        int entero;
        double decimal;
            try{
                entero = Integer.parseInt(txtCantidad.getText());
                decimal = Double.parseDouble(txtPrecioPlato.getText());
                return true;
            }catch(Exception e){
                e.printStackTrace();

                return false;
            }
    }
}
