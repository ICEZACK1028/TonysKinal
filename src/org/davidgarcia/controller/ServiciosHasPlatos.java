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
import org.davidgarcia.bd.Conexion;
import org.davidgarcia.system.MainApp;
import org.davidgarcia.bean.Servicio;
import org.davidgarcia.bean.Plato;

public class ServiciosHasPlatos implements Initializable {
    private MainApp escenarioPrincipal;
    
    private ObservableList<Servicio> listaServicio;
    private ObservableList<Plato> listaPlato;
    
    @FXML private ComboBox cmbCodigoServicio;
    @FXML private ComboBox cmbCodigoPlato;
    @FXML private TableColumn colCodigoServicio;
    @FXML private TableColumn colCodigoPlato;
    @FXML private TableView tblServiciosHasPlatos;
    
    //Método para Menu Principal
    public MainApp getEscenarioPrincipal(){
        return escenarioPrincipal;
    }
    public void setEscenarioPrincipal(MainApp escenarioPrincipal){
        this.escenarioPrincipal = escenarioPrincipal;
    }
    //Método para llamar y mostrar los registros de servicios
    public ObservableList<Servicio> getServicio(){
        ArrayList<Servicio> lista = new ArrayList<>();
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
    //Método para llamar y mostrar los registros de platos
    public ObservableList<Plato> getPlato(){
        ArrayList<Plato> lista = new ArrayList<>();
        try{
            PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarPlato}");
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
    
    @Override
    public void initialize(URL location, ResourceBundle resources){
    }
    
}
