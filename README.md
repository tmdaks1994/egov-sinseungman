스프링 작업순서
스프링 HelloWorld MVC 프로젝트 org.edu.controller 제작OK.
wamp(만세아이콘)으로 마리아DB(3306포트) 설치, 사용자암호 추가 및 한글처리OK.
워크벤치 설치 및 ERD 작성연습, 샘플DB(edu)임포트 및 리버스 엔지니어링으로 ERD제작OK.
샘플반응형 웹페이지(mobile,tablet,pc용) 및 J쿼리 페이지 작성OK.
스프링 프로젝트 관리자단 AdminLTE(부트스트랩)기반으로 제작OK.
스프링 테스트 pom.xml(외부라이브러리가져다가사용하는 방식) 디펜던시 의존성 추가OK.
메이븐기반 전자정부표준프레임워크 egov3.9버전 -> 3.10으로 업드레이드OK.
junit(JavaUnit) 테스트 설정 후 기본 unit유닛(단위)테스트OK.
jdbc(JavaDataBaseConnection)사용 pom.xml 의존성 추가OK.
Mysql사용 pom.xml 의존성 추가OK.
마이바티스 사용(CRUD쿼리를관리하는툴) pom.xml 의존성 추가OK.
junit으로 DB접근 후 관리자단 회원관리 CRUD unit테스트 마무리OK.
@Component애노테이션사용으로 MemberVO 인젝션사용 가능OK.
DB 디버그용 드라이버 사용 pom.xml 의존성 추가 후, log4jdbc.log4j2.properties 추가 OK.
실제 회원관리 화면 CRUD 적용 중 jsp중 member_list(select+검색)처리 후 페이징처리 OK.
member_write, member_update, member_delete 만들기 작업OK.
스프링 AOP(관점지향프로그래밍-OOP의 확장기능)기능으로 개발용 디버그출력환경 만들기 시작.
pom.xml에 AOP모듈 추가 필수
root-context.xml에서 aop태그 추가
관리자단 실제 게시판 화면 CRUD 적용OK.
트랜잭션 @Tansactional추가: root-context.xml에서 dataSource에 트랜잭션 설정추가필수OK.
파일업로드 라이브러리 사용 pom.xml 의존성 추가OK.
관리자단 게시판 업로드 화면 구현OK.
댓글에서 Json데이터 사용 pom.xml 의존성 추가.(댓글 Rest-Api에서필요)
보통 jackson, Gson 외부라이브러리를 사용할때는 pom.xml에 모듈을 추가해야 하지만,
Rest컨트롤러 클래스안에 ResponseEntity를 사용해서 Json데이터로 반환합니다.
그래서, pom.xml 모듈추가 없가없이 작업 진행 합니다.X(잘못된 정보)
jackson-databind 모듈추가했음.
실제 댓글 화면CRUD적용.(우리가 만들어서 제공 Rest-API백엔드단)OK.
사용자단 html(https://miniplugin.github.io/) 소스를 커스터마이징 후 jsp로 만들기OK.
인터셉터(가로채기-Interceptor)클래스를 이용해서, 예외처리를 공통 spring_error.jsp 로 바인딩 처리OK.
스프링시큐리티 로그인 구현 pom.xml 의존성 추가OK.
web.xml에 스프링시큐리티 설정 추가OK.
security-context.xml OK.
스프링빈클래스작업: 로그인 구현 + 관리자 회원등록시 패스워드 암호화 추가 OK.
사용자단 CRUD 구현(RestAPI 댓글포함)OK.
헤로쿠 클라우드로 배포(Hsql데이터베이스사용).
이후 유효성검사(객체검증,마이페이지,회원가입-탈퇴), 네이버아이디 로그인(네이버에서 제공Rest-API백엔드단) 사용 등등. pom.xml 의존성 추가.
게시판분리(공지사항과 겔러리게시판): 부모테이블과 필드추가 를 이용해서 다중게시판 생성처리.
오라클로 마이그레이션 작업OK.
웹프로젝트 소스를 스프링프레임워크 버전으로 5.2.5 마이그레이션(버전 업그레이드) 헤로쿠배포OK.
오라클 이론OK. ---------------------- 작업중 ------------------------------
eGovFrame메뉴에서 Start > New TemplateProject 심플홈 템플릿 만들어서 커스터 마이징 작업중.
파스타클라우드 제일 마직막 달에 2주 기간중 배포(클라우드용-mysql을사용)
IoT(아두이노,노드MCU보드로 실습-C언어책3권) 2주
안드로이드앱(클라이언트)-통신-자바:스프링웹프로젝트(API서버) 2주


20210216(화) 작업.
이클립스 빌드 속도 때문에 window메뉴->preference->validation검색->build탭의 체크모두해제 시킴->OK.
web패키지(컨트롤러) 작업 + AdminLTE로 뷰jsp단 작업.(마이바티스기반-새로만들기)
jsp단의 root(최상위)경로를 html태그의 / 에서 <c:url value='/' /> 로 변경했음.
/admin/home.do URL액션을 주는 설정(web.xml) 확인.
/admin/**/*.do URL액션에 로그인체크권한설정추가(egov-com-servlet.xml).
결과는 사용자(기존)과 관리자단을 분리했습니다.
관리자단 home.jsp > header, footer 로 분리.
코드 인스펙션(소스 분석): egov프로젝트의 진입점(webapp/index.jsp) 확인.
egov프로젝트의 jsp(동적페이지)폴더에 html(정적페이지)를 jsp로 변환해서 배치합니다.
공통코드(기본코드) 테이블 이용에 대한 개념이 있어야 합니다. 오늘은 여기까지
-- Mysql 데이터베이스에서 CODE테이블의 코드명 가져오는 함수 만들기 (아래)
CREATE FUNCTION FN_CODE_NM (
P_CODE_ID VARCHAR(255),
P_CODE VARCHAR(255)
)
RETURNS VARCHAR(255)
BEGIN
DECLARE RETURN_VAR VARCHAR(255);
SELECT CODE_NM INTO RETURN_VAR
FROM lettccmmndetailcode 
WHERE 
CODE_ID = P_CODE_ID AND CODE = P_CODE;
RETURN RETURN_VAR;
END

CREATE FUNCTION FN_GROUP_NM(P_GROUP_ID VARCHAR(255)
) RETURNS varchar(255) CHARSET utf8
BEGIN
DECLARE RETURN_VAR VARCHAR(255);
SELECT GROUP_NM INTO RETURN_VAR
FROM lettnauthorgroupinfo
WHERE 
GROUP_ID = P_GROUP_ID;
RETURN RETURN_VAR;
END