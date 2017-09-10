/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WS;

import base_mysql.ConexionDB_mysql;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author erika
 */
public class ForgotPassword {
    
    String email = "";
    String respuesta = "";
    Connection miConexion;
    String finalJson = "";
    JSONObject userJson = new JSONObject();
    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();
    boolean validEmail = false;
    
    public String sendMail(String json){
        
        String jsonObjects[] = new String[1];
        try{
        JSONArray array = new JSONArray(json); 

            
                JSONObject jsonObj = array.getJSONObject(0);
                jsonObjects[0] = jsonObj.getString("email");               
                
            
        } catch(JSONException e){
            System.out.println("Error al parsear json "+ e);
        }
        
        email = jsonObjects[0];
        miConexion = ConexionDB_mysql.GetConnection();
        try{
        Statement stmt = miConexion.createStatement();
        ResultSet rs;        
       
        rs = stmt.executeQuery("select * from alumno");
        while (rs.next()) {
            
            if(email.equals(rs.getNString("correo"))){        
                                  
                    respuesta = "1";
                        try{
                        userJson.put("email", email);
                        userJson.put("respuesta", respuesta);                     
                        finalJson = userJson.toString();
                        } catch(JSONException ex){
                          System.out.println("Error al crear JSON: "+ex);
                        }
                    
                       validEmail = send(email);
                       if(validEmail == false){
                           respuesta = "0";
                       } else {
                           respuesta = "1";
                       }
                    return finalJson;
                
            } else{
                respuesta = "0";
                try{
                                            
                        userJson.put("email", "");
                        userJson.put("respuesta", respuesta);
                        finalJson = userJson.toString();
                        } catch(JSONException ex){
                          System.out.println("Error al crear JSON: "+ex);
                        }
                               
            }
                           
            }
            miConexion.close();
         }catch (SQLException ex) {
            System.out.println("Error en la consulta"+ex);
        }
        
                
        return finalJson;
    }
    
    public boolean send(String email){
      
      //Generating random password with length 8
      StringBuilder sb = new StringBuilder( 8 );
      for( int i = 0; i < 8; i++ ) 
      sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );

        
      final String username = "datops7@gmail.com";
      final String passwordMail = "_7_d_1_123_7";  
        
      // Get system properties
      Properties properties = System.getProperties();
      
      properties.put("mail.smtp.starttls.enable", "true");
      properties.put("mail.smtp.auth", "true");
      properties.put("mail.smtp.host", "smtp.gmail.com");
      properties.put("mail.smtp.port", "587");
      //properties.setProperty("mail.user", "datops7@gmail.com");
      //properties.setProperty("mail.password", "_7_d_1_123_7");
      
      // Get the default Session object.
      //Session session = Session.getDefaultInstance(properties);

      Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
      protected PasswordAuthentication getPasswordAuthentication(){
          return new PasswordAuthentication(username, passwordMail);
      }
      });
      
      try {
         // Create a default MimeMessage object.
         MimeMessage message = new MimeMessage(session);

         // Set From: header field of the header.
         message.setFrom(new InternetAddress("datops7@gmail.com"));

         // Set To: header field of the header.
         message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));

         // Set Subject: header field
         message.setSubject("Recuperación de Contraseña");

         // Now set the actual message
         message.setText("Esta es tu nueva contraseña: "+ sb.toString());

         // Send message
         Transport.send(message);
         System.out.println("Sent message successfully....");
         
         updatePass(sb.toString(), email);
         
      }catch (MessagingException mex) {
         //mex.printStackTrace();
         System.out.print("Error al mandar mail:" + mex);
         return false;
      }
      
        return true;
    }
    
    public void updatePass(String newPass, String email){
        
        miConexion = ConexionDB_mysql.GetConnection();
        
        try{
                String query = " update alumno set password = ? where correo = ?";
                
                PreparedStatement preparedStmt = miConexion.prepareStatement(query);
                preparedStmt.setString (1, newPass);
                preparedStmt.setString (2, email);
                
                
                 preparedStmt.execute();
                 
                 miConexion.close();
                
            } catch(SQLException ex){
                System.out.print("Error: "+ex);
            }
        
    }
    
}
