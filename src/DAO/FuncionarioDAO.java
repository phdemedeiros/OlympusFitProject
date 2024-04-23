package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import Conexao.ConnectionBD;

public class FuncionarioDAO {
    
    public static void cadastrarFuncionario() throws SQLException {
        try (Scanner sc = new Scanner(System.in);
             Connection conector = ConnectionBD.getInstanciador().getConector()) {
            char minusculo = 's';
            char alternativa;
            do {
                limparTela();
                System.out.print("\nInsira o nome: ");
                String nome = sc.nextLine();
               
                System.out.print("Insira o CPF: ");
                Long cpf = sc.nextLong();
                sc.nextLine();
                
                System.out.print("Insira a idade: ");
                Integer idade = sc.nextInt();

                System.out.print("Insira o sexo: ");
                String sexo = sc.nextLine();
                sc.nextLine();

                System.out.print("Insira o telefone: ");
                Long telefone = sc.nextLong();
                sc.nextLine();
                
                System.out.print("Insira o email: ");
                String email = sc.nextLine();
                
                System.out.print("Insira o cargo: ");
                String cargo = sc.nextLine();
                
                try {
                    String maxIdSql = "SELECT MAX(id) FROM funcionariosbd";
                    try (PreparedStatement maxIdInstrucao = conector.prepareStatement(maxIdSql);
                         ResultSet maxIdResultado = maxIdInstrucao.executeQuery()) {
                        int novoId = 1; 
                        if (maxIdResultado.next()) {
                            novoId = maxIdResultado.getInt(1) + 1;
                        }
                        String sql = "INSERT INTO funcionariosbd (id, nome, idade, cpf, sexo, telefone, email, cargo) VALUES (?,?,?,?,?,?,?)";
                        try (PreparedStatement instrucao = conector.prepareStatement(sql)) {
                            instrucao.setInt(1, novoId);
                            instrucao.setString(2, nome);
                            instrucao.setInt(3, idade);
                            instrucao.setLong(4, cpf);

                            instrucao.setString(5, sexo);
                            instrucao.setLong(6, telefone);
                            instrucao.setString(7, email);
                            instrucao.setString(8, cargo);
                            instrucao.executeUpdate();
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                
                System.out.print("\n\nDeseja cadastrar um novo funcionário? (s/n): ");
                alternativa = sc.next().charAt(0);
                sc.nextLine();
                minusculo = Character.toLowerCase(alternativa);
                minusculo = confirmandoCaractere(minusculo, alternativa);
            } while (minusculo == 's');
        }
    }
    
    public static void cadastrarUsuarioFuncionario() throws SQLException {
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
                    String verificaUsuarioSql = "SELECT COUNT(*) FROM loginfuncionario WHERE usuario = ?";
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
                    String sql = "INSERT INTO loginfuncionario (usuario, senha) VALUES (?, ?)";
                    try (PreparedStatement instrucao = conector.prepareStatement(sql)) {
                        instrucao.setString(1, usuario);
                        instrucao.setString(2, senha);
                        instrucao.executeUpdate();
                        System.out.println("Usuário e senha cadastrados com sucesso!");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                System.out.print("\nDeseja cadastrar outro usuário e senha para funcionário? (s/n): ");
                alternativa = sc.next().charAt(0);
                sc.nextLine();
                minusculo = Character.toLowerCase(alternativa);
                minusculo = confirmandoCaractere(minusculo, alternativa);

            } while (minusculo == 's');
        }
    }

    public static void exibirDadosFuncionarioLogado(String usuario, String senha) {
        // Obtendo o ID do funcionário logado
        int idFuncionarioLogado = getIdFuncionario(usuario, senha);

        // Consultando o banco de dados para obter os dados do funcionário logado
        String sql = "SELECT id, nome, cpf, sexo, telefone, email, cargo FROM funcionariosbd WHERE id = ?";
        
        try (Connection conexao = ConnectionBD.getInstanciador().getConector();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            
            stmt.setInt(1, idFuncionarioLogado);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                Long cpf = rs.getLong("cpf");
                String sexo = rs.getString("sexo");
                Long telefone = rs.getLong("telefone");
                String email = rs.getString("email");
                String cargo = rs.getString("cargo");
                
                // Exibindo os detalhes do funcionário logado
                System.out.println("Detalhes do funcionário logado:");
                System.out.println("ID: " + id);
                System.out.println("Nome: " + nome);
                System.out.println("CPF: " + cpf);
                System.out.println("Sexo: " + sexo);
                System.out.println("Telefone: " + telefone);
                System.out.println("Email: " + email);
                System.out.println("Cargo: " + cargo);
            } else {
                System.out.println("Funcionário não encontrado.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getIdFuncionario(String usuario, String senha) {
        int id = 0;
        Connection conector = ConnectionBD.getInstanciador().getConector();
        try {
            PreparedStatement stmt = conector.prepareStatement("SELECT id FROM loginfuncionario WHERE usuario = ? AND senha = ?");
            stmt.setString(1, usuario);
            stmt.setString(2, senha);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                id = rs.getInt("id");
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Ocorreu uma exceção ao obter o ID do funcionário:");
            e.printStackTrace();
        }
        return id;
    }

    public static boolean validarLoginFuncinario(String usuario, String senha) {
		Connection conector = ConnectionBD.getInstanciador().getConector();
        boolean loginValido = false;
        try {
            PreparedStatement stmt = conector.prepareStatement("SELECT COUNT(*) AS count FROM loginfuncionario WHERE usuario = ? AND senha = ?");
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

    public static void listarFuncionarios() {
        try (Connection conector = ConnectionBD.getInstanciador().getConector()) {
            String sql = "SELECT id, nome, idade, sexo, telefone, email, cargo, cpf FROM funcionariosbd ORDER BY id";
    
            try (PreparedStatement instrucao = conector.prepareStatement(sql);
                 ResultSet resultado = instrucao.executeQuery()) {
    
                System.out.println("--------- |Funcionários cadastrados| ---------");
                System.out.println("ID\tNome\t\tIdade\t\tSexo\t\tTelefone\t\tEmail\t\tCargo\t\tCpf");
                
                while (resultado.next()) {
                    Integer id = resultado.getInt("id");
                    String nome = resultado.getString("nome");
                    Integer idade = resultado.getInt("idade");
                    String sexo = resultado.getString("sexo");
                    Long telefone = resultado.getLong("telefone");
                    String email = resultado.getString("email");
                    String cargo = resultado.getString("cargo");
                    Long cpf = resultado.getLong("cpf");
    
                    System.out.println(id + "\t" + nome + "\t\t" + idade + "\t\t" + sexo + "\t\t" + telefone + "\t\t" + email + "\t\t" + cargo + "\t\t" + cpf);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    public static void removerFuncionario() throws SQLException {
        try (Scanner sc = new Scanner(System.in);
             Connection conector = ConnectionBD.getInstanciador().getConector()) {
            System.out.print("Informe o ID que será deletado: ");
            int id;
            if (sc.hasNextInt()) {
                id = sc.nextInt();
                sc.nextLine();

                String deleteSql = "DELETE FROM funcionariosbd WHERE id = ?";
                String updateSql = "UPDATE funcionariosbd SET id = id - 1 WHERE id > ?";

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
            } else {
                System.out.println("Entrada inválida. Por favor, insira um número inteiro.");
            }
        }
    }

    public static void editarFuncionario() throws SQLException {
        try (Scanner sc = new Scanner(System.in);
             Connection conector = ConnectionBD.getInstanciador().getConector()) {
            char minusculo = 's';
            char alternativa;
            do {
                limparTela();
                System.out.print("Informe o ID que será atualizado: ");
                int id = caractereInvalido(sc);
                try {
                    String sql = "UPDATE funcionariosbd SET nome = ?, idade = ?, cpf = ?, sexo = ?, email = ?, cargo = ? WHERE id = ?";

                    try (PreparedStatement instrucao = conector.prepareStatement(sql)) {
                        System.out.print("Informe o nome: ");
                        String nome = sc.nextLine();
                        System.out.print("Informe a idade: ");
                        Integer idade = sc.nextInt();
                        System.out.print("Informe o cpf: ");
                        Long cpf = sc.nextLong();
                        System.out.print("Informe o sexo: ");
                        String sexo = sc.nextLine();
                        System.out.print("Informe o telefone: ");
                        Long telefone = sc.nextLong();
                        System.out.print("Informe o email: ");
                        String email = sc.nextLine();
                        System.out.print("Informe o cargo: ");
                        String cargo = sc.nextLine();
                        instrucao.setString(1, nome);
                        instrucao.setLong(2, cpf);
                        instrucao.setInt(3, idade);
                        instrucao.setString(4, sexo);
                        instrucao.setLong(5, telefone);
                        instrucao.setString(6, email);
                        instrucao.setString(7, cargo);
                        instrucao.setInt(8, id);
                        instrucao.executeUpdate();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                limparTela();
                System.out.print("\n\nDeseja editar um novo funcionário (s/n): ");
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

    public static char confirmandoCaractere(char minusculo, char alternativa) {
        try (Scanner sc = new Scanner(System.in)) {
            char confirmacao = minusculo;
            if (minusculo != 's' && minusculo != 'n') {
                do {
                    limparTela();
                    System.out.println("\nCaractere inválido!!");
                    System.out.print("Deseja cadastrar um novo funcionário (s/n): ");
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