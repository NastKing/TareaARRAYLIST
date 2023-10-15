package hola;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Connection connection = null;
        String url = "jdbc:mariadb://localhost:3306/hola";
        String user = "root";
        String pwd = "1234";

        try {
            connection = DriverManager.getConnection(url, user, pwd);

            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("Menú de Operaciones CRUD:");
                System.out.println("1. Insertar Producto");
                System.out.println("2. Leer Productos");
                System.out.println("3. Actualizar Precio de Producto");
                System.out.println("4. Eliminar Producto");
                System.out.println("5. Salir");
                System.out.print("Ingrese la opción deseada: ");
                int opcion = scanner.nextInt();
                scanner.nextLine(); // Consume la línea en blanco después de la entrada numérica

                switch (opcion) {
                    case 1:
                        System.out.print("Ingrese el nombre del producto: ");
                        String nombre = scanner.nextLine();
                        System.out.print("Ingrese el precio del producto: ");
                        double precio = scanner.nextDouble();
                        insertarProducto(connection, nombre, precio);
                        break;
                    case 2:
                        leerProductos(connection);
                        break;
                    case 3:
                        System.out.print("Ingrese el nombre del producto a actualizar: ");
                        String nombreActualizar = scanner.nextLine();
                        System.out.print("Ingrese el nuevo precio: ");
                        double nuevoPrecio = scanner.nextDouble();
                        actualizarPrecio(connection, nombreActualizar, nuevoPrecio);
                        break;
                    case 4:
                        System.out.print("Ingrese el nombre del producto a eliminar: ");
                        String nombreEliminar = scanner.nextLine();
                        eliminarProducto(connection, nombreEliminar);
                        break;
                    case 5:
                        System.out.println("Saliendo del programa.");
                        return;
                    default:
                        System.out.println("Opción no válida.");
                        break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Cerrar la conexión
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void insertarProducto(Connection connection, String nombre, double precio) throws SQLException {
        String sql = "INSERT INTO producto (nombre, precio) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, nombre);
            preparedStatement.setDouble(2, precio);
            preparedStatement.executeUpdate();
            System.out.println("Producto insertado con éxito.");
        }
    }

    public static void leerProductos(Connection connection) throws SQLException {
        String sql = "SELECT * FROM producto";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                String nombre = resultSet.getString("nombre");
                double precio = resultSet.getDouble("precio");
                System.out.println("Nombre: " + nombre + ", Precio: " + precio);
            }
        }
    }

    public static void actualizarPrecio(Connection connection, String nombre, double nuevoPrecio) throws SQLException {
        String sql = "UPDATE producto SET precio = ? WHERE nombre = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDouble(1, nuevoPrecio);
            preparedStatement.setString(2, nombre);
            preparedStatement.executeUpdate();
            System.out.println("Precio actualizado con éxito.");
        }
    }

    public static void eliminarProducto(Connection connection, String nombre) throws SQLException {
        String sql = "DELETE FROM producto WHERE nombre = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, nombre);
            preparedStatement.executeUpdate();
            System.out.println("Producto eliminado con éxito.");
        }
    }
}
