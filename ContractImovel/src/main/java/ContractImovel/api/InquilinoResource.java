package ContractImovel.api;

import ContractImovel.model.Inquilino;
import ContractImovel.model.dao.inquilinoDao;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/inquilinos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class InquilinoResource {

    private inquilinoDao dao = new inquilinoDao();

    @GET
    public List<Inquilino> listar() {
        return dao.buscarTodos();
    }

    @GET
    @Path("/{id}")
    public Response buscar(@PathParam("id") Long id) {
        Inquilino i = dao.buscarPeloCodigo(id);
        if (i == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(i).build();
    }

    @POST
    public Response inserir(Inquilino inquilino) {
        Inquilino salvo = dao.salvar(inquilino);
        return Response.status(Response.Status.CREATED).entity(salvo).build();
    }

    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") Long id, Inquilino inquilino) {
        Inquilino existente = dao.buscarPeloCodigo(id);
        if (existente == null) return Response.status(Response.Status.NOT_FOUND).build();

        inquilino.setId(id);
        Inquilino salvo = dao.salvar(inquilino);
        return Response.ok(salvo).build();
    }

    @DELETE
    @Path("/{id}")
    public Response remover(@PathParam("id") Long id) {
        Inquilino i = dao.buscarPeloCodigo(id);
        if (i == null) return Response.status(Response.Status.NOT_FOUND).build();

        dao.excluir(i);
        return Response.noContent().build();
    }
}
