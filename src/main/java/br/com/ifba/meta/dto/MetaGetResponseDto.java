package br.com.ifba.meta.dto;

import br.com.ifba.meta.enums.CategoriaMeta;
import br.com.ifba.meta.enums.StatusMeta;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class MetaGetResponseDto implements Serializable {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("titulo")
    private String titulo;

    @JsonProperty("categoria")
    private CategoriaMeta categoria;

    @JsonProperty("dataLimite")
    private LocalDate dataLimite;

    @JsonProperty("status")
    private StatusMeta status;
}