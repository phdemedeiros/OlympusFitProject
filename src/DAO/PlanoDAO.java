package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import Conexao.ConnectionBD;

public class PlanoDAO {
    
    public static void cadastrarPlano() throws SQLException {
        try (Scanner sc = new Scanner(System.in);
             Connection conector = ConnectionBD.getInstanciador().getConector()) {
            char minusculo = 's';
            char alternativa;
            do {
                limparTela();
                System.out.print("\nInsira o tipo do plano: ");
                String tipo = sc.nextLine();
               
                System.out.print("Insira o valor do plano: ");
                double valor = sc.nextDouble();
                
                try {
                    String maxIdSql = "SELECT MAX(id) FROM planobd";
                    try (PreparedStatement maxIdInstrucao = conector.prepareStatement(maxIdSql);
                         ResultSet maxIdResultado = maxIdInstrucao.executeQuery()) {
                        int novoId = 1; 
                        if (maxIdResultado.next()) {
                            novoId = maxIdResultado.getInt(1) + 1;
                        }
                        String sql = "INSERT INTO planobd (id, tipo, valor) VALUES (?,?,?)";
                        try (PreparedStatement instrucao = conector.prepareStatement(sql)) {
                            instrucao.setInt(1, novoId);
                            instrucao.setString(2, tipo);
                            instrucao.setDouble(3, valor);
                            instrucao.executeUpdate();
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                
                System.out.print("\n\nDeseja cadastrar um novo plano? (s/n): ");
                alternativa = sc.next().charAt(0);
                sc.nextLine();
                minusculo = Character.toLowerCase(alternativa);
                minusculo = confirmandoCaractere(minusculo, alternativa);
            } while (minusculo == 's');
        }
    }
    
    public static void listarPlanos() {
        try (Connection conector = ConnectionBD.getInstanciador().getConector()) {
            String sql = "SELECT id, tipo, valor FROM planobd ORDER BY id";
    
            try (PreparedStatement instrucao = conector.prepareStatement(sql);
                 ResultSet resultado = instrucao.executeQuery()) {
    
                System.out.println("--------- |Planos cadastrados| ---------");
                System.out.println("ID\tTipo\tValor");
                
                while (resultado.next()) {
                    int id = resultado.getInt("id");
                    String tipo = resultado.getString("tipo");
                    double valor = resultado.getDouble("valor");
    
                    System.out.println(id + "\t" + tipo + "\t" + valor);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Método para remover um plano pelo seu ID
    public static void removerPlano() throws SQLException {
        try (Scanner sc = new Scanner(System.in);
             Connection conector = ConnectionBD.getInstanciador().getConector()) {
            System.out.print("Informe o ID do plano que será removido: ");
            int id = sc.nextInt();
            
            String deleteSql = "DELETE FROM planobd WHERE id = ?";
            
            try (PreparedStatement deleteInstrucao = conector.prepareStatement(deleteSql)) {
                deleteInstrucao.setInt(1, id);
                deleteInstrucao.executeUpdate();
                System.out.println("Plano removido com sucesso!");
            } catch(SQLException e) {
                e.printStackTrace();
            } 
        }
    }
    
    // Método para editar um plano pelo seu ID
    public static void editarPlano() throws SQLException {
        try (Scanner sc = new Scanner(System.in);
             Connection conector = ConnectionBD.getInstanciador().getConector()) {
            char minusculo = 's';
            char alternativa;
            do {
                limparTela();
                System.out.print("Informe o ID do plano que será atualizado: ");
                int id = sc.nextInt();
                
                try {
                    String sql = "UPDATE planobd SET tipo = ?, valor = ? WHERE id = ?";
    
                    try (PreparedStatement instrucao = conector.prepareStatement(sql)) {
                        sc.nextLine(); // Limpar o buffer
                        System.out.print("Informe o novo tipo: ");
                        String tipo = sc.nextLine();
                        System.out.print("Informe o novo valor: ");
                        double valor = sc.nextDouble();
    
                        instrucao.setString(1, tipo);
                        instrucao.setDouble(2, valor);
                        instrucao.setInt(3, id);
                        instrucao.executeUpdate();
                        System.out.println("Plano atualizado com sucesso!");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                
                System.out.print("\n\nDeseja editar outro plano? (s/n): ");
                alternativa = sc.next().charAt(0);
                sc.nextLine();
                minusculo = Character.toLowerCase(alternativa);
                minusculo = confirmandoCaractere(minusculo, alternativa);
            } while (minusculo == 's'); 
        }
    }

    public static void limparTela() {
        String os = System.getProperty("os.name").toLowerCase();
        try {
            if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para confirmar se o caractere digitado é 's' ou 'n'
    public static char confirmandoCaractere(char minusculo, char alternativa) {
        try (Scanner sc = new Scanner(System.in)) {
            char confirmacao = minusculo;
            if (minusculo != 's' && minusculo != 'n') {
                do {
                    limparTela();
                    System.out.println("\nCaractere inválido!!");
                    System.out.print("Deseja continuar (s/n): ");
                    alternativa = sc.next().charAt(0);
                    minusculo = Character.toLowerCase(alternativa);
                } while (minusculo != 's' && minusculo != 'n');
                confirmacao = minusculo;
            }
            return confirmacao;
        }
    }
}
