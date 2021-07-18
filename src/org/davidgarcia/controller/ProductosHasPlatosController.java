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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.naming.spi.DirStateFactory;
import org.davidgarcia.bd.Conexion;
import org.davidgarcia.bean.Plato;
import org.davidgarcia.bean.Producto;
import org.davidgarcia.bean.ProductosHasPlatos;
import org.davidgarcia.system.MainApp;

public class ProductosHasPlatosController implements Initializable{
    private MainApp escenarioPrincipal;
    
    private ObservableList<ProductosHasPlatos> listaPHP;
    private ObservableList<Plato> listaPlato;
    private ObservableList<Producto> listaProducto;
    
    @FXML private ComboBox cmbCodigoProducto;
    @FXML private ComboBox cmbCodigoPlato;
    @FXML private TableColumn colCodigoProducto;
    @FXML private TableColumn colCodigoPlato;
    @FXML private TableView tblProductosHasPlatos;
    
    @Override
    public void initialize(URL location, ResourceBundle resource){
        cargarDatos();
        cmbCodigoPlato.setItems(getPlato());
        cmbCodigoProducto.setItems(getProducto());
    }
    //Método para ejecutar la ventana en el menú principal
    public MainApp getEscenarioPrincipal(){
        return escenarioPrincipal;
    }
    public void setEscenarioPrincipal(MainApp escenarioPrincipal){
        this.escenarioPrincipal = escenarioPrincipal;
    }
    //Método para retornar al menú principal
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    //Método para regresar a la ventana de Entidades de unión
    public void entidadesDeUnion(){
        escenarioPrincipal.EntidadesDeUnion();
    }
    //Método para cargar los datos en la TableView
    public void cargarDatos(){
        tblProductosHasPlatos.setItems(getProductosHasPlatos());                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory<ProductosHasPlatos, Integer>("Platos_codigoPlato"));
        colCodigoProducto.setCellValueFactory(new PropertyValueFactory<ProductosHasPlatos, Integer>("Productos_codigoProducto"));
    }
    //Método para seleccionar datos
    public void seleccionarDatos(){
        if(tblProductosHasPlatos.getSelectionModel().getSelectedItem()!= null){
            cmbCodigoPlato.getSelectionModel().select(buscarPlato(((ProductosHasPlatos)tblProductosHasPlatos.getSelectionModel().getSelectedItem()).getPlatos_codigoPlato()));
            cmbCodigoProducto.getSelectionModel().select(buscarProducto(((ProductosHasPlatos)tblProductosHasPlatos.getSelectionModel().getSelectedItem()).getProductos_codigoProducto()));
        }
    }
    //Método para obtener y asignarle a las columnas de tableView a Mysql
    public ObservableList<ProductosHasPlatos> getProductosHasPlatos(){
        ArrayList<ProductosHasPlatos> lista = new ArrayList<ProductosHasPlatos>();
            try{
                PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProductos_has_Plato()}");
                ResultSet resultado = sp.executeQuery();
                    while(resultado.next()){
                        lista.add(new ProductosHasPlatos(resultado.getInt("codigoPlato"),
                                                         resultado.getInt("codigoProducto")));
                    }
            }catch(Exception e){
                e.printStackTrace();
            }
        return listaPHP  = FXCollections.observableArrayList(lista);
    }
    //Método para obtener y asignarle las columnas de la entidad Platos a la TableView
    public ObservableList<Plato> getPlato(){
        ArrayList<Plato> lista = new ArrayList<>();
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
    //Método para obtener el plato y mostrarlo en el ComboBox
    public Plato buscarPlato(int codigoPlato){
        Plato resultado = null;
            try{
                PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarPlato(?)}");
                sp.setInt(1, codigoPlato);
                ResultSet registro = sp.executeQuery();
                    while(registro.next()){
                        resultado = new Plato(registro.getInt("codigoPlato"),
                                              registro.getInt("cantidad"),
                                              registro.getString("nombrePlato"),
                                              registro.getString("descripcionPlato"),
                                              registro.getDouble("precioPlato"),
                                              registro.getInt("codigoTipoPlato"));
                    }
            }catch(Exception e){
                e.printStackTrace();
            }
        return resultado;
    }
    //Método para obtener y asignarle las columnas de la entidad Productos a la TableView
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
        return listaProducto = FXCollections.observableArrayList(lista);
    }
    //Método para buscar por codigoProducto y Mostrarlo en el ComboBox
    public Producto buscarProducto (int codigoProducto){
        Producto  resultado = null;
            try{
                PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarProducto(?)");
                sp.setInt(1, codigoProducto);
                ResultSet registro = sp.executeQuery();
                    while(registro.next()){
                        resultado = new Producto(registro.getInt("codigoProducto"),
                                                 registro.getString("nombreProducto"),
                                                 registro.getInt("cantidad"));
                    }
            }catch(Exception e){
                e.printStackTrace();
            }
        return resultado;
    }
}
            
