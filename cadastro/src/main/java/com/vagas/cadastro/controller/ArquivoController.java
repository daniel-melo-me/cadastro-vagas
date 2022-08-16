package com.vagas.cadastro.controller;

import com.vagas.cadastro.dto.response.UploadFileResponseDTO;
import com.vagas.cadastro.model.Arquivo;
import com.vagas.cadastro.service.ArquivoStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/arquivo")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class ArquivoController {

    private final Map<String, String> erro = new HashMap<>();
    private final ArquivoStorageService service;

    @PostMapping("/uploadFile")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            Arquivo dbFile = service.storeFile(file);

            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/downloadFile/")
                    .path(dbFile.getId())
                    .toUriString();

            return validarTamanho(file, dbFile, fileDownloadUri);

        } catch (Exception e) {
            return retornoErro(e.getMessage());
        }
    }

    @GetMapping("/downloadFile/{fileId}")
    public ResponseEntity<?> downloadFile(@PathVariable String fileId) {
        try {
            Arquivo dbFile = service.getFile(fileId);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(dbFile.getFileType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dbFile.getFileName() + "\"")
                    .body(new ByteArrayResource(dbFile.getData()));
        } catch (Exception e) {
            return retornoErro(e.getMessage());
        }
    }

    @PutMapping("/uploadNewFile/{fileId}")
    public ResponseEntity<?> uploadNewFile(@PathVariable(value = "fileId") String id, @RequestParam("file") MultipartFile file) {
        try {
            Arquivo dbFile = service.editFile(id, file);

            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/downloadFile/")
                    .path(dbFile.getId())
                    .toUriString();

            return validarTamanho(file, dbFile, fileDownloadUri);

        } catch (Exception e) {
            return retornoErro(e.getMessage());
        }
    }

    private ResponseEntity<?> retornoErro(String mensagem) {
        erro.put("erro", mensagem);
        log.error("Erro encontrado: " + mensagem);
        return ResponseEntity.badRequest().body(erro);
    }

    private ResponseEntity<?> validarTamanho(MultipartFile file, Arquivo dbFile, String fileDownloadUri) {
        if(file.getSize() == 1000000) {
            return retornoErro("Arquivo muito Grande");
        }
        return ResponseEntity.ok().body(new UploadFileResponseDTO(dbFile.getId(), dbFile.getFileName(), fileDownloadUri,
                file.getContentType(), file.getSize()));
    }
}
