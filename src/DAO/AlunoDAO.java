package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import Conexao.ConnectionBD;

public class AlunoDAO {
	
	public static void cadastrarAluno() throws SQLException {
		
               try( Scanner sc = new Scanner(System.in);
            		   Connection conector = ConnectionBD.getInstanciador().getConector()){
            	   char minusculo = 's';
                   char alternativa;
                do {
                limparTela();
                System.out.print("\nInsira o nome: ");
                String nome = sc.nextLine();

				System.out.println("Insira a idade: ");
				Integer idade = sc.nextInt();
				sc.nextLine();

                System.out.print("Insira o sexo: ");
                String sexo = sc.nextLine();
                
                System.out.print("Insira o telefone: ");
                Long telefone = sc.nextLong();
                sc.nextLine();
                
                System.out.print("Insira o email: ");
                String email = sc.nextLine();
                
                System.out.print("Insira o objetivo: ");
                String objetivo = sc.nextLine();
                
                try {
                    // Obtenha o ID mais alto atual
                    String maxIdSql = "SELECT MAX(id) FROM alunosbd";
                    
                    try (PreparedStatement maxIdInstrucao = conector.prepareStatement(maxIdSql);
                    ResultSet maxIdResultado = maxIdInstrucao.executeQuery()) {

                    int novoId = 1; // Valor padrão se não houver registros na tabela

                    if (maxIdResultado.next()) {
                        novoId = maxIdResultado.getInt(1) + 1;
                    }

                    String sql = "INSERT INTO alunosbd (id, nome, idade, sexo, telefone, email, objetivo) VALUES (?,?,?,?,?,?,?)";
                    try (PreparedStatement instrucao = conector.prepareStatement(sql)) {
                    instrucao.setInt(1, novoId);
                    instrucao.setString(2, nome);
					instrucao.setInt(3, idade);
                    instrucao.setString(4, sexo);
                    instrucao.setLong(5, telefone);
                    instrucao.setString(6, email);
                    instrucao.setString(7, objetivo);
                    instrucao.executeUpdate();
                    }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                System.out.print("\n\nDeseja cadastrar um novo aluno? (s/n): ");
                alternativa = sc.next().charAt(0);
                sc.nextLine();
            minusculo = Character.toLowerCase(alternativa);
            minusculo = confirmandoCaractere(minusculo, alternativa);

        } while (minusculo == 's');
    }
}
	
	public static void exibirDadosAlunoLogado(String usuario, String senha) {
	    // Obtendo o ID do aluno logado
	    int idAlunoLogado = getIdAluno(usuario, senha);

	    // Consultando o banco de dados para obter os dados do aluno logado
	    String sql = "SELECT id, nome, idade, sexo, telefone, email, objetivo FROM alunosbd WHERE id_aluno = ?";
	    
	    try (Connection conexao = ConnectionBD.getInstanciador().getConector();
	         PreparedStatement stmt = conexao.prepareStatement(sql)) {
	        
	        stmt.setInt(1, idAlunoLogado);
	        ResultSet rs = stmt.executeQuery();
	        
	        if (rs.next()) {
	            int id = rs.getInt("id");
	            String nome = rs.getString("nome");
				Integer idade = rs.getInt("idade");
	            String sexo = rs.getString("sexo");
	            Long telefone = rs.getLong("telefone");
	            String email = rs.getString("email");
	            String objetivo = rs.getString("objetivo");
	            
	            // Exibindo os detalhes do aluno logado
	            System.out.println("Detalhes do aluno logado:");
	            System.out.println("ID: " + id);
	            System.out.println("Nome: " + nome);
				System.out.println("Idade: " + idade);
	            System.out.println("Sexo: " + sexo);
	            System.out.println("Telefone: " + telefone);
	            System.out.println("Email: " + email);
	            System.out.println("Objetivo: " + objetivo);
	        } else {
	            System.out.println("Aluno não encontrado.");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public static int getIdAluno(String usuario, String senha) {
		int id = 0;
		Connection conector = ConnectionBD.getInstanciador().getConector();
		try {
			PreparedStatement stmt = conector.prepareStatement("SELECT loginaluno_id FROM loginaluno WHERE usuario = ? AND senha = ?");
			stmt.setString(1, usuario);
			stmt.setString(2, senha);
	
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				id = rs.getInt("loginaluno_id"); // Alteração aqui para buscar loginaluno_id
			}
	
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}
	
	
	public static void cadastrarUsuarioAluno() throws SQLException {
	    try (Scanner sc = new Scanner(System.in);
	         Connection conector = ConnectionBD.getInstanciador().getConector()) {
	        char minusculo = 's';
	        char alternativa;
	        do {
	            limparTela();
	            System.out.print("\nInsira o nome de usuário: ");
	            String usuario = sc.nextLine();

	            System.out.print("Insira a senha: ");
	            String senha = sc.nextLine();

	            try {
	                // Verifica se o nome de usuário já existe
	                String verificaUsuarioSql = "SELECT COUNT(*) FROM loginaluno WHERE usuario = ?";
	                try (PreparedStatement verificaUsuarioStmt = conector.prepareStatement(verificaUsuarioSql)) {
	                    verificaUsuarioStmt.setString(1, usuario);
	                    ResultSet resultado = verificaUsuarioStmt.executeQuery();
	                    resultado.next();
	                    int count = resultado.getInt(1);
	                    if (count > 0) {
	                        System.out.println("Nome de usuário já existe. Por favor, escolha outro.");
	                        continue; // Volta ao início do loop para cadastrar outro usuário
	                    }
	                }

	                // Insere o novo nome de usuário e senha
	                String sql = "INSERT INTO loginaluno (usuario, senha) VALUES (?, ?)";
	                try (PreparedStatement instrucao = conector.prepareStatement(sql)) {
	                    instrucao.setString(1, usuario);
	                    instrucao.setString(2, senha);
	                    instrucao.executeUpdate();
	                    System.out.println("Usuário e senha cadastrados com sucesso!");
	                }
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }

	            System.out.print("\nDeseja cadastrar outro usuário e senha para aluno? (s/n): ");
	            alternativa = sc.next().charAt(0);
	            sc.nextLine();
	            minusculo = Character.toLowerCase(alternativa);
	            minusculo = confirmandoCaractere(minusculo, alternativa);

	        } while (minusculo == 's');
	    }
	}

	
	public boolean validarLoginAluno(String usuario, String senha) {
		Connection conector = ConnectionBD.getInstanciador().getConector();
        boolean loginValido = false;
        try {
            PreparedStatement stmt = conector.prepareStatement("SELECT COUNT(*) AS count FROM loginaluno WHERE usuario = ? AND senha = ?");
            stmt.setString(1, usuario);
            stmt.setString(2, senha);

            // Executando a consulta
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt("count");
                loginValido = (count == 1);
            }

            // Fechando recursos
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loginValido;
    }

	
	public static void listarAluno() {
    Connection conector = ConnectionBD.getInstanciador().getConector();
    String sql = "SELECT id, nome, idade, sexo, telefone, email, objetivo FROM alunosbd ORDER BY id";

    try {
        PreparedStatement instrucao = conector.prepareStatement(sql);
        ResultSet resultado = instrucao.executeQuery();

        System.out.println("--------- |Alunos cadastrados| ---------");
        System.out.println("Id\tNome\t\tIdade\tSexo\tTelefone\t\tEmail\t\t\t\t\tObjetivo");
        while (resultado.next()) {
            Integer id = resultado.getInt("id");
            String nome = resultado.getString("nome");
			Integer idade = resultado.getInt("idade");
            String sexo = resultado.getString("sexo");
            Long telefone = resultado.getLong("telefone");
            String email = resultado.getString("email");
            String objetivo = resultado.getString("objetivo");

            System.out.println(id + "\t" + nome + "\t\t" + idade + "\t" + sexo + "\t" + telefone + "\t" + email + "\t" + objetivo);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

	
	public static void removerAluno() throws SQLException {
		try (Scanner sc = new Scanner(System.in);
			 Connection conector = ConnectionBD.getInstanciador().getConector()) {
			System.out.print("Informe o ID que será deletado: ");
			int id; // Renomeando para id_aluno_id
			if (sc.hasNextInt()) {
				id = sc.nextInt();
				sc.nextLine();
	
				String deleteSql = "DELETE FROM AlunosBD WHERE id_aluno = ?";
				String updateSql = "UPDATE AlunosBD SET id_aluno = id_aluno - 1 WHERE id_aluno > ?";
	
				try {
					PreparedStatement deleteInstrucao = conector.prepareStatement(deleteSql);
					deleteInstrucao.setInt(1, id);
					deleteInstrucao.executeUpdate();
	
					PreparedStatement updateInstrucao = conector.prepareStatement(updateSql);
					updateInstrucao.setInt(1, id);
					updateInstrucao.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("Entrada inválida. Por favor, insira um número inteiro.");
			}
		}
	}
	
	
	public static void editarAluno() throws SQLException {
		try (Scanner sc = new Scanner(System.in);
			 Connection conector = ConnectionBD.getInstanciador().getConector()) {
			char minusculo = 's';
			char alternativa;
			do {
				limparTela();
				System.out.print("Informe o ID que será atualizado: ");
				int id = caractereInvalido(sc); // Renomeando para id_aluno_id
				try {
					String sql = "UPDATE alunosbd SET nome = ?, idade = ?, sexo = ?, email = ?, objetivo = ? WHERE id = ?";
	
					try (PreparedStatement instrucao = conector.prepareStatement(sql)) {
						//sc.nextLine();
						System.out.print("Informe o nome: ");
						String nome = sc.nextLine();
						// Limpar o buffer
						System.out.print("Informe a idade: ");
						Integer idade = sc.nextInt();
						System.out.print("Informe o sexo: ");
						String sexo = sc.nextLine();
						System.out.print("Informe o telefone: ");
						Long telefone = sc.nextLong();
						sc.nextLine(); // Limpar o buffer
						System.out.print("Informe o email: ");
						String email = sc.nextLine();
						System.out.print("Informe o objetivo: ");
						String objetivo = sc.nextLine();
	
						instrucao.setString(1, nome);
						instrucao.setInt(2, idade);
						instrucao.setString(3, sexo);
						instrucao.setLong(4, telefone);
						instrucao.setString(5, email);
						instrucao.setString(6, objetivo);
						instrucao.setInt(7, id); 
						instrucao.executeUpdate();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				limparTela();
				System.out.print("\n\nDeseja editar um novo aluno? (s/n): ");
				alternativa = sc.next().charAt(0);
				sc.nextLine(); // Limpar o buffer
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
 
 public static char confirmandoCaractere(char minusculo, char alternativa) {
        try (Scanner sc = new Scanner(System.in)) {
            char confirmacao = minusculo;
            if (minusculo != 's' && minusculo != 'n') {
                do {
                    limparTela();
                    System.out.println("\nCaractere invalido!!");
                    System.out.print("Deseja cadastrar um novo cliente (s/n): ");
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