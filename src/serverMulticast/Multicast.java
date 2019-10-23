package serverMulticast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**A classe Multicast � respons�vel pela comunica��o multicast entre os servidores.
 * Nela est� configurado de forma est�tica o ip do grupo multicast como "230.0.0.0" e a porta para 4448.
 * 
 *  
 * */
public class Multicast {
	
	private static String ip_group = "230.0.0.0";
	private static int port = 4448;
	private MulticastSocket socket;
	private InetAddress group;
	
	/**Cria o socket multicast e se junta ao grupo. 
	 * 
	 * @throws IOException Lan�a exce��o caso o programa n�o consiga abrir um socket multicast na porta estabelecida.
	 **/
	public Multicast() throws IOException {
		socket = new MulticastSocket(port);
		group = InetAddress.getByName(ip_group);
		socket.joinGroup(group);
	}
	
	/**Aceita pacotes UDP do grupo multicast. 
	 * 
	 * @param buf 			Vetor de bytes que indica o tamanho do array que vai ser recebido.
	 * @throws IOException	Lan�a exce��o se n�o for poss�vel aceitar o pacote recebido.
	 * @return 				Retorna um vetor de bytes com os dados do pacote recebido.
	 * */
	public byte[] acceptPacket(byte[] buf) throws IOException {
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		socket.receive(packet);
		return packet.getData();
	}
	
	/**Envia pacotes UDP para o grupo multicast. 
	 * 
	 * @param buf			Vetor de bytes com os dados que ser�o enviados.
	 * @throws IOException	Lan�a exce��o caso n�o seja poss�vel enviar o pacote.
	 **/
	public void sendPacket(byte[] buf) throws IOException {
		DatagramPacket packet = new DatagramPacket(buf, buf.length, group, port);
		socket.send(packet);
	}
	
	/**Deixa o grupo e fecha o socket. 
	 * 
	 * @throws IOException Lan�a exce��o quando h� algum problema ao deixar o grupo, ou fechar a conex�o.
	 * 
	 */
	public void closeConection() throws IOException {
		socket.leaveGroup(group);
		socket.close();
	}
	
	
	

}
