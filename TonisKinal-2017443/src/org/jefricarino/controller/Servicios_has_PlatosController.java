/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import org.jefricarino.bean.Servicio_has_Plato;
import org.jefricarino.bean.Servicios;
import org.jefricarino.db.Conexion;
import org.jefricarino.system.Principal;

/**
 *
 * @author Carino
 */
public class Servicios_has_PlatosController implements Initializable {
    private Principal escenarioPrincipal;
    private enum Operaciones {NUEVO,GUARDAR,ELIMINAR,NINGUNO}
    private Operaciones tipoDeOperacion = Operaciones.NINGUNO;
    private ObservableList<Servicios> listaServicio;
    private ObservableList<Platos> listaPlatos; 
    private ObservableList<Servicio_has_Plato> listaServiPlato;
    
    @FXML private ComboBox cmbCodigoServicio;
    @FXML private ComboBox cmbCodigoPlatos;
    @FXML private TableView tblServiPlatos;
    @FXML private TableColumn colCodigoServicio;
    @FXML private TableColumn colCodigoPlato;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnServicios;
    @FXML private Button btnPlatos;
            
    @Override
    public void initialize(URL location, ResourceBundle resources){
        cargarDatos();
        cmbCodigoServicio.setItems(getServicio());
        cmbCodigoPlatos.setItems(getPlato());
    }
    
    public void cargarDatos(){
        tblServiPlatos.setItems(getServiPlato());
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory<Servicio_has_Plato,Integer>("codigoServicio"));
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory<Servicio_has_Plato,Integer>("codigoPlato"));
    }
    
    public void seleccionarElemento(){
        if(tblServiPlatos.getSelectionModel().getSelectedItem() !=null){
            cmbCodigoServicio.getSelectionModel().select(buscarServicio(((Servicio_has_Plato)tblServiPlatos.getSelectionModel().getSelectedItem()).getCodigoServicio()));
            cmbCodigoPlatos.getSelectionModel().select(buscarPlato(((Servicio_has_Plato)tblServiPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato()));
        }
    }
    
    public ObservableList getServiPlato(){
        ArrayList<Servicio_has_Plato> lista = new ArrayList<Servicio_has_Plato>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarServicios_has_Platos}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Servicio_has_Plato(resultado.getInt("codigoServicio"),
                                                 resultado.getInt("codigoPlato")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaServiPlato = FXCollections.observableArrayList(lista);
    }
    
    
    public ObservableList<Servicios> getServicio(){
        ArrayList<Servicios> lista = new ArrayList<Servicios>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarServicios}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Servicios(resultado.getInt("codigoServicio"),
                                        resultado.getDate("fechaServicio"),
                                        resultado.getString("tipoServicio"),
                                        resultado.getString("horaServicio"),
                                        resultado.getString("lugarServicio"),
                                        resultado.getString("telefonoContacto"),
                                        resultado.getInt("codigoEmpresa")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaServicio = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<Platos>getPlato(){
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
        
        return listaPlatos = FXCollections.observableArrayList(lista);
    }
    
    private boolean validacion(ComboBox codServicio, ComboBox codPlato){
        if(codServicio.getSelectionModel().getSelectedItem() !=null && codPlato.getSelectionModel().getSelectedItem() !=null) {
            return true;
        }else{
            Alert a = new Alert(AlertType.WARNING);
            a.setTitle("Campos del ComboBox");
            a.setHeaderText(null);
            a.setContentText("Debe eligir datos en ambos campos del ComboBox");
            a.showAndWait();
        }
            return false;
    }
    
    public Servicios buscarServicio(int codigoServicio){
        Servicios lista = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarServicios(?)}");
            procedimiento.setInt(1, codigoServicio);
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista = new Servicios(resultado.getInt("codigoServicio"),
                                      resultado.getDate("fechaServicio"),
                                      resultado.getString("tipoServicio"),
                                      resultado.getString("horaServicio"),
                                      resultado.getString("lugarServicio"),
                                      resultado.getString("telefonoContacto"),
                                      resultado.getInt("codigoEmpresa")
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
    
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                tipoDeOperacion = Operaciones.GUARDAR;
                break;
            case GUARDAR:
                if(validacion(cmbCodigoServicio,cmbCodigoPlatos)){
                    guardar();
                    desactivarControles();
                    btnNuevo.setText("Nuevo");
                    btnEliminar.setText("Eliminar");
                    tipoDeOperacion = Operaciones.NINGUNO;
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
                break;
            case NINGUNO:
                if(tblServiPlatos.getSelectionModel().getSelectedItem() !=null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar este registro?","Eliminar Servicios_has_Platos", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarServicios_has_Platos(?)}");
                            procedimiento.setInt(1, ((Servicio_has_Plato)tblServiPlatos.getSelectionModel().getSelectedItem()).getCodigoServicio());
                            procedimiento.execute();
                            listaServiPlato.remove(tblServiPlatos.getSelectionModel().getFocusedIndex());
                            desactivarControles();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Debe Seleccionar un elemento");
                }
        }
    }
    
    public void guardar(){
        Servicio_has_Plato registro = new Servicio_has_Plato();
        registro.setCodigoServicio(((Servicios)cmbCodigoServicio.getSelectionModel().getSelectedItem()).getCodigoServicio());
        registro.setCodigoPlato(((Platos)cmbCodigoPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_Servicios_has_platos(?,?)}");
            procedimiento.setInt(1, registro.getCodigoServicio());
            procedimiento.setInt(2, registro.getCodigoPlato());
            procedimiento.execute();
            listaServiPlato.add(registro);
        }catch(MySQLIntegrityConstraintViolationException e){
            JOptionPane.showMessageDialog(null, "Este registro ya existe");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void activarControles(){
        cmbCodigoServicio.setDisable(false);
        cmbCodigoPlatos.setDisable(false);
    }
    
    public void desactivarControles(){
        cmbCodigoServicio.setDisable(true);
        cmbCodigoPlatos.setDisable(true);
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaServicio(){
        escenarioPrincipal.ventanaServicios();
    }
    
    public void ventanaPlato(){
        escenarioPrincipal.ventanaPlatos();
    }
        
}
