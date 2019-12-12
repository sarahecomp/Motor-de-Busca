package model;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import rmiInterface.InterfaceServerRMI;

/** Implementa os m�todos remotos do servidor RMI. Cada m�todo � uma solicita��o ao servidor multicast.
 * Na implementa��o dos m�todos o servidor RMI converte os dados de entrada para o protocolo do sistema e envia
 * via pacote UDP para o servidor multicast.
 */
public class ServerRMI extends UnicastRemoteObject implements InterfaceServerRMI {
	
	private ClienteUDP serverComunication;

	public ServerRMI() throws RemoteException, SocketException, UnknownHostException {
		serverComunication = new  ClienteUDP();
	}

	
	public boolean login(String username, String password) throws RemoteException, IOException {
		String request = "login|username;"+username + "|password;"+password+"|";
		String answer = this.makeRequest(request);
			
		if(answer.split("\\|")[1].split(";")[1].equals("true")) {
			return true;
		}
		
		return false;
	}

	
	public boolean registerUser(String username, String password) throws IOException {
		String request = "useRegistry|username;"+username + "|password;"+password+"|";
		String answer = this.makeRequest(request);
				
		if(answer.split("\\|")[1].split(";")[1].equals("true")) {
			return true;
		}
		
		return false;
		
	}

	@Override
	public boolean userIsAdmin(String username) throws RemoteException, IOException {
		String request = "userIsAdmin|username;"+username + "|";
		String answer = this.makeRequest(request);
		
		if(answer.split("\\|")[1].split(";")[1].equals("true")) {
			return true;
		}

		return false;
	}


	public boolean changeUserPermission(String username) throws IOException {
		String request = "changeUserPermission|username;"+username + "|";
		String answer = this.makeRequest(request);
		
		if(answer.split("\\|")[1].split(";")[1].equals("true")) {
			return true;
		}

		return false;
	}


	@Override
	public ArrayList<String> getHistoric(String username) throws IOException {
		String request = "getHistoric|username;"+username + "|";
		String[] answer = this.makeRequest(request).split("\\|");
		ArrayList<String> historic = new ArrayList<String>();
		
		if(answer.length < 3) {
			return null;
		}
		
		for(int i=3; i<answer.length;i++) {
			String time = "Data: "+answer[i].split(";")[0]+" Hora: "+answer[i].split(";")[1];
			String url = answer[i].split(";")[2];
			historic.add(time);
			historic.add(url);
			
		}
		
		return historic;
	}


	@Override
	public void addHistoric(String username, String date, String hour, String url) throws IOException {
		String request = "addHistoric|username;"+username + "|date;"+date+"|hour;"+hour+"|url;"+url+"|";
		this.makeRequestNoAnswer(request);
		
	}


	@Override
	public boolean userHasNotification(String username) throws IOException {
		String request = "userHasNotification|username;"+username + "|";
		String answer = this.makeRequest(request);

		if(answer.split("\\|")[1].equals("true")) {
			return true;
		}
		return false;
	}
	
	@Override
	public String getUserNotification(String username) throws RemoteException, IOException {
		String request = "getUserNotification|username;"+username + "|";
		String answer = this.makeRequest(request);
		

		return answer.split("\\|")[1].split(";")[1];
	}


	@Override
	public void removeUserNotification(String username) throws IOException {
		String request = "removeUserNotification|username;"+username + "|";
		makeRequestNoAnswer(request);
	}


	@Override
	public void indexURL(String url) throws IOException {
		String request = "indexURL|url;"+url+ "|";
		makeRequestNoAnswer(request);

	}


	@Override
	public ArrayList<String> getImportantPages() throws IOException {
		String request = "getImportantPages|";
		String[] answer = this.makeRequest(request).split("\\|");
		ArrayList<String> importantPages = new ArrayList<String>();
		
		if(answer.length < 3) {
			return null;
		}
		
		for(int i=3; i<answer.length; i++) {
			String url = answer[i].split(";")[0];
			String numAcesso = answer[i].split(";")[1];
			
			importantPages.add(url);
			importantPages.add(numAcesso);
		}
		
		return importantPages;
	}


	@Override
	public ArrayList<String> getImportantSearch() throws IOException {
		String request = "getImportantSearch|";
		String[] answer = this.makeRequest(request).split("\\|");
		ArrayList<String> importantSearch = new ArrayList<String>();
		
		if(answer.length < 3) {
			return null;
		}
		
		for(int i=3; i<answer.length; i++) {
			String search = answer[i].split(";")[0];
			String numAcesso = answer[i].split(";")[1];
			
			importantSearch.add(search);
			importantSearch.add(numAcesso);
		}
		

		return importantSearch;
	}
	
	@Override
	public ArrayList<String> search(String search) throws IOException {
		String request = "search|"+search+"|";
		String[] answer = this.makeRequest(request).split("\\|");
		
		ArrayList<String> result = new ArrayList<String>();
		
		
		for(int i=3; i<answer.length; i++) {
			if(answer[i].equals("") || !answer[i].contains(";"))
				continue;
			
			String title = answer[i].split(";")[0];
			String url = answer[i].split(";")[1];
			String text = answer[i].split(";")[2];

			
			result.add(title);
			result.add(url);
			result.add(text);

		}
		

		return result;
	}

	
	/** Envia uma solicita��o aos sevidores multicast.
	 * 
	 * @param request		Mensagem de solicita��o de acordo com o protocolo.
	 * @throws IOException	Exce��o lan�ada caso haja algum problema ao enviar a solicita��o. 	
	 */
	public String makeRequest(String request) throws IOException {
		serverComunication.sendPacket(request.getBytes());
		return waitAnswer();

	}
	
	/** Envia uma solicita��o aos sevidores multicast.
	 * 
	 * @param request		Mensagem de solicita��o de acordo com o protocolo.
	 * @throws IOException	Exce��o lan�ada caso haja algum problema ao enviar a solicita��o. 	
	 */
	public void makeRequestNoAnswer(String request) throws IOException {
		serverComunication.sendPacket(request.getBytes());

	}
	
	public String waitAnswer() throws IOException {
		byte[] buf = serverComunication.acceptPacket();
		return new String(buf);

	}


	



	


	


	

}
