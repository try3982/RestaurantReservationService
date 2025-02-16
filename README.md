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
- [x] 파트너 회원가입(따로 승인 조건 없음)
- [x] 상점 등록(매장명, 상점 위치, 상점 설명)
- [x] 유저 방문 확인
- [] 리뷰 삭제 권한 
 

### 매장 이용자
- [x] 회원가입
- [x] 매장 검색 & 상세정보(매장명, 상점위치, 상점 설명)
- [x] 예약(회원가입 필수)
- [x] 키오스크를 통해 방문 확인
- [] 리뷰 작성(리뷰 수정은 작성자만, 삭제는 매장관리자와  작성자)


### api 명세서
---

##  매장 이용자 (고객)

### 1️⃣ 회원가입 (POST `/signup/register`)
- **파라미터**  
  - `email` (이메일)  
  - `password` (비밀번호)  
  - `name` (이름)  
  - `phone` (전화번호)  
- **정책**  
  - 이메일이 이미 존재하는 경우 실패 응답  
- **성공 응답 정보**  
  - `email`, `name`, `phone`, `createdAt`  

### 2️⃣ 로그인 (POST `/login`)
- **파라미터**  
  - `email` (이메일)  
  - `password` (비밀번호)  
- **정책**  
  - 이메일이 존재하지 않거나 비밀번호가 틀린 경우 실패 응답  
- **성공 응답 정보**  
  - `token`, `userId`  

### 3️⃣ 예약 생성 (POST `/reservation/create`)
- **파라미터**  
  - `phoneNumber` (전화번호)  
  - `storeId` (매장 ID)  
  - `reservationTime` (예약 시간)  
- **정책**  
  - 존재하지 않는 사용자 또는 매장일 경우 실패 응답  
  - 과거 시간 예약 불가  
  - 동일한 시간에 중복 예약 불가  
- **성공 응답 정보**  
  - `reservationId`, `customerId`, `storeId`, `reservationTime`, `createdAt`  

### 4️⃣ 고객의 예약 목록 조회 (GET `/reservation/list/{customerId}`)
- **파라미터**  
  - `customerId` (고객 ID)  
- **정책**  
  - 존재하지 않는 사용자일 경우 실패 응답  
- **성공 응답 정보**  
  - `reservationId`, `storeId`, `reservationTime`, `status`  

### 5️⃣ 예약 취소 (DELETE `/reservation/cancel/{reservationId}`)
- **파라미터**  
  - `reservationId` (예약 ID)  
- **정책**  
  - 존재하지 않는 예약일 경우 실패 응답  
- **성공 응답 정보**  
  - `message`  

---

##  매장 관리자

### 1️⃣ 회원가입 (POST `/signup/partnerRegister`)
- **파라미터**  
  - `email`, `password`, `name`, `phone`  
- **정책**  
  - 이메일이 이미 존재하는 경우 실패 응답  
- **성공 응답 정보**  
  - `email`, `name`, `phone`, `createdAt`  

---

##  매장

### 1️⃣ 매장 등록 (POST `/store/register`)
- **파라미터**  
  - `managerName`, `restaurantName`, `restaurantAddress`, `restaurantDetail`, `lat`, `lnt`  
- **정책**  
  - 동일한 `restaurantName`이 이미 존재하는 경우 실패 응답  
- **성공 응답 정보**  
  - `storeId`, `managerName`, `restaurantName`, `restaurantAddress`, `restaurantDetail`, `lat`, `lnt`, `createdAt`  

### 2️⃣ 매장 수정 (PUT `/store/update/{storeId}`)
- **파라미터**  
  - `managerName`, `restaurantName`, `restaurantAddress`, `restaurantDetail`, `lat`, `lnt`  
- **정책**  
  - 동일한 `restaurantName`이 이미 존재하는 경우 실패 응답  
- **성공 응답 정보**  
  - `storeId`, `managerName`, `restaurantName`, `restaurantAddress`, `restaurantDetail`, `lat`, `lnt`, `modifiedAt`  

### 3️⃣ 매장 삭제 (DELETE `/store/delete/{storeId}`)
- **파라미터**  
  - `storeId` (매장 ID)  
- **정책**  
  - 존재하지 않는 매장일 경우 실패 응답  
- **성공 응답 정보**  
  - `message`  

### 4️⃣ 매장 검색 (GET `/store/search`)
- **파라미터**  
  - `query` (검색 키워드)  
- **성공 응답 정보**  
  - `storeId`, `restaurantName`, `restaurantAddress`, `rating`  

---

##  방문 확인 (키오스크)

### 1️⃣ 방문 확인 (PUT `/kiosk/confirm/{reservationId}`)
- **파라미터**  
  - `reservationId` (예약 ID)  
- **정책**  
  - 존재하지 않는 예약일 경우 실패 응답  
  - 이미 방문 확인이 된 경우 실패 응답  
- **성공 응답 정보**  
  - `reservationId`, `status`, `visitedAt`  


 
