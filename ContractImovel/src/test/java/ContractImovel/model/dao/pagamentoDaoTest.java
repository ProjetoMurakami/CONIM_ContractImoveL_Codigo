package ContractImovel.model.dao;

import ContractImovel.model.*;
import ContractImovel.enums.FormasPagamento;
import ContractImovel.enums.StatusImovel;
import ContractImovel.enums.TiposCliente;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class pagamentoDaoTest {

    private EntityManager em;
    private pagamentoDao pagamentoDao;
    private EntityManagerFactory emf;

    @BeforeAll
    void setUpAll() {
        emf = Persistence.createEntityManagerFactory("test-persistence-unit");
    }

    @BeforeEach
    void setUp() {
        em = emf.createEntityManager();
        pagamentoDao = new pagamentoDao();

        try {
            Field field = pagamentoDao.getClass().getDeclaredField("manager");
            field.setAccessible(true);
            field.set(pagamentoDao, em);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao injetar EntityManager", e);
        }

        em.getTransaction().begin();
        em.createQuery("DELETE FROM Pagamento").executeUpdate();
        em.createQuery("DELETE FROM ContratoLocacao").executeUpdate();
        em.createQuery("DELETE FROM Fiador").executeUpdate();
        em.createQuery("DELETE FROM Inquilino").executeUpdate();
        em.createQuery("DELETE FROM Imovel").executeUpdate();
        em.createQuery("DELETE FROM Usuario").executeUpdate();
        em.getTransaction().commit();
    }

    @AfterEach
    void tearDown() {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }

    @AfterAll
    void tearDownAll() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

    @Test
    void testSalvarNovoPagamento() {
        ContratoLocacao contrato = criarContratoCompleto();
        
        Pagamento pagamento = criarPagamentoExemplo();
        pagamento.setContratoLocacao(contrato);

        em.getTransaction().begin();
        Pagamento pagamentoSalvo = pagamentoDao.salvar(pagamento);
        em.getTransaction().commit();

        assertNotNull(pagamentoSalvo.getId());
        assertEquals(1500.0, pagamentoSalvo.getValor());
        assertEquals(FormasPagamento.BOLETO, pagamentoSalvo.getFormaDePagamento());
    }

    @Test
    void testAtualizarPagamento() {
        ContratoLocacao contrato = criarContratoCompleto();
        Pagamento pagamento = criarPagamentoExemplo();
        pagamento.setContratoLocacao(contrato);
        
        em.getTransaction().begin();
        Pagamento pagamentoSalvo = pagamentoDao.salvar(pagamento);
        em.getTransaction().commit();

        pagamentoSalvo.setValor(2000.0);
        pagamentoSalvo.setFormaDePagamento(FormasPagamento.PIX);

        em.getTransaction().begin();
        Pagamento pagamentoAtualizado = pagamentoDao.salvar(pagamentoSalvo);
        em.getTransaction().commit();

        assertEquals(2000.0, pagamentoAtualizado.getValor());
        assertEquals(FormasPagamento.PIX, pagamentoAtualizado.getFormaDePagamento());
        assertEquals(pagamentoSalvo.getId(), pagamentoAtualizado.getId());
    }

    @Test
    void testBuscarPorId() {
        ContratoLocacao contrato = criarContratoCompleto();
        Pagamento pagamento = criarPagamentoExemplo();
        pagamento.setContratoLocacao(contrato);
        
        em.getTransaction().begin();
        Pagamento pagamentoSalvo = pagamentoDao.salvar(pagamento);
        em.getTransaction().commit();

        Pagamento pagamentoEncontrado = pagamentoDao.buscarPorId(pagamentoSalvo.getId());

        assertNotNull(pagamentoEncontrado);
        assertEquals(pagamentoSalvo.getId(), pagamentoEncontrado.getId());
        assertEquals(1500.0, pagamentoEncontrado.getValor());
    }

    @Test
    void testBuscarPorContrato() {
        ContratoLocacao contrato = criarContratoCompleto();
        
        Pagamento pagamento = criarPagamentoExemplo();
        pagamento.setContratoLocacao(contrato);

        em.getTransaction().begin();
        pagamentoDao.salvar(pagamento);
        em.getTransaction().commit();

        List<Pagamento> pagamentos = pagamentoDao.buscarPorContrato(contrato.getId());

        assertNotNull(pagamentos);
        assertEquals(1, pagamentos.size());
        assertEquals(contrato.getId(), pagamentos.get(0).getContratoLocacao().getId());
    }

    @Test
    void testBuscarTodos() {
        ContratoLocacao contrato = criarContratoCompleto();
        
        Pagamento pagamento1 = criarPagamentoExemplo();
        pagamento1.setContratoLocacao(contrato);
        
        Pagamento pagamento2 = criarPagamentoExemplo();
        pagamento2.setValor(2000.0);
        pagamento2.setContratoLocacao(contrato);

        em.getTransaction().begin();
        pagamentoDao.salvar(pagamento1);
        pagamentoDao.salvar(pagamento2);
        em.getTransaction().commit();

        List<Pagamento> pagamentos = pagamentoDao.buscarTodos();

        assertNotNull(pagamentos);
        assertEquals(2, pagamentos.size());
    }

    @Test
    void testExcluirPagamento() {
        ContratoLocacao contrato = criarContratoCompleto();
        Pagamento pagamento = criarPagamentoExemplo();
        pagamento.setContratoLocacao(contrato);
        
        em.getTransaction().begin();
        Pagamento pagamentoSalvo = pagamentoDao.salvar(pagamento);
        em.getTransaction().commit();

        Long id = pagamentoSalvo.getId();

        em.getTransaction().begin();
        pagamentoDao.excluir(pagamentoSalvo);
        em.getTransaction().commit();

        Pagamento pagamentoExcluido = em.find(Pagamento.class, id);
        assertNull(pagamentoExcluido);
    }

    @Test
    void testBuscarPorIdNaoExistente() {

        Pagamento pagamento = pagamentoDao.buscarPorId(999L);

        assertNull(pagamento);
    }

    @Test
    void testBuscarPorContratoSemPagamentos() {
        ContratoLocacao contrato = criarContratoCompleto();

        List<Pagamento> pagamentos = pagamentoDao.buscarPorContrato(contrato.getId());

        assertNotNull(pagamentos);
        assertTrue(pagamentos.isEmpty());
    }

    private Pagamento criarPagamentoExemplo() {
        Pagamento pagamento = new Pagamento();
        pagamento.setValor(1500.0);
        pagamento.setDataVencimento(LocalDate.now().plusDays(10));
        pagamento.setDataPagamento(LocalDate.now());
        pagamento.setStatus(true);
        pagamento.setFormaDePagamento(FormasPagamento.BOLETO);
        return pagamento;
    }

    private ContratoLocacao criarContratoCompleto() {
        Imovel imovel = criarImovelExemplo();
        Inquilino inquilino = criarInquilinoExemplo();
        Fiador fiador = criarFiadorExemplo();

        em.getTransaction().begin();
        em.persist(imovel);
        em.persist(inquilino);
        em.persist(fiador);
        em.getTransaction().commit();

        ContratoLocacao contrato = new ContratoLocacao();
        contrato.setDataInicio(LocalDate.now());
        contrato.setDataFinal(LocalDate.now().plusYears(1));
        contrato.setValorAluguel(1500.0);
        contrato.setImovel(imovel);
        contrato.setInquilino(inquilino);
        contrato.setFiador(fiador);

        em.getTransaction().begin();
        em.persist(contrato);
        em.getTransaction().commit();

        return contrato;
    }

    private Imovel criarImovelExemplo() {
        Imovel imovel = new Imovel();
        imovel.setEndereco("Rua Exemplo, 123");
        imovel.setNumero("123");
        imovel.setBairro("Centro");
        imovel.setCidade("São Paulo");
        imovel.setCEP("01234-567");
        imovel.setArea(80.0);
        imovel.setQuartos(3);
        imovel.setBanheiros(2);
        imovel.setValorAluguel(1500.0);
        imovel.setStatusImovel(StatusImovel.DISPONIVEL);
        imovel.setObservacoes("Apartamento bem localizado");
        return imovel;
    }

    private Inquilino criarInquilinoExemplo() {
        Inquilino inquilino = new Inquilino();
        inquilino.setNome("João Silva");
        inquilino.setCpf("123.456.789-00");
        inquilino.setEmail("joao@email.com");
        inquilino.setTelefone("(11) 99999-9999");
        inquilino.setRendaMensal(5000.0);
        inquilino.setCategoria(TiposCliente.PESSOA_FISICA);
        return inquilino;
    }

    private Fiador criarFiadorExemplo() {
        Fiador fiador = new Fiador();
        fiador.setNome("Maria Santos");
        fiador.setCpf("987.654.321-00");
        fiador.setEmail("maria@email.com");
        fiador.setTelefone("(11) 88888-8888");
        fiador.setRendaMensal(8000.0);
        fiador.setCategoria(TiposCliente.PESSOA_FISICA);
        fiador.setEndereco("Av. Garantia, 456");
        fiador.setNumero("456");
        fiador.setBairro("Jardins");
        fiador.setCEP("04567-890");
        fiador.setObservacoes("Fiador sólido");
        return fiador;
    }

}