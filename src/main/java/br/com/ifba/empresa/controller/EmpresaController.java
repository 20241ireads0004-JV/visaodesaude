package br.com.ifba.empresa.controller;

import br.com.ifba.empresa.dto.EmpresaGetResponseDto;
import br.com.ifba.empresa.dto.EmpresaPostRequestDto;
import br.com.ifba.empresa.entity.Empresa;
import br.com.ifba.empresa.service.EmpresaIService;
import br.com.ifba.infraestructure.util.ObjectMapperUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/empresas")
@RequiredArgsConstructor
public class EmpresaController {

    private final EmpresaIService empresaService;
    private final ObjectMapperUtil objectMapperUtil;

    @PostMapping
    public ResponseEntity<EmpresaGetResponseDto> cadastrar( @Valid @RequestBody EmpresaPostRequestDto requestDto){

        //Controller converte o DTO para Entidade
        Empresa empresa = objectMapperUtil.map(requestDto, Empresa.class);

        //Passa a Entidade limpa para o Service processar
        Empresa empresaSalva = empresaService.cadastrarEmpresa(empresa);

        //Controller converte a Entidade que voltou da base de dados para o DTO de Resposta
        EmpresaGetResponseDto responseDto = objectMapperUtil.map(empresaSalva, EmpresaGetResponseDto.class);

        // Retorna HTTP 201 (Created) e o DTO para o frontend
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmpresaGetResponseDto> editar(
            @PathVariable Long id,
            @Valid @RequestBody EmpresaPostRequestDto requestDto) {

        Empresa empresa = objectMapperUtil.map(requestDto, Empresa.class);
        Empresa atualizada = empresaService.editarEmpresa(id, empresa);
        EmpresaGetResponseDto responseDto = objectMapperUtil.map(atualizada, EmpresaGetResponseDto.class);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        empresaService.excluirEmpresa(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<EmpresaGetResponseDto>> listar() {

        List<EmpresaGetResponseDto> lista = empresaService.listar()
                .stream()
                .map(e -> objectMapperUtil.map(e, EmpresaGetResponseDto.class))
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpresaGetResponseDto> buscarPorId(@PathVariable Long id) {

        Empresa empresa = empresaService.buscarPorId(id);
        EmpresaGetResponseDto responseDto = objectMapperUtil.map(empresa, EmpresaGetResponseDto.class);
        return ResponseEntity.ok(responseDto);
    }



}
