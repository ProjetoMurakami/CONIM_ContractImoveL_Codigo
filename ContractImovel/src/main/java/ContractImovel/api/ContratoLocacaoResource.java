package ContractImovel.api;

import ContractImovel.model.ContratoLocacao;
import ContractImovel.model.dao.contratoLocacaoDao;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/contratos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ContratoLocacaoResource {

    private contratoLocacaoDao dao = new contratoLocacaoDao();

    @GET
    public List<ContratoLocacao> listar() {
        return dao.buscarTodos();
    }

    @GET
    @Path("/{id}")
    public Response buscar(@PathParam("id") Long id) {
        ContratoLocacao c = dao.buscarPeloCodigo(id);
        if (c == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(c).build();
    }

    @POST
    public Response inserir(ContratoLocacao contrato) {
        ContratoLocacao salvo = dao.salvar(contrato);
        return Response.status(Response.Status.CREATED).entity(salvo).build();
    }

    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") Long id, ContratoLocacao contrato) {
        ContratoLocacao existente = dao.buscarPeloCodigo(id);
        if (existente == null) return Response.status(Response.Status.NOT_FOUND).build();

        contrato.setId(id);
        ContratoLocacao salvo = dao.salvar(contrato);
        return Response.ok(salvo).build();
    }

    @DELETE
    @Path("/{id}")
    public Response remover(@PathParam("id") Long id) {
        ContratoLocacao c = dao.buscarPeloCodigo(id);
        if (c == null) return Response.status(Response.Status.NOT_FOUND).build();

        dao.excluir(c);
        return Response.noContent().build();
    }
}
