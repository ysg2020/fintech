# :bank: 핀테크 서비스 API
입출금 및 송금 기능이 있는 핀테크 서비스 API 입니다


## :wrench: 기술 스택
- Spring boot , Java
- MySql, Jpa

## :page_with_curl: 프로젝트 기능 및 설계
### :warning: 제약 사항
1. 계좌 제약 사항
   - 계좌는 최대 10개까지 개설 가능
   - 계좌 해지시 계좌에 잔액이 있거나 이미 해지 상태라면 실패
   - 계좌 상태는 사용중이거나 해지 총 2가지의 상태가 존재 
2. 거래 제약 사항
   - 계좌가 존재하지 않거나 해지 상태일경우 거래 실패
   - 거래할 금액이 잔액보다 클 경우 거래 실패 (입금 제외)
   - 거래 취소는 거래 일시로부터 5일까지 가능
   - 거래 상태는 성공, 취소 총 2가지의 상태가 존재
   - 거래 종류는 입출금 및 송금 총 3가지의 상태가 존재
   - 거래 종류가 송금일 경우에만 거래 대상자가 존재
---
### :moneybag: 프로젝트 기능
- 계좌 관리 기능
  - 회원이 계좌를 생성하거나 해지, 조회 할 수 있습니다
- 송금 기능
  - 회원이 다른 회원에게 입금 할 수 있습니다
- 입금 및 출금 기능
  - 내 계좌에 금액을 입금하거나 출금 할 수 있습니다
- 거래 내역 조회 기능
  - 내 계좌 별로 보유하고 있는 잔액과 거래내역을 조회 할 수 있습니다
- 내 정보 조회 기능
  - 회원 가입시 입력한 정보를 조회,수정 할 수 있고 탈퇴 할 수 있습니다
---
## :pushpin: API
계좌 개설
- POST /account

계좌 해지
- DEL /account

계좌 목록 조회
- GET /account?memberidx={memberidx}

계좌 조회
- GET /account?accNum={accNum}

거래내역 조회
- GET /trans?accountidx={accountidx}

거래 (입금,출금,송금)
- POST /trans

거래취소 (입금,출금,송금)
- DEL /trans

회원가입
- POST /member

회원탈퇴
- DEL /member


## :ledger: ERD 설계
![fintechERD](https://github.com/ysg2020/fintech/assets/70841944/f0a45198-95ea-4c09-9f79-7e4354f3b72b)



