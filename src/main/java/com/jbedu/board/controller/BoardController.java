package com.jbedu.board.controller;

import java.io.IOException;
import java.util.List;

import com.jbedu.board.command.BContentCommand;
import com.jbedu.board.command.BDeleteCommand;
import com.jbedu.board.command.BListCommand;
import com.jbedu.board.command.BWriteCommand;
import com.jbedu.board.dao.BoardDao;
import com.jbedu.board.dto.BoardDto;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("*.do") //컨트롤러 기능의 클래스로 서버가 인식하도록 함
public class BoardController extends HttpServlet{

	public BoardController() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		actionDo(request, response);
		
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		actionDo(request, response);
		
	}
	
	private void actionDo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("actionDo 호출!");
		
		request.setCharacterEncoding("utf-8");
		
		String viewPage = null; //글 목록 페이지 파일 이름
		
		String uri = request.getRequestURI();///jsp_mvc_board-20241212/list.jsp
		String conPath = request.getContextPath();///jsp_mvc_board-20241212
		String com = uri.substring(conPath.length());//uri - conPath(길이)->/list.jsp
		
		//System.out.println(uri);
		//System.out.println(conPath);
		//System.out.println(com);
		
		if(com.equals("/list.do")) {
			BListCommand command = new BListCommand();
			command.execute(request, response);
			
			viewPage="list.jsp";
		} else if(com.equals("/write_form.do")) {
			viewPage="write_form.jsp";
		} else if(com.equals("/write.do")) {
			BWriteCommand command = new BWriteCommand();
			command.execute(request, response);
			
			viewPage="list.do";//주의!! list.jsp X list.do O
		} else if(com.equals("/content_view.do")) {
			BContentCommand command = new BContentCommand();
			command.execute(request, response);
			
			viewPage="content_view.jsp";
		} else if(com.equals("/delete.do")) {
			BDeleteCommand command = new BDeleteCommand();
			command.execute(request, response);
			
			viewPage="list.do";//주의!! list.jsp X list.do O
		} else if(com.equals("/modify_form.do")) {
			String bnum = request.getParameter("bnum");//유저가 삭제를 원하는 글의 번호
			String modifyFlag = request.getParameter("modify");
			BoardDao boardDao = new BoardDao();
			BoardDto bDto = boardDao.content_view(bnum);
			request.setAttribute("boardDto", bDto);
			viewPage="modify_form.jsp";
		} else if(com.equals("/modify.do")) {
			String bnum = request.getParameter("bnum");//유저가 수정을 원하는 글의 번호
			String bname = request.getParameter("bname");
			String btitle = request.getParameter("btitle");
			String bcontent = request.getParameter("bcontent");
			BoardDao boardDao = new BoardDao();
			boardDao.board_modify(bnum, bname, btitle, bcontent);
			
			viewPage="list.do";
		} 
		
		RequestDispatcher dispatcher = request.getRequestDispatcher(viewPage);
		dispatcher.forward(request, response);
	}
	
	
}
