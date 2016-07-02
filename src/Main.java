import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class Main {

	private final static int CADASTRAR_PESSOA = 1; 
	private final static int ALTERAR_PESSOA = 2;
	private final static int DELETAR_PESSOA = 3;
	private final static int LISTAR_PESSOAS = 4;
	private final static int SAIR = 5;
	
	private static PessoaDB db;

	public static void main(String[] args) {

		
		System.out.println("Inicializando o sistema...");
		db = new PessoaDB();
		System.out.println("Conectando ao BD...");
		if (db.conectar()) {
			System.out.println("Conectado ao BD com sucesso!");
			if(db.createTable()){
				System.out.println("Tabela criada com sucesso!");				
			}
		} 
		int op;
		while (true) {
			showMenu();
			Scanner prompt = new Scanner(System.in);
			op = prompt.nextInt();
			switch (op) {
			case CADASTRAR_PESSOA:
				cadastrarPessoa(prompt);	
				break;
			case ALTERAR_PESSOA:
				alterarPessoa(prompt);
				break;
			case DELETAR_PESSOA:
				deletarPessoa(prompt);
				break;
			case LISTAR_PESSOAS:
				listarPessoas();
				break;
			case SAIR:
				System.exit(0);
				break;
			default:
				System.out.println("Opção inválida!");
				break;
			}
		}

	}

	private static void showMenu() {
		System.out.println("");
		System.out.println("*** SISTEMA CRUD de Pessoas ***");
		System.out.println("(1) - Cadastrar Pessoa");
		System.out.println("(2) - Editar Pessoa");
		System.out.println("(3) - Deletar Pessoa");
		System.out.println("(4) - Listar Pessoas");
		System.out.println("(5) - Sair");
		System.out.println("");
		System.out.print("Opção:\t");
	}

	private static void cadastrarPessoa(Scanner scanner) {			
		System.out.println("");
		System.out.println("*** Cadastrar  Pessoa ***");
		System.out.print("Nome:\t");
		scanner.reset();
		String nome = scanner.next();
		System.out.print("CPF:\t");
		scanner.reset();
		String cpf = scanner.next();		
		System.out.print("Data de Nascimento:\t");
		scanner.reset();
		String dataNascimento = scanner.next();

		Pessoa pessoa = new Pessoa(nome, cpf, dataNascimento);
		if (db.inserir(pessoa)) {
			System.out.println("Pessoa cadasrada com sucesso!");
		} else {
			System.out.println("Erro no processo de cadastro!");
		}
	}

	private static void alterarPessoa(Scanner scanner) {
		System.out.println("");
		System.out.println("*** Editar Pessoa ***");
		System.out.print("cod:\t");
		scanner.reset();
		int cod = scanner.nextInt();
		Pessoa pessoa = db.selecionarPeloId(cod);
		System.out.println("Obs:Usar dígito '0' para indicar que não deseja alterar o valor atual do campo");
		System.out.print("Nome:\t");
		scanner.reset();
		String nome = scanner.next();		
		System.out.print("CPF:\t");
		scanner.reset();
		String cpf = scanner.next();		
		System.out.print("Data de Nascimento:\t");
		scanner.reset();
		String dataNascimento = scanner.next();
		if(!cpf.equalsIgnoreCase("0")){
			pessoa.setCpf(cpf);
		}
		if(!nome.equalsIgnoreCase("0")){
			pessoa.setNome(nome);
		}
		if(!dataNascimento.equalsIgnoreCase("0")){
			pessoa.setDataNascimento(dataNascimento);
		}
		db.atualizar(pessoa);
	}

	private static void deletarPessoa(Scanner scanner) {
		System.out.println("");
		System.out.println("*** Editar Pessoa ***");
		System.out.print("cod:\t");
		scanner.reset();
		int cod = scanner.nextInt();
		if(db.deletarPorId(cod)){
			System.out.println("Pessoa deletada com sucesso!");
		}
	}

	private static void listarPessoas() {
		System.out.println("");
		System.out.println("*** Lista de Pessoas ***");
		System.out.println("");
		System.out.println("cod  |   nome   |       cpf       |  data nascimento");
		for(Pessoa pessoa: db.selecionarTodos()){
			System.out.println(pessoa.getId()+"    |  "+pessoa.getNome()+"  |  "+pessoa.getCpf()+"  |  "+pessoa.getDataNascimento());
		}
	}
	

}
