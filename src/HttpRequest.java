import java.net.Socket;

public class HttpRequest implements Runnable {
	
	final static String CRLF = "\r\n";

	Socket socket;

	// Construtor
	public HttpRequest(Socket socket) throws Exception {
		this.socket = socket;
	}

	// Implemente o método run() da interface Runnable.
	@Override
	public void run() {
		try {
			processRequest();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private void processRequest() throws Exception {
		// TODO
		// Obter uma referência para os trechos de entrada e saída do socket.

		//InputStream is = ?;

		//DataOutputStream os = ?;

		// Ajustar os filtros do trecho de entrada.

		// ?

		// BufferedReader br = ?;

		// TODO
		
		
		// Obter a linha de requisição da mensagem de requisição HTTP.
		String requestLine = "?";
		// Exibir a linha de requisição.
		System.out.println();
		System.out.println(requestLine);
		
		// Obter e exibir as linhas de cabeçalho.

		String headerLine = null;

		/*while ((headerLine = br.readLine()).length() != 0) {
			System.out.println(headerLine);
		}*/
		
		// Fechando tudo e socket...
		//os.close();
		//br.close();
		//socket.close();
	}
}