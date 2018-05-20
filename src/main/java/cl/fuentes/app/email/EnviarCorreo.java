package cl.fuentes.app.email;

import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

import java.io.BufferedReader;
//import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

public class EnviarCorreo {
	
	private static final String SMTP_HOST_NAME = "smtp.servidor.com";
    private static final String SMTP_AUTH_USER = "no-reply@servidor.com";
    private static final String SMTP_AUTH_PWD  = "xxxxxx";
    

	public static void main(String[] args)  throws Exception{
		
		// Inicia lectura de flayer
		String contenido = "";
		String ruta = "d:\\publicidad.html";
		 try {
		        BufferedReader in = new BufferedReader(new FileReader(ruta));
		        String str;
		        while ((str = in.readLine()) != null) {
		        	contenido +=str;
		        }
		        in.close();
		    } catch (IOException e) {
		    }
		// Inicia lectura base de correso en csv
		  String csvFile = "D:\\correos.csv";
	      String line = "";
	      String cvsSplitBy = ";";
	        
	      try {
	    	   BufferedReader br = new BufferedReader(new FileReader(csvFile)); 
	            while ((line = br.readLine()) != null) {
	                String[] country = line.split(cvsSplitBy);
	                if(country[2].equals("SI")){
	                	System.out.print("Contacto [Nombre= " + country[0] + " , Correo=" + country[1] + "]");
	                	new EnviarCorreo().enviar_correo(country[0], country[1], contenido);
	                	System.out.println(" - Enviado");
	                }
	            }
	            br.close();
	            System.out.println("Proceso finalizado");
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
    } 	
	
	public void enviar_correo(String contacto, String correo, String contenido) throws Exception {
		
        try {
			Properties props = new Properties();
	        props.put("mail.transport.protocol", "smtp");
	        props.put("mail.smtp.host", SMTP_HOST_NAME);
	        props.put("mail.smtp.auth", "true");
	
	        Authenticator auth = new SMTPAuthenticator();
	        Session mailSession = Session.getDefaultInstance(props, auth);
	        // mailSession.setDebug(true);
	        Transport transport = mailSession.getTransport();
	
	        MimeMessage message = new MimeMessage(mailSession);
	        //message.setContent(contenido, "text/plain");
	        message.setContent(contenido, "text/html; charset=utf-8");
	        
	        message.setSentDate(new Date());
	        message.setFrom(new InternetAddress("no-reply@servidor.com", "Título del mensaje"));
	        message.addRecipient(Message.RecipientType.TO, new InternetAddress(correo));
	        message.setSubject("Estimado(a) " + contacto + ": ");
	
	        transport.connect();
	        transport.sendMessage(message,message.getRecipients(Message.RecipientType.TO));
	        transport.close();
		} catch (Exception e) {
			System.out.println("Error: no fue posible enviar el correo.");
		}
    }

    private class SMTPAuthenticator extends javax.mail.Authenticator {
        public PasswordAuthentication getPasswordAuthentication() {
           String username = SMTP_AUTH_USER;
           String password = SMTP_AUTH_PWD;
           return new PasswordAuthentication(username, password);
        }
    }
}
