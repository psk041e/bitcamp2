// Controller 규칙에 따라 메서드 작성
package bitcamp.java106.pms.servlet.task;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bitcamp.java106.pms.dao.TaskDao;
import bitcamp.java106.pms.dao.TeamDao;
import bitcamp.java106.pms.domain.Task;
import bitcamp.java106.pms.domain.Team;
import bitcamp.java106.pms.server.ServerRequest;
import bitcamp.java106.pms.server.ServerResponse;
import bitcamp.java106.pms.servlet.InitServlet;


@SuppressWarnings("serial")
@WebServlet("/task/list")
public class TaskListServlet extends HttpServlet {
    
    TeamDao teamDao;
    TaskDao taskDao;
    
    @Override
    public void init() throws ServletException {
        teamDao = InitServlet.getApplicationContext().getBean(TeamDao.class);
        taskDao = InitServlet.getApplicationContext().getBean(TaskDao.class);
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html; charset=utf-8");
        PrintWriter out = response.getWriter();
        String teamName = request.getParameter("teamName");
        
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<title>작업 목록</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>작업 목록</h1>");
        
        try {
            Team team = teamDao.selectOne(teamName);
            if (team == null) {
                out.printf("<p>'%s' 팀은 존재하지 않습니다.\n</p>",
                        teamName);
                return;
            }
            List<Task> list = taskDao.selectList(team.getName());
            out.println("<p><a href='form.html'>작업자 추가</a></p>");
            out.println("<table border='1'>");
            out.println("<tr>");
            out.println("   <th>번호</th><th>작업명</th><th>시작일</th>"
                    +      "<th>종료일</th><th>작업자</th>");
            out.println("</tr>");
            
            for (Task task : list) {
                out.println("<tr>");
                out.printf("    <td><a href='view?no=%d'>%d</a></td><td>%s</td>"
                        +      "<td>%s</td>,<td>%s</td>,<td>%s</td>,<td>%s</td>\n", 
                        task.getNo(), task.getNo(), task.getTitle(), 
                        task.getStartDate(), task.getEndDate(),
                        (task.getWorker() == null) ? 
                                "-" : task.getWorker().getId());
                out.println("</tr>");
            }
            out.println("</table>");
        } catch (Exception e) {
            out.println("<p>목록 가져오기 실패!</p>");
            e.printStackTrace(out);
        }
        out.println("</body>");
        out.println("</html>");
    }

}

//ver 31 - JDBC API가 적용된 DAO 사용
//ver 28 - 네트워크 버전으로 변경
//ver 26 - TaskController에서 list() 메서드를 추출하여 클래스로 정의.
//ver 23 - @Component 애노테이션을 붙인다.
//ver 22 - TaskDao 변경 사항에 맞춰 이 클래스를 변경한다.
//ver 18 - ArrayList가 적용된 TaskDao를 사용한다.
//ver 17 - 클래스 생성
