## 📌 기술 스택 및 사용 라이브러리

  ### ✅ 기술 스택
  
  - 언어: Kotlin
  - 빌드 시스템: Gradle
  - 아키텍처 패턴: MVVM
  - 버전 관리 도구: Git, GitHub
  - 의존성 관리: Retrofit
  
  ### ✅ 사용 라이브러리
  
  | 라이브러리            | 목적            |
  | ---------------- | ------------- |
  | **ViewPager2**     | 배너 슬라이드 및 튜토리얼 화면 구현   |
  | **Gson**     | JSON 파싱 및 네트워크 통신 데이터를 처리   |
  | **Retrofit**     | REST API 통신   |
  | **Coroutines**   | 비동기 처리        |
  | **Room**         | 로컬 데이터베이스 관리  |
  | **Glide**        | 이미지 로딩 및 캐싱   |
  | **DotIndicator** | 튜토리얼 화면 단계 표시 |

---

## 📌 Commit convention

```bash
type: Subject

body

footer
```

- **Type**

  * [Feature] : 새로운 기능 구현
  * [Mod] : 코드 수정 및 내부 파일 수정
  * [Add] : 부수적인 코드 추가 및 라이브러리 추가, 새로운 파일 생성
  * [Chore] : 버전 코드 수정, 패키지 구조 변경, 타입 및 변수명 변경 등의 작은 작업
  * [Delete] : 쓸모없는 코드나 파일 삭제
  * [UI] : UI 작업
  * [Fix] : 버그 및 오류 해결
  * [Hotfix] : issue나 QA에서 문의된 급한 버그 및 오류 해결
  * [Merge] : 다른 브랜치와의 MERGE
  * [Move] : 프로젝트 내 파일이나 코드의 이동
  * [Rename] : 파일 이름 변경
  * [Refactor] : 전면 수정
  * [Docs] : README나 WIKI 등의 문서 개정

- **Subject**

  * 제목은 최대 50글자까지 작성: ex) Feat: Add Key mapping  
  * 제목 첫 글자를 대문자로  
  * 제목은 명령문으로  
  * 제목 끝에 마침표(.) 금지  
  * 제목과 본문을 한 줄 띄워 분리하기  
  * 본문은 "어떻게" 보다 "무엇을", "왜"를 설명한다.  
  * 본문에 여러줄의 메시지를 작성할 땐 "-"로 구분 

    > ex)
    > Fixed --> Fix   
    > Added --> Add   
    > Modified --> Modify   

- **Body**
  
  * 72자이내로 작성한다.
  * 최대한 상세히 작성한다. (코드 변경의 이유를 명확히 작성할수록 좋다)
  * 어떻게 변경했는지보다 무엇을, 왜 변경했는지 작성한다.

- **Footer**
  
  * issue tracker ID 명시하고 싶은 경우에 작성한다.
  * 유형: #이슈 번호 형식으로 작성한다.
  * 여러 개의 이슈번호는 쉼표(,)로 구분한다.
  * 이슈 트래커 유형은 다음 중 하나를 사용한다.
  
    - Fixes: 이슈 수정중 (아직 해결되지 않은 경우)
    - Resolves: 이슈를 해결했을 때 사용
    - Ref: 참고할 이슈가 있을 때 사용
    - Related to: 해당 커밋에 관련된 이슈번호 (아직 해결되지 않은 경우)
  
    > ex) Fixes: #45 Related to: #34, #23

---

## 📌 Android Studio 환경 설정

#### ✅ Android Studio 버전

- Koala (최신 버전)

#### ✅ SDK 버전

- targetSDK: 34
- minSDK: 24

#### ✅ 테스트 환경

- IDE 내 Emulator, 실제 디바이스

---


