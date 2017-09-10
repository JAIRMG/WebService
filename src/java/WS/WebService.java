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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import static org.eclipse.persistence.platform.database.oracle.plsql.OraclePLSQLTypes.Int;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * REST Web Service
 *
 * @author erika
 */
@Path("generic")
public class WebService {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of WebService
     */
    public WebService() {
    }

    /**
     * Retrieves representation of an instance of WS.WebService
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of WebService
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }
    
    
    //Write and read json https://www.mkyong.com/java/json-simple-example-read-and-write-json/
    @GET
    @Path("/login/{json}")
    @Produces(MediaType.APPLICATION_JSON)
    public String login(@PathParam("json") String json){
        
        
        //[{"userName":"jair","password":"1234","email":"jair.mg.27@gmail.com"}]
        String finalJson = "";
        User user = new User();
        finalJson = user.parseJson(json);
        
        return "[" + finalJson + "]";
        
    }
    
    @GET
    @Path("/register/{json}")
    @Produces(MediaType.APPLICATION_JSON)
    public String register(@PathParam("json") String json){
        
        //localhost:8084/WebServiceForApp/webresources/generic/register/[{"userName":"pedrito","password":"1234","email":"jair.mg.27@gmail.com","nombre":"pedro sola", "localidad":"veracruz","edad":"12","genero":"masculino"}]
        String finalJson = "";
        Register register = new Register();
        finalJson = register.altaAlumno(json);
        
        return "[" + finalJson + "]";
        
    }
    
    @GET
    @Path("/forgotPassword/{json}")
    @Produces(MediaType.APPLICATION_JSON)
    public String forgotPassword(@PathParam("json") String json){
        
        String finalJson = "";
        ForgotPassword forgot = new ForgotPassword();
        finalJson = forgot.sendMail(json);
        
        return "[" + finalJson + "]";
        
    }
    
    @GET
    @Path("/updatePassword/{json}")
    @Produces(MediaType.APPLICATION_JSON)
    public String savePassword(@PathParam("json") String json){
        
        String finalJson = "";
        SavePassword save = new SavePassword();
        finalJson = save.editPassword(json);
        
        return "[" + finalJson + "]";
        
    }
    
}
