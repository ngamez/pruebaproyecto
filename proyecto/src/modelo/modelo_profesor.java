/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import vista.vista_profesor;

/**
 *
 * @author USER
 */
public class modelo_profesor {
    
    conexion con=new conexion();
    Connection cn = con.conexion();
    public void profesor(vista_profesor pro,String cedula){
        try {
           Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery("select nombre,apellido from usuarios where cedula='"+cedula+"'");
            while(rs.next()){
                pro.jLabel3.setText(rs.getString(1));
                pro.jLabel4.setText(rs.getString(2));
                pro.jLabel8.setText(cedula);
            }
        } catch (Exception e) {
        }
           
    }
    
}
