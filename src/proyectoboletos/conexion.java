/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoboletos;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;
/**
 *
 * @author Jean
 */
public class conexion {
    Connection conectar = null;
    
    public Connection conectar() {
        try {
            // Driver nativo de PostgreSQL
            Class.forName("org.postgresql.Driver");
            // URL de conexión (localhost, puerto 5432, base de datos bd_boletos)
            conectar = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bd_boletos", "postgres", "1742");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error de conexión a PostgreSQL: " + e.getMessage());
        }
        return conectar;
    }
}
