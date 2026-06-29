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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/alertas")
@RequiredArgsConstructor
public class AlertaController {

    private final AlertaIService alertaService;
    private final ObjectMapperUtil objectMapperUtil;

    @PostMapping("/cadastrar")
    public ResponseEntity<AlertaGetResponseDto> cadastrar(
            @Valid @RequestBody AlertaPostRequestDto requestDto) {

        Alerta alerta = new Alerta();
        alerta.setTipo(requestDto.getTipo());
        alerta.setDescricao(requestDto.getDescricao());
        alerta.setData(requestDto.getData());
        alerta.setVisualizacao(requestDto.getVisualizacao());

        Alerta alertaSalvo = alertaService.save(alerta);

        AlertaGetResponseDto responseDto =
                objectMapperUtil.map(alertaSalvo, AlertaGetResponseDto.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlertaGetResponseDto> editar(
            @PathVariable Long id,
            @Valid @RequestBody AlertaPutRequestDto requestDto) {

        Alerta alerta = new Alerta();
        alerta.setTipo(requestDto.getTipo());
        alerta.setDescricao(requestDto.getDescricao());
        alerta.setData(requestDto.getData());
        alerta.setVisualizacao(requestDto.getVisualizacao());

        Alerta atualizado = alertaService.update(id, alerta);

        AlertaGetResponseDto responseDto =
                objectMapperUtil.map(atualizado, AlertaGetResponseDto.class);

        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        alertaService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/listar")
    public ResponseEntity<Page<AlertaGetResponseDto>> listar(Pageable pageable) {

        Page<AlertaGetResponseDto> lista = alertaService.findAll(pageable)
                .map(alerta ->
                        objectMapperUtil.map(alerta, AlertaGetResponseDto.class));

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlertaGetResponseDto> buscarPorId(@PathVariable Long id) {

        Alerta alerta = alertaService.findById(id);

        AlertaGetResponseDto responseDto =
                objectMapperUtil.map(alerta, AlertaGetResponseDto.class);

        return ResponseEntity.ok(responseDto);
    }
}