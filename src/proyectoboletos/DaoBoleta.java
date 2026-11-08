/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoboletos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author Jean
 */
public class DaoBoleta {
    conexion cc = new conexion();

    // 1. Cargar eventos de la BD para mostrarlos en la Taquilla
    public ArrayList<Evento> obtenerEventos() {
        ArrayList<Evento> lista = new ArrayList<>();
        String sql = "SELECT * FROM evento";
        try (Connection cn = cc.conectar();
             PreparedStatement pst = cn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                Evento ev = new Evento(
                    rs.getInt("id_evento"),
                    rs.getString("nombre"),
                    rs.getString("fecha"),
                    rs.getDouble("precio"),
                    rs.getInt("aforo_maximo"),
                    rs.getInt("boletos_vendidos")
                );
                lista.add(ev);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cargar eventos: " + e.getMessage());
        }
        return lista;
    }

    // 2. Registrar la venta validando que no pase del aforo máximo
    public boolean venderBoleta(int idEvento, String codigoUnico) {
        try (Connection cn = cc.conectar()) {
            // Revisar aforo actual
            String sqlCupo = "SELECT aforo_maximo, boletos_vendidos FROM evento WHERE id_evento = ?";
            PreparedStatement pstCupo = cn.prepareStatement(sqlCupo);
            pstCupo.setInt(1, idEvento);
            ResultSet rs = pstCupo.executeQuery();

            if (rs.next()) {
                int maximo = rs.getInt("aforo_maximo");
                int vendidos = rs.getInt("boletos_vendidos");
                if (vendidos >= maximo) {
                    JOptionPane.showMessageDialog(null, "¡Aforo lleno! No se pueden vender más boletos.");
                    return false;
                }
            }

            // Insertar boleto
            String sqlInsert = "INSERT INTO boleta (id_evento, codigo_unico, estado) VALUES (?, ?, 'DISPONIBLE')";
            PreparedStatement pstIns = cn.prepareStatement(sqlInsert);
            pstIns.setInt(1, idEvento);
            pstIns.setString(2, codigoUnico);
            pstIns.executeUpdate();

            // Sumar 1 a los vendidos del evento
            String sqlUpdateEv = "UPDATE evento SET boletos_vendidos = boletos_vendidos + 1 WHERE id_evento = ?";
            PreparedStatement pstUpd = cn.prepareStatement(sqlUpdateEv);
            pstUpd.setInt(1, idEvento);
            pstUpd.executeUpdate();

            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al vender: " + e.getMessage());
            return false;
        }
    }

    // 3. Listar boletas vendidas para la tabla
    public DefaultTableModel listarBoletas() {
        String[] columnas = {"ID", "Evento", "Código de Barras", "Estado"};
        DefaultTableModel modelo = new DefaultTableModel(null, columnas);
        String sql = "SELECT b.id_boleta, e.nombre, b.codigo_unico, b.estado "
                   + "FROM boleta b INNER JOIN evento e ON b.id_evento = e.id_evento "
                   + "ORDER BY b.id_boleta DESC";
        try (Connection cn = cc.conectar();
             PreparedStatement pst = cn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                Object[] fila = new Object[4];
                fila[0] = rs.getInt("id_boleta");
                fila[1] = rs.getString("nombre");
                fila[2] = rs.getString("codigo_unico");
                fila[3] = rs.getString("estado");
                modelo.addRow(fila);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al listar: " + e.getMessage());
        }
        return modelo;
    }

    // 4. METODO CLAVE DEL INFORME: Validación atómica en la puerta
    public String validarEntradaEnPuerta(String codigoEscaneado) {
        String sqlAtomico = "UPDATE boleta SET estado = 'USADA' WHERE codigo_unico = ? AND estado = 'DISPONIBLE'";
        
        try (Connection cn = cc.conectar();
             PreparedStatement pst = cn.prepareStatement(sqlAtomico)) {
            
            pst.setString(1, codigoEscaneado);
            int filasAfectadas = pst.executeUpdate();

            if (filasAfectadas == 1) {
                return "APROBADO"; // Se cambió a USADA exitosamente
            } else {
                // Si no se actualizó, verificamos el porqué
                String sqlCheck = "SELECT estado FROM boleta WHERE codigo_unico = ?";
                PreparedStatement pstCheck = cn.prepareStatement(sqlCheck);
                pstCheck.setString(1, codigoEscaneado);
                ResultSet rs = pstCheck.executeQuery();

                if (rs.next()) {
                    return "DUPLICADO"; // Existe pero ya fue usado
                } else {
                    return "INEXISTENTE"; // El código es falso
                }
            }
        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }
}
