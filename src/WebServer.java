import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {
	public static void main(String[] args) throws Exception {
		
		// Ajustar o número da porta.
		int port = 6789;
		
		// Estabelecer o socket de escuta.
		ServerSocket socket = new ServerSocket(port);

		// Processar a requisição de serviço HTTP em um laço infinito.
		while (true) {
			Socket connectionSocket = socket.accept();
            
			// Escutar requisição de conexão TCP.
			
			/** Quando receber requisição **/
			//Construir um objeto para processar a mensagem de requisição HTTP.
			HttpRequest request = new HttpRequest(connectionSocket);

			// Criar um novo thread para processar a requisição.
			Thread thread = new Thread(request);
			
			//Iniciar o thread.
			thread.start();
		}
		
		
	}
}
