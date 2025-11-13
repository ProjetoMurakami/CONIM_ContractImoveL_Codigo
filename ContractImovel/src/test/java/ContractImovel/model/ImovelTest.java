package ContractImovel.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import ContractImovel.enums.StatusImovel;

class ImovelTest {

    @Test
    void testCriacaoImovel() {
        Imovel imovel = new Imovel();
        imovel.setCEP("12345-678");
        imovel.setCidade("São Paulo");
        imovel.setBairro("Centro");
        imovel.setEndereco("Rua das Flores");
        imovel.setNumero("123");
        imovel.setValorAluguel(1500.0);
        imovel.setStatusImovel(StatusImovel.DISPONIVEL);
        imovel.setQuartos(3);
        imovel.setBanheiros(2);
        imovel.setArea(85.5);
        imovel.setObservacoes("Apartamento reformado");

        assertNull(imovel.getId()); 
        assertEquals("12345-678", imovel.getCEP());
        assertEquals("São Paulo", imovel.getCidade());
        assertEquals("Centro", imovel.getBairro());
        assertEquals("Rua das Flores", imovel.getEndereco());
        assertEquals("123", imovel.getNumero());
        assertEquals(1500.0, imovel.getValorAluguel());
        assertEquals(StatusImovel.DISPONIVEL, imovel.getStatusImovel());
        assertEquals(3, imovel.getQuartos());
        assertEquals(2, imovel.getBanheiros());
        assertEquals(85.5, imovel.getArea());
        assertEquals("Apartamento reformado", imovel.getObservacoes());
    }

    @Test
    void testEqualsAndHashCode() {
        Imovel imovel1 = new Imovel();
        imovel1.setId(1L);
        
        Imovel imovel2 = new Imovel();
        imovel2.setId(1L);
        
        Imovel imovel3 = new Imovel();
        imovel3.setId(2L);

        assertEquals(imovel1, imovel2);
        assertNotEquals(imovel1, imovel3);
        assertEquals(imovel1.hashCode(), imovel2.hashCode());
        assertNotEquals(imovel1.hashCode(), imovel3.hashCode());
    }

    @Test
    void testToString() {
        Imovel imovel = new Imovel();
        imovel.setId(1L);
        imovel.setCidade("Rio de Janeiro");

        String toString = imovel.toString();

        assertTrue(toString.contains("1"));
        assertTrue(toString.contains("Rio de Janeiro"));
    }
}