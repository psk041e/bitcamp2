// Controller 규칙에 따라 메서드 작성
package bitcamp.java106.pms.servlet.task;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bitcamp.java106.pms.dao.TaskDao;
import bitcamp.java106.pms.dao.TeamDao;
import bitcamp.java106.pms.domain.Task;
import bitcamp.java106.pms.servlet.InitServlet;

@SuppressWarnings("serial")
@WebServlet("/task/view")
public class TaskViewServlet extends HttpServlet {
    
    TeamDao teamDao;
    TaskDao taskDao;
    
    @Override
    public void init() throws ServletException {
        teamDao = InitServlet.getApplicationContext().getBean(TeamDao.class);
        taskDao = InitServlet.getApplicationContext().getBean(TaskDao.class);
    }
    
    @Override
    protected void doGet(
            HttpServletRequest request, 
            HttpServletResponse response) throws ServletException, IOException {

        int no = Integer.parseInt(request.getParameter("no"));

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<title>작업 상세</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>작업 상세 보기</h1>");
        out.println("<form action='update' method='post'>");
        
        try {
            Task task = taskDao.selectOne(no);
            if (task == null) {
                throw new Exception("해당 작업을 찾을 수 없습니다.\n");
            }
            
            out.println("<table border='1'>");
            out.println("<tr><th>팀명</th><td>");
            out.printf("    <input type='text' name='teamName' value='%s' readonly></td></tr>\n", 
                    task.getTeam().getName());
            out.println("<tr><th>작업번호</th><td>");
            out.printf("    <input type='text' name='no' value='%d' readonly></td></tr>\n", 
                    no);
            out.println("<tr><th>작업명</th>");
            out.printf("    <td><input type='text' name='title' value='%s'></td></tr>\n",
                    task.getTitle());
            out.println("<tr><th>시작일</th>");
            out.printf("    <td><input type='date' name='startDate' value='%s'></td></tr>\n",
                    task.getStartDate());
            out.println("<tr><th>종료일</th>");
            out.printf("    <td><input type='date' name='endDate' value='%s'></td></tr>\n",
                    task.getEndDate());
            out.println("<tr><th>작업자</th>");
            out.printf("    <td><input type='text' name='memberId' value='%s'></td></tr>\n",
                    (task.getWorker() == null) ? "-" : task.getWorker().getId());
            out.println("<tr><th>작업상태</th>");
            out.printf("    <td><input type='text' name='state' value='%s'></td></tr>\n",
                    getStateLabel(task.getState()));
            out.println("</table>");
            
        } catch (Exception e) {
            out.printf("<p>%s</p>\n", e.getMessage());
            e.printStackTrace(out);
        }
        out.println("<p>");
        out.println("<a href='../index.html'>[첫 화면]</a>");        
        out.println("<a href='javascript:history.back();'>[목록]</a>");
        out.println("<button>변경</button>");
        out.printf("<a href='delete?no=%d'>[삭제]</a>\n", no);
        out.println("</p>");
        out.println("</form>");
        out.println("</body>");
        out.println("</html>");
    }

    public static String getStateLabel(int state) {
        switch (state) {
        case Task.READY: return "작업대기";
        case Task.WORKING: return "작업중";
        case Task.COMPLETE: return "작업완료";
        default:
            return null;
        }
    }
}

//ver 31 - JDBC API가 적용된 DAO 사용
//ver 28 - 네트워크 버전으로 변경
//ver 26 - TaskController에서 view() 메서드를 추출하여 클래스로 정의.
//ver 23 - @Component 애노테이션을 붙인다.
//ver 22 - TaskDao 변경 사항에 맞춰 이 클래스를 변경한다.
//ver 18 - ArrayList가 적용된 TaskDao를 사용한다.
//ver 17 - 클래스 생성
