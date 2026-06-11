package br.com.ifba.painelcorporativo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class PainelCorporativoPostResponseDto {

    /**
     * ID da Empresa (= idPainel conforme a tarefa)
     */
    private Long idPainel;

    private DadosAgregados dadosAgregados;

    //  Classe interna: DadosAgregados
    @Data
    public static class DadosAgregados {

        /**
         * Número total de funcionários vinculados à empresa
         */
        private Long totalColaboradores;

        /**
         * % de funcionários que registraram hábitos no último mês
         */
        private Double percentualAdesao;

        /**
         * Média do score de saúde/hábitos (0–100)
         */
        private Double scoreMedio;

        /**
         * Média de horas de sono
         */
        private Double mediaSono;

        /**
         * % de redução de risco desde a implantação
         */
        private Double reducaoRiscoProjetado;

        /**
         * null = todos os departamentos; preenchido quando há filtro ativo
         */
        private String departamentoFiltrado;

        /**
         * Dados para o gráfico de linha "Evolução da Qualidade do Sono Média"
         */
        private List<EvolucaoSonoMensalDTO> evolucaoSono;
        /**
         * Dados para o gráfico de barras "Distribuição de Risco por Departamento"
         */
        private List<RiscoPorDepartamentoDTO> riscoPorDepartamento;


        //  Classe interna: EvolucaoSonoMensalDTO (gráfico de linha)
        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class EvolucaoSonoMensalDTO {
            private String mes;        // "Jan", "Fev", "Mar"...
            private Double mediaSono;  // Ex: 6.8

        }

        //  Classe interna: RiscoPorDepartamentoDTO (gráfico de barras)
        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class RiscoPorDepartamentoDTO {
            private String departamento;
            private Long riscoBaixo;  // qtd de colaboradores com risco baixo
            private Long riscoMedio;
            private Long riscoAlto;

        }
    }
}

