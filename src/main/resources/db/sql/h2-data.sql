-- TODO data
-- (콘텐츠 샘플만, 회원은 DataInitializer가 처리)
insert into contents (title, description, view_count, created_by, created_date)
values ('공지사항', '환영합니다!', 0, 'admin', now());

insert into contents (title, description, view_count, created_by, created_date)
values ('첫 번째 게시글', '내용입니다.', 0, 'user1', now());
