package Conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionBD {
    private static ConnectionBD instanciador;
    private Connection conector;
    private final String url = "jdbc:postgresql://localhost:5432/postgres";
    private final String user = "postgres";
    private final String password = "root";

    private ConnectionBD(){
        try {
            this.conector = DriverManager.getConnection(url, user,password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized ConnectionBD getInstanciador(){
        if(instanciador == null){
            instanciador = new ConnectionBD();
        }
        return instanciador;
    }

    public Connection getConector(){
        return conector;
    }

    public void fecharConector(){
        if(conector != null){
            try {
                conector.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}