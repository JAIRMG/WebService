/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WS;

import base_mysql.ConexionDB_mysql;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
public class SavePassword {
   
    String password = "";
    String email = "";
    String respuesta = "";
    String finalJson = "";
    Connection miConexion;
    JSONObject userJson = new JSONObject();
    
    public String editPassword(String json){
        
        String jsonObjects[] = new String[2];
        try{
        JSONArray array = new JSONArray(json); 

            
                JSONObject jsonObj = array.getJSONObject(0);
                jsonObjects[0] = jsonObj.getString("password");
                jsonObjects[1] = jsonObj.getString("email"); 
                
            
        } catch(JSONException e){
            System.out.println("Error al parsear json "+ e);
        }
        
        password = jsonObjects[0];
        email = jsonObjects[1];
        
        miConexion = ConexionDB_mysql.GetConnection();
        
         try{
                
                String query = " update alumno set password = ? where correo = ?";                
                PreparedStatement preparedStmt = miConexion.prepareStatement(query);
                preparedStmt.setString (1, password);
                preparedStmt.setString (2, email);
                
                
                 preparedStmt.execute();
                 respuesta = "1";
                        try{
                        userJson.put("email", email);
                        userJson.put("password", password);
                        userJson.put("respuesta", respuesta);                     
                        finalJson = userJson.toString();
                        return finalJson;
                        } catch(JSONException ex){
                            System.out.print("Error json: "+ex);
                            respuesta = "0";
                        }
                         
                        
                 miConexion.close();
                
            } catch(SQLException ex){
                System.out.print("Error en la consulta: "+ex);
                respuesta = "0";
            }
         
         if(respuesta.equals("0")){
             try{
             userJson.put("email", "");
             userJson.put("password", "");
             userJson.put("respuesta", respuesta);                     
             finalJson = userJson.toString();
             } catch(JSONException ex){
                 System.out.println("Error json: "+ex);
             }
         }
        
        return finalJson;
    }
    
}
