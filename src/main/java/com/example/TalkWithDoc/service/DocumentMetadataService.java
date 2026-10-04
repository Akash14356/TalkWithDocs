package com.example.TalkWithDoc.service;

import com.example.TalkWithDoc.dto.DocumentResponseDto;
import com.example.TalkWithDoc.entity.DocumentMetadata;
import com.example.TalkWithDoc.entity.DocumentStatus;
import com.example.TalkWithDoc.repository.DocumentMetadataRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Service
@RequiredArgsConstructor
public class DocumentMetadataService {

    private static  final Logger log = LoggerFactory.getLogger(DocumentMetadataService.class);

    private final DocumentMetadataRepo documentMetadataRepo;
    private final JdbcTemplate jdbcTemplate;
    private final DocumentParserService parserService;
    private final DocumentIngestionService ingestionService;

    //upload and parse doc@
    @Transactional
    public DocumentResponseDto uploadAndProcess(MultipartFile file){

        String fileName  = file.getOriginalFilename() != null? file.getOriginalFilename() : "document";
        String contentType = file.getContentType() != null ? file.getContentType() :"application/octat-stream";

        //document meta data create

        DocumentMetadata documentMetadata=  DocumentMetadata.builder()
                .filename(fileName)
                .contentType(contentType)
                .status(DocumentStatus.UPLOADING)
                .fileSize(file.getSize())
                .build();



        documentMetadata= documentMetadataRepo.save(documentMetadata);


        //parse the file

        List<Document> parsedDocs = parserService.parse(file);

        //ingest service
        int chunksCreated = ingestionService.ingest(documentMetadata,parsedDocs);
//        documentMetadata.setTotalChunks(chunksCreated);
        //save the document metadata


        return DocumentResponseDto.builder()
                .id(documentMetadata.getId())
                .fileName(documentMetadata.getFilename())
                .fileSize(documentMetadata.getFileSize())
                .chunksCreated(chunksCreated)
                .status(documentMetadata.getStatus())
                .message("Document Successfully processed and Indexed ")
                .build();
    }


}
