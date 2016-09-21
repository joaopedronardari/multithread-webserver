import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {
	public static void main(String[] args) throws Exception {
		
		// Ajustar o numero da porta.
		int port = 6789;
		
		// Estabelecer o socket de escuta.
		ServerSocket socket = new ServerSocket(port);

		// Processar a requisicao de servico HTTP em um laco infinito.
		while (true) {
			Socket connectionSocket = socket.accept();
            
			// Escutar requisicao de conexão TCP.
			
			/** Quando receber requisição **/
			//Construir um objeto para processar a mensagem de requisicao HTTP.
			HttpRequest request = new HttpRequest(connectionSocket);

			// Criar um novo thread para processar a requisicao.
			Thread thread = new Thread(request);
			
			//Iniciar o thread.
			thread.start();
		}
		
		
	}
}
