
package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import modelo.modelo_profesor;
import vista.vista_login;
import vista.vista_profesor;

public class controlador_profesor implements ActionListener{

    String cedula;
    vista_profesor profesor;
    public controlador_profesor(String c,vista_profesor p){
        profesor=p;
        cedula=c;
        modelo.modelo_profesor pr=new modelo_profesor();
        pr.profesor(profesor, cedula);
        profesor.jButton5.addActionListener(this);
    }
   
    
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==profesor.jButton5){
            profesor.hide();
            vista_login log=new vista_login();
            controlador_login l=new controlador_login(log);
            log.setLocationRelativeTo(null);
            log.show();
                   
            
        }
    }

    
    
    
    
}
