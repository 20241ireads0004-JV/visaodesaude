package br.com.ifba.projecaosaude.controller;

import br.com.ifba.infraestructure.util.ObjectMapperUtil;
import br.com.ifba.projecaosaude.dto.ProjecaoSaudeGetResponseDto;
import br.com.ifba.projecaosaude.dto.ProjecaoSaudePostRequestDto;
import br.com.ifba.projecaosaude.dto.ProjecaoSaudePutRequestDto;
import br.com.ifba.projecaosaude.entity.ProjecaoSaude;
import br.com.ifba.projecaosaude.service.ProjecaoSaudeIService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/projecoes-saude")
@RequiredArgsConstructor
public class ProjecaoSaudeController {

    private final ProjecaoSaudeIService projecaoSaudeService;

    private ObjectMapperUtil objectMapperUtil;

    /**
     * @author João Victor
     * @apiNote Endpoint criado desde a versão V1.0.1
     * Lista de todas as projeções de saúde cadastradas.
     */

    // =========================
    // POST
    // =========================
    @PostMapping(
            path = "/save",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ProjecaoSaudeGetResponseDto> save(
            @RequestBody @Valid ProjecaoSaudePostRequestDto requestDto
    ) {

        ProjecaoSaude projecaoSaude = ObjectMapperUtil.map(
                requestDto,
                ProjecaoSaude.class
        );

        ProjecaoSaude projecaoSaudeSalva =
                projecaoSaudeService.save(projecaoSaude);

        ProjecaoSaudeGetResponseDto responseDto =
                ObjectMapperUtil.map(
                        projecaoSaudeSalva,
                        ProjecaoSaudeGetResponseDto.class
                );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(responseDto);
    }

    // =========================
    // GET BY ID
    // =========================
    @GetMapping(
            path = "/findById/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ProjecaoSaudeGetResponseDto> findById(
            @PathVariable Long id
    ) {

        ProjecaoSaudeGetResponseDto responseDto =
                ObjectMapperUtil.map(
                        projecaoSaudeService.findById(id),
                        ProjecaoSaudeGetResponseDto.class
                );

        return ResponseEntity.status(HttpStatus.OK)
                .body(responseDto);
    }

    // =========================
    // GET ALL
    // =========================
    @GetMapping(
            path = "/findAll",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Page<ProjecaoSaudeGetResponseDto>> findAll(
            Pageable pageable
    ) {

        Page<ProjecaoSaudeGetResponseDto> responseDto =
                projecaoSaudeService.findAll(pageable)
                        .map(projecaoSaude ->
                                ObjectMapperUtil.map(
                                        projecaoSaude,
                                        ProjecaoSaudeGetResponseDto.class
                                )
                        );

        return ResponseEntity.status(HttpStatus.OK)
                .body(responseDto);
    }

    // =========================
    // PUT
    // =========================
    @PutMapping(
            path = "/update/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ProjecaoSaudeGetResponseDto> update(
            @PathVariable Long id,
            @RequestBody @Valid ProjecaoSaudePutRequestDto dto
    ) {

        ProjecaoSaude projecaoSaude = ObjectMapperUtil.map(
                dto,
                ProjecaoSaude.class
        );

        ProjecaoSaude projecaoSaudeAtualizada =
                projecaoSaudeService.update(
                        id,
                        projecaoSaude
                );

        ProjecaoSaudeGetResponseDto responseDto =
                ObjectMapperUtil.map(
                        projecaoSaudeAtualizada,
                        ProjecaoSaudeGetResponseDto.class
                );

        return ResponseEntity.status(HttpStatus.OK)
                .body(responseDto);
    }

    // =========================
    // DELETE
    // =========================
    @DeleteMapping(
            path = "/delete/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {

        projecaoSaudeService.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }
}