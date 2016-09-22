import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Classe que representa a requisicao HTTP
 */
public class HttpRequest implements Runnable {
	
	final static String CRLF = "\r\n";

	Socket socket;

	// Construtor
	public HttpRequest(Socket socket) throws Exception {
		this.socket = socket;
	}

	// Implemente o metodo run() da interface Runnable.
	@Override
	public void run() {
		try {
			processRequest();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	private String contentType( String filename )
	{
		if(filename.endsWith(".htm") || filename.endsWith(".html")) {
			return "text/html";
		}
		if(filename.endsWith(".gif")) {
			return "image/gif";
		}
		if(filename.endsWith(".jpg") || filename.endsWith(".jpeg")) {
			return "image/jpeg";
		}
		return "application/octet-stream";
	}
	
	private static void sendBytes(FileInputStream fis, OutputStream os)
	throws Exception
	{
		// Construir um buffer de 1K para comportar os bytes no caminho para o socket.
		byte[] buffer = new byte[1024];
		int bytes = 0;
		// Copiar o arquivo requisitado dentro da cadeia de saida do socket.
		while((bytes = fis.read(buffer)) != -1 ) {
			os.write(buffer, 0, bytes);
		}
	}


	private void processRequest() throws Exception {
		
		int bytesSize = socket.getInputStream().available();        
		
		// Obter uma referencia para os trechos de entrada e saida do socket.
		InputStreamReader is = new InputStreamReader(socket.getInputStream());
		DataOutputStream os = new DataOutputStream(socket.getOutputStream());
        
		// Ajustar os filtros do trecho de entrada.
		BufferedReader br = new BufferedReader(is);
        		
		// Obter a linha de requisicao da mensagem de requisicao HTTP.
		String requestLine = br.readLine();
		
		// Extrair o nome do arquivo a linha de requisicao.
		StringTokenizer tokens = new StringTokenizer(requestLine);
		tokens.nextToken(); // pular o metodo, que deve ser GET
		String fileName = tokens.nextToken();
		
		System.out.println(fileName);
		
		// Acrescente um . de modo que a requisicao do arquivo esteja dentro do diretorio atual.
		fileName = "." + fileName;
		
		// Captura endereco de quem fez a requisicao
		SocketAddress address = socket.getRemoteSocketAddress();
					
		// Log de requisicao
		Log.persistLogOperation(address.toString(), fileName, bytesSize);		
		
		// Abrir o arquivo requisitado.
		FileInputStream fis = null;
		Boolean fileExists = true;
		try {
			fis = new FileInputStream(fileName);
		} catch (FileNotFoundException e) {
			fileExists = false;
		}
		
		// Construir a mensagem de resposta.
		String statusLine = null;
		String contentTypeLine = null;
		String entityBody = null;
		if (fileExists) {
			statusLine = "HTTP/1.1 200 OK" + CRLF;
			contentTypeLine = "Content-Type: " + contentType( fileName ) + CRLF;
		} else {
			File f = new File(fileName);
			if (f.exists() && f.isDirectory()) {
				// FIXME - fiz uma versao paliativa so p testar...
				statusLine = "HTTP/1.1 200 OK" + CRLF;
				contentTypeLine = "Content-Type: text/html" + CRLF;
				entityBody = "<HTML>" +
					"<HEAD><TITLE>"+fileName+"</TITLE></HEAD>" +
					"<BODY>";
				
				List<String> paths = WebServer.listFilesAndDirectories(fileName);
				
				for (String path : paths) {
					entityBody += path + "<BR/>";
				}
				
				entityBody += "</BODY></HTML>" + CRLF;
				
			} else {
				statusLine = "HTTP/1.1 404 Not Found" + CRLF;
				contentTypeLine = "Content-Type: text/html" + CRLF;
				entityBody = "<HTML>" +
					"<HEAD><TITLE>Not Found</TITLE></HEAD>" +
					"<BODY>Not Found</BODY></HTML>" + CRLF;
			}
		}
        
		// Enviar a linha de status.
		os.writeBytes(statusLine);
		// Enviar a linha de tipo de conteudo.
		os.writeBytes(contentTypeLine);
		// Enviar uma linha em branco para indicar o fim das linhas de cabecalho.
		os.writeBytes(CRLF);
		
		// Enviar o corpo da entidade.
		if (fileExists) {
			sendBytes(fis, os);
			fis.close();
		} else {
			os.writeBytes(entityBody);
		}
		
        // Obter e exibir as linhas de cabecalho.
 		String headerLine = null;
 		while ((headerLine = br.readLine()).length() != 0) {
 			System.out.println(headerLine);
 		}
        
		// Fechando tudo e socket...
		os.close();
		br.close();
		socket.close();
	}
}