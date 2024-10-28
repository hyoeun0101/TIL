- JSON과 미디어 파일 (multipart)를 동시에 받아야 하는 상황일 때.

## 🍎@RequestPart
- HTTP 요청 body에 multipart/form-data가 포함되어 있을 때 사용하는 어노테이션
- 요청에 MultipartFile이 포함되어 있는 경우, MultipartResolver가 동작하여 역직렬화를 한다. 
- 요청에 MultipartFile이 포함되어 있지 않은 경우, @RequestBody처럼 동작한다.

### 사용법
```java
@PostMapping(value = "/api/v1/posts/file", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<PostResponseDTO> savePostFile(@RequestPart(value = "file", required = false) MultipartFile multipartFile) throws IOException {
        if (!multipartFile.isEmpty()) {
            log.info("파일 이름 : " + multipartFile.getOriginalFilename());
        } else {
            log.info("파일이 존재하지 않습니다.");
        }
        return ResponseEntity.ok(new PostResponseDTO());
    }
```
- @PostMapping의 consumes 속성
    - 메서드에서 받을 MediaType을 지정. 지정하지 않으면 415 Unsupported MediaType ERROR 발생.
- @RequestPart의 속성
    - value : 해당 이름을 가진 MultipartFile을 추출.
    - required : true이면 MultipartFile이 필수. false이면 필수 아님.
