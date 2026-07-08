package br.com.ifba.habito.controller;

import br.com.ifba.habito.dto.HabitoGetResponseDto;
import br.com.ifba.habito.dto.HabitoPostRequestDto;
import br.com.ifba.habito.dto.HabitoPutRequestDto;
import br.com.ifba.habito.entity.Habito;
import br.com.ifba.habito.service.HabitoIService;
import br.com.ifba.infraestructure.util.ObjectMapperUtil;
import br.com.ifba.usuario.entity.Usuario;
import br.com.ifba.usuario.service.UsuarioIService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/habitos")
@RequiredArgsConstructor
public class HabitoController {

    private final HabitoIService habitoService;
    private final UsuarioIService usuarioService;

    private ObjectMapperUtil objectMapperUtil;

    /**
     * @author João Victor
     * @apiNote Endpoint criado desde a versão V1.0.1
     * Lista de todos os hábitos cadastrados na base de dados.
     */

    // =========================
    // POST
    // =========================
    @PostMapping(
            path = "/save",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<HabitoGetResponseDto> save(
            @RequestBody @Valid HabitoPostRequestDto requestDto
    ) {

        Habito habito = ObjectMapperUtil.map(
                requestDto,
                Habito.class
        );

// Busca o usuário pelo ID enviado
        Usuario usuario = usuarioService.buscarPorId(
                requestDto.getUsuarioId()
        );

// Associa o usuário ao hábito
        habito.setUsuario(usuario);

        Habito habitoSalvo = habitoService.save(habito);

        HabitoGetResponseDto responseDto = ObjectMapperUtil.map(
                habitoSalvo,
                HabitoGetResponseDto.class
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
    public ResponseEntity<HabitoGetResponseDto> findById(
            @PathVariable Long id
    ) {

        HabitoGetResponseDto responseDto = ObjectMapperUtil.map(
                habitoService.findById(id),
                HabitoGetResponseDto.class
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
    public ResponseEntity<Page<HabitoGetResponseDto>> findAll(
            Pageable pageable
    ) {

        Page<HabitoGetResponseDto> responseDto =
                habitoService.findAll(pageable)
                        .map(habito ->
                                ObjectMapperUtil.map(
                                        habito,
                                        HabitoGetResponseDto.class
                                )
                        );

        return ResponseEntity.status(HttpStatus.OK)
                .body(responseDto);
    }

    @GetMapping(
            path = "/hoje/{usuarioId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<HabitoGetResponseDto> buscarHabitoHoje(
            @PathVariable Long usuarioId
    ) {

        Habito habito = habitoService.buscarHabitoHoje(usuarioId);

        if (habito == null) {
            return ResponseEntity.noContent().build();
        }

        HabitoGetResponseDto response = ObjectMapperUtil.map(
                habito,
                HabitoGetResponseDto.class
        );

        return ResponseEntity.ok(response);
    }

    // =========================
    // PUT
    // =========================
    @PutMapping(
            path = "/update/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<HabitoGetResponseDto> update(
            @PathVariable Long id,
            @RequestBody @Valid HabitoPutRequestDto dto
    ) {

        Habito habito = ObjectMapperUtil.map(
                dto,
                Habito.class
        );

        Habito habitoAtualizado = habitoService.update(
                id,
                habito
        );

        HabitoGetResponseDto responseDto = ObjectMapperUtil.map(
                habitoAtualizado,
                HabitoGetResponseDto.class
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

        habitoService.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }
}