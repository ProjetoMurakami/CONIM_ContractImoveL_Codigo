package ContractImovel.util;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class FacesContextMocker {

    private FacesContextMocker() {
    }

    public static FacesContext mockFacesContext() {
        FacesContext facesContext = mock(FacesContext.class);
        ExternalContext externalContext = mock(ExternalContext.class);
        Flash flash = mock(Flash.class);
        
        when(facesContext.getExternalContext()).thenReturn(externalContext);
        when(externalContext.getFlash()).thenReturn(flash);
        
        Map<String, Object> flashMap = new HashMap<>();
        when(flash.put(anyString(), any())).thenAnswer(invocation -> {
            String key = invocation.getArgument(0);
            Object value = invocation.getArgument(1);
            return flashMap.put(key, value);
        });
        
        when(flash.get(anyString())).thenAnswer(invocation -> {
            String key = invocation.getArgument(0);
            return flashMap.get(key);
        });
        
        return facesContext;
    }
}