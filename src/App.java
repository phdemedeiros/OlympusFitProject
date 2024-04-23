
import java.sql.SQLException;
import java.util.Scanner;
import DAO.AlunoDAO;
import DAO.AvaliacaoDAO;
import DAO.AvisosDAO;
import DAO.InstrutorDAO;
import DAO.PlanoDAO;
import DAO.TreinoDAO;
import DAO.FuncionarioDAO;

public class App {
    static AlunoDAO alunodao = new AlunoDAO();
    static InstrutorDAO instrutordao = new InstrutorDAO();
    static FuncionarioDAO funcionariodao = new FuncionarioDAO();
    static AvisosDAO avisosdao = new AvisosDAO();
    static TreinoDAO treinodao = new TreinoDAO();
    static AvaliacaoDAO avaliacaodao = new AvaliacaoDAO();
    static PlanoDAO planodao = new PlanoDAO();

    public static void main(String[] args) throws InterruptedException, SQLException {

        limparTela();

        try (Scanner sc = new Scanner(System.in)) {
            while (true) {
                System.out.println("=================================");
                System.out.println("     BEM VINDO A OLYMPUS FIT    ");
                System.out.println("=================================");
                System.out.println("|| 1. Acessar como Aluno       ||");
                System.out.println("|| 2. Acessar como Instrutor   ||");
                System.out.println("|| 3. Acessar como Funcionário ||");
                System.out.println("|| 4. Sair                     ||");
                System.out.println("=================================");
                System.out.println("Escolha uma opção: ");
                int choice = sc.nextInt();
                
                switch (choice) {
                    case 1:
                        loginAluno(sc);
                        painelAluno(sc);
                        break;
                    case 2:
                        loginInstrutor(sc);
                        painelInstrutor(sc);
                        break;
                    case 3:
                        loginFuncionario(sc);
                        painelAdmin(sc);
                        break;
                    case 4:
                        System.out.println("Saindo do sistema...");
                        return; 
                    default:
                        opcaoInvalida();
                }
            }
        }
    }
    
    public static boolean loginAluno(Scanner sc) throws SQLException, InterruptedException {
    	limparTela();
    	System.out.println("=======LOGIN DE ALUNO========");
        sc.nextLine();
    	System.out.println("Digite seu nome de usuário:");
        String usuario = sc.nextLine();
        System.out.println("Digite sua senha:");
        String senha = sc.nextLine();
        
        // Criando uma instância de AlunoDAO
        AlunoDAO alunoDAO = new AlunoDAO();
        
        // Chamando o método validarLoginAluno com os dados do usuário
        boolean loginValido = alunoDAO.validarLoginAluno(usuario, senha);
        
        // Verificando se o login é válido e exibindo mensagem adequada
        if (loginValido) {
            System.out.println("Login do aluno validado com sucesso!");
            return true; // Se o login for válido, retorna true
        } else {
            System.out.println("Usuário ou senha inválidos!");
            return false; // Se o login for inválido, retorna false
        }
        
    }    
    
    public static boolean loginInstrutor(Scanner sc) throws SQLException, InterruptedException {
    	limparTela();
    	System.out.println("=======LOGIN DE INSTRUTOR========");
        sc.nextLine();
    	System.out.println("Digite seu nome de usuário:");
        String usuario = sc.nextLine();
        System.out.println("Digite sua senha:");
        String senha = sc.nextLine();
        
        // Criando uma instância de AlunoDAO
        InstrutorDAO instrutorDAO = new InstrutorDAO();
        
        // Chamando o método validarLoginAluno com os dados do usuário
        boolean loginValido = instrutorDAO.validarLoginInstrutor(usuario, senha);
        
        // Verificando se o login é válido e exibindo mensagem adequada
        if (loginValido) {
            System.out.println("Login do instrutor validado com sucesso!");
            return true; // Se o login for válido, retorna true
        } else {
            System.out.println("Usuário ou senha inválidos!");
            return false; // Se o login for inválido, retorna false
        }
    }
    
    public static boolean loginFuncionario(Scanner sc) throws SQLException, InterruptedException {
    	
    	System.out.println("=======LOGIN DE FUNCIONARIO========");
        sc.nextLine();
    	System.out.println("Digite seu nome de usuário:");
        String usuario = sc.nextLine();
        System.out.println("Digite sua senha:");
        String senha = sc.nextLine();
        
        FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
        // Chamando o método validarLoginAluno com os dados do usuário
        boolean loginValido = FuncionarioDAO.validarLoginFuncinario(usuario, senha);
        
        if (loginValido) {
            System.out.println("Login do funcionário validado com sucesso!");
            return true; // Se o login for válido, retorna true
        } else {
            System.out.println("Usuário ou senha inválidos!");
            return false; // Se o login for inválido, retorna false
        }
    }
    
