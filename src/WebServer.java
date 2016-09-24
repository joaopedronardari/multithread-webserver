import java.io.File;
import java.io.FileNotFoundException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Base64;

public class WebServer {
	static String configFile = "webserver.cfg";
	
	//Configurações do WebServer
	public static int shouldListDirectoryContent;
	static String authUser;
	static String authPassword;
	static ArrayList<String> restrictedDirectories;
	
	public static void main(String[] args) throws Exception {
		
		// Ajustar o numero da porta.
		int port = 6789;
		
		// Estabelecer o socket de escuta.
		ServerSocket socket = new ServerSocket(port);
		
		// Lê arquivo de configuracao
		readConfigFile();

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
	 * Metodo para listagem de arquivos 
	 * e diretorios do servidor
	 * @param directoryPath - caminho do diretorio para listagem
	 * @return List<String> lista de arquivos e diretorios do caminho passado
	 */
	public static List<String> listFilesAndDirectories(String directoryPath) {
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
	
	/**
	 * Metodo para fazer parsing do arquivo de configuracao
	 */
	static void readConfigFile(){
		File cfg = new File(configFile);
		
		//Seta valores padrao
		shouldListDirectoryContent = 3;
		authUser = "";
		authPassword = "";
		restrictedDirectories = new ArrayList<String>();
		
		try {
			Scanner in = new Scanner(cfg);
			while(in.hasNext()){
				String line = in.nextLine();
				
				//Pula comentários
				if(line.startsWith("#"))
					continue;
				
				//Faz parsing da linha
				String values[] = line.split(":");
				if(values.length == 2)
				{
					switch(values[0])
					{
					case "list_directory_content":
						shouldListDirectoryContent = Integer.parseInt(values[1]);
						break;
					case "auth_username":
						authUser = values[1];
						break;
					case "auth_password":
						authPassword = values[1];
						break;
					case "restricted_directory":
						restrictedDirectories.add(values[1]);
						System.out.println("Adding: " + values[1]);
						break;
					default:
						System.out.println("Comando inválido! " + values[0]);
					}
				} else
				{
					System.out.println("Linha inválida de configuração! " + line);
				}	
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("Arquivo de configuração não encontrado!");
		}
	}
	
	/**
	 * Verifica se diretorio e restrito
	 * @return diretorio e restrito?
	 */
	public static boolean dirIsRestricted(String filename)
	{
		System.out.println("Testing: " + filename);
		return restrictedDirectories.contains(filename);
	}
	
	/**
	 * Verifica login e senha do usuario
	 * @return usuario foi autorizado?
	 */
	public static boolean authenticate(String token)
	{
		Base64.Encoder encoder = Base64.getEncoder();
		String serverString = authUser+":"+authPassword;
		String serverToken = encoder.encodeToString(serverString.getBytes());
		return serverToken.compareTo(token) == 0;
	}
}
