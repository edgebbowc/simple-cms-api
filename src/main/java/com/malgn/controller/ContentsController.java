package com.malgn.controller;


import com.malgn.dto.request.CreateContentsRequest;
import com.malgn.dto.request.UpdateContentsRequest;
import com.malgn.dto.response.ContentsResponse;
import com.malgn.dto.response.PageResponse;
import com.malgn.service.ContentsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contents")
@RequiredArgsConstructor
public class ContentsController {

    private final ContentsService contentsService;

    // 콘텐츠 추가
    @PostMapping
    public ResponseEntity<ContentsResponse> create(
            @Valid @RequestBody CreateContentsRequest request,
            Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(contentsService.create(request, auth.getName()));
    }

    // 콘텐츠 목록 조회 (페이징)
    @GetMapping
    public ResponseEntity<PageResponse<ContentsResponse>> findAll(
            @PageableDefault(size = 10, sort = "createdDate",
                    direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(contentsService.findAll(pageable));
    }

    // 콘텐츠 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<ContentsResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(contentsService.findById(id));
    }

    // 콘텐츠 수정
    @PutMapping("/{id}")
    public ResponseEntity<ContentsResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateContentsRequest request,
            Authentication auth) {
        return ResponseEntity.ok(contentsService.update(id, request, auth));
    }

    // 콘텐츠 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            Authentication auth) {
        contentsService.delete(id, auth);
        return ResponseEntity.noContent().build();
    }
}
