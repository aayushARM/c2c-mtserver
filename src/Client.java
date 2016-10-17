import static java.lang.System.out;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

	public static void main(String[] args) {
		int SERV_PORT=13267;
		Socket server;
		File source;
		File destination;
		byte[] byteBuffer=new byte[10240];
		InputStream is;
		OutputStream os;
		String s,fName;
		BufferedOutputStream fileBos;
		BufferedInputStream bis;
		OutputStreamWriter osw;
		BufferedReader br;
		int input;
		int count=0;
		int mode;

		try{
			out.println("Tyring to connect to server...");
			server=new Socket("localhost",SERV_PORT);
			out.println("Connected to server: "+server.getRemoteSocketAddress());
			os=server.getOutputStream();
			is=server.getInputStream();
			osw=new OutputStreamWriter(os, "UTF-8");
			br=new BufferedReader(new InputStreamReader(is, "UTF-8"));
			Scanner scn=new Scanner(System.in);
			out.println("Press '0' to open client in receive mode or '1' to open in send mode:");
			mode=scn.nextInt();
			if(mode==1)
			{
			osw.write(mode);
			osw.flush();
			out.println("Getting list of clients in recieve mode from server...");
			osw.write("get_clients\n");
			osw.flush();
			out.println("Select the client you want to transfer files to:");
			while((s=br.readLine())!=null && s.length() > 0){
				out.println(s);
			}
			input=scn.nextInt();
			osw.write(input);
			osw.flush();
			scn.nextLine();
			out.println("Enter the path of the file you want to send(Use fwd slash '/'):");//Use forward slash while specifying path instead of 
			fName=scn.nextLine();										//conventional windows backward slash here!!
			source=new File(fName);
			osw.write(source.getName()+"\n");
			osw.flush();
			os.flush();
			bis=new BufferedInputStream(new FileInputStream(source));
			out.println("Sending file...");
			while((count=bis.read(byteBuffer))>0){
				os.write(byteBuffer, 0, count);
			}
			out.println("File sent.");
			os.flush();
			os.close();
			bis.close();
			}
			else if(mode==0){
				out.println("Client opened in receive mode, waiting to receive files...");
				osw.write(mode);
				osw.flush();
				br.readLine();
				scn.nextLine();
				out.println("Enter the directory where you want to save the recieved file(Use fwd slash '/'):");
				String temp=scn.nextLine();						//Use forward slash while specifying path instead of
				out.println("Recieving file from: "+br.readLine());//conventional windows backward slash here!!
				destination=new File(temp+br.readLine());
				fileBos=new BufferedOutputStream(new FileOutputStream(destination));
				while((count=is.read(byteBuffer))>0){
					fileBos.write(byteBuffer, 0, count);
				}
				out.println("File "+destination.getName()+" successfully recieved");
			}
			
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
