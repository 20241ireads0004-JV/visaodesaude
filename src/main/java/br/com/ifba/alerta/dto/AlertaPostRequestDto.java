package br.com.ifba.alerta.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class AlertaPostRequestDto implements Serializable {

    @NotNull(message = "O usuário é obrigatório.")
    @JsonProperty("usuarioId")
    private Long usuarioId;

    @NotBlank(message = "O tipo do alerta é obrigatório.")
    @Size(max = 50, message = "O tipo do alerta deve possuir no máximo 50 caracteres.")
    @JsonProperty("tipo")
    private String tipo;

    @NotBlank(message = "A descrição do alerta é obrigatória.")
    @Size(max = 255, message = "A descrição do alerta deve possuir no máximo 255 caracteres.")
    @JsonProperty("descricao")
    private String descricao;

    @NotNull(message = "A data do alerta é obrigatória.")
    @JsonProperty("data")
    private LocalDate data;

    @NotNull(message = "O campo visualização é obrigatório.")
    @JsonProperty("visualizacao")
    private Boolean visualizacao;
}
