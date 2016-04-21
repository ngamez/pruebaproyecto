
package proyecto;

import controlador.controlador_login;
import java.sql.Connection;
import modelo.conexion;
import vista.vista_login;

/**
 *
 * @author USER
 */
public class Proyecto {

  conexion con=new conexion();
    Connection cn = con.conexion();
    public static void main(String[] args) {
       
        vista_login v=new vista_login();
        controlador_login log=new controlador_login(v);
        v.setLocationRelativeTo(null);
        v.show();
    }
    
}

//hello mundo