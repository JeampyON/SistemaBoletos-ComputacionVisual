/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoboletos;

/**
 *
 * @author Jean
 */
public class Evento {
    private int idEvento;
    private String nombre;
    private String fecha;
    private double precio;
    private int aforoMaximo;
    private int boletosVendidos;

    public Evento() {}

    public Evento(int idEvento, String nombre, String fecha, double precio, int aforoMaximo, int boletosVendidos) {
        this.idEvento = idEvento;
        this.nombre = nombre;
        this.fecha = fecha;
        this.precio = precio;
        this.aforoMaximo = aforoMaximo;
        this.boletosVendidos = boletosVendidos;
    }

    public int getIdEvento() { return idEvento; }
    public void setIdEvento(int idEvento) { this.idEvento = idEvento; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    public int getAforoMaximo() { return aforoMaximo; }
    public void setAforoMaximo(int aforoMaximo) { this.aforoMaximo = aforoMaximo; }
    public int getBoletosVendidos() { return boletosVendidos; }
    public void setBoletosVendidos(int boletosVendidos) { this.boletosVendidos = boletosVendidos; }

    // Este método es clave: hace que al meter el objeto en un ComboBox, se vea el nombre directamente
    @Override
    public String toString() {
        return idEvento + " - " + nombre;
    }
}
