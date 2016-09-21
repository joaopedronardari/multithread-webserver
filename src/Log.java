import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

/**
 * Classe auxiliar para gerenciar o log do sistema
 */
public class Log {
	
	// Nome do arquivo de log
	private static final String fileName = "./log_sistema.txt";
	
	// Classe estatica, removido construtor
	private Log() {}
	
	/**
	 * Metodo para persistir nova linha de log do sistema
	 * @param originAddress - Endereco de origem
	 * @param requestHour - Horario de Requisicao
	 * @param requestContent - Conteudo requisitado
	 * @param bytesSize - Quantidade de Bytes Transmitidos
	 */
	public static void persistLogOperation(String originAddress,
			String requestContent, int bytesSize) {
		try {
			// Abre ou cria arquivo com o FileWriter
			FileWriter file = new FileWriter(fileName,true);
			
			// Formata linha de log
			String logLine = String.format("|%1s|from %2s|content: %3s|%4d bytes|", getServerHour(), 
					originAddress, 
					requestContent, bytesSize);
			
			// Adiciona linha de log
			file.append(logLine);
			file.append(System.getProperty("line.separator"));
			
			// Fecha arquivo
			file.close();
		} catch (IOException e) {
			System.out.println("Falha ao guardar log " + e.getMessage());
		}
	}
	
	/**
	 * Metodo para obter String de horario atual do servidor
	 * @return horario do servidor
	 */
	private static String getServerHour() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);
	}
	
	/**
	 * Metodo que retorna todo o log
	 * @return String do log
	 */
	public static String getAllLog() {
		return "";
	}
	
}
