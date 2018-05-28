package bitcamp.java106.pms.servlet.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
@WebServlet("/auth/logout")
public class LogoutServlet extends HttpServlet {
    
    @Override
    protected void doGet(
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 세션 강제 종료
        request.getSession().invalidate();
        
        // 웹 어플리ㅔ이션의 시작 페이지로 가라고 웹브라우저에게 얘기한다.
        response.sendRedirect(request.getContextPath()); // ==> "/java106-java-project"
    }
}

//ver 41 - 클래스 추가