package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import Conexao.ConnectionBD;

public class AvaliacaoDAO {
    
    // Método para cadastrar uma nova avaliação física
    public static void cadastrarAvaliacao() throws SQLException {
        try (Scanner sc = new Scanner(System.in);
             Connection conector = ConnectionBD.getInstanciador().getConector()) {
            char minusculo = 's';
            char alternativa;
            do {
                limparTela();
                System.out.print("\nInsira o peso (em kg): ");
                double peso = sc.nextDouble();
               
                System.out.print("Insira a altura (em metros): ");
                double altura = sc.nextDouble();
                
                System.out.print("Insira a medida do peito (em cm): ");
                double medidaPeito = sc.nextDouble();

                System.out.print("Insira a medida da cintura (em cm): ");
                double medidaCintura = sc.nextDouble();
                
                System.out.print("Insira a medida do quadril (em cm): ");
                double medidaQuadril = sc.nextDouble();
                
                System.out.print("Insira a medida do bíceps direito (em cm): ");
                double medidaBicepsDireito = sc.nextDouble();
                
                System.out.print("Insira a medida do bíceps esquerdo (em cm): ");
                double medidaBicepsEsquerdo = sc.nextDouble();
                
                System.out.print("Insira a medida da coxa direita (em cm): ");
                double medidaCoxaDireita = sc.nextDouble();
                
                System.out.print("Insira a medida da coxa esquerda (em cm): ");
                double medidaCoxaEsquerda = sc.nextDouble();
                
                System.out.print("Insira a medida da panturrilha direita (em cm): ");
                double medidaPanturrilhaDireita = sc.nextDouble();
                
                System.out.print("Insira a medida da panturrilha esquerda (em cm): ");
                double medidaPanturrilhaEsquerda = sc.nextDouble();
                
                try {
                    String maxIdSql = "SELECT MAX(id) FROM avaliacoes";
                    try (PreparedStatement maxIdInstrucao = conector.prepareStatement(maxIdSql);
                         ResultSet maxIdResultado = maxIdInstrucao.executeQuery()) {
                        int novoId = 1; 
                        if (maxIdResultado.next()) {
                            novoId = maxIdResultado.getInt(1) + 1;
                        }
                        String sql = "INSERT INTO avaliacoesbd (id, peso, altura, medida_peito, medida_cintura, medida_quadril, medida_biceps_direito, medida_biceps_esquerdo, medida_coxa_direita, medida_coxa_esquerda, medida_panturrilha_direita, medida_panturrilha_esquerda) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
                        try (PreparedStatement instrucao = conector.prepareStatement(sql)) {
                            instrucao.setInt(1, novoId);
                            instrucao.setDouble(2, peso);
                            instrucao.setDouble(3, altura);
                            instrucao.setDouble(4, medidaPeito);
                            instrucao.setDouble(5, medidaCintura);
                            instrucao.setDouble(6, medidaQuadril);
                            instrucao.setDouble(7, medidaBicepsDireito);
                            instrucao.setDouble(8, medidaBicepsEsquerdo);
                            instrucao.setDouble(9, medidaCoxaDireita);
                            instrucao.setDouble(10, medidaCoxaEsquerda);
                            instrucao.setDouble(11, medidaPanturrilhaDireita);
                            instrucao.setDouble(12, medidaPanturrilhaEsquerda);
                            instrucao.executeUpdate();
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                
                System.out.print("\n\nDeseja cadastrar uma nova avaliação física? (s/n): ");
                alternativa = sc.next().charAt(0);
                sc.nextLine();
                minusculo = Character.toLowerCase(alternativa);
                minusculo = confirmandoCaractere(minusculo, alternativa);
            } while (minusculo == 's');
        }
    }
    
    // Método para listar todas as avaliações físicas cadastradas
    public static void listarAvaliacoes() {
        try (Connection conector = ConnectionBD.getInstanciador().getConector()) {
            String sql = "SELECT id, peso, altura, medida_peito, medida_cintura, medida_quadril, medida_biceps_direito, medida_biceps_esquerdo, medida_coxa_direita, medida_coxa_esquerda, medida_panturrilha_direita, medida_panturrilha_esquerda FROM avaliacoesbd ORDER BY id";
    
            try (PreparedStatement instrucao = conector.prepareStatement(sql);
                 ResultSet resultado = instrucao.executeQuery()) {
    
                System.out.println("--------- |Avaliações físicas cadastradas| ---------");
                System.out.println("ID\tPeso\tAltura\tPeito\tCintura\tQuadril\tBíceps Dir.\tBíceps Esq.\tCoxa Dir.\tCoxa Esq.\tPanturrilha Dir.\tPanturrilha Esq.");
                
                while (resultado.next()) {
                    int id = resultado.getInt("id");
                    double peso = resultado.getDouble("peso");
                    double altura = resultado.getDouble("altura");
                    double medidaPeito = resultado.getDouble("medida_peito");
                    double medidaCintura = resultado.getDouble("medida_cintura");
                    double medidaQuadril = resultado.getDouble("medida_quadril");
                    double medidaBicepsDireito = resultado.getDouble("medida_biceps_direito");
                    double medidaBicepsEsquerdo = resultado.getDouble("medida_biceps_esquerdo");
                    double medidaCoxaDireita = resultado.getDouble("medida_coxa_direita");
                    double medidaCoxaEsquerda = resultado.getDouble("medida_coxa_esquerda");
                    double medidaPanturrilhaDireita = resultado.getDouble("medida_panturrilha_direita");
                    double medidaPanturrilhaEsquerda = resultado.getDouble("medida_panturrilha_esquerda");
    
                    System.out.println(id + "\t" + peso + "\t" + altura + "\t" + medidaPeito + "\t" + medidaCintura + "\t" + medidaQuadril + "\t" + medidaBicepsDireito + "\t" + medidaBicepsEsquerdo + "\t" + medidaCoxaDireita + "\t" + medidaCoxaEsquerda + "\t" + medidaPanturrilhaDireita + "\t" + medidaPanturrilhaEsquerda);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para remover uma avaliação física pelo ID
    public static void removerAvaliacao() throws SQLException {
    try (Scanner sc = new Scanner(System.in);
         Connection conector = ConnectionBD.getInstanciador().getConector()) {
        System.out.print("Informe o ID da avaliação física que será removida: ");
        int id = sc.nextInt();
        
        String deleteSql = "DELETE FROM avaliacoesbd WHERE id = ?";
        
        try (PreparedStatement deleteInstrucao = conector.prepareStatement(deleteSql)) {
            deleteInstrucao.setInt(1, id);
            int linhasAfetadas = deleteInstrucao.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Avaliação física removida com sucesso!");
            } else {
                System.out.println("Nenhuma avaliação física encontrada com o ID informado.");
            }
        } catch(SQLException e) {
            e.printStackTrace();
        } 
    }
}

// Método para editar uma avaliação física pelo ID
    public static void editarAvaliacao() throws SQLException {
    try (Scanner sc = new Scanner(System.in);
         Connection conector = ConnectionBD.getInstanciador().getConector()) {
        char minusculo = 's';
        char alternativa;
        do {
            limparTela();
            System.out.print("Informe o ID da avaliação física que será atualizada: ");
            int id = sc.nextInt();
            
            try {
                String sql = "UPDATE avaliacoesbd SET peso = ?, altura = ?, medida_peito = ?, medida_cintura = ?, medida_quadril = ?, medida_biceps_direito = ?, medida_biceps_esquerdo = ?, medida_coxa_direita = ?, medida_coxa_esquerda = ?, medida_panturrilha_direita = ?, medida_panturrilha_esquerda = ? WHERE id = ?";

                try (PreparedStatement instrucao = conector.prepareStatement(sql)) {
                    System.out.print("Informe o peso (em kg): ");
                    double peso = sc.nextDouble();
                    System.out.print("Informe a altura (em metros): ");
                    double altura = sc.nextDouble();
                    System.out.print("Informe a medida do peito (em cm): ");
                    double medidaPeito = sc.nextDouble();
                    System.out.print("Informe a medida da cintura (em cm): ");
                    double medidaCintura = sc.nextDouble();
                    System.out.print("Informe a medida do quadril (em cm): ");
                    double medidaQuadril = sc.nextDouble();
                    System.out.print("Informe a medida do bíceps direito (em cm): ");
                    double medidaBicepsDireito = sc.nextDouble();
                    System.out.print("Informe a medida do bíceps esquerdo (em cm): ");
                    double medidaBicepsEsquerdo = sc.nextDouble();
                    System.out.print("Informe a medida da coxa direita (em cm): ");
                    double medidaCoxaDireita = sc.nextDouble();
                    System.out.print("Informe a medida da coxa esquerda (em cm): ");
                    double medidaCoxaEsquerda = sc.nextDouble();
                    System.out.print("Informe a medida da panturrilha direita (em cm): ");
                    double medidaPanturrilhaDireita = sc.nextDouble();
                    System.out.print("Informe a medida da panturrilha esquerda (em cm): ");
                    double medidaPanturrilhaEsquerda = sc.nextDouble();

                    instrucao.setDouble(1, peso);
                    instrucao.setDouble(2, altura);
                    instrucao.setDouble(3, medidaPeito);
                    instrucao.setDouble(4, medidaCintura);
                    instrucao.setDouble(5, medidaQuadril);
                    instrucao.setDouble(6, medidaBicepsDireito);
                    instrucao.setDouble(7, medidaBicepsEsquerdo);
                    instrucao.setDouble(8, medidaCoxaDireita);
                    instrucao.setDouble(9, medidaCoxaEsquerda);
                    instrucao.setDouble(10, medidaPanturrilhaDireita);
                    instrucao.setDouble(11, medidaPanturrilhaEsquerda);
                    instrucao.setInt(12, id);
                    int linhasAfetadas = instrucao.executeUpdate();
                    if (linhasAfetadas > 0) {
                        System.out.println("Avaliação física atualizada com sucesso!");
                    } else {
                        System.out.println("Nenhuma avaliação física encontrada com o ID informado.");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            
            System.out.print("\n\nDeseja editar outra avaliação física? (s/n): ");
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
