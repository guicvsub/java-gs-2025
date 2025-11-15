package br.com.cashplus.service;

import br.com.cashplus.dto.external.RiscoFraudeRequestDTO;
import br.com.cashplus.dto.external.RiscoFraudeResponseDTO;
import br.com.cashplus.model.enums.RiscoFraudeEnum;
import br.com.cashplus.model.enums.TipoPagamentoEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.math.BigDecimal;
import java.time.Duration;

/**
 * Serviço para consulta de risco de fraude em API externa.
 * Implementa tratamento de timeout, retry e fallback.
 */
@Service
public class RiscoFraudeService {

    private static final Logger logger = LoggerFactory.getLogger(RiscoFraudeService.class);
    
    private final WebClient webClient;
    private final boolean apiExternaHabilitada;
    private final int timeoutSegundos;
    
    public RiscoFraudeService(
            WebClient.Builder webClientBuilder,
            @Value("${app.risco-fraude.api.url:http://api.riscofraude.com/v1/consulta}") String apiUrl,
            @Value("${app.risco-fraude.api.enabled:false}") boolean apiExternaHabilitada,
            @Value("${app.risco-fraude.api.timeout:5}") int timeoutSegundos) {
        this.apiExternaHabilitada = apiExternaHabilitada;
        this.timeoutSegundos = timeoutSegundos;
        this.webClient = webClientBuilder
                .baseUrl(apiUrl)
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024 * 1024))
                .build();
    }

    /**
     * Consulta o risco de fraude em API externa ou usa cálculo local como fallback.
     * 
     * @param valor O valor da transação
     * @param tipoPagamento O tipo de pagamento
     * @return O nível de risco de fraude
     */
    public RiscoFraudeEnum consultarRisco(BigDecimal valor, TipoPagamentoEnum tipoPagamento) {
        if (!apiExternaHabilitada) {
            logger.debug("API externa desabilitada, usando cálculo local");
            return calcularRiscoFraudeLocal(valor, tipoPagamento);
        }
        
        try {
            RiscoFraudeRequestDTO request = new RiscoFraudeRequestDTO(
                    valor, 
                    tipoPagamento != null ? tipoPagamento.name() : null
            );
            
            RiscoFraudeResponseDTO response = webClient.post()
                    .uri("/consultar")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(RiscoFraudeResponseDTO.class)
                    .timeout(Duration.ofSeconds(timeoutSegundos))
                    .retryWhen(Retry.fixedDelay(2, Duration.ofMillis(500))
                            .filter(throwable -> throwable instanceof WebClientException))
                    .onErrorResume(throwable -> {
                        logger.warn("Erro ao consultar API externa de risco de fraude: {}", 
                                throwable.getMessage());
                        RiscoFraudeEnum riscoLocal = calcularRiscoFraudeLocal(valor, tipoPagamento);
                        RiscoFraudeResponseDTO fallbackResponse = new RiscoFraudeResponseDTO();
                        fallbackResponse.setRisco(riscoLocal.name());
                        return Mono.just(fallbackResponse);
                    })
                    .block();
            
            if (response != null && response.getRisco() != null) {
                try {
                    return RiscoFraudeEnum.valueOf(response.getRisco().toUpperCase());
                } catch (IllegalArgumentException e) {
                    logger.warn("Risco retornado pela API inválido: {}, usando cálculo local", 
                            response.getRisco());
                    return calcularRiscoFraudeLocal(valor, tipoPagamento);
                }
            }
            
            return calcularRiscoFraudeLocal(valor, tipoPagamento);
            
        } catch (Exception e) {
            logger.error("Erro inesperado ao consultar risco de fraude: {}", e.getMessage(), e);
            return calcularRiscoFraudeLocal(valor, tipoPagamento);
        }
    }

    /**
     * Calcula o risco de fraude localmente baseado em regras de negócio.
     * Regras:
     * - DINHEIRO: sempre BAIXO
     * - PIX: sempre BAIXO
     * - CARTAO: BAIXO se valor < 100, MEDIO se 100-500, ALTO se > 500
     */
    private RiscoFraudeEnum calcularRiscoFraudeLocal(BigDecimal valor, TipoPagamentoEnum tipoPagamento) {
        if (tipoPagamento == null) {
            return RiscoFraudeEnum.MEDIO;
        }

        if (tipoPagamento == TipoPagamentoEnum.DINHEIRO || 
            tipoPagamento == TipoPagamentoEnum.PIX) {
            return RiscoFraudeEnum.BAIXO;
        }

        if (tipoPagamento == TipoPagamentoEnum.CARTAO) {
            BigDecimal cem = new BigDecimal("100");
            BigDecimal quinhentos = new BigDecimal("500");
            
            if (valor.compareTo(cem) < 0) {
                return RiscoFraudeEnum.BAIXO;
            } else if (valor.compareTo(quinhentos) <= 0) {
                return RiscoFraudeEnum.MEDIO;
            } else {
                return RiscoFraudeEnum.ALTO;
            }
        }

        return RiscoFraudeEnum.MEDIO;
    }
}
