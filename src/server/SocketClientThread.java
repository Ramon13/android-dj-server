package server;

import java.io.IOException;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

import controller.SocketRequest;
import util.RequestType;

/**
 * 
 * @author ramoncosta
 * @version 1.0
 *	handle new client sockets
 */
public class SocketClientThread extends Thread {
	
	/**
	 * Objeto java.net.Socket, obtém o valor via construtor da classe.
	 */
	private static Socket socket;
	
	/**
	 * 
	 * @param socket
	 */
	public SocketClientThread() {
		
	}
	
	public static SocketClientThread getNewSocketThread(Socket socket){
		SocketClientThread.socket = socket;
		return new SocketClientThread();
	}
	
	/**
	 * Método sobreescrito da classe java.lang.Thread.
	 * Armazena em uma variável somente a primeira linha da String enviada pela aplicação cliente.
	 */
	@Override
	public void run() {
		try(Scanner socketInput = new Scanner(socket.getInputStream())){
			
			String requestData = socketInput.nextLine();
			processRequestData(requestData);
			
		}catch(IOException | NoSuchElementException e){
			e.printStackTrace();
			
		}finally {
			try {
				if(socket != null)
					socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
	}
	
	private void processRequestData(String requestData) throws IOException{
		if(requestData.startsWith("GET"))
			new SocketRequest().processRequestData(RequestType.HTTP, requestData);
		else
			new SocketRequest().processRequestData(RequestType.SOCKET, requestData);
	}
	
	
	


}
