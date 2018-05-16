// Controller 규칙에 따라 메서드 작성
package bitcamp.java106.pms.servlet.classroom;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bitcamp.java106.pms.dao.ClassroomDao;
import bitcamp.java106.pms.domain.Classroom;
import bitcamp.java106.pms.servlet.InitServlet;

@SuppressWarnings("serial")
@WebServlet("/classroom/list")
public class ClassroomListServlet extends HttpServlet {
    ClassroomDao classroomDao;
    
    @Override
    public void init() throws ServletException {
        classroomDao = InitServlet.getApplicationContext().getBean(ClassroomDao.class);
    }

    @Override
    protected void doGet(
            HttpServletRequest request, 
            HttpServletResponse response) throws ServletException, IOException {

        
        response.setContentType("text/html; charset=utf-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<title>수업 목록</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>수업 목록</h1>");
        
        try {
            List<Classroom> list = classroomDao.selectList();
            
            out.println("<p><a href='form.html'>새 수업 추가</a></p>");
            out.println("<table border='1'>");
            out.println("<tr>");
            out.println("    <th>번호</th><th>수업명</th><th>시작일</th><th>종료일</th><th>강의실</th>");
            out.println("</tr>");
            for (Classroom classroom : list) {
                out.println("<tr>");
                out.printf("    <td>%d</td><td><a href='view?no=%d'>%s</a></td><td>%s</td><td>%s</td><td>%s</td>\n",
                    classroom.getNo(), 
                    classroom.getNo(),
                    classroom.getTitle(), 
                    classroom.getStartDate(),
                    classroom.getEndDate(),
                    classroom.getRoom());
                out.println("</tr>");
            }
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
//ver 26 - ClassroomController에서 list() 메서드를 추출하여 클래스로 정의.

