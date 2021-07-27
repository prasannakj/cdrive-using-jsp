<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<%@ include file="header.html" %>
</head>
<body><div style="text-align:center" class="row container">
		 <div class="row" style="display: inline-block; padding: 32px 48px 0px 48px; ">
		    <h2 class="header">Sign up</h2>
		    <div class="card horizontal">
		      <div class="card-stacked">
		        <div class="card-content">
		         <p id="message" class=" deep-orange-text">
		          <%
						if((request.getParameter("uname")!=null)&&(request.getParameter("password")!=null)&&(request.getParameter("fullname")!=null)&&(request.getParameter("email")!=null)){
							
							
							com.johnroid.User user=new com.johnroid.User();
							
							if(user.newUser(request.getParameter("uname"), request.getParameter("password"), request.getParameter("email"), request.getParameter("fullname"), "standard")){
								System.out.print("User created Successfully");
								session.setAttribute("user", user);
								response.sendRedirect("MyDrive.jsp");
							}
							else
								out.println("Failed to create account");
						}
					%>
		          
		          </p>
		         	<div class="row">
					    <form class="col s12" method="post">
					      <div class="row">
					        <div class="input-field col s12">
					          <i class="material-icons prefix">perm_identity</i>
					          <input id="full_name" required type="text" name="fullname" class="validate">
					          <label for="full_name">Full Name</label>
					        </div>
					        <div class="input-field col s12">
					          <i class="material-icons prefix">email</i>
					          <input id="email_id" required name="email" type="email" class="validate">
					          <label for="email_id">Email id</label>
					        </div>
					      </div>
					      <div class="row">
					        <div class="input-field col s12">
					          <i class="material-icons prefix">account_circle</i>
					          <input id="username" required name="uname" type="text" class="validate">
					          <label for="username">User Name</label>
					        </div>
					        <div class="input-field col s12">
					          <i class="material-icons prefix">lock</i>
					          <input id="password" required type="password" name="password" class="validate">
					          <label for="password">Password</label>
					        </div>
					      </div>
					      <button class="waves-effect blue btn">Sign up</button>
					    </form>
					  </div>
		        </div>
		        <div class="card-action">
		          <a href="Login.jsp">Already have an account?</a>
		        </div>
		      </div>
		      </div> 
		</div>
  </div>			
</body>
</html>