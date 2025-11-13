package ContractImovel.model.converter;

import ContractImovel.model.Inquilino;
import ContractImovel.service.inquilinoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.enterprise.inject.spi.CDI;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class inquilinoConverterTest {

    @Mock
    private inquilinoService inquilinoService;

    @Mock
    private FacesContext facesContext;

    @Mock
    private UIComponent uiComponent;

    @Mock
    private CDI<Object> cdi;

    private inquilinoConverter converter;

    @BeforeEach
    void setUp() {
        converter = new inquilinoConverter();
        
    }

    @Test
    void testGetAsObjectComStringVazia() {
        Object result = converter.getAsObject(facesContext, uiComponent, "");

        assertNull(result);
    }

    @Test
    void testGetAsObjectComStringNula() {
        Object result = converter.getAsObject(facesContext, uiComponent, null);

        assertNull(result);
    }

    @Test
    void testGetAsObjectComStringInvalida() {
        Object result = converter.getAsObject(facesContext, uiComponent, "abc");

        assertNull(result);
    }

    @Test
    void testGetAsStringComNull() {
        String result = converter.getAsString(facesContext, uiComponent, null);

        assertEquals("", result);
    }

    @Test
    void testGetAsStringComInquilinoSemId() {
        Inquilino inquilino = new Inquilino();

        String result = converter.getAsString(facesContext, uiComponent, inquilino);

        assertEquals("", result);
    }

    @Test
    void testGetAsStringComInquilinoComId() {
        Inquilino inquilino = new Inquilino();
        inquilino.setId(1L);

        String result = converter.getAsString(facesContext, uiComponent, inquilino);

        assertEquals("1", result);
    }
}