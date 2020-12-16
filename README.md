![issue badge](https://img.shields.io/github/license/kong0512/programmers-flo-android-public)

# programmers-flo-android-public
Programmers 과제관 중 안드로이드 앱(FLO 뮤직 플레이어) 구현 (공개판)

## 구현한 기능
 - 2초간 스플래시 이미지 표현 후 메인 화면으로 이동
 - 웹의 json 파일을 읽어 곡에 대한 정보/앨범 커버 이미지/음악 재생
 - 곡 재생 중 실시간으로 가사 표시
 - 가사 부분을 탭하면 전체 가사 보기 화면으로 전환(프래그먼트 사용)
 - 가사로 이동할수 있는 토글 버튼/off시 전체 가사 화면 닫기/on시 해당 가사로 이동
 - 메인 화면에서 '뒤로' 버튼을 탭하면 '종료하시려면 한번 더 터치해주세요'라는 Toast 메시지 표시. 이떄 한번 더 누르면 앱 종료(음악 재생도 종료)하도록 구현
 
## 세부 구현 방식
 - Kotlin & MVVM(AAC)로 구현
 - 플레이어의 UI(재생/정지, Seekbar)는 Activity에, 나머지 화면은 Fragment에서 관리
 - 실제 플레이어(Exoplayer)의 초기화/곡 데이터 로드는 ViewModel에서 관리
 - 메인 화면은 Portrait 모드와 Landscape 모드의 레이아웃을 다르게 구성
 
## 사용 라이브러리
 - JetPack: Android Architecture Component(ViewModel, LiveData)용
 - Retrofit(https://square.github.io/retrofit): HTTP 통신
 - Glide(https://github.com/bumptech/glide): 이미지 로딩
 - ExoPlayer(https://github.com/google/ExoPlayer): 음악 파일 재생용
 - Koin(https://github.com/InsertKoinIO/koin): 의존성 주입
 
## 앞으로 구현/개선할 수 있는 부분?
 - 원래 데이터 로드용 비동기 처리로 Kotlin의 Coroutine을 사용하였는데, 여러 문제가 발생했고, 숙련도가 낮은 편이어서 Retrofit의 자체적인 비동기 코드로 변경. 조금 Coroutine에 대한 숙련도가 생기면 변경할 수 있을지도?
 - ExoPlayer를 서비스 쪽으로 빼서 처리하는 것도 좋을듯?
 - 좀 더 완성된 형태의 앱을 만들자. 실제 음악 앱처럼 미리 음악 데이터베이스를 구축하여 화면에서 선택할 수 있고, 계정 기능을 붙이는 등...
