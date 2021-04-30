package no.hvl.dat110.aciotdevice.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RestClient {
	private OkHttpClient client;
	private Gson gson;

	public RestClient() {
		gson = new Gson();
		client = new OkHttpClient();
	}

	private static String logpath = "/accessdevice/log/";

	public void doPostAccessEntry(String message) {

		// TODO: implement a HTTP POST on the service to post the message
		try (Socket s = new Socket(Configuration.host, Configuration.port)) {

		String body = gson.toJson(new AccessMessage(message));
		String req = "POST " + logpath + " HTTP/1.1\r\n" + 
				"Host: " + Configuration.host + "\r\n"
				+ "Content-type: application/json\r\n" + "Content-length: " + body.length() + "\r\n"
				+ "Connection: close\r\n" + "\r\n" + body + "\r\n";
		
		OutputStream out = s.getOutputStream();
		PrintWriter pw = new PrintWriter(out, false);
		pw.print(req);
		pw.flush();
		
		InputStream in = s.getInputStream();
		Scanner scan = new Scanner(in);
		
		StringBuilder res = new StringBuilder();
		boolean header = true;
		
		while(scan.hasNext()) {
			String next = scan.nextLine();
			
			if(header) {
				System.out.println(next);
			} else {
				res.append(next);
			}
			
			if(next.isEmpty()) header = false;
		}
		
		System.out.println("Body: ");
		System.out.println(res.toString());
		scan.close();
		
		} catch (IOException ex) { System.err.println(ex); }
	}

	private static String codepath = "/accessdevice/code";

	public AccessCode doGetAccessCode() {

		AccessCode code = null;
	

		try(Socket s = new Socket(Configuration.host, Configuration.port))
		{
			String req = "GET " + codepath + " HTTP/1.1\r\n" + 
					"Accept: application/json\r\n" + 
					"Host: "+ Configuration.host + "\r\n" + 
					"Connection: close\r\n" + "\r\n";
			OutputStream out = s.getOutputStream();
			PrintWriter pw = new PrintWriter(out, false);
			
			pw.print(req);
			pw.flush();
			
			InputStream in = s.getInputStream();
			Scanner scan = new Scanner(in);
			StringBuilder res = new StringBuilder();
			
			boolean header = true;
			while(scan.hasNext()) {
				String next = scan.nextLine();
				
				if(header) System.out.println(next);
				else res.append(next);
				
				if(next.isEmpty()) header = false;
			}
			
			code = gson.fromJson(res.toString(), AccessCode.class);
			scan.close();
			
		} catch(IOException ex) { System.err.println(ex); }
		// TODO: implement a HTTP GET on the service to get current access code
		return code;
	}
}
