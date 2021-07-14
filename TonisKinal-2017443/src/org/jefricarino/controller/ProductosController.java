
package org.jefricarino.controller;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.jefricarino.bean.Productos;
import org.jefricarino.db.Conexion;
import org.jefricarino.report.GenerarReporte;
import org.jefricarino.system.Principal;


public class ProductosController implements Initializable {
    private enum operaciones{NUEVO,GUARDAR,ELIMINAR,EDITAR,ACTUALIZAR,CANCELAR,REPORTE,NINGUNO}
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Productos> listaProducto; 
    
    @FXML private TextField txtCodigoProducto;
    @FXML private TextField txtNombreProducto;
    @FXML private TextField txtCantidadProducto;
    @FXML private TableView tblProductos;
    @FXML private TableColumn colCodigoProducto;
    @FXML private TableColumn colNombreProducto;
    @FXML private TableColumn colCantidad;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private Button btnProduPlat;
    @FXML private Button btnMenuPrincipal;
    
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    cargarDatos();    
    }
    
    public void cargarDatos(){
    tblProductos.setItems(getProducto());
    colCodigoProducto.setCellValueFactory(new PropertyValueFactory<Productos,Integer>("codigoProducto"));
    colNombreProducto.setCellValueFactory(new PropertyValueFactory<Productos,String>("nombreProducto"));
    colCantidad.setCellValueFactory(new PropertyValueFactory<Productos,Integer>("cantidad"));
    }
    
    public ObservableList<Productos> getProducto(){
        ArrayList<Productos> lista = new ArrayList<Productos>(); 
        try{
            PreparedStatement procemiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProductos}");
            ResultSet resultado = procemiento.executeQuery();
            while(resultado.next()){
                lista.add(new Productos(resultado.getInt("codigoProducto"),
                                        resultado.getString("nombreProducto"),
                                        resultado.getInt("cantidad")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
            
        return listaProducto = FXCollections.observableArrayList(lista);
    }
    
    public void seleccionarElemento(){
        if(tblProductos.getSelectionModel().getSelectedItem() !=null){
        txtCodigoProducto.setText(String.valueOf(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto()));
        txtNombreProducto.setText(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getNombreProducto());
        txtCantidadProducto.setText(String.valueOf(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getCantidad()));
        
        }
    }
    
    private boolean validacion(TextField codProdu, TextField nombre, TextField cantidad){
        if(!codProdu.getText().equals("") && !nombre.getText().equals("") && !cantidad.getText().equals(""))
            return true;
        else{
            Alert a = new Alert(AlertType.WARNING);
            a.setTitle("Validacion de campos");
            a.setHeaderText(null);
            a.setContentText("Es necesario que todos los campos tengan datos");
            a.showAndWait();
        }
            return false;
    }
    
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                activarControles();
                limpiarControles();
                btnNuevo.setText("Guardar");                
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                cargarDatos();
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                if(validacion(txtCodigoProducto,txtNombreProducto,txtCantidadProducto)){
                guardar();
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                break;    
            }
        }        
    }
    
    public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                cargarDatos();
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            default:
                if(tblProductos.getSelectionModel().getSelectedItem() !=null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro que desea Eliminar el registro","Eliminar Producto",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION ){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarProductos(?)}");
                            procedimiento.setInt(1, ((Productos)tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto());
                            procedimiento.execute();
                            listaProducto.remove(tblProductos.getSelectionModel().getFocusedIndex());
                            limpiarControles();
                            desactivarControles();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }else{
                        JOptionPane.showMessageDialog(null, "Debe seleccionar un registro");
            }
        } 
    }
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblProductos.getSelectionModel().getSelectedItem() !=null){
                    activarControles();
                    txtCodigoProducto.setEditable(false);
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                    break;                     
                }else{
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar un elemento");
                }
            case ACTUALIZAR:
                if(tblProductos.getSelectionModel().getSelectedItem() !=null){
                desactivarControles();
                actualizar();
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                cargarDatos();
                tipoDeOperacion = operaciones.NINGUNO;
                break;    
            }                
        }
    }
    
    public void reporte(){
        switch(tipoDeOperacion){
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                cargarDatos();
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            case NINGUNO:
                cargarDatos();
                imprimirReporte();
                break;
        }
    }
    
    public void imprimirReporte(){
        Map parametros = new HashMap();
        parametros.put("codigoProducto", null);
        GenerarReporte.mostrarReporte("ReporteProductos.jasper", "Reporte de Producto", parametros);
    }
    
    public void guardar(){
        Productos registro = new Productos();
        registro.setCodigoProducto(Integer.parseInt(txtCodigoProducto.getText()));
        registro.setNombreProducto(txtNombreProducto.getText());
        registro.setCantidad(Integer.parseInt(txtCantidadProducto.getText()));
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarProductos(?,?,?)}");
            procedimiento.setInt(1, registro.getCodigoProducto());
            procedimiento.setString(2, registro.getNombreProducto());
            procedimiento.setInt(3, registro.getCantidad());
            procedimiento.execute();
            listaProducto.add(registro);
        }catch(MySQLIntegrityConstraintViolationException e){
            Alert a = new Alert(AlertType.WARNING);
            a.setTitle("Codigo Identificador");
            a.setHeaderText(null);
            a.setContentText("El codigo identificador ya existe, seleccione otro");
            a.showAndWait();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void actualizar(){
        Productos registro = (Productos)tblProductos.getSelectionModel().getSelectedItem();
//        registro.setCodigoProducto(Integer.parseInt(txtCodigoProducto.getText()));
          registro.setNombreProducto(txtNombreProducto.getText());
          registro.setCantidad(Integer.parseInt(txtCantidadProducto.getText()));
          try{
              PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarProductos(?,?,?)}");
              procedimiento.setInt(1, registro.getCodigoProducto());
              procedimiento.setString(2, registro.getNombreProducto());
              procedimiento.setInt(3, registro.getCantidad());
              procedimiento.execute();
          }catch(Exception e){
              e.printStackTrace();
          }
              
        
    }
    
    public void activarControles(){
        txtCodigoProducto.setEditable(true);
        txtNombreProducto.setEditable(true);
        txtCantidadProducto.setEditable(true);
    }
    
    public void desactivarControles(){
        txtCodigoProducto.setEditable(false);
        txtNombreProducto.setEditable(false);
        txtCantidadProducto.setEditable(false);
    }
    
    public void limpiarControles(){
        txtCodigoProducto.setText("");
        txtNombreProducto.setText("");
        txtCantidadProducto.setText("");
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void ventanaProductos_has_Platos(){
        escenarioPrincipal.ventanaProductos_has_Platos();
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
    
}
