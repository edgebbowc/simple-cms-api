package com.malgn.service;

import com.malgn.domain.Contents;
import com.malgn.dto.request.CreateContentsRequest;
import com.malgn.dto.request.UpdateContentsRequest;
import com.malgn.dto.response.ContentsResponse;
import com.malgn.dto.response.PageResponse;
import com.malgn.exception.ForbiddenException;
import com.malgn.exception.NotFoundException;
import com.malgn.repository.ContentsRepository;
import com.malgn.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ContentsService {
    private final ContentsRepository contentsRepository;

    @Transactional
    public ContentsResponse create(CreateContentsRequest request, String username) {
        Contents contents = Contents.builder()
                .title(request.title())
                .description(request.description())
                .createdBy(username)
                .lastModifiedBy(username)
                .build();
        return ContentsResponse.from(contentsRepository.save(contents));
    }

    @Transactional(readOnly = true)
    public PageResponse<ContentsResponse> findAll(Pageable pageable) {
        return PageResponse.from(
                contentsRepository.findAll(pageable)
                        .map(ContentsResponse::from)
        );
    }

    @Transactional
    public ContentsResponse findById(Long id) {
        Contents contents = getContentsOrThrow(id);
        contents.increaseViewCount();  // 상세 조회 시 조회수 증가
        return ContentsResponse.from(contents);
    }

    @Transactional
    public ContentsResponse update(Long id, UpdateContentsRequest request,
                                   Authentication auth) {
        Contents contents = getContentsOrThrow(id);
        checkPermission(contents, auth);
        contents.update(request.title(), request.description(), auth.getName());
        return ContentsResponse.from(contents);
    }

    @Transactional
    public void delete(Long id, Authentication auth) {
        Contents contents = getContentsOrThrow(id);
        checkPermission(contents, auth);
        contentsRepository.delete(contents);
    }

    private Contents getContentsOrThrow(Long id) {
        return contentsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Contents not found with id: " + id));
    }

    private void checkPermission(Contents contents, Authentication auth) {
        boolean isAdmin = auth.getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        boolean isOwner = contents.getCreatedBy().equals(auth.getName());

        if (!isAdmin && !isOwner) {
            throw new ForbiddenException("수정/삭제 권한이 없습니다.");
        }
    }
}
