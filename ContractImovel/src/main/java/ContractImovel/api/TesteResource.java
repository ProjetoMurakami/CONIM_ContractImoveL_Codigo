package ContractImovel.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/teste")
public class TesteResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String ping() {
        return "{\"status\":\"API funcionando\"}";
    }
}
