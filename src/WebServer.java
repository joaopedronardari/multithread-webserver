import java.net.Socket;

public class WebServer {
	public static void main(String[] args) throws Exception {
		
		// Ajustar o número da porta.
		int port = 6789;
		
		// Estabelecer o socket de escuta.

		// TODO Criar socket - Falta implementação ainda
		Socket socket = new Socket();

		// Processar a requisição de serviço HTTP em um laço infinito.

		while (true) {
			
			// Escutar requisição de conexão TCP.
			// TODO
			
			/** Quando receber requisição **/
			//Construir um objeto para processar a mensagem de requisição HTTP.
			// FIXME verificar se não precisa de outros parametros...
			HttpRequest request = new HttpRequest(socket);

			// Criar um novo thread para processar a requisição.

			Thread thread = new Thread(request);

			//Iniciar o thread.

			thread.start();
		}
		
		
	}
}
