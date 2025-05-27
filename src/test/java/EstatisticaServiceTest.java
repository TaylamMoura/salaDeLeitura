import com.reading.sala_de_leitura.dto.EstatisticaGeralDTO;
import com.reading.sala_de_leitura.dto.EstatisticaLivroDTO;
import com.reading.sala_de_leitura.entity.Usuario;
import com.reading.sala_de_leitura.repository.EstatisticaRepository;
import com.reading.sala_de_leitura.service.EstatisticaService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalTime;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class EstatisticaServiceTest {

    @InjectMocks
    private EstatisticaService estatisticaService;

    @Mock
    private EstatisticaRepository estatisticaRepository;


    @Test
    public void deveCalcularDiasParaTerminarLivro() {
        Mockito.when(estatisticaRepository.calcularDiasParaTerminarLivro(1L))
                .thenReturn(5); // Simulação: levou 5 dias para terminar

        EstatisticaLivroDTO estatisticaLivro = estatisticaService.estatisticasLivro(1L, new Usuario());

        //Log de saída
        System.out.println("Dias para terminar livro: " + estatisticaLivro.diasLidos());
        Assertions.assertEquals(5, estatisticaLivro.diasLidos());
    }


    @Test
    public void deveCalcularMediaPaginasPorDia() {
        Mockito.when(estatisticaRepository.calcularMediaPaginasPorDia(1L))
                .thenReturn(25.0); // Simulação: média de 25 páginas por dia

        EstatisticaLivroDTO estatisticaLivro = estatisticaService.estatisticasLivro(1L, new Usuario());

        //Log de saída
        System.out.println("Média de páginas por dia: " + estatisticaLivro.mediaPaginasPorDia());
        Assertions.assertEquals(25.0, estatisticaLivro.mediaPaginasPorDia());
    }


    @Test
    public void deveCalcularMediaTempoSessao() {
        Mockito.when(estatisticaRepository.calcularMediaTempoSessao(1L))
                .thenReturn(900.0); // Simulação: 15 minutos de média (900 segundos)

        EstatisticaLivroDTO estatisticaLivro = estatisticaService.estatisticasLivro(1L, new Usuario());

        //Log de saída
        System.out.println("Média de tempo por sessão: " + estatisticaLivro.mediaTempoSessao());
        Assertions.assertEquals(900.0, estatisticaLivro.mediaTempoSessao());
    }


    @Test
    public void deveCalcularTotalHorasLidas() {
        Mockito.when(estatisticaRepository.totalHorasLidas())
                .thenReturn("05:30:00"); // Simulação: string no formato HH:mm:ss

        EstatisticaGeralDTO estatisticaGeral = estatisticaService.estatisticaGeral(new Usuario());

        //Log de saída
        System.out.println("Valor totalHorasLidas recebido no DTO: " + estatisticaGeral.totalHorasLidas());

        Assertions.assertEquals(19800L, estatisticaGeral.totalHorasLidas());
    }


    @Test
    public void converterHorasParaSegundos() {
        String tempoStr = "05:30:00";
        long totalSegundos = converterHorasParaSegundos(tempoStr);

        //Log de saída
        System.out.println("Conversão direta de HH:MM:SS para segundos: " + totalSegundos);

        Assertions.assertEquals(19800L, totalSegundos);
    }


    @Test
    public void deveCalcularTotalPaginasLidas() {
        Mockito.when(estatisticaRepository.totalPaginasLidas())
                .thenReturn(450); // Simulação: usuário leu 450 páginas no total

        EstatisticaGeralDTO estatisticaGeral = estatisticaService.estatisticaGeral(new Usuario());

        //Log de saída
        System.out.println("Total de páginas lidas: " + estatisticaGeral.totalPaginasLidas());

        Assertions.assertEquals(450, estatisticaGeral.totalPaginasLidas());
    }


    @Test
    public void deveCalcularTotalLivrosLidos() {
        Mockito.when(estatisticaRepository.totalLivrosLidos())
                .thenReturn(7); // Simulação: usuário finalizou 7 livros

        EstatisticaGeralDTO estatisticaGeral = estatisticaService.estatisticaGeral(new Usuario());

        //Log de saída
        System.out.println("Total de livros lidos: " + estatisticaGeral.totalLivrosLidos());

        Assertions.assertEquals(7, estatisticaGeral.totalLivrosLidos());
    }

    //  Método auxiliar para converter "HH:MM:SS" em segundos
    private long converterHorasParaSegundos(String horasFormatadas) {
        if (horasFormatadas == null || horasFormatadas.isEmpty()) {
            return 0L;
        }

        LocalTime tempo = LocalTime.parse(horasFormatadas);
        return (tempo.getHour() * 3600) + (tempo.getMinute() * 60) + tempo.getSecond();
    }
}
