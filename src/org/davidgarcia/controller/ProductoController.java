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
import javax.swing.JOptionPane;
import org.davidgarcia.bd.Conexion;
import org.davidgarcia.bean.Producto;
import org.davidgarcia.system.MainApp;

public class ProductoController implements Initializable{
    private enum Operacion{GUARDAR, ACTUALIZAR, NINGUNO};
    private Operacion tipoOperacion = Operacion.NINGUNO;
    
    private MainApp escenarioPrincipal;
    private ObservableList<Producto> listaProductos;
    
    @FXML private TextField txtCodigoProducto;
    @FXML private TextField txtNombreProducto;
    @FXML private TextField txtCantidad;
    @FXML private TableColumn colCodigoProducto;
    @FXML private TableColumn colNombreProducto;
    @FXML private TableColumn colCantidad;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private TableView tblProductos;
    
    @Override
    public void initialize(URL location, ResourceBundle resources){
        cargarDatos();
    }
    
    //Método para poder ejecutar la ventana en el Escenario Principal
    public MainApp getEscenarioPrincipal(){
        return escenarioPrincipal;
    }
    public void setEscenarioPrincipal(MainApp escenarioPrincipal){
        this.escenarioPrincipal = escenarioPrincipal;
    }
    //Método para regresar a modulos
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
    //Método para regresar a menu principal
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
        tblProductos.setItems(getProducto());
        colCodigoProducto.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("codigoProducto"));
        colNombreProducto.setCellValueFactory(new PropertyValueFactory<Producto, String>("nombreProducto"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("cantidad"));
    }
    //Método para obtener los datos de la DB por medio de sp
    public ObservableList<Producto> getProducto(){
        ArrayList<Producto> lista = new ArrayList<Producto>();
        try{
            PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProducto()}");
            ResultSet resultado = sp.executeQuery();
                while(resultado.next()){
                lista.add(new Producto(resultado.getInt("codigoProducto"),
                                       resultado.getString("nombreProducto"),
                                       resultado.getInt("cantidad")));
                }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaProductos = FXCollections.observableArrayList(lista);
    }
    ///Método para activar controles 
    public void activarControles(){
        txtNombreProducto.setEditable(true);
        txtCantidad.setEditable(true);
    }
    //Método para desactivar controles
    public void desactivarControles(){
        txtCodigoProducto.setEditable(false);
        txtNombreProducto.setEditable(false);
        txtCantidad.setEditable(false);
    }
    //Método para limpiar controles
    public void limpiarControles(){
        txtCodigoProducto.setText(null);
        txtNombreProducto.setText(null);
        txtCantidad.setText(null);
        tblProductos.getSelectionModel().clearSelection();
    }
    //Método para seleccionar elementos de la tableView
    public void seleccionarElementos(){
        if(tipoOperacion == Operacion.NINGUNO){
            if(tblProductos.getSelectionModel().getSelectedItem()!=null){
                txtCodigoProducto.setText(String.valueOf(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto()));
                txtNombreProducto.setText(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getNombreProducto());
                txtCantidad.setText(String.valueOf(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getCantidad()));
            }
        }
    }
    //Método para el botón nuevo y la acción guardar
    public void nuevo(){
        switch(tipoOperacion){
            case NINGUNO:
                activarControles();
                limpiarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setVisible(false);
                btnReporte.setVisible(false);
                txtCodigoProducto.setDisable(true);
                tipoOperacion = Operacion.GUARDAR;
                break;
            case GUARDAR:
                if(CamposVacios() == true){
                    if(ValidacionEntero() == true){    
                        guardar();
                        desactivarControles();
                        limpiarControles();
                        btnNuevo.setText("Nuevo");
                        btnEliminar.setText("Eliminar");
                        btnEditar.setVisible(true);
                        btnReporte.setVisible(true);
                        txtCodigoProducto.setDisable(false);
                        tipoOperacion = Operacion.NINGUNO;
                        cargarDatos();
                    }else{
                        JOptionPane.showMessageDialog(null,"'"+txtCantidad.getText()+"'"+"\n No es una Cantidad");
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Debe llenar todos los campos");
                }
                break;
        }
    }
    //Método para guardar nuevos registros
    public void guardar(){
        Producto registro = new Producto();
        registro.setNombreProducto(txtNombreProducto.getText());
        registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
            try{
                PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarProducto(?,?)}");
                sp.setString(1,registro.getNombreProducto());
                sp.setInt(2, registro.getCantidad());
                sp.execute();
                listaProductos.add(registro);
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
                txtCodigoProducto.setDisable(false);
                tipoOperacion = Operacion.NINGUNO;
                break;
        default:
            if(tblProductos.getSelectionModel().getSelectedItem()!= null){
            int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro que quiere eliminar el registro?", "Eliminar Producto", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                if(respuesta == JOptionPane.YES_OPTION){
                    try{
                        PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarProducto(?)}");
                        sp.setInt(1, ((Producto)tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto());
                        sp.execute();
                        listaProductos.remove(tblProductos.getSelectionModel().getSelectedIndex());
                        limpiarControles();
                        JOptionPane.showMessageDialog(null, "Registro Eliminado");
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
                if(tblProductos.getSelectionModel().getSelectedItem()!= null){
                    activarControles();
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setVisible(false);
                    btnEliminar.setVisible(false);
                    txtCodigoProducto.setDisable(true);
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
                txtCodigoProducto.setDisable(false);
                tipoOperacion = Operacion.NINGUNO;
                cargarDatos();
                break;
        }
    }
    //Método para actualizar un registro
    public void actualizar(){
        try{
            PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarProducto(?,?,?)}");
            Producto productoActualizado =((Producto)tblProductos.getSelectionModel().getSelectedItem());
            productoActualizado.setCodigoProducto(Integer.parseInt (txtCodigoProducto.getText()));
            productoActualizado.setNombreProducto(txtNombreProducto.getText());
            productoActualizado.setCantidad(Integer.parseInt(txtCantidad.getText()));
            sp.setInt(1, productoActualizado.getCodigoProducto());
            sp.setString(2, productoActualizado.getNombreProducto());
            sp.setInt(3, productoActualizado.getCantidad());
            sp.execute();
            listaProductos.add(productoActualizado);
            JOptionPane.showMessageDialog(null, "Datos Actualizados");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    //Método para el botón reporte y cancelar el editar
    public void reporte(){
        switch(tipoOperacion){
            case ACTUALIZAR:
                desactivarControles();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setVisible(true);
                btnEliminar.setVisible(true);
                txtCodigoProducto.setDisable(false);
                tipoOperacion = Operacion.NINGUNO;
                break;
        }
    }
    //Método para no dejar los textfield vacíos
    public boolean CamposVacios(){
        boolean validacion = false;
            if(txtCantidad.getText()!=null && txtNombreProducto.getText()!=null){
                validacion= true;
            }
        return validacion;
    }
    //Método para evaluar un textfied entero
    public boolean ValidacionEntero(){
        int entero;
        try{
            entero = Integer.parseInt(txtCantidad.getText());
            return true;
        }catch(Exception e){
            return false;
        }
    }
}
