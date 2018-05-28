package bitcamp.java106.pms.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bitcamp.java106.pms.domain.Member;

@SuppressWarnings("serial")
@WebServlet("/header")
public class HeaderServlet extends HttpServlet {
    
    // 여기서 각 게시판 마다 로그인 및 로그아웃을 수행하는 기능을 말한다!
    @Override
    protected void service(
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 이 서블릿을 include하는 쪽에서 출력스트림의 콘텐트 타입을 설정하기 때문에
        // 이 서블릿에서는 콘텐트 타입을 설정할 필요가 없다.
        //response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        HttpSession session = request.getSession();
        Member loginUser = (Member) session.getAttribute("loginUser");
        
        // 여기서 로그인 하면 로그아웃 창 뜨게 하고, 로그아웃 창 뜨면 로그인 창 뜨게 한다!
        out.println("<div id='header'>");
        if (loginUser != null) {
            out.printf("    %s", loginUser.getId());
            out.printf(" <a href='%s/auth/logout'>로그아웃</a>", 
                    request.getContextPath());
        } else {
            out.printf("<a href='%s/auth/login'>로그인</a>",
                    request.getContextPath());
        }
        out.println("</div>");
    }
}
