/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;

public class modelo_registro {
conexion con=new conexion();
    Connection cn = con.conexion();
    public int registrar(String nom,String ape,String ced,String tel,String corr,String paip,String tipo,String usu,String pass){
     int cont=0;
        try{
           
        PreparedStatement pt=cn.prepareStatement("insert into usuarios(cedula,nombre,apellido,telefono,correo,pais_r) values(?,?,?,?,?,?)");
        pt.setString(1, ced);
        pt.setString(2, nom);
        pt.setString(3, ape);
        pt.setString(4, tel);
        pt.setString(5, corr);
        pt.setString(6, paip);
        pt.executeUpdate();
        cont++;
        
        PreparedStatement pt1=cn.prepareStatement("insert into login(usuario,contrasena,tipo,cedula) values (?,?,?,?)");
        pt1.setString(1, usu);
        pt1.setString(2, pass);
        pt1.setString(3, tipo);
        pt1.setString(4, ced);
        pt1.executeUpdate();
        cont++;
        
        if(tipo.equals("consejo")){
            PreparedStatement pt2=cn.prepareStatement("insert into consejo(cedula,descripcion) values (?,?)");
            pt2.setString(1, ced);
            pt2.setString(2, "Furuto Archivo");
            pt2.executeUpdate();
        }else if(tipo.equals("profesor")){
        PreparedStatement pt2=cn.prepareStatement("insert into profesores(cedula,descripcion) values (?,?)");
            pt2.setString(1, ced);
            pt2.setString(2, "Furuto Archivo");
            pt2.executeUpdate();
        }else if(tipo.equals("estudiante")){
        PreparedStatement pt2=cn.prepareStatement("insert into estudiantes(cedula,descripcion) values (?,?)");
            pt2.setString(1, ced);
            pt2.setString(2, "Furuto Archivo");
            pt2.executeUpdate();
        }
        
        cont++;
        return 0;
     }catch(Exception e){
         JOptionPane.showMessageDialog(null, e);
         if(cont==0){
             JOptionPane.showMessageDialog(null, "Ya se encuentra registrado");
         }else if(cont==1){
             JOptionPane.showMessageDialog(null, "Este usuario ya existe");
         }else{
             JOptionPane.showMessageDialog(null, "No mames");
         }
     delete_registro(ced);
     return 1;
     }
    }
    
    private void delete_registro(String ced){
        try {
            PreparedStatement pt=cn.prepareStatement("delete from usuarios where cedula='"+ced+"'");
            PreparedStatement pt1=cn.prepareStatement("delete from login where cedula='"+ced+"'");
            pt.executeUpdate();
            pt1.executeUpdate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    
}
