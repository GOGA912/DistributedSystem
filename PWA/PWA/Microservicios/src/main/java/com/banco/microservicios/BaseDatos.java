package com.banco.microservicios;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseDatos {
    private static final String BD = "jdbc:sqlite:Banco.db"; 


    public static boolean validarCuenta(String numero, int nip) {
        String sql = "SELECT * FROM cuentas WHERE numero = ? AND nip = ?";
        try (Connection conn = DriverManager.getConnection(BD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, numero);
            stmt.setInt(2, nip);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Error en la base de datos: " + e.getMessage());
            return false;
        }
    }
    
    public static Titular consultarNombreSexo(String cuenta) {
        String sqlID = "SELECT titular_id FROM Cuentas WHERE numero = ?";
        String sqlNombre = "SELECT nombre,sexo FROM Clientes WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(BD);
            PreparedStatement stmt1 = conn.prepareStatement(sqlID)) {
            stmt1.setString(1, cuenta);
            ResultSet rs1 = stmt1.executeQuery();
            if (rs1.next()) {
                int id = rs1.getInt("titular_id");
                try (PreparedStatement stmt2 = conn.prepareStatement(sqlNombre)) {
                    stmt2.setInt(1, id);
                    ResultSet rs2 = stmt2.executeQuery();
                    if (rs2.next()) {
                        String nombre = rs2.getString("nombre");
                        String sexo = rs2.getString("sexo");
                        return new Titular(nombre, sexo);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en la base de datos (consultarNombreSexo): " + e.getMessage());
        }
        return null;
    }

    public static double consultarSaldo(String cuenta) {
        String sql = "SELECT saldo FROM Cuentas WHERE numero = ?";
        try (Connection conn = DriverManager.getConnection(BD);
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cuenta);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("saldo");
            } else {
                return -1; // Cuenta no encontrada
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar saldo: " + e.getMessage());
            return -1;
        }
    }

    public static boolean actualizarSaldo(String cuenta, double nuevoSaldo) {
        String sql = "UPDATE Cuentas SET saldo = ? WHERE numero = ?";
        try (Connection conn = DriverManager.getConnection(BD);
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, nuevoSaldo);
            stmt.setString(2, cuenta);
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar saldo: " + e.getMessage());
            return false;
        }
    }
    
    public static void registrarMovimiento(String cuenta, String tipo, double monto) {
        String sql = "INSERT INTO Movimientos (cuenta, tipo, monto, fecha) VALUES (?, ?, ?, datetime('now'))";
        try (Connection conn = DriverManager.getConnection(BD);
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cuenta);
            stmt.setString(2, tipo);
            stmt.setDouble(3, monto);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al registrar movimiento: " + e.getMessage());
        }
    }

    public static void registrarTransferencia(String origen, String destino, double monto) {
        String sql = "INSERT INTO Transferencias (cuenta_origen, cuenta_destino, monto, fecha) VALUES (?, ?, ?, datetime('now'))";
        try (Connection conn = DriverManager.getConnection(BD);
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, origen);
            stmt.setString(2, destino);
            stmt.setDouble(3, monto);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al registrar transferencia: " + e.getMessage());
        }
    }
    
    public static List<Map<String, String>> consultarMovimientos(String cuenta) {
        List<Map<String, String>> lista = new ArrayList<>();
        String sql = "SELECT tipo, monto, fecha FROM Movimientos WHERE cuenta = ? ORDER BY fecha DESC LIMIT 10";
        try (Connection conn = DriverManager.getConnection(BD);
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cuenta);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Map<String, String> movimiento = new HashMap<>();
                movimiento.put("tipo", rs.getString("tipo"));
                movimiento.put("monto", String.valueOf(rs.getDouble("monto")));
                movimiento.put("fecha", rs.getString("fecha"));
                lista.add(movimiento);
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar movimientos: " + e.getMessage());
        }
        return lista;
    }

}
