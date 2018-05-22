package bitcamp.java106.pms.servlet.teammember;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bitcamp.java106.pms.dao.TeamMemberDao;
import bitcamp.java106.pms.domain.Member;
import bitcamp.java106.pms.servlet.InitServlet;

@SuppressWarnings("serial")
@WebServlet("/team/member/list")
public class TeamMemberLIstServlet extends HttpServlet {

    TeamMemberDao teamMemberDao;
    
    @Override
    public void init() throws ServletException {
        teamMemberDao = InitServlet.getApplicationContext().getBean(TeamMemberDao.class);
    }
    
    
    @Override
    protected void doGet(
            HttpServletRequest request, 
            HttpServletResponse response) throws ServletException, IOException {
        
        String name = request.getParameter("name");
        
        PrintWriter out = response.getWriter();

        List<Member> members;
        try {
            members = teamMemberDao.selectListWithEmail(name);
        
        out.println("<h2>회원 목록</h2>");
        out.println("<form action='member/add' method='post'>");
        out.println("<input type='text' name='memberId' placeholder='회원아이디'>");
        out.printf("<input type='hidden' name='teamName' value='%s'>\n", name);
        out.println("<button>추가</button>");
        out.println("</form>");
        out.println("<table border='1'>");
        out.println("<tr><th>아이디</th><th>이메일</th><th> </th></tr>");
        for (Member member : members) {
            out.printf("<tr>"
                    + "<td>%s</td>"
                    + "<td>%s</td>"
                    + "<td><a href='member/delete?teamName=%s&memberId=%s'>삭제</a></td>"
                    + "</tr>\n", 
                    member.getId(), 
                    member.getEmail(),
                    name,
                    member.getId());
        }
        out.println("</table>");
        
        } catch (Exception e) {
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/error");
            request.setAttribute("error", e);
            request.setAttribute("title", "팀 회원 조회 실패");
            requestDispatcher.forward(request, response);
            
        }
        
    }
    
}
