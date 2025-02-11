## 개요
스프링 부트를 활용한 서버 API기반 매장 테이블 예약 서비스
  

Use : Spring, JPA, Mysql,Java

Goal : 매장을 방문할때 미리 방문 예약을 진행하는 기능을 구현하고자 한다.

## 요구사항 
- 예약을 받기 위해서는 "점장(매장의 관리자)이 매장정보를 등록하고자 하는 기능"이 필요
- 매장을 등록하기 위해서는 "파트너 가입 기능"이 구현되어야 함
- 매장 이용자가 "서비스를 통해서 매장을 검색하고 상세정보를 확인하는 기능"을 구현
- 서비스를 통해서 예약한 이후에 "예약 10분전에 도착하여 키오스크를 통해서 방문 확인을 진행하는 기능"을 구현
- 예약 및 사용 이후에 "리뷰를 작성하는 기능" 구현


  ## 시나리오
  "식당이나 점포를 이용하기 전에 미리 예약을 하여 편하게 식당/점포를 이용할수 있는 서비스 개발"
  
  - 매장의 점장은 예약 서비스 앱에 상점을 등록한다.(매장 명, 상점위치, 상점 설명)
  - 매장을 등록하기 위해서는 파트너 회원 가입이 되어야 한다.(따로, 승인 조건은 없으며 가입 후바로 이용 가능)
  - 매장 이용자는 앱을 통해서 매장을 검색하고 상세 정보를 확인한다.
  - 매장의 상세 정보를 보고, 예약을 진행한다.(예약을 진행하기 위해서는 회원 가입이 필수적으로이루어 져야 한다.)
  - 서비스를 통해서 예약한 이후에, 예약 10분전에 도착하여 키오스크를 통해서 방문 확인을 진행한다.
  - 예약 및 사용 이후에 리뷰를 작성할 수 있다.
  - 리뷰의 경우, 수정은 리뷰 작성자만, 삭제 권한은 리뷰를 작성한 사람과 매장의 관리자(점장등)만삭제할 수 있습니다.
       


### 점장
- [ ] 파트너 회원가입(따로 승인 조건 없음)
- [ ] 상점 등록(매장명, 상점 위치, 상점 설명)
- [ ] 유저 방문 확인
- [ ] 리뷰 삭제 권한 
 

### 매장 이용자
- [ ] 회원가입
- [ ] 매장 검색 & 상세정보(매장명, 상점위치, 상점 설명)
- [ ] 예약(회원가입 필수)
- [ ] 키오스크를 통해 방문 확인
- [ ] 리뷰 작성(리뷰 수정은 작성자만, 삭제는 매장관리자와  작성자)

 
