package br.com.ifba.gestorcorporativo.controller;

import br.com.ifba.gestorcorporativo.dto.GestorCorporativoGetResponseDto;
import br.com.ifba.gestorcorporativo.dto.GestorCorporativoPostRequestDto;
import br.com.ifba.gestorcorporativo.entity.GestorCorporativo;
import br.com.ifba.gestorcorporativo.service.GestorCorporativoIService;
import br.com.ifba.infraestructure.util.ObjectMapperUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/gestores")
@RequiredArgsConstructor
public class GestorCorporativoController {

    private final GestorCorporativoIService gestorCorporativoService;
    private final ObjectMapperUtil objectMapperUtil;

    @PostMapping
    public ResponseEntity<GestorCorporativoGetResponseDto> cadastrar(
            @Valid @RequestBody GestorCorporativoPostRequestDto requestDto) {

        GestorCorporativo gestor = new GestorCorporativo();
        gestor.setNome(requestDto.getNome());
        gestor.setEmail(requestDto.getEmail());
        gestor.setSenha(requestDto.getSenha());
        gestor.setIdade(requestDto.getIdade());
        gestor.setSexo(requestDto.getSexo());
        gestor.setIdGestor(requestDto.getIdGestor());

        GestorCorporativo salvo = gestorCorporativoService.cadastrar(gestor, requestDto.getIdEmpresa());

        GestorCorporativoGetResponseDto responseDto = objectMapperUtil.map(salvo, GestorCorporativoGetResponseDto.class);
        responseDto.setNomeEmpresa(salvo.getEmpresa().getNome());

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GestorCorporativoGetResponseDto> editar(
            @PathVariable Long id, @Valid @RequestBody GestorCorporativoPostRequestDto requestDto){

        GestorCorporativo gestor = new GestorCorporativo();
        gestor.setNome(requestDto.getNome());
        gestor.setEmail(requestDto.getEmail());
        gestor.setSenha(requestDto.getSenha());
        gestor.setIdade(requestDto.getIdade());
        gestor.setSexo(requestDto.getSexo());
        gestor.setIdGestor(requestDto.getIdGestor());

        GestorCorporativo atualizado = gestorCorporativoService.editar(id, gestor, requestDto.getIdEmpresa());

        GestorCorporativoGetResponseDto responseDto = objectMapperUtil.map(atualizado, GestorCorporativoGetResponseDto.class);
        responseDto.setNomeEmpresa(atualizado.getEmpresa().getNome());

        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        gestorCorporativoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<GestorCorporativoGetResponseDto>> listar() {
        List<GestorCorporativoGetResponseDto> lista = gestorCorporativoService.listar()
                .stream()
                .map(g -> {
                    GestorCorporativoGetResponseDto dto = objectMapperUtil.map(g, GestorCorporativoGetResponseDto.class);
                    dto.setNomeEmpresa(g.getEmpresa().getNome());
                    return dto;
                })
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GestorCorporativoGetResponseDto> buscarPorId(@PathVariable Long id) {
        GestorCorporativo gestor = gestorCorporativoService.buscarPorId(id);
        GestorCorporativoGetResponseDto responseDto = objectMapperUtil.map(gestor, GestorCorporativoGetResponseDto.class);
        responseDto.setNomeEmpresa(gestor.getEmpresa().getNome());
        return ResponseEntity.ok(responseDto);
    }

    // Rota exclusiva do GestorCorporativo — Regra de Negócio
    @GetMapping("/{id}/painel")
    public ResponseEntity<Map<String, Object>> acessarPainel(@PathVariable Long id) {
        return ResponseEntity.ok(gestorCorporativoService.acessarPainel(id));
    }


}
