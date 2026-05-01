package com.example.filesystemprocessor.adapters.input.web;

import com.example.filesystemprocessor.file.core.exception.DomainException;
import com.example.filesystemprocessor.file.core.i18n.MessageKey;
import com.example.filesystemprocessor.file.core.model.File;
import com.example.filesystemprocessor.file.core.model.FileType;
import com.example.filesystemprocessor.file.core.model.Folder;
import com.example.filesystemprocessor.file.core.result.ProcessResult;
import com.example.filesystemprocessor.file.core.service.FileProcessingService;
import com.example.filesystemprocessor.file.core.service.FileStorageService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "*")
public class FileUploadController {

    private final FileProcessingService fileProcessingService;
    private final FileStorageService fileStorageService;

    public FileUploadController(
            FileProcessingService fileProcessingService,
            FileStorageService fileStorageService
    ) {
        this.fileProcessingService = fileProcessingService;
        this.fileStorageService = fileStorageService;
    }

    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ProcessResult> uploadFile(
            @RequestParam("file") MultipartFile multipartFile,
            @RequestParam("fileType") FileType fileType
    ) throws IOException {

        File file = toDomainFile(multipartFile, fileType);
        ProcessResult result = fileProcessingService.processFile(file);

        return ResponseEntity.ok(result);
    }

    @PostMapping(
            value = "/upload/folder",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ProcessResult> uploadFolder(
            @RequestParam("files") List<MultipartFile> multipartFiles
    ) throws IOException {

        Folder folder = new Folder("uploaded-folder");

        for (MultipartFile multipartFile : multipartFiles) {
            FileType fileType = inferFileType(multipartFile.getOriginalFilename());
            File file = toDomainFile(multipartFile, fileType);
            folder.add(file);
        }

        ProcessResult result = fileProcessingService.processFolder(folder);

        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<List<String>> listFiles() {
        List<String> files = fileStorageService.listStoredFileNames();
        return ResponseEntity.ok(files);
    }

    @DeleteMapping
    public ResponseEntity<Void> clearFiles() {
        fileStorageService.clearStoredFiles();
        return ResponseEntity.noContent().build();
    }

    private File toDomainFile(MultipartFile multipartFile, FileType fileType) throws IOException {
        String content = new String(multipartFile.getBytes(), StandardCharsets.UTF_8);

        return new File(
                multipartFile.getOriginalFilename(),
                fileType,
                multipartFile.getSize(),
                content
        );
    }

    private FileType inferFileType(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            throw new DomainException(MessageKey.FILE_NAME_REQUIRED);
        }

        String lowerName = fileName.toLowerCase();

        if (lowerName.endsWith(".xml") || lowerName.endsWith(".json")) {
            return FileType.INVOICE;
        }

        if (lowerName.endsWith(".pdf")) {
            return FileType.CONTRACT;
        }

        if (lowerName.endsWith(".csv") || lowerName.endsWith(".xlsx")) {
            return FileType.REPORT;
        }

        throw new DomainException(MessageKey.FILE_EXTENSION_UNSUPPORTED);
    }
}