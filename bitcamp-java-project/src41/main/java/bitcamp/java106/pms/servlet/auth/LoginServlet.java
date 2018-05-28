package bitcamp.java106.pms.servlet.auth;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;

import bitcamp.java106.pms.dao.MemberDao;
import bitcamp.java106.pms.domain.Member;
import bitcamp.java106.pms.support.WebApplicationContextUtils;

@SuppressWarnings("serial")
@WebServlet("/auth/login")
public class LoginServlet extends HttpServlet {
    MemberDao memberDao;    // 회원 기능 필수 요소
    
    @Override
    public void init() throws ServletException {
        // 자원 초기화!
        ApplicationContext iocContainer = 
                WebApplicationContextUtils.getWebApplicationContext(
                        this.getServletContext());
        memberDao = iocContainer.getBean(MemberDao.class);
    }
    
    // 여기는 로그인 폼을 구현하는 메서드
    @Override
    protected void doGet(
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String id = "";
        
        // 쿠기 등록 (Session)
        Cookie[] cookies = request.getCookies();
        
        // 쿠기가 등록 되어있고,
        if(cookies != null) {
            // 쿠키로 이용한 아이디를 찾는다!
            for (Cookie cookie : cookies) {
                // 아이디가 있을 시, 해당 id를 등록한다
                if (cookie.getName().equals("id")) {
                    id = cookie.getValue();
                    break;
                }
            }
        }
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        // 로그인 폼 생성!
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<title>로그인</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>로그인</h1>");
        // 이부분을 get 방식에서 post 방식으로 읽어 들이는 방식으로 LoginServlet의 post에서 읽는다
        out.println("<form action='login' method='post'>");
        out.println("<table border='1'>");
        out.println("<tr><th>아이디</th>");
        out.printf("    <td><input type='text' name='id' value='%s'></td></tr>\n", id);
        out.println("<tr><th>암호</th>");
        out.println("    <td><input type='password' name='password'></td></tr>");
        out.println("</table>");
        out.println("<p><input type='checkbox' name='saveId'> 아이디 저장</p>");
        out.println("<button>로그인</button>");
        out.println("</form>");
        out.println("</body>");
        out.println("</html>");
        
    }
    
    // 여기는 아이디와 비밀번호가 일치한지 검사하는 부분!(doGet() 메서드에서 로그인 이후 바로 실행)
    @Override
    protected void doPost(
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 아이디와 비밀번호 request로 불려오기!
        String id = request.getParameter("id");
        String password = request.getParameter("password");
        
        // 쿠키 시간 지정 최대 7일(아이디 저장 여부)
        Cookie cookie = null;
        if (request.getParameter("saveId") != null) {
            cookie = new Cookie("id", id); // 
            cookie.setMaxAge(60 * 60 * 24 * 7);
        } else { // "아이디 저장" 체크박스를 체크 하지 않을 시
            cookie = new Cookie("id", "");
            cookie.setMaxAge(0);  // 웹브라우저에 "id"라는 이름으로 저장된 쿠키가 있다면 제거한다.
            // 즉 유효기간을 0으로 설정함으로써 "id"라는 이름의 쿠키를 무효화시키는 것이다.
        } 
        response.addCookie(cookie);
        
        // 로그인 제어
        try {
            // 여기서 MyBatis를 이용하여 아이디와 비밀번호 일치 여부 검사한다. 일치 하지 않을 때 null
            Member member = memberDao.selectOneWithPassword(id, password);
            
            // 세션 정의
            HttpSession session = request.getSession();
            
            if (member != null) { // 로그인 성공시 
                response.sendRedirect(request.getContextPath()); // => "/java106-java-project"
                session.setAttribute("loginUser", member);
            } else { // 로그인 실패시
                session.invalidate(); // 세션 강제 종료
                
                response.setContentType("text/html;charset=UTF-8");
                PrintWriter out = response.getWriter();
                
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<meta charset='UTF-8'>");
                out.printf("<meta http-equiv='Refresh' content='1;url=%s'>", 
                        request.getContextPath() + "/auth/login"); 
                out.println("<title>로그인</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>로그인 실패!</h1>");
                out.println("<p>아이디 또는 암호가 맞지 않습니다.</p>");
                out.println("</body>");
                out.println("</html>");
                
            }
            
        } catch (Exception e) {
            RequestDispatcher 요청배달자 = request.getRequestDispatcher("/error");
            request.setAttribute("error", e);
            request.setAttribute("title", "로그인 실패!");
            요청배달자.forward(request, response);
        }
    }
}

//[웹브라우저]                                  [웹서버] 
//GET 요청: /java106-java-project/auth/login ===>
//                                      <=== 응답: 로그인폼 
//POST 요청: /java106-java-project/auth/login ===>
//                                      <=== 응답: redirect URL
//GET 요청: /java106-java-project ===> 
//                                      <=== 응답: index.html
//메인화면 출력!

//ver 41 - 클래스 추가



