import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
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
		// Obter uma referência para os trechos de entrada e saída do socket.
		InputStreamReader is = new InputStreamReader(socket.getInputStream());
		DataOutputStream os = new DataOutputStream(socket.getOutputStream());
        
		// Ajustar os filtros do trecho de entrada.
		// ?
		BufferedReader br = new BufferedReader(is);
        		
		// Obter a linha de requisição da mensagem de requisição HTTP.
		String requestLine = br.readLine();
		
		// Exibir a linha de requisição.
		System.out.println();
		System.out.println(requestLine);
		
		// Obter e exibir as linhas de cabeçalho.
		String headerLine = null;
		while ((headerLine = br.readLine()).length() != 0) {
			System.out.println(headerLine);
		}
		
        os.writeBytes("Respondendo qualquer coisa :P");
        
		// Fechando tudo e socket...
		os.close();
		br.close();
		socket.close();
	}
}