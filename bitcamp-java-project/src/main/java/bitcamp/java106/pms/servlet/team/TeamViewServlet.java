// Controller 규칙에 따라 메서드 작성
package bitcamp.java106.pms.servlet.team;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bitcamp.java106.pms.dao.TeamDao;
import bitcamp.java106.pms.domain.Team;
import bitcamp.java106.pms.servlet.InitServlet;

@SuppressWarnings("serial")
@WebServlet("/team/view")
public class TeamViewServlet extends HttpServlet {

    TeamDao teamDao;
    
    @Override
    public void init() throws ServletException {
        teamDao = InitServlet.getApplicationContext().getBean(TeamDao.class);
    }
    
    @Override
    protected void doGet(
            HttpServletRequest request, 
            HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<title>팀 정보 보기</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>팀 정보 보기</h1>");
        out.println("<form action='update' method='post'>");
        try {
            Team team = teamDao.selectOne(name);
    
            if (team == null) {
                out.println("해당 이름의 팀이 없습니다.");
            } 
            out.println("<table border='1'>");
            out.printf("<tr><th>팀명</th><td><input type='text' name='name' value='%s' readonly></td></tr>",team.getName());
            out.printf("<tr><th>설명</th><td><textarea name='description' rows='10' cols='60'>%s</textarea></td></tr>",team.getDescription());
            out.printf("<tr><th>최대인원</th><td><input type='text' name='maxQty' value='%d'></td></tr>",team.getMaxQty());
            out.printf("<tr><th>기간</th><td><input type='date' name='startDate' value='%s'> ~"
                    + "<input type='date' name='endDate' value='%s'></td></tr>", team.getStartDate(), team.getEndDate());
            out.println("</table>");
            
            
        } catch (Exception e) {
            out.printf("<p>&s</p>",e.getMessage());
        }
        out.println("<p>");
        out.println("<a href='list'>목록</a>");
        out.println("<button>변경</button>");
        out.printf("<a href='delete?name=%s'>삭제</a>\n", name);
        out.println("</p>");
        out.println("</form>");
        out.println("</body>");
        out.println("</html>");
    }
}

//ver 31 - JDBC API가 적용된 DAO 사용
//ver 28 - 네트워크 버전으로 변경
//ver 26 - TeamController에서 view() 메서드를 추출하여 클래스로 정의.
//ver 23 - @Component 애노테이션을 붙인다.
//ver 22 - TaskDao 변경 사항에 맞춰 이 클래스를 변경한다.
//ver 18 - ArrayList가 적용된 TeamDao를 사용한다.
//ver 16 - 인스턴스 변수를 직접 사용하는 대신 겟터, 셋터 사용.
// ver 15 - TeamDao를 생성자에서 주입 받도록 변경.
// ver 14 - TeamDao를 사용하여 팀 데이터를 관리한다.
// ver 13 - 시작일, 종료일을 문자열로 입력 받아 Date 객체로 변환하여 저장.