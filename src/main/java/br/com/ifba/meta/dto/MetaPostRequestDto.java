package br.com.ifba.meta.dto;

import br.com.ifba.meta.enums.CategoriaMeta;
import br.com.ifba.meta.enums.StatusMeta;
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
public class MetaPostRequestDto implements Serializable {

    @NotBlank(message = "O título ou descrição é obrigatório.")
    @Size(max = 255, message = "O título deve possuir no máximo 255 caracteres.")
    @JsonProperty("titulo")
    private String titulo;

    @NotNull(message = "A categoria é obrigatória.")
    @JsonProperty("categoria")
    private CategoriaMeta categoria;

    @NotNull(message = "A data limite é obrigatória.")
    @JsonProperty("dataLimite")
    private LocalDate dataLimite;

    @NotNull(message = "O status é obrigatório.")
    @JsonProperty("status")
    private StatusMeta status;
}