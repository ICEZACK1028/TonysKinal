package org.davidgarcia.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
import org.davidgarcia.system.MainApp;
import org.davidgarcia.bean.Empresa;
import org.davidgarcia.bd.Conexion;
import org.davidgarcia.report.GenerarReporte;

public class EmpresaController implements Initializable{
    private enum Operacion{GUARDAR, ACTUALIZAR, NINGUNO}
    private Operacion tipoOperacion = Operacion.NINGUNO;// ponemos default ninguno 
    private MainApp escenarioPrincipal;
    private ObservableList <Empresa> listaEmpresa;
    
    @FXML private Button btnNuevo;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private Button btnEliminar;
    @FXML private TextField txtCodigoEmpresa;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtNombreEmpresa;
    @FXML private TextField txtTelefono;
    @FXML private TableView tblEmpresa;
    @FXML private TableColumn colCodigoEmpresa;
    @FXML private TableColumn colNombreEmpresa;
    @FXML private TableColumn colDireccion;
    @FXML private TableColumn colTelefono;
    @FXML private ImageView imgPresupuesto;
    @FXML private ImageView imgServicio;
    
    public void insertarImagen(){
        imgPresupuesto.setImage(new Image("/org/davidgarcia/img/presupuestoBrillo.png"));
    }
    public void devolverImagen(){
        imgPresupuesto.setImage(new Image("/org/davidgarcia/img/presupuesto.png"));
    }
    public void insertarImagen2(){
        imgServicio.setImage(new Image("/org/davidgarcia/img/ManoServicioBrillo.png"));
    }
    public void devolverImagen2(){
        imgServicio.setImage(new Image("/org/davidgarcia/img/ManoServicio.png"));
    }
    
@Override
public void initialize(URL location, ResourceBundle resources) {
   cargarDatos();
}
    
public MainApp getEscenarioPrincipal() {
    return escenarioPrincipal;
}

public void setEscenarioPrincipal(MainApp escenarioPrincipal) {
    this.escenarioPrincipal = escenarioPrincipal;
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
//Método para la vista de Presupuesto...
public void presupuesto(){
    if(tipoOperacion == Operacion.NINGUNO){
            escenarioPrincipal.Presupuesto();
        }else{
            int respuesta = JOptionPane.showConfirmDialog(null, "   ¿Está seguro que quiere ingresar?\nSi ingresa perderá los datos ingresados","Aviso:",
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(respuesta == JOptionPane.OK_OPTION){
                escenarioPrincipal.Presupuesto();
            }
        }
}
//Método para la vista de Servicio...
public void servicio(){
      if(tipoOperacion == Operacion.NINGUNO){
            escenarioPrincipal.Servicio();
        }else{
            int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro que quiere ingresar?\nSi ingresa perderá los datos ingresados","Aviso:",
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(respuesta == JOptionPane.OK_OPTION){
                escenarioPrincipal.Servicio();
            }
        }
}
//Método para regresar a módulos...
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
//Método para cargar los datos a la vista
public void cargarDatos(){
  tblEmpresa.setItems(getEmpresa());
  colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, Integer>("codigoEmpresa"));
  colNombreEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, String>("nombreEmpresa"));
  colDireccion.setCellValueFactory(new PropertyValueFactory<Empresa, String>("direccion"));
  colTelefono.setCellValueFactory(new PropertyValueFactory<Empresa, String>("telefono"));
}
    
//Método para ejecutar el procedimiento almacenado y llenar una lista del resultado
public ObservableList<Empresa> getEmpresa(){
ArrayList<Empresa> lista = new ArrayList<Empresa>();
    try{
        PreparedStatement procedimiento= Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEmpresa}");
        ResultSet resultado= procedimiento.executeQuery();
            while(resultado.next()){
            lista.add(new Empresa(resultado.getInt("codigoEmpresa"), 
                    resultado.getString("nombreEmpresa"),
                    resultado.getString("direccion"),
                    resultado.getString("telefono")));
            }
    }catch(Exception e){
        e.printStackTrace();
    }
    return listaEmpresa= FXCollections.observableArrayList(lista);
}
//Método para seleccionar elementos
public void seleccionarElemento(){
    if(tipoOperacion == Operacion.NINGUNO){
        if(tblEmpresa.getSelectionModel().getSelectedItem()!=null){
            txtCodigoEmpresa.setText(String.valueOf(((Empresa)tblEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
            txtNombreEmpresa.setText(((Empresa)tblEmpresa.getSelectionModel().getSelectedItem()).getNombreEmpresa());
            txtDireccion.setText(((Empresa)tblEmpresa.getSelectionModel().getSelectedItem()).getDireccion());
            txtTelefono.setText(((Empresa)tblEmpresa.getSelectionModel().getSelectedItem()).getTelefono());
        }
    }
}
//Método para activar los controles
public void activarControles(){
    txtNombreEmpresa.setEditable(true);
    txtDireccion.setEditable(true);
    txtTelefono.setEditable(true);        
}

//Método para desactivar los Controles
public void desactivarControles(){
    txtCodigoEmpresa.setEditable(false);
    txtNombreEmpresa.setEditable(false);
    txtDireccion.setEditable(false);
    txtTelefono.setEditable(false);      
}

//Método para limpiar los controles
public void limpiarControles(){
    txtCodigoEmpresa.setText(null);
    txtNombreEmpresa.setText(null);
    txtDireccion.setText(null);
    txtTelefono.setText(null);
    tblEmpresa.getSelectionModel().clearSelection();
}

//Método para el botón nuevo 
public void nuevo(){
    switch(tipoOperacion){
        case NINGUNO:
            activarControles();
            limpiarControles();
            btnEliminar.setText("Cancelar");
            btnNuevo.setText("Guardar");
            btnEditar.setVisible(false);
            btnReporte.setVisible(false);
            txtCodigoEmpresa.setDisable(true);
            tblEmpresa.getSelectionModel().clearSelection();
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
                txtCodigoEmpresa.setDisable(false);
                tipoOperacion = Operacion.NINGUNO;
                //cargar los nuevos datos en la tabla
                cargarDatos();
            }else{
                JOptionPane.showMessageDialog(null,"Debe llenar todos los campos");
            }
        break;
    }
}

//Método para el botón Eliminar y Cancelar
public void eliminar(){
    switch (tipoOperacion){
        case GUARDAR:
            desactivarControles();
            limpiarControles();
            btnNuevo.setText("Nuevo");
            btnEliminar.setText("Eliminar");
            btnEditar.setVisible(true);
            btnReporte.setVisible(true);
            txtCodigoEmpresa.setDisable(false);
            tipoOperacion = Operacion.NINGUNO;
        break;
        default:
            //Verificar que tenga seleccionado un registro de la tabla
            if(tblEmpresa.getSelectionModel().getSelectedItem() != null){
            int respuesta = JOptionPane.showConfirmDialog(null,"¿Esta seguro que quiere eliminar el registro?","Eliminar Empresa",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                if (respuesta == JOptionPane.YES_OPTION){
                    try {
                        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarEmpresa(?)}");
                        procedimiento.setInt(1, ((Empresa)tblEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
                        procedimiento.execute();
                        if(procedimiento.execute()== false){
                        listaEmpresa.remove(tblEmpresa.getSelectionModel().getSelectedIndex());
                        limpiarControles();
                        JOptionPane.showMessageDialog(null, "Registro Eliminado");
                        }
                    }catch(Exception e){
                        JOptionPane.showMessageDialog(null," No se puede eliminar el registro porque está\n ligado a registro(s) de Servicios/Presupuestos","Nota:",JOptionPane.CLOSED_OPTION);
                        e.printStackTrace();
                    }
                }
            }else{
                JOptionPane.showMessageDialog(null, "Debe seleccionar un registro de la tabla");
            }
    }
}

//Metodo para el botón Editar y Actualizar
public void editar(){
    switch(tipoOperacion){
        case NINGUNO:
            if(tblEmpresa.getSelectionModel().getSelectedItem()!=null){
            btnEditar.setText("Actualizar");
            btnReporte.setText("Cancelar");
            btnNuevo.setVisible(false);
            btnEliminar.setVisible(false);
            txtCodigoEmpresa.setDisable(true);
            activarControles();
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
            txtCodigoEmpresa.setDisable(false);
            tipoOperacion = Operacion.NINGUNO;
            cargarDatos();
        break;
    }
}

//Método para guardar los nuevos registros en la BD y en la TableView
public void guardar(){
    Empresa registro = new Empresa();
    registro.setNombreEmpresa(txtNombreEmpresa.getText());// ya lo obtuve y lo tengo listo para enviar
    registro.setDireccion(txtDireccion.getText());
    registro.setTelefono(txtTelefono.getText());
    //Puedo hacer la llamada ql proceso
    try{
        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarEmpresa(?,?,?)}");
        procedimiento.setString(1,registro.getNombreEmpresa());
        procedimiento.setString(2,registro.getDireccion());
        procedimiento.setString(3,registro.getTelefono());
        procedimiento.execute();
        ///Va ahora que ya tenemos esto hay que agregarlo a nuestra lista como la que esta arriba
        listaEmpresa.add(registro);
    }catch(Exception e){
        e.printStackTrace();
    }
}

//Método para actualizar los registros en la BD y en la TableView
public void actualizar(){
    try{
        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarEmpresa(?,?,?,?)}");
        Empresa empresaActualizada = ((Empresa)tblEmpresa.getSelectionModel().getSelectedItem());
        //Obteniendo los datos de la vista al modelo en java
        empresaActualizada.setNombreEmpresa(txtNombreEmpresa.getText());
        empresaActualizada.setDireccion(txtDireccion.getText());
        empresaActualizada.setTelefono(txtTelefono.getText());
        //Enviando los datos actualizados a ejecutar en el objeto procedimiento
        procedimiento.setInt(1,empresaActualizada.getCodigoEmpresa());
        procedimiento.setString(2,empresaActualizada.getNombreEmpresa());
        procedimiento.setString(3,empresaActualizada.getDireccion());
        procedimiento.setString(4,empresaActualizada.getTelefono());
        procedimiento.execute();
        JOptionPane.showMessageDialog(null, "Datos Actualizados");
        listaEmpresa.add(empresaActualizada);
    }catch(Exception e){
        e.printStackTrace();
    }
}

//Método para el botón de reporte y cancelar lo editado
public void reporte(){
    switch(tipoOperacion){
        case ACTUALIZAR:
            desactivarControles();
            btnEditar.setText("Editar");
            btnReporte.setText("Reporte");
            btnNuevo.setVisible(true);
            btnEliminar.setVisible(true);
            txtCodigoEmpresa.setDisable(false);
            tipoOperacion = Operacion.NINGUNO;
            break;
        case NINGUNO:
            imprimirReporte();
            break;
    }
}

//Método para enviar los parámetros para generar reporte
public void imprimirReporte(){
    Map parametros = new HashMap();
    parametros.put("codigoEmpresa", null);
    GenerarReporte.mostrarReporte("reportesEmpresas.jasper", "Reporte de Empresas", parametros);
}
//Método para no dejar campos vacíos
public boolean CamposVacios(){
    boolean validacion = false;
    if(txtDireccion.getText()!=null && txtNombreEmpresa.getText()!=null && txtTelefono.getText()!=null){
        validacion = true;
    }else{
    }
    return validacion;
}
}