    public static void painelAdmin(Scanner sc) throws SQLException, InterruptedException {
        limparTela();
        System.out.println("------------------------------");
        System.out.println("1. Gerenciar Alunos           ");
        System.out.println("2. Gerenciar Instrutores      ");
        System.out.println("3. Gerenciar Funcionarios     ");
        System.out.println("4. Gerenciar Planos     ");
        System.out.println("5. Gerenciar Avisos           ");
        System.out.println("6. Voltar para o menu         ");
        System.out.println("------------------------------");
        System.out.println("Escolha uma opção: ");
        int escolha = sc.nextInt();
        sc.nextLine(); 

        switch (escolha) {
            case 1:
                gerenciarAlunos(sc);
                break;
            case 2:
                gerenciarInstrutores(sc);
                break;
            case 3:
                gerenciarFuncionarios(sc);
                break;
            case 4:
                gerenciarPlano(sc);
                break;
            case 5:
                gerenciarAvisos(sc);
                break;
            case 6:
                break;
            default:
                opcaoInvalida();
        }
    }
    
    public static void painelAluno(Scanner sc) throws SQLException, InterruptedException {
        limparTela();
        System.out.println("------------------------------");
        System.out.println("1. Visualizar seus dados      ");
        System.out.println("2. Visualizar o treino      ");
        System.out.println("3. Visualizar a avaliação   ");
        System.out.println("4. Visualizar o plano         ");
        System.out.println("5. Visualizar os avisos       ");
        System.out.println("6. Voltar para o menu         ");
        System.out.println("------------------------------");
        System.out.println("Escolha uma opção: ");
        int escolha = sc.nextInt();
        sc.nextLine(); 

        switch (escolha) {
            case 1:
                AlunoDAO.exibirDadosAlunoLogado(null, null);
                break;
            case 2:
                gerenciarInstrutores(sc);
                break;
            case 3:
                gerenciarFuncionarios(sc);
                break;
            case 4:
                PlanoDAO.listarPlanos();
                break;
            case 5:
                AvisosDAO.listarAvisos();
                break;
            case 6:
                break;
            default:
                opcaoInvalida();
        }
    }

    public static void painelInstrutor(Scanner sc) throws SQLException, InterruptedException {
        limparTela();
        System.out.println("---------------------------------");
        System.out.println("1. Visualizar alunos             ");
        System.out.println("2. Visualizar instrutores        ");
        System.out.println("3. Cadastrar alunos              ");
        System.out.println("4. Cadastrar avaliação de aluno  ");
        System.out.println("5. Cadastrar treino de aluno     ");
        System.out.println("6. Visualizar os avisos          ");
        System.out.println("7. Voltar para o menu            ");
        System.out.println("---------------------------------");
        System.out.println("Escolha uma opção: ");
        int escolha = sc.nextInt();
        sc.nextLine(); 

        switch (escolha) {
            case 1:
                AlunoDAO.listarAluno();
                break;
            case 2:
                InstrutorDAO.listarInstrutores();
                break;
            case 3:
                AlunoDAO.cadastrarAluno();
                break;
            case 4:
                AvaliacaoDAO.cadastrarAvaliacao();
                break;
            case 5:
                TreinoDAO.cadastrarTreino();
            case 6:
                AvisosDAO.listarAvisos();
                break;
            default:
                opcaoInvalida();
        }
    }
    
    public static void gerenciarAvisos(Scanner sc) throws SQLException, InterruptedException {
        limparTela();
        System.out.println("------------------------------");
        System.out.println("1. Cadastrar Aviso          ");
        System.out.println("2. Listar Avisos            ");
        System.out.println("3. Remover Aviso            ");
        System.out.println("4. Editar Aviso             ");
        System.out.println("5. Voltar para o menu       ");
        System.out.println("------------------------------");
        System.out.println("Escolha uma opção: ");
        int escolha = sc.nextInt();
        sc.nextLine(); 

        switch (escolha) {
            case 1:
                AvisosDAO.cadastrarAviso();
                break;
            case 2:
                AvisosDAO.listarAvisos();
                break;
            case 3:
                AvisosDAO.removerAviso();
                break;
            case 4:
                AvisosDAO.editarAviso();
                break;
            case 5:
                break;
            default:
                opcaoInvalida();
        }
    }

