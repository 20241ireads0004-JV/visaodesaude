package br.com.ifba.projecaosaude.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjecaoSaudePutRequestDto implements  Serializable {

    @NotNull(message = "O risco cardiovascular é obrigatório.")
    @Min(value = 0, message = "O risco cardiovascular deve ser maior ou igual a 0.")
    @Max(value = 100, message = "O risco cardiovascular deve ser menor ou igual a 100.")
    @JsonProperty("riscoCardioVascular")
    private Integer riscoCardioVascular;

    @NotBlank(message = "A descrição é obrigatória.")
    @Size(max = 255, message = "A descrição deve possuir no máximo 255 caracteres.")
    @JsonProperty("descricao")
    private String descricao;

    @NotNull(message = "A data é obrigatória.")
    @JsonProperty("data")
    private LocalDate data;

    @NotNull(message = "O campo visualização é obrigatório.")
    @JsonProperty("visualizacao")
    private Boolean visualizacao;
}
