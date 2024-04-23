package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import Conexao.ConnectionBD;

public class TreinoDAO {
    
    public static void cadastrarTreino() throws SQLException {
        try (Scanner sc = new Scanner(System.in);
             Connection conector = ConnectionBD.getInstanciador().getConector()) {
            char minusculo = 's';
            char alternativa;
            do {
                limparTela();
                System.out.print("\nInsira o dia: ");
                int dia = sc.nextInt();
               
                System.out.print("Insira o mês: ");
                int mes = sc.nextInt();
                
                sc.nextLine(); // Limpar o buffer
                
                System.out.print("Insira o nome do exercício: ");
                String nome = sc.nextLine();

                System.out.print("Insira o grupo muscular: ");
                String grupoMuscular = sc.nextLine();
                
                System.out.print("Insira o número de séries: ");
                int series = sc.nextInt();
                
                System.out.print("Insira o número de repetições: ");
                int repeticoes = sc.nextInt();
                
                try {
                    String maxIdSql = "SELECT MAX(id) FROM treinos";
                    try (PreparedStatement maxIdInstrucao = conector.prepareStatement(maxIdSql);
                         ResultSet maxIdResultado = maxIdInstrucao.executeQuery()) {
                        int novoId = 1; 
                        if (maxIdResultado.next()) {
                            novoId = maxIdResultado.getInt(1) + 1;
                        }
                        String sql = "INSERT INTO treinosbd (id, dia, mes, nome, grupoMuscular, series, repeticoes) VALUES (?,?,?,?,?,?,?)";
                        try (PreparedStatement instrucao = conector.prepareStatement(sql)) {
                            instrucao.setInt(1, novoId);
                            instrucao.setInt(2, dia);
                            instrucao.setInt(3, mes);
                            instrucao.setString(4, nome);
                            instrucao.setString(5, grupoMuscular);
                            instrucao.setInt(6, series);
                            instrucao.setInt(7, repeticoes);
                            instrucao.executeUpdate();
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                
                System.out.print("\n\nDeseja cadastrar um novo treino? (s/n): ");
                alternativa = sc.next().charAt(0);
                sc.nextLine();
                minusculo = Character.toLowerCase(alternativa);
                minusculo = confirmandoCaractere(minusculo, alternativa);
            } while (minusculo == 's');
        }
    }
    public static void listarTreinos() {
        try (Connection conector = ConnectionBD.getInstanciador().getConector()) {
            String sql = "SELECT id, dia, mes, nome, grupoMuscular, series, repeticoes FROM treinosbd ORDER BY id";
    
            try (PreparedStatement instrucao = conector.prepareStatement(sql);
                 ResultSet resultado = instrucao.executeQuery()) {
    
                System.out.println("--------- |Treinos cadastrados| ---------");
                System.out.println("ID\tDia\tMês\tExercício\tGrupo Muscular\tSéries\tRepetições");
                
                while (resultado.next()) {
                    int id = resultado.getInt("id");
                    int dia = resultado.getInt("dia");
                    int mes = resultado.getInt("mes");
                    String nome = resultado.getString("nome");
                    String grupoMuscular = resultado.getString("grupoMuscular");
                    int series = resultado.getInt("series");
                    int repeticoes = resultado.getInt("repeticoes");
    
                    System.out.println(id + "\t" + dia + "\t" + mes + "\t" + nome + "\t" + grupoMuscular + "\t" + series + "\t" + repeticoes);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Método para remover um treino pelo seu ID
    public static void removerTreino() throws SQLException {
        try (Scanner sc = new Scanner(System.in);
             Connection conector = ConnectionBD.getInstanciador().getConector()) {
            System.out.print("Informe o ID do treino que será removido: ");
            int id = sc.nextInt();
            
            String deleteSql = "DELETE FROM treinosbd WHERE id = ?";
            
            try (PreparedStatement deleteInstrucao = conector.prepareStatement(deleteSql)) {
                deleteInstrucao.setInt(1, id);
                deleteInstrucao.executeUpdate();
                System.out.println("Treino removido com sucesso!");
            } catch(SQLException e) {
                e.printStackTrace();
            } 
        }
    }
    
    // Método para editar um treino pelo seu ID
    public static void editarTreino() throws SQLException {
        try (Scanner sc = new Scanner(System.in);
             Connection conector = ConnectionBD.getInstanciador().getConector()) {
            char minusculo = 's';
            char alternativa;
            do {
                limparTela();
                System.out.print("Informe o ID do treino que será atualizado: ");
                int id = sc.nextInt();
                
                try {
                    String sql = "UPDATE treinosbd SET dia = ?, mes = ?, nome = ?, grupoMuscular = ?, series = ?, repeticoes = ? WHERE id = ?";
    
                    try (PreparedStatement instrucao = conector.prepareStatement(sql)) {
                        System.out.print("Informe o dia: ");
                        int dia = sc.nextInt();
                        System.out.print("Informe o mês: ");
                        int mes = sc.nextInt();
                        sc.nextLine(); // Limpar o buffer
                        System.out.print("Informe o nome do exercício: ");
                        String nome = sc.nextLine();
                        System.out.print("Informe o grupo muscular: ");
                        String grupoMuscular = sc.nextLine();
                        System.out.print("Informe o número de séries: ");
                        int series = sc.nextInt();
                        System.out.print("Informe o número de repetições: ");
                        int repeticoes = sc.nextInt();
    
                        instrucao.setInt(1, dia);
                        instrucao.setInt(2, mes);
                        instrucao.setString(3, nome);
                        instrucao.setString(4, grupoMuscular);
                        instrucao.setInt(5, series);
                        instrucao.setInt(6, repeticoes);
                        instrucao.setInt(7, id);
                        instrucao.executeUpdate();
                        System.out.println("Treino atualizado com sucesso!");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                
                System.out.print("\n\nDeseja editar outro treino? (s/n): ");
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
