
package modelo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;


public class modelo_login {
    conexion con=new conexion();
    Connection cn = con.conexion();
    public String ver(String us, String cont) {
        
        String consultado = "";
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM login WHERE usuario ='" + us + "' and "
                    + "contrasena='"+cont+"'");
            while (rs.next()) {
               consultado = rs.getString(3);
            }
            return consultado;
        } catch (SQLException e) {
            Logger.getLogger(modelo_login.class.getName()).log(Level.SEVERE, null, e);
        }
        return consultado;
    }
    
    public String get_cedula(String us){
        String ced="";
        try {
            
             Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery("select cedula from login where usuario='"+us+"'");
            while(rs.next()){
                ced=rs.getString(1);
            }
            return ced;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,e);
            return ced;
        }
        
    }
    
}