    public static void gerenciarAlunos(Scanner sc) throws SQLException, InterruptedException {
        limparTela();
        System.out.println("------------------------------");
        System.out.println("1. Cadastrar login para Aluno ");
        System.out.println("2. Cadastrar Aluno            ");
        System.out.println("3. Listar Alunos              ");
        System.out.println("4. Remover Aluno              ");
        System.out.println("5. Editar Aluno               ");
        System.out.println("6. Voltar para o menu         ");
        System.out.println("------------------------------");
        System.out.println("Escolha uma opção: ");
        int escolha = sc.nextInt();
        sc.nextLine(); 

        switch (escolha) {
            case 1:
                AlunoDAO.cadastrarUsuarioAluno();
                break;
            case 2:
                AlunoDAO.cadastrarAluno();
                break;
            case 3:
                AlunoDAO.listarAluno();
                break;
            case 4:
                AlunoDAO.removerAluno();
                break;
            case 5:
                AlunoDAO.editarAluno();
                break;
            case 6:
                break;
            default:
                opcaoInvalida();
        }
    }

    public static void gerenciarInstrutores(Scanner sc) throws SQLException, InterruptedException {
        limparTela();
        System.out.println("------------------------------");
        System.out.println("1. Cadastrar Instrutor       ");
        System.out.println("2. Listar Instrutores        ");
        System.out.println("3. Remover Instrutor         ");
        System.out.println("4. Editar Instrutor          ");
        System.out.println("5. Voltar para o menu        ");
        System.out.println("------------------------------");
        System.out.println("Escolha uma opção: ");
        int escolha = sc.nextInt();
        sc.nextLine();

        switch (escolha) {
            case 1:
                InstrutorDAO.cadastrarInstrutor();
                break;
            case 2:
                InstrutorDAO.listarInstrutores();
                break;
            case 3:
                InstrutorDAO.removerInstrutor();
                break;
            case 4:
                InstrutorDAO.editarInstrutor();
                break;
            case 5:
                break;
            default:
                opcaoInvalida();
        }
    }

    public static void gerenciarFuncionarios(Scanner sc) throws SQLException, InterruptedException {
        limparTela();
        System.out.println("------------------------------");
        System.out.println("1. Cadastrar Funcionário     ");
        System.out.println("2. Listar Funcionários       ");
        System.out.println("3. Remover Funcionário       ");
        System.out.println("4. Editar Funcionário        ");
        System.out.println("5. Voltar para o menu        ");
        System.out.println("------------------------------");
        System.out.println("Escolha uma opção: ");
        int escolha = sc.nextInt();

        switch (escolha) {
            case 1:
                FuncionarioDAO.cadastrarFuncionario();
                break;
            case 2:
                FuncionarioDAO.listarFuncionarios();
                break;
            case 3:
                FuncionarioDAO.removerFuncionario();
                break;
            case 4:
                FuncionarioDAO.editarFuncionario();
                break;
            case 5:
                break;
            default:
                opcaoInvalida();
        }
    }

    public static void gerenciarPlano(Scanner sc) throws SQLException, InterruptedException {
        limparTela();
        System.out.println("------------------------------");
        System.out.println("1. Cadastrar plano     ");
        System.out.println("2. Listar planos       ");
        System.out.println("3. Remover plano       ");
        System.out.println("4. Editar plano        ");
        System.out.println("5. Voltar para o menu        ");
        System.out.println("------------------------------");
        System.out.println("Escolha uma opção: ");
        int escolha = sc.nextInt();

        switch (escolha) {
            case 1:
                PlanoDAO.cadastrarPlano();
            case 2:
                PlanoDAO.listarPlanos();
                break;
            case 3:
                PlanoDAO.removerPlano();
                break;
            case 4:
                PlanoDAO.editarPlano();
                break;
            case 5:
                break;
            default:
                opcaoInvalida();
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

    public static void opcaoInvalida() throws InterruptedException {
        System.out.print("Opção inválida, voltando ao menu em");
        for (int i = 3; i > 0; i--) {
            System.out.print(" " + i);
            Thread.sleep(1000);
        }
        System.out.println();
    }
}
