#### 블로그 프로젝트

<hr>



##### 구현 목록

0. 회원가입

   - 회원가입, 로그인 기능

     Spring Security: form 로그인을 통한 로그인 구현

   - 아이디, email 중복 시 에러 메시지 출력 (BindingResult 이용)

   - 회원 가입이 완료되면 로그인 form으로 이동



1. 메인 페이지

   - 상단 우측

     로그인 했을 경우: 사용자 아이콘(클릭 시 내 블로그, 임시 글 목록, 정보 수정, 로그아웃 선택)

     로그인 하지 않았을 경우: 로그인 링크
   - 중앙 화면
     - 사용자들의 포스트 목록 출력 (페이징 처리)
     - 메인 페이지의 목록 글에서는 제목, 내용 일부, 썸네일 사진, 작성자 아이디 출력



2. 블로그 (메인 페이지에서 링크)

   - 회원 1명은 1개의 블로그를 가짐

     회원가입 시 블로그 생성

   - 글 상세 보기 (URL: `[GET] 도메인/@사용자아이디`)

     - 좌측에 tag 목록이 나오고 tag에 글의 수가 보여짐 (태그는 여러 개 설정 가능)
     - 글 목록, 시리즈 목록, 소개 확인 가능



3. 포스트
   - 글을 작성하자마자 발행 가능
   - 제목, 내용 필수 / 태그 선택 (자동으로 작성일 저장: `@CreateAt` 이용)
   - 비공개 글인 경우 비공개 표시 (secret: t/f) (전공/비공 올릴 때 선택)
   - 글 등록 후 URL: `/@사용자아이디/{postId}`
   - 임시 저장 구현



4. 임시글 저장 목록 보기

   - 회원 아이콘에서 '임시 글 목록' 클릭 시 목록으로 이동

   - 임시 글 저장 목록에서 수정 또는 삭제 가능 (임시저장 또는 출간)



5. 블로그 글 보기
    - 자신의 글인 경우 수정 / 삭제



6. 댓글
    - 로그인한 사용자만 댓글 작성 가능
    - 블로그 글 보기를 하면 댓글 수 표시
    - 댓글 수정/삭제 가능



<hr>

##### 구현해야 할 목록

1. 메인 페이지

   - 상단 우측

     정보 수정 기능 구현



2. 블로그 (메인 페이지에서 링크)
   - 태그 클릭 시 태그에 해당하는 포스트 출력



3. 포스트
   - 글을 작성할 때 이미지, URL 등 포함



4. 정보 수정
   - 프로필 이미지 등록
   - 블로그 이름 설정 (defalut: 사용자 아이디)
   - 이메일 주소 변경 (회원가입 시 등록 가능)
   - 회원 탈퇴 기능



6. 댓글
   - 대댓글 기능



7. 관리자
   - 모든 포스팅의 글 보기 (공개 / 비공 상관 X)
   - 어떤 글이든 삭제
