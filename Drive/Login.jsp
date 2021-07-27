<%@ page import="com.johnroid.*" %>
<%@ page import="java.util.List" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<%@ include file="header.html"%>


</head>
<body>
	<div style="text-align:center" class="row container">
		 <div class="row" style="display: inline-block; padding: 32px 48px 0px 48px; ">
		    <h2 class="header">Login</h2>
		    <div class="card horizontal">
		      <div class="card-stacked">
		        <div class="card-content">
		          <p id="message" class=" deep-orange-text">
		          <%
				  		com.johnroid.DbConnect file = new com.johnroid.DbConnect();
						if((request.getParameter("uname")!=null)&&(request.getParameter("password")!=null)){
							
							String uname=request.getParameter("uname");
							String pswd=request.getParameter("password");
							//System.out.println(uname);
							//System.out.println(pswd);
							
							User user=new User();
							
							if(user.signIn(uname, pswd)){
								//System.out.print("Login Success");
								session.setAttribute("user", user);
								response.sendRedirect("MyDrive.jsp");
							}
							else
								out.println("Login Details Incorrect. Please try again.");
						}
					%>
		          
		          </p>
		         	<div class="row">
					    <form method="post" class="col s12">
					      <div class="row">
					        <div class="input-field col s12">
					          <i class="material-icons prefix">account_circle</i>
					          <input id="icon_prefix" required name="uname" type="text" class="validate">
					          <label for="icon_prefix">User Name</label>
					        </div>
					        <div class="input-field col s12">
					          <i class="material-icons prefix">lock</i>
					          <input id="icon_telephone" required type="password" name="password" class="validate">
					          <label for="icon_telephone">Password</label>
					        </div>
					      </div>
					      <button class="waves-effect blue btn">Login</button>
					    </form>
					  </div>
		        </div>
		        <div class="card-action">
		          <a href="signup.jsp">Don’t have an account?</a>
		        </div>
		      </div>
		      </div> 
		</div>
  </div>				
</body>
</html>