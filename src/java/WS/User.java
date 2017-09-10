/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WS;

import base_mysql.ConexionDB_mysql;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author erika
 */
public class User {
    
    String userName;
    String password;
    String email;
    String genero;
    String localidad; 
    String edadString;
    String respuesta;
    String nombreCompleto;
    int edad;
    String finalJson;
    Connection miConexion;
    JSONObject userJson = new JSONObject(); 
    
   
    
    public String parseJson(String json){
        
        String jsonObjects[] = new String[3];
        try{
        JSONArray array = new JSONArray(json); 

            
                JSONObject jsonObj = array.getJSONObject(0);
                jsonObjects[0] = jsonObj.getString("userName");
                System.out.println("usuario: "+jsonObj.getString("userName"));
                jsonObjects[1] = jsonObj.getString("password");
                System.out.println("password: "+jsonObj.getString("password"));
                jsonObjects[2] = jsonObj.getString("email");
                System.out.println("email: "+jsonObj.getString("email"));
            
        } catch(JSONException e){
            System.out.println("Error al parsear json "+ e);
        }
        
        userName = jsonObjects[0];
        password = jsonObjects[1];
        email = jsonObjects[2];
        
        miConexion = ConexionDB_mysql.GetConnection();
        
        try{
        Statement stmt = miConexion.createStatement();
        ResultSet rs;
        
        //rs = stmt.executeQuery("select * from alumnos for xml auto");
        rs = stmt.executeQuery("select * from alumno");
        while (rs.next()) {
            
            if(userName.equals(rs.getNString("usuario")) || email.equals(rs.getNString("correo"))){        
                if(password.equals(rs.getNString("password"))){
                    edad = rs.getInt("edad");
                    edadString = Integer.toString(edad);
                    localidad = rs.getNString("localidad");
                    email = rs.getNString("correo");
                    nombreCompleto = rs.getNString("nombre");
                    respuesta = "1";
                        try{
                        userJson.put("userName", userName);
                        userJson.put("password", password);
                        userJson.put("email", email);
                        userJson.put("localidad", localidad);
                        userJson.put("edad", edadString);
                        userJson.put("respuesta", respuesta);
                        userJson.put("name", nombreCompleto);
                        finalJson = userJson.toString();
                        } catch(JSONException ex){
                          System.out.println("Error al crear JSON: "+ex);
                        }
                    
                    return finalJson;
                }
            } else{
                respuesta = "0";
                try{
                        userJson.put("userName", "");
                        userJson.put("password", "");
                        userJson.put("email", "");
                        userJson.put("localidad", "");
                        userJson.put("edad", "");
                        userJson.put("name", "");
                        userJson.put("respuesta", respuesta);
                        finalJson = userJson.toString();
                        } catch(JSONException ex){
                          System.out.println("Error al crear JSON: "+ex);
                        }    
               
            }
                           
            }
        
            miConexion.close();
         }catch (SQLException ex) {
            //Logger.getLogger(datos_xml.class.getName()).log(Level.SEVERE, null, ex);
            //info("Intente de nuevo","Error de conexiÃ³n");
            System.out.println("Error en la consulta"+ex);
        }
        
        return finalJson;
    }
    
    
    
}
