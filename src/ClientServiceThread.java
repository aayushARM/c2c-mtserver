import java.io.*;
import java.net.*;

import static java.lang.System.out;

public class ClientServiceThread extends Thread {
	Socket client1,client2;
	BufferedReader br;
	OutputStreamWriter osw;
	String fName="",toBeSent="";
	InputStream client1Is;
	OutputStream client1Os;
	OutputStream client2Os;
	
	byte[] byteBuffer=new byte[10240];
	int count=0;
	
	ClientServiceThread(Socket s){
		client1=s;
	}
	@Override
	public void run() {
		try{
		client1Is=client1.getInputStream();
		client1Os=client1.getOutputStream();
		br=new BufferedReader(new InputStreamReader(client1Is, "UTF-8"));
		osw=new OutputStreamWriter(client1Os, "UTF-8");
		int mode=br.read();
		MTServer.clientModes.add(mode);
		if(mode==1)
		{
		out.println("Client "+client1.getRemoteSocketAddress()+" just joined in send mode.");
		br.readLine();
		
		for(int i=0;i<MTServer.clients.size();i++){
			if(MTServer.clientModes.get(i)==0)
				toBeSent+=(i+1)+") "+MTServer.clients.get(i).getRemoteSocketAddress()+"\n";
			}
			osw.write(toBeSent+"\n");
			osw.flush();
		
		int input=br.read();
		client2=MTServer.clients.get(input-1);
		fName=br.readLine();
		client2Os=client2.getOutputStream();
		osw=new OutputStreamWriter(client2Os);
		osw.write("Ready to send\n");
		osw.flush();
		osw.write(client1.getRemoteSocketAddress().toString()+"\n");
		osw.flush();
		osw.write(fName+"\n");
		osw.flush();
		out.println("Client "+client1.getRemoteSocketAddress()+" is sending file to "
				+client2.getRemoteSocketAddress());
		while((count=client1Is.read(byteBuffer))>0){
			client2Os.write(byteBuffer, 0, count);
		}
		client2Os.flush();
		out.println("File successfully sent from "+client1.getRemoteSocketAddress()+" to "+client2.getRemoteSocketAddress());
		
		client1Is.close();
		client1Os.close();
		client2Os.close();
		
		}
		else if(mode==0){
			out.println("Client "+client1.getRemoteSocketAddress()+" just joined in recieve mode.");
			
		}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
