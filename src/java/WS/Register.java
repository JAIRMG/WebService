/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WS;

import static WS.ForgotPassword.AB;
import base_mysql.ConexionDB_mysql;
import static com.sun.org.apache.xalan.internal.lib.ExsltDatetime.date;
import static java.lang.String.format;
import static java.lang.String.format;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
public class Register {
    
    int edad = 0;
    String nombre = "";
    String userName = "";
    String password = "";
    String email = "";
    String localidad = "";     
    String edadString = "";
    String respuesta = "";
    String nombreCompleto = "";
    String genero = "";    
    String finalJson = "";
    String checkEmail = "";
    Connection miConexion;
    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();
    JSONObject userJson = new JSONObject();
    boolean validEmail = false;
    
    public String altaAlumno(String json)
    {
    
        String jsonObjects[] = new String[7];
        try{
        JSONArray array = new JSONArray(json); 

            
                JSONObject jsonObj = array.getJSONObject(0);
                jsonObjects[0] = jsonObj.getString("nombre");
                jsonObjects[1] = jsonObj.getString("userName");                
                jsonObjects[2] = jsonObj.getString("password");                
                jsonObjects[3] = jsonObj.getString("email");
                jsonObjects[4] = jsonObj.getString("localidad");
                jsonObjects[5] = jsonObj.getString("edad");
                jsonObjects[6] = jsonObj.getString("genero");
                
            
        } catch(JSONException e){
            System.out.println("Error al parsear json "+ e);
        }
        
        userName = jsonObjects[1];
        email = jsonObjects[3];
        
        miConexion = ConexionDB_mysql.GetConnection();
        
        try{
        Statement stmt = miConexion.createStatement();
        ResultSet rs;        
       
        rs = stmt.executeQuery("select * from alumno");
        while (rs.next()) {
            
            if(userName.equals(rs.getNString("usuario")) || email.equals(rs.getNString("correo"))){        
                
                  
                    respuesta = "0";
                    checkEmail = "0";
                        try{
                        userJson.put("nombre", "");
                        userJson.put("userName", "");
                        userJson.put("password", "");
                        userJson.put("email", "");
                        userJson.put("localidad", "");
                        userJson.put("edad", "");
                        userJson.put("genero", "");
                        userJson.put("respuesta", respuesta);
                        userJson.put("validEmail", checkEmail); 
                        finalJson = userJson.toString();
                        } catch(JSONException ex){
                          System.out.println("Error al crear JSON: "+ex);
                        }
                    
                    return finalJson;
                
            } else{
                respuesta = "1";
                checkEmail = "1";
                try{
                    
                        nombre = jsonObjects[0];
                        userName = jsonObjects[1];
                        password = jsonObjects[2];
                        email = jsonObjects[3];
                        localidad = jsonObjects[4];
                        edadString = jsonObjects[5];
                        genero = jsonObjects[6];
                        userJson.put("userName", userName);
                        userJson.put("password", password);
                        userJson.put("email", email);
                        userJson.put("localidad", localidad);
                        userJson.put("edad", edadString);
                        userJson.put("nombre", nombre);
                        userJson.put("genero", genero);
                        userJson.put("respuesta", respuesta);
                        userJson.put("validEmail", checkEmail);
                        finalJson = userJson.toString();
                        } catch(JSONException ex){
                          System.out.println("Error al crear JSON: "+ex);
                        }
                
               
            }
                           
            }
         }catch (SQLException ex) {
            System.out.println("Error en la consulta"+ex);
        }
        
        
        validEmail = send(email);
        System.out.println("Email "+ validEmail+ "Respuesta "+respuesta);
        if(respuesta.equals("1") && validEmail == true){
            
            java.util.Date utilDate = new java.util.Date();
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
           
          
            try{
                String query = " insert into alumno (usuario, correo, password, nombre, genero, "
                        + "localidad, edad, fecha_registro)"
                + " values (?, ?, ?, ?, ?, ?, ?, ?)";
                
                PreparedStatement preparedStmt = miConexion.prepareStatement(query);
                preparedStmt.setString (1, userName);
                preparedStmt.setString (2, email);
                preparedStmt.setString (3, password);
                preparedStmt.setString (4, nombre);
                preparedStmt.setString (5, genero);
                preparedStmt.setString (6, localidad);
                preparedStmt.setInt (7, Integer.parseInt(edadString));
                preparedStmt.setDate (8, sqlDate);
                
                 preparedStmt.execute();
                 
                 try{
                    
                        nombre = jsonObjects[0];
                        userName = jsonObjects[1];
                        password = jsonObjects[2];
                        email = jsonObjects[3];
                        localidad = jsonObjects[4];
                        edadString = jsonObjects[5];
                        genero = jsonObjects[6];
                        userJson.put("userName", userName);
                        userJson.put("password", password);
                        userJson.put("email", email);
                        userJson.put("localidad", localidad);
                        userJson.put("edad", edadString);
                        userJson.put("nombre", nombre);
                        userJson.put("genero", genero);
                        userJson.put("respuesta", respuesta);
                        userJson.put("validEmail", checkEmail);
                        finalJson = userJson.toString();
                 }catch(JSONException ex){
                     System.out.print("Error: "+ex);
                 }
                 
                 miConexion.close();
                
            } catch(SQLException ex){
                System.out.print("Error: "+ex);
            }
        } else {
            try{
                        checkEmail = "0";
                        respuesta = "1";
                        userJson.put("userName", "");
                        userJson.put("password", "");
                        userJson.put("email", "");
                        userJson.put("localidad", "");
                        userJson.put("edad", "");
                        userJson.put("genero", "");
                        userJson.put("nombre", "");
                        userJson.put("validEmail", checkEmail);
                        userJson.put("respuesta", respuesta);
                        finalJson = userJson.toString();
                        } catch(JSONException ex){
                          System.out.println("Error al crear JSON: "+ex);
                        }
            
            
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
         message.setSubject("¡Bienvenido a la aplicación!");

         // Now set the actual message
         message.setText("Desde este momento ya podrás iniciar a practicar matemáticas");

         // Send message
         Transport.send(message);
         System.out.println("Sent message successfully....");
         
        
         return true;
      }catch (MessagingException mex) {
         //mex.printStackTrace();
         System.out.print("Error al mandar mail:" + mex);
         return false;
      }
      
        
    }
    
}
