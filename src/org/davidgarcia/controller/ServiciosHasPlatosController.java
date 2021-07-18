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
import org.davidgarcia.bd.Conexion;
import org.davidgarcia.system.MainApp;
import org.davidgarcia.bean.Servicio;
import org.davidgarcia.bean.Plato;
import org.davidgarcia.bean.ServiciosHasPlatos;

public class ServiciosHasPlatosController implements Initializable {
    private MainApp escenarioPrincipal;
    
    private ObservableList<Servicio> listaServicio;
    private ObservableList<Plato> listaPlato;
    private ObservableList<ServiciosHasPlatos> listaSHP;
    
    @FXML private ComboBox cmbCodigoServicio;
    @FXML private ComboBox cmbCodigoPlato;
    @FXML private TableColumn colCodigoServicio;
    @FXML private TableColumn colCodigoPlato;
    @FXML private TableView tblServiciosHasPlatos;
    
    @Override
    public void initialize(URL location, ResourceBundle resources){
        cargarDatos();
        cmbCodigoPlato.setItems(getPlato());
        cmbCodigoServicio.setItems(getServicio());
    }
    //Método para Menu Principal
    public MainApp getEscenarioPrincipal(){
        return escenarioPrincipal;
    }
    public void setEscenarioPrincipal(MainApp escenarioPrincipal){
        this.escenarioPrincipal = escenarioPrincipal;
    }
    //Método para regresar al menú de Entidades De Unión
    public void entidadesDeUnion(){
        escenarioPrincipal.EntidadesDeUnion();
    }
    //Método para retornar al menú principal
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    //Método para mostrar los datos en el TableView 
    public void cargarDatos(){
        tblServiciosHasPlatos.setItems(getServiciosHasPlatos());
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory<ServiciosHasPlatos, Integer>("Servicios_codigoServicio"));
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory<ServiciosHasPlatos, Integer>("Platos_codigoPlato"));
    }
    //Método para seleccionar datos y mostrarlos en los comboBox
    public void seleccionarDatos(){
        if(tblServiciosHasPlatos.getSelectionModel().getSelectedItem()!=null){
            cmbCodigoPlato.getSelectionModel().select(buscarPlato(((ServiciosHasPlatos)tblServiciosHasPlatos.getSelectionModel().getSelectedItem()).getPlatos_codigoPlato()));
            cmbCodigoServicio.getSelectionModel().select(buscarServicio(((ServiciosHasPlatos)tblServiciosHasPlatos.getSelectionModel().getSelectedItem()).getServicios_codigoServicio()));
        }
    }
    //Método para llamar y mostrar las columnas y registros de servicios has platos
    public ObservableList<ServiciosHasPlatos> getServiciosHasPlatos(){
        ArrayList<ServiciosHasPlatos> lista = new ArrayList<ServiciosHasPlatos>();
            try{
                PreparedStatement sp  = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarServicios_has_Plato()}");
                ResultSet resultado = sp.executeQuery();
                    while(resultado.next()){
                        lista.add(new ServiciosHasPlatos (resultado.getInt("codigoPlato"),
                                                          resultado.getInt("codigoServicio")));
                    }
            }catch(Exception e){
                e.printStackTrace();
            }
        return listaSHP = FXCollections.observableArrayList(lista);
    }
    
    //Método para llamar y mostrar los registros de servicios
    public ObservableList<Servicio> getServicio(){
        ArrayList<Servicio> lista = new ArrayList<>();
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
    //Método para buscar el servicio y obtener el texto para el comboBox de Servicio
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
    //Método para llamar y mostrar los registros de platos
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
    //Método para buscar el plato y obtener el texto para el mostrarlo en el comboBox de Plato
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
    
}
