
package org.jefricarino.controller;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.jefricarino.bean.Platos;
import org.jefricarino.bean.Productos;
import org.jefricarino.bean.Productos_has_Platos;
import org.jefricarino.bean.Servicios;
import org.jefricarino.db.Conexion;
import org.jefricarino.system.Principal;


public class Productos_has_PlatosController implements Initializable{
    private Principal escenarioPrincipal;
    private enum Operaciones {NUEVO,GUARDAR,ELIMINAR,NINGUNO}
    private Operaciones tipoDeOperacion = Operaciones.NINGUNO;
    private ObservableList<Productos_has_Platos> listaProduPlato;
    private ObservableList<Platos> listaPlato;
    private ObservableList<Productos> listaProducto;
    
    @FXML private ComboBox cmbCodigoProducto;
    @FXML private ComboBox cmbCodigoPlato;
    @FXML private TableView tblProduPlatos;
    @FXML private TableColumn colCodigoProducto;
    @FXML private TableColumn colCodigoPlato;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnProductos;
    @FXML private Button btnPlatos;
   
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoProducto.setItems(getProducto());
        cmbCodigoPlato.setItems(getPlato());
    }
    
    public void cargarDatos(){
        tblProduPlatos.setItems(getProduPlatos());
        colCodigoProducto.setCellValueFactory(new PropertyValueFactory<Productos_has_Platos,Integer>("Productos_codigoProducto"));
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory<Productos_has_Platos,Integer>("Platos_codigoPlato"));
    }
    
    public void seleccionarElemento(){
        if(tblProduPlatos.getSelectionModel().getSelectedItem() !=null){
            cmbCodigoProducto.getSelectionModel().select(buscarProducto(((Productos_has_Platos)tblProduPlatos.getSelectionModel().getSelectedItem()).getProductos_codigoProducto()));
            cmbCodigoPlato.getSelectionModel().select(buscarPlato(((Productos_has_Platos)tblProduPlatos.getSelectionModel().getSelectedItem()).getPlatos_codigoPlato()));
        }
    }
    
    public ObservableList<Productos_has_Platos> getProduPlatos(){
        ArrayList<Productos_has_Platos> lista = new ArrayList<Productos_has_Platos>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProductos_has_Platos}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Productos_has_Platos(resultado.getInt("Productos_codigoProducto"),
                                                   resultado.getInt("Platos_codigoPlato")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaProduPlato = FXCollections.observableArrayList(lista);
    } 
    
    public ObservableList<Productos> getProducto(){
        ArrayList<Productos> lista = new ArrayList<Productos>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProductos}");
            ResultSet resultado = procedimiento.executeQuery();
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
    
    public ObservableList<Platos> getPlato(){
        ArrayList<Platos> lista = new ArrayList<Platos>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarPlatos}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Platos(resultado.getInt("codigoPlato"),
                                     resultado.getInt("cantidad"),
                                     resultado.getString("nombrePlato"),
                                     resultado.getString("descripcionPlato"),
                                     resultado.getDouble("precioPlato"),
                                     resultado.getInt("codigoTipoPlato")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaPlato = FXCollections.observableArrayList(lista);
    }
    
    public Productos buscarProducto(int codigoProducto){
        Productos lista = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarProductos(?)}");
            procedimiento.setInt(1, codigoProducto);
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista = new Productos(resultado.getInt("codigoProducto"),
                                      resultado.getString("nombreProducto"),
                                      resultado.getInt("cantidad")
                );
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return lista;
    }
    
    public Platos buscarPlato(int codigoPlato){
        Platos lista = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarPlatos(?)}");
            procedimiento.setInt(1, codigoPlato);
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista = new Platos(resultado.getInt("codigoPlato"),
                                   resultado.getInt("cantidad"),
                                   resultado.getString("nombrePlato"),
                                   resultado.getString("descripcionPlato"),
                                   resultado.getDouble("precioPlato"),
                                   resultado.getInt("codigoTipoPlato")         
                );
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return lista;
    }
    
    public boolean validarCampos(ComboBox codProducto, ComboBox codPlato){
        if(codProducto.getSelectionModel().getSelectedItem() !=null && codPlato.getSelectionModel().getSelectedItem() !=null){
            return true;
        }else{
            Alert a = new Alert(AlertType.WARNING);
            a.setTitle("Validacion de campos");
            a.setHeaderText(null);
            a.setContentText("Debe de seleccionar datos");
            a.showAndWait();
        }
            return false;
    }
    
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                tipoDeOperacion = Operaciones.GUARDAR;
                break;
            case GUARDAR:
                if(validarCampos(cmbCodigoProducto,cmbCodigoPlato)){
                    guardar();
                    desactivarControles();
                    btnNuevo.setText("Nuevo");
                    btnEliminar.setText("Eliminar");
                    tipoDeOperacion = Operaciones.NINGUNO;
                    cargarDatos();
                    break;
                }
        }
    }
    
    public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                tipoDeOperacion = Operaciones.NINGUNO;
                cargarDatos();
                break;
            case NINGUNO:
                if(tblProduPlatos.getSelectionModel().getSelectedItem() !=null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el registro?","Elimnar Productos_has_Platos",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarProductos_has_Platos(?)}");
                            procedimiento.setInt(1, ((Productos_has_Platos)tblProduPlatos.getSelectionModel().getSelectedItem()).getProductos_codigoProducto());
                            procedimiento.execute();
                            listaProduPlato.remove(tblProduPlatos.getSelectionModel().getFocusedIndex());
                            desactivarControles();
                            cargarDatos();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un registro");
                }                     
        }
    }
    
    public void guardar(){
        Productos_has_Platos registro = new Productos_has_Platos();
        registro.setProductos_codigoProducto(((Productos)cmbCodigoProducto.getSelectionModel().getSelectedItem()).getCodigoProducto());
        registro.setPlatos_codigoPlato(((Platos)cmbCodigoPlato.getSelectionModel().getSelectedItem()).getCodigoPlato());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarProductos_has_Platos(?,?)}");
            procedimiento.setInt(1, registro.getProductos_codigoProducto());
            procedimiento.setInt(2, registro.getPlatos_codigoPlato());
            procedimiento.execute();
            listaProduPlato.add(registro);
        }catch(MySQLIntegrityConstraintViolationException e){
            Alert a = new Alert(AlertType.WARNING);
            a.setTitle("Llaves Primarias ");
            a.setHeaderText(null);
            a.setContentText("Este registro ya existe");
            a.showAndWait();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void activarControles(){
        cmbCodigoProducto.setDisable(false);
        cmbCodigoPlato.setDisable(false);
    }
    
    public void desactivarControles(){
        cmbCodigoProducto.setDisable(true);
        cmbCodigoPlato.setDisable(true);
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaProductos(){
        escenarioPrincipal.ventanaProductos();
    }
    
    public void ventanaPlatos(){
        escenarioPrincipal.ventanaPlatos();
    }
}
