package com.example.DocumentManagement.service;

import com.example.DocumentManagement.entity.VersionEntity;
import com.example.DocumentManagement.repository.VersionRepository;
import com.example.DocumentManagement.response.ListResponse;
import com.example.DocumentManagement.response.MessageResponse;
import com.example.DocumentManagement.supportFunction.SupportFunction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VersionService extends SupportFunction {
    private final VersionRepository versionRepository;

    public ListResponse getAllVersions(String documentIdFromParam) {
        int documentId = checkRequest(documentIdFromParam);

        List<VersionEntity> listVersion = versionRepository.findByDocumentId(documentId);

        return new ListResponse(listVersion);
    }

    public VersionEntity getOneVersion(String idFromPathVariable) {
        int id = checkRequest(idFromPathVariable);
        return versionRepository.findById(id);
    }

    public MessageResponse getLatestVersionByDocumentId(String idFromPathVariable) {
        int id = checkRequest(idFromPathVariable);
        String latestResponse = versionRepository.findLatestVersionByDocumentId(id);
        return new MessageResponse(latestResponse);
    }

    public MessageResponse helloWorld(){
        return new MessageResponse("Hello World!!!!!!!!!!!!!!!!");
    }
}
