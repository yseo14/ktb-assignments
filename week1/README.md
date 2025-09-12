# 주제: 1대1 농구 시뮬레이션

- 팀/선수 목록에서 플레이할 팀과 선수를 선택합니다.
- 상대 선수는 랜덤으로 선택됩니다.
- 11점을 타겟 점수로 두 선수가 1대1 대결을 진행합니다.
    - 선공은 랜덤으로 정해집니다.
    - 슛이 성공하면 공격권을 이어갑니다.
    - 슛이 실패하면 공격권은 상대방에게 넘어갑니다.
    - 각 선수는 공격권을 가졌을 때 2점, 3점슛을 랜덤으로 시도합니다.
    - 누군가 타겟 점수에 도달하면 경기가 종료됩니다.
    - 연승을 하는 경우 체력 이슈로 부상 상태가 됩니다.
        - 부상 상태인 선수는 슛 성공률이 20% 감소합니다.
- 사용자가 이기면, 나머지 선수 중 랜덤으로 대결을 이어갑니다.
- 사용자가 지면, 처음부터 게임을 시작하거나 프로그램을 종료할 수 있습니다.

## 클래스 설계

### Person

조상 클래스에 해당하며, 사람의 기본 정보인 이름, 키, 몸무게를 필드로 가집니다.

### Ahtlete

부모 클래스에 해당하며, 등 번호화 부상 여부를 필드로 가집니다.

### BasketballPlayer

자식 클래스에 해당하며, 초기 슛 성공률, 현재 슛 성공률, 승리수, 패배수, 총 경기수, 연승수, 소속 팀을 필드로 가집니다.

### Team

팀 이름과 선수 목록을 필드로 가집니다.

### Ball

공격권과 경기 종료 여부를 관리하는 **스레드 간 공유 자원** 클래스입니다.

공을 소유한 선수, 경기 종료 여부, lock 객체를 필드로 가집니다.

### PlayerThread

각 농구 선수의 행동을 스레드로 처리하는 클래스입니다.

스레드 처리할 BasketballPlayer 객체, 공격권과 게임의 상태를 공유 받기 위한 Ball 객체, 선수의 득점, 타깃 스코어를 필드로 가집니다.

### Round

1대1 대결 클래스입니다. 경기 전체 흐름을 제어합니다.

해당 클래스 내에서 두 선수가 각각 스레드로 처리되어 실행됩니다.

### GameManager

게임(프로그램) 전체 흐름을 컨트롤하는 클래스입니다.

입출력, 1대1 경기 루프 처리, 게임 흐름을 처리합니다.

## 실행 화면
<img width="697" height="865" alt="ex1" src="https://github.com/user-attachments/assets/8475b0c8-edad-432a-b433-e564626e8116" />
<img width="1010" height="330" alt="ex2" src="https://github.com/user-attachments/assets/0a9d73a8-ce9f-450a-a5f3-6db0965ec0cd" />
<img width="955" height="991" alt="ex3" src="https://github.com/user-attachments/assets/d34108ef-4fb9-41ef-8a32-741a7b9c5065" />
<img width="912" height="832" alt="ex4" src="https://github.com/user-attachments/assets/835a8e05-2c08-484b-b568-d58c63e30a97" />
<img width="760" height="706" alt="ex5" src="https://github.com/user-attachments/assets/ef11fa78-a589-4d35-8703-0a8cb8d47981" />
