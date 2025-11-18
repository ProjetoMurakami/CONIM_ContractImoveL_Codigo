package ContractImovel.api;

import ContractImovel.model.dao.pagamentoDao;
import ContractImovel.model.Pagamento;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/pagamentos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PagamentoResource {

    private pagamentoDao dao = new pagamentoDao();

    @GET
    public List<Pagamento> listar() {
        return dao.buscarTodos();
    }

    @GET
    @Path("/{id}")
    public Response buscar(@PathParam("id") Long id) {
        Pagamento pag = dao.buscarPorId(id);
        if (pag == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(pag).build();
    }

    @POST
    public Response inserir(Pagamento pagamento) {
        Pagamento salvo = dao.salvar(pagamento);
        return Response.status(Response.Status.CREATED).entity(salvo).build();
    }

    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") Long id, Pagamento pagamento) {
        Pagamento atual = dao.buscarPorId(id);
        if (atual == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        pagamento.setId(id);
        Pagamento salvo = dao.salvar(pagamento);

        return Response.ok(salvo).build();
    }

    @DELETE
    @Path("/{id}")
    public Response remover(@PathParam("id") Long id) {

        Pagamento pag = dao.buscarPorId(id);
        if (pag == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        dao.excluir(pag);
        return Response.noContent().build();
    }
}
