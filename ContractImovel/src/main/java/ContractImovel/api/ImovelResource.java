package ContractImovel.api;

import ContractImovel.model.Imovel;
import ContractImovel.model.dao.imovelDao;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/imoveis")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ImovelResource {

    private imovelDao dao = new imovelDao();

    @GET
    public List<Imovel> listar() {
        return dao.buscarTodos();
    }

    @GET
    @Path("/disponiveis")
    public List<Imovel> listarDisponiveis() {
        return dao.buscarDisponiveis();
    }

    @GET
    @Path("/{id}")
    public Response buscar(@PathParam("id") Long id) {
        Imovel im = dao.buscarPeloCodigo(id);
        if (im == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(im).build();
    }

    @POST
    public Response inserir(Imovel imovel) {
        Imovel salvo = dao.salvar(imovel);
        return Response.status(Response.Status.CREATED).entity(salvo).build();
    }

    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") Long id, Imovel imovel) {
        Imovel existente = dao.buscarPeloCodigo(id);
        if (existente == null) return Response.status(Response.Status.NOT_FOUND).build();

        imovel.setId(id);
        Imovel salvo = dao.salvar(imovel);
        return Response.ok(salvo).build();
    }

    @DELETE
    @Path("/{id}")
    public Response remover(@PathParam("id") Long id) {
        Imovel im = dao.buscarPeloCodigo(id);
        if (im == null) return Response.status(Response.Status.NOT_FOUND).build();

        dao.excluir(im);
        return Response.noContent().build();
    }
}
