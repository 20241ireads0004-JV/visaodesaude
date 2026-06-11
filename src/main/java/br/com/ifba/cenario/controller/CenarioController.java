package br.com.ifba.cenario.controller;

import br.com.ifba.cenario.dto.CenarioGetResponseDto;
import br.com.ifba.cenario.dto.CenarioPostRequestDto;
import br.com.ifba.cenario.entity.Cenario;
import br.com.ifba.cenario.service.CenarioIService;
import br.com.ifba.infraestructure.util.ObjectMapperUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/cenarios")
@RequiredArgsConstructor
public class CenarioController {

    private final CenarioIService cenarioService;

    private ObjectMapperUtil objectMapperUtil;

    /**
     * @author João Victor
     * @apiNote Endpoint criado desde a versão V1.0.1
     * Lista de todos os cenários cadastrados na base de dados.
     */

    // =========================
    // POST
    // =========================
    @PostMapping(
            path = "/save",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CenarioGetResponseDto> save(
            @RequestBody @Valid CenarioPostRequestDto requestDto
    ) {

        Cenario cenario = ObjectMapperUtil.map(
                requestDto,
                Cenario.class
        );

        Cenario cenarioSalvo = cenarioService.save(cenario);

        CenarioGetResponseDto responseDto = ObjectMapperUtil.map(
                cenarioSalvo,
                CenarioGetResponseDto.class
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
    public ResponseEntity<CenarioGetResponseDto> findById(
            @PathVariable Long id
    ) {

        CenarioGetResponseDto responseDto = ObjectMapperUtil.map(
                cenarioService.findById(id),
                CenarioGetResponseDto.class
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
    public ResponseEntity<Page<CenarioGetResponseDto>> findAll(
            Pageable pageable
    ) {

        Page<CenarioGetResponseDto> responseDto =
                cenarioService.findAll(pageable)
                        .map(cenario ->
                                ObjectMapperUtil.map(
                                        cenario,
                                        CenarioGetResponseDto.class
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
    public ResponseEntity<CenarioGetResponseDto> update(
            @PathVariable Long id,
            @RequestBody @Valid CenarioPostRequestDto dto
    ) {

        Cenario cenario = ObjectMapperUtil.map(
                dto,
                Cenario.class
        );

        Cenario cenarioAtualizado = cenarioService.update(
                id,
                cenario
        );

        CenarioGetResponseDto responseDto = ObjectMapperUtil.map(
                cenarioAtualizado,
                CenarioGetResponseDto.class
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

        cenarioService.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }
}