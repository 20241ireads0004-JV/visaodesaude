package br.com.ifba.alerta.controller;

import br.com.ifba.alerta.dto.AlertaGetResponseDto;
import br.com.ifba.alerta.dto.AlertaPostRequestDto;
import br.com.ifba.alerta.dto.AlertaPutRequestDto;
import br.com.ifba.alerta.entity.Alerta;
import br.com.ifba.alerta.service.AlertaIService;
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
@RequestMapping(path = "/alertas")
@RequiredArgsConstructor
public class AlertaController {

    private final AlertaIService alertaService;

    private ObjectMapperUtil objectMapperUtil;

    /**
     * @author João Victor
     * @apiNote Endpoint criado desde a versão V1.0.1
     * Lista de todos os alertas cadastrados na base de dados.
     */

    // =========================
    // POST
    // =========================
    @PostMapping(
            path = "/save",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<AlertaGetResponseDto> save(
            @RequestBody @Valid AlertaPostRequestDto requestDto
    ) {

        Alerta alerta = ObjectMapperUtil.map(
                requestDto,
                Alerta.class
        );

        Alerta alertaSalvo = alertaService.save(alerta);

        AlertaGetResponseDto responseDto = ObjectMapperUtil.map(
                alertaSalvo,
                AlertaGetResponseDto.class
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
    public ResponseEntity<AlertaGetResponseDto> findById(
            @PathVariable Long id
    ) {

        AlertaGetResponseDto responseDto = ObjectMapperUtil.map(
                alertaService.findById(id),
                AlertaGetResponseDto.class
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
    public ResponseEntity<Page<AlertaGetResponseDto>> findAll(
            Pageable pageable
    ) {

        Page<AlertaGetResponseDto> responseDto =
                alertaService.findAll(pageable)
                        .map(alerta ->
                                ObjectMapperUtil.map(
                                        alerta,
                                        AlertaGetResponseDto.class
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
    public ResponseEntity<AlertaGetResponseDto> update(
            @PathVariable Long id,
            @RequestBody @Valid AlertaPutRequestDto dto
    ) {

        Alerta alerta = ObjectMapperUtil.map(
                dto,
                Alerta.class
        );

        Alerta alertaAtualizado = alertaService.update(
                id,
                alerta
        );

        AlertaGetResponseDto responseDto = ObjectMapperUtil.map(
                alertaAtualizado,
                AlertaGetResponseDto.class
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

        alertaService.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }
}