import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PessoaDB {

	private final static String NOME_DB = "pessoa";
	private final static String USUARIO_DB = "SA";
	private final static String SENHA_DB = "";

	private final static String INSERIR_QUERY = "INSERT INTO " + NOME_DB + " (nome,cpf,data_nasc) VALUES (?,?,?)";
	private final static String ATUALIZAR_QUERY = "UPDATE " + NOME_DB
			+ " set nome=?, cpf=?, data_nasc=? WHERE pessoa_id=?";
	private final static String DELETAR_QUERY = "DELETE FROM " + NOME_DB + " WHERE pessoa_id=?";
	private final static String DELETAR_TODOS_QUERY = "DELETE FROM " + NOME_DB;
	private final static String SELECIONAR_TODOS_QUERY = "SELECT * FROM " + NOME_DB;

	private Connection conexaoDB;
	private PreparedStatement statement;

	public boolean conectar() {
		try {
			conexaoDB = DriverManager.getConnection("jdbc:hsqldb:mem:" + NOME_DB, USUARIO_DB, SENHA_DB);
			conexaoDB.prepareStatement("set database sql syntax mys true").execute();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println("Problema ao se conectar o Banco de Dados!");
			return false;
		}
	}
	
	public boolean createTable(){
		try {
			statement = conexaoDB.prepareStatement(
					"CREATE TABLE IF NOT EXISTS pessoa("
					+ "pessoa_id int auto_increment primary key,"
					+ "nome varchar(45) not null,"
					+ "cpf varchar(45) not null,"
					+ "data_nasc varchar(45) not null)");
			statement.execute();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Problema ao criar tabela no Banco de Dados!");
			return false;
		}
	}

	public boolean sair() {
		try {
			conexaoDB.close();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println("Problema ao encerrar conexão com o Banco de Dados!");
			return false;
		}
	}

	public boolean inserir(Pessoa pessoa) {
		try {
			statement = conexaoDB.prepareStatement(INSERIR_QUERY);
			statement.setString(1, pessoa.getNome());
			statement.setString(2, pessoa.getCpf());
			statement.setString(3, pessoa.getDataNascimento());
			statement.execute();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block			
			e.printStackTrace();
			System.err.println("Problema ao inserir no Banco de Dados!");
			return false;
		}
	}

	public boolean atualizar(Pessoa pessoa) {
		try {
			statement = conexaoDB.prepareStatement(ATUALIZAR_QUERY);
			statement.setString(1, pessoa.getNome());
			statement.setString(2, pessoa.getCpf());
			statement.setString(3, pessoa.getDataNascimento());
			statement.setInt(4, pessoa.getId());
			statement.execute();
			conexaoDB.commit();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			try {
				conexaoDB.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.err.println("Problema ao atualizar no Banco de Dados!");
			return false;
		}
	}

	public boolean deletarPorId(int pessoa_id) {
		try {
			statement = conexaoDB.prepareStatement(DELETAR_QUERY);
			statement.setInt(1, pessoa_id);
			statement.execute();
			conexaoDB.commit();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			try {
				conexaoDB.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.err.println("Problema ao deletar por ID no Banco de Dados!");
			return false;
		}
	}

	public boolean deletarTodos() {
		try {
			statement = conexaoDB.prepareStatement(DELETAR_TODOS_QUERY);
			statement.execute();
			conexaoDB.commit();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			try {
				conexaoDB.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.err.println("Problema ao deletar todos no Banco de Dados!");
			return false;
		}
	}

	public List<Pessoa> selecionarTodos() {
		List<Pessoa> pessoas = new ArrayList<>();
		try {
			ResultSet rs = conexaoDB.createStatement().executeQuery(SELECIONAR_TODOS_QUERY);
			while (rs.next()) {
				Pessoa pessoa = new Pessoa();
				pessoa.setId(rs.getInt("pessoa_id"));
				pessoa.setNome(rs.getString("nome"));
				pessoa.setCpf(rs.getString("cpf"));
				pessoa.setDataNascimento(rs.getString("data_nasc"));
				pessoas.add(pessoa);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println("Problema ao selecionar todos no Banco de Dados!");
		}
		return pessoas;
	}

	public Pessoa selecionarPeloId(int pessoa_id) {
		Pessoa pessoa = null;
		try {
			ResultSet rs = conexaoDB.createStatement().executeQuery(SELECIONAR_TODOS_QUERY);
			while (rs.next()) {
				if (rs.getInt("pessoa_id") == pessoa_id) {
					pessoa = new Pessoa();
					pessoa.setId(rs.getInt("pessoa_id"));
					pessoa.setNome(rs.getString("nome"));
					pessoa.setCpf(rs.getString("cpf"));
					pessoa.setDataNascimento(rs.getString("data_nasc"));
					break;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println("Problema ao selecionar pelo ID no Banco de Dados!");
		}
		return pessoa;
	}

}
