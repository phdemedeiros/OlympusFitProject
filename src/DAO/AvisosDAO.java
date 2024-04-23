package DAO;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import Conexao.ConnectionBD;

public class AvisosDAO {
	
	public static void cadastrarAviso() throws SQLException {
		
               try( Scanner sc = new Scanner(System.in);
            		   Connection conector = ConnectionBD.getInstanciador().getConector()){
            	   char minusculo = 's';
                   char alternativa;
                do {
                limparTela();
                System.out.print("\nInsira o título do aviso: ");
                String titulo = sc.nextLine();
               
                System.out.print("Insira o conteúdo do aviso: ");
                String conteudo = sc.nextLine();
                
                try {
                    // Obtenha o ID mais alto atual
                    String maxIdSql = "SELECT MAX(id) FROM avisosbd";
                    
                    try (PreparedStatement maxIdInstrucao = conector.prepareStatement(maxIdSql);
                    ResultSet maxIdResultado = maxIdInstrucao.executeQuery()) {

                    int novoId = 1; // Valor padrão se não houver registros na tabela

                    if (maxIdResultado.next()) {
                        novoId = maxIdResultado.getInt(1) + 1;
                    }

                    String sql = "INSERT INTO avisosbd (id, titulo, conteudo) VALUES (?,?,?)";
                    try (PreparedStatement instrucao = conector.prepareStatement(sql)) {
                    instrucao.setInt(1, novoId);
                    instrucao.setString(2, titulo);
                    instrucao.setString(3, conteudo);
                    instrucao.executeUpdate();
                    }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                limparTela();
                System.out.print("\n\nDeseja cadastrar um novo aviso? (s/n): ");
                alternativa = sc.next().charAt(0);
                sc.nextLine();
            minusculo = Character.toLowerCase(alternativa);
            minusculo = confirmandoCaractere(minusculo, alternativa);

        } while (minusculo == 's');
    }
}
	
	public static void listarAvisos() {
		Connection conector = ConnectionBD.getInstanciador().getConector();
		String sql = "SELECT id, titulo, conteudo FROM avisosbd ORDER BY id";
		
		try {
			PreparedStatement instrucao = conector.prepareStatement(sql);
            ResultSet resultado = instrucao.executeQuery();
            
            System.out.println("--------- |Avisos cadastrados| ---------");
            System.out.println("ID" + " " + "Titulo" + " " + "Conteudo");
            while(resultado.next()) {
            	Integer id = resultado.getInt("id");
                String titulo = resultado.getString("titulo");
                String conteudo = resultado.getString("conteudo");
                
                System.out.println(id + " " + titulo + " " + conteudo);
                System.out.println();
            }
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void removerAviso() throws SQLException {
	    try (Scanner sc = new Scanner(System.in);
	         Connection conector = ConnectionBD.getInstanciador().getConector()) {
	    	System.out.print("Informe o ID que será deletado: ");
	        int id;
	        if (sc.hasNextInt()) {
	            id = sc.nextInt();
	            sc.nextLine();

	        String deleteSql = "DELETE FROM avisosbd WHERE id = ?";
	        String updateSql = "UPDATE avisosbd SET id = id - 1 WHERE id > ?";

	        try {
	            PreparedStatement deleteInstrucao = conector.prepareStatement(deleteSql);
	            deleteInstrucao.setInt(1, id);
	            deleteInstrucao.executeUpdate();

	            PreparedStatement updateInstrucao = conector.prepareStatement(updateSql);
	            updateInstrucao.setInt(1, id);
	            updateInstrucao.executeUpdate();
	        } catch(SQLException e) {
	            e.printStackTrace();
	        } 
	        }else {
	            System.out.println("Entrada invalida. Por favor, insira um numero inteiro.");
	        }
	    }
	}
	
	
	public static void editarAviso() throws SQLException {
		          
		           try (Scanner sc = new Scanner(System.in);
                    Connection conector = ConnectionBD.getInstanciador().getConector()) {
		        	   char minusculo = 's';
		        	   char alternativa;
		        	   do {
		        		   limparTela();
		        		   System.out.print("Informe o ID que será atualizado: ");
		        		   int id = caractereInvalido(sc);
		        		   try {
                           String sql = "UPDATE avisosbd SET titulo = ?, conteudo = ?";

                    

                    try (PreparedStatement instrucao = conector.prepareStatement(sql)) {
                        //sc.nextLine();
                        System.out.print("Informe o titulo: ");
                        String titulo = sc.nextLine();
                        System.out.print("Informe o conteudo: ");
                        String conteudo = sc.nextLine();
                        instrucao.setString(1, titulo);
                        instrucao.setString(2, conteudo);
                        instrucao.setInt(3, id);
                        instrucao.executeUpdate();
                    }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    limparTela();
                    System.out.print("\n\nDeseja editar um novo aviso (s/n): ");
                        alternativa = sc.next().charAt(0);
                
		        minusculo = Character.toLowerCase(alternativa);
		            minusculo = confirmandoCaractere(minusculo, alternativa);

		        } while (minusculo == 's'); 
		  }
	}
	
	public static void limparTela() {
        String os = System.getProperty("os.name").toLowerCase();
        try {
            // Limpar o terminal com base no sistema operacional
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
 
 public static char confirmandoCaractere(char minusculo, char alternativa) {
        try (Scanner sc = new Scanner(System.in)) {
            char confirmacao = minusculo;
            if (minusculo != 's' && minusculo != 'n') {
                do {
                    limparTela();
                    System.out.println("\nCaractere invalido!!");
                    System.out.print("Deseja cadastrar um novo aviso (s/n): ");
                    alternativa = sc.next().charAt(0);
                    minusculo = Character.toLowerCase(alternativa);
                } while (minusculo != 's' && minusculo != 'n');
                confirmacao = minusculo;
            }
            return confirmacao;
        }
    }
 
 public static int caractereInvalido(Scanner scanner) {
	    int id;
	    while (true) {
	        System.out.print("\nInforme o ID que será atualizado: ");
	        if (scanner.hasNextInt()) {
	            id = scanner.nextInt();
	            break;
	        } else {
	            System.out.println("Entrada inválida. Informe um número inteiro.");
	            scanner.next(); // Limpar o buffer do scanner
	        }
	    }
	    return id;
	}
}