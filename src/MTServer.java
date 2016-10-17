import java.io.*;
import java.net.*;
import java.util.ArrayList;
import static java.lang.System.out;

public class MTServer {
	static int c=0;
	static ArrayList<Socket> clients=new ArrayList<Socket>();
	static ArrayList<Integer> clientModes=new ArrayList<Integer>();
	public static void main(String[] args) {
		int PORT=13267;
		ServerSocket servSocket=null;
		Socket client=null;
		try{
		servSocket=new ServerSocket(PORT);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		out.println("Server running on port: "+PORT+", waiting for clients...");

		while(true)
		{
		try{
		client=servSocket.accept();
		client.setKeepAlive(true);
		clients.add(client);
		ClientServiceThread cts=new ClientServiceThread(client);
		c++;
		cts.start();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		}
	}
}
