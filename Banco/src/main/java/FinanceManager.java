import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FinanceManager {
    private static final String URL = "jdbc:sqlite:financas.db";

    // Conectar ao banco de dados
    private Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco de dados: " + e.getMessage());
        }
        return conn;
    }

    // Criar tabela de transações
    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS transacoes (\n"
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " descricao TEXT NOT NULL,\n"
                + " valor REAL NOT NULL,\n"
                + " receita BOOLEAN NOT NULL\n"
                + ");";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Erro ao criar a tabela: " + e.getMessage());
        }
    }

    // Adicionar uma nova transação
    public void adicionarTransacao(String descricao, double valor, boolean receita) {
        String sql = "INSERT INTO transacoes(descricao, valor, receita) VALUES(?, ?, ?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, descricao);
            pstmt.setDouble(2, valor);
            pstmt.setBoolean(3, receita);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao adicionar a transação: " + e.getMessage());
        }
    }

    // Listar todas as transações
    public List<Transacao> listarTransacoes() {
        String sql = "SELECT id, descricao, valor, receita FROM transacoes";
        List<Transacao> transacoes = new ArrayList<>();

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Transacao transacao = new Transacao(
                        rs.getInt("id"),
                        rs.getString("descricao"),
                        rs.getDouble("valor"),
                        rs.getBoolean("receita")
                );
                transacoes.add(transacao);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar as transações: " + e.getMessage());
        }

        return transacoes;
    }

    // Calcular o saldo
    public double calcularSaldo() {
        List<Transacao> transacoes = listarTransacoes();
        double saldo = 0;
        for (Transacao transacao : transacoes) {
            if (transacao.isReceita()) {
                saldo += transacao.getValor();
            } else {
                saldo -= transacao.getValor();
            }
        }
        return saldo;
    }

    // Visualizar relatório financeiro
    public void visualizarRelatorio() {
        double totalReceitas = 0;
        double totalDespesas = 0;
        List<Transacao> transacoes = listarTransacoes();

        for (Transacao transacao : transacoes) {
            if (transacao.isReceita()) {
                totalReceitas += transacao.getValor();
            } else {
                totalDespesas += transacao.getValor();
            }
        }

        double saldo = calcularSaldo();

        System.out.println("Relatório Financeiro:");
        System.out.println("Total de Receitas: R$ " + totalReceitas);
        System.out.println("Total de Despesas: R$ " + totalDespesas);
        System.out.println("Saldo Final: R$ " + saldo);
    }

    public static void main(String[] args) {
        FinanceManager manager = new FinanceManager();
        manager.createTable(); // Criar a tabela no banco de dados

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Adicionar Receita");
            System.out.println("2. Adicionar Despesa");
            System.out.println("3. Listar Transações");
            System.out.println("4. Visualizar Relatório");
            System.out.println("5. Sair");
            System.out.print("Escolha uma opção: ");
            int opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir nova linha

            if (opcao == 1) {
                System.out.print("Descrição da Receita: ");
                String descricao = scanner.nextLine();
                System.out.print("Valor da Receita: ");
                double valor = scanner.nextDouble();
                manager.adicionarTransacao(descricao, valor, true);
            } else if (opcao == 2) {
                System.out.print("Descrição da Despesa: ");
                String descricao = scanner.nextLine();
                System.out.print("Valor da Despesa: ");
                double valor = scanner.nextDouble();
                manager.adicionarTransacao(descricao, valor, false);
            } else if (opcao == 3) {
                List<Transacao> transacoes = manager.listarTransacoes();
                for (Transacao transacao : transacoes) {
                    System.out.println(transacao);
                }
            } else if (opcao == 4) {
                manager.visualizarRelatorio();
            } else if (opcao == 5) {
                break;
            } else {
                System.out.println("Opção inválida. Tente novamente.");
            }
        }
        scanner.close();
    }
}
