import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class WebServer {
	public static void main(String[] args) throws Exception {
		
		// Ajustar o numero da porta.
		int port = 6789;
		
		// Estabelecer o socket de escuta.
		ServerSocket socket = new ServerSocket(port);

		// Processar a requisicao de servico HTTP em um laco infinito.
		while (true) {
			Socket connectionSocket = socket.accept();
            
			// Escutar requisicao de conexao TCP.
			
			/** Quando receber requisicao **/
			//Construir um objeto para processar a mensagem de requisicao HTTP.
			HttpRequest request = new HttpRequest(connectionSocket);

			// Criar um novo thread para processar a requisicao.
			Thread thread = new Thread(request);
			
			//Iniciar o thread.
			thread.start();
		}
		
		
	}
	
	/**
	 * Metodo para auxiliar listagem de arquivos 
	 * e diretorios do servidor
	 * @return lista de arquivos e diretorios da raiz do servidor
	 */
	public static List<String> listFilesAndDirectories(String diretorio) {
		File folder = new File(diretorio);
		File[] listOfFiles = folder.listFiles();
		List<String> paths = new ArrayList<String>();

	    for (int i = 0; i < listOfFiles.length; i++) {
	      if (listOfFiles[i].isFile()) {
	    	paths.add(listOfFiles[i].getName());
	      } else if (listOfFiles[i].isDirectory()) {
	    	paths.add(listOfFiles[i].getName() + "/");
	      }
	    }
	    
	    return paths;
	}
}
