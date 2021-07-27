package com.johnroid;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.RequestDispatcher;
import java.util.ArrayList;
import com.johnroid.*;

import org.json.simple.*; 
import org.json.simple.parser.*; 

@WebServlet("/AjaxFileOpr")
@SuppressWarnings("unchecked")
public class AjaxFileOpr extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject jo = new JSONObject(); 
		HttpSession session=request.getSession();
		PrintWriter out=response.getWriter();
		User user=(User)session.getAttribute("user");
		boolean jsonResult=true;
		if(session.getAttribute("user")==null){
			//response.sendRedirect("Login.jsp");
			jo.put("status", "Forbidden"); 
			//jo.put("code", "403"); 
		}
		else{
			if(request.getParameter("q")!=null)
			{	
				String fname;
				//jo.put("q",request.getParameter("q"));
				switch(request.getParameter("q")){
					
					case "new_file":
						fname=request.getParameter("f_name");
						MyFile file=new MyFile(request.getParameter("parent")+"/"+fname);
						if(file.exists()){
							jo.put("status","failed");
							jo.put("text","File name \""+fname+"\" already exist. Try different name!");
						}
						else if(file.createNewFile(user.getUserName())){
							jo.put("status","success");
							jo.put("text","File \""+fname+"\" was created successfully!");
							jo.put("newrow","<tr id='"+fname+"'><td>"+ fname +"</td><td>"+ file.getSizeText() +"</td><td>File</td><td>"+ file.getDateText() +"</td></tr>");
						}
						else{
							jo.put("status","failed");
							jo.put("text","Sorry! there was an error creating the file!");
						}
						break;
					case "new_folder":
						fname=request.getParameter("f_name");
						MyFile folder=new MyFile(request.getParameter("parent")+"/"+fname);
						if(folder.exists()){
							jo.put("status","failed");
							jo.put("text","Folder name \""+fname+"\" already exist. Try different name!");
						}
						else
						if(folder.mkdir(user.getUserName())){
							jo.put("status","success");
							jo.put("text","Folder \""+fname+"\" was created successfully!");
							jo.put("newrow","<tr id='"+fname+"'><td>"+ fname +"</td><td> - </td><td>Folder</td><td>"+ folder.getDateText() +"</td></tr>");
						}
						else{
							jo.put("status","failed");
							jo.put("text","Sorry! there was an error creating the folder!");
						}
						break;
					case "rename":
						String oldname=request.getParameter("oldname");
						String newname=request.getParameter("newname");
						String parent=request.getParameter("parent");
						MyFile f=new MyFile(parent+"/"+oldname);
						//jo.put("name",parent+"/"+oldname);
						// if(f.exists())
						// 	jo.put("exist","yes");
						// else
						// 	jo.put("exist","no");
						if(f.rename(parent+"/"+newname)){
							jo.put("status","success");
							jo.put("text","Renamed successfully!");
							//jo.put("parent",request.getParameter("parent"));
						}
						else{
							jo.put("status","failed");
							jo.put("text","Sorry! there was an error in renaming!");						
						}
						// jo.put("old",oldname);
						// jo.put("new",newname);
						// jo.put("parent",request.getParameter("parent"));
						//out.print(oldname+"<br>"+newname);
						break;
					case "fileOpen":
						String f1=request.getParameter("parent")+"/"+request.getParameter("filename");
						jo.put("text",new MyFile(f1).open());
						//jo.put("fname",f1);
						break;
					case "save_file":
						String f2=request.getParameter("parent")+"/"+request.getParameter("filename");
						jo.put("fname",f2);
						jo.put("text",request.getParameter("text"));
						try{
							FileWriter fw=new FileWriter(f2);
							fw.write(request.getParameter("text"));
							fw.close();
							jo.put("status","success");
							jo.put("text","File '"+request.getParameter("filename")+"' was saved successfully!");
						}
						catch(Exception e){
							jo.put("status","failed");
							jo.put("text","Sorry! there was an error saving the file!");
						}
						break;
					case "get_files":
						String f3=request.getParameter("parent")+"/"+request.getParameter("folder");
						request.setAttribute("files", new MyFile(f3).listFiles());
						RequestDispatcher re=request.getRequestDispatcher("listFiles.jsp");
						re.include(request, response);
						jsonResult=false;
						break;
					case "remove_file":
						if(new MyFile(request.getParameter("parent")+"/"+request.getParameter("files")).moveToTrash()){
							jo.put("status","success");
							jo.put("text","File '"+request.getParameter("files")+"' was removed!");
						}
						else
							jo.put("status","failed");
						break;
					case "move":
						MyFile mf=new MyFile(request.getParameter("src"));
						if(mf.moveTo(request.getParameter("dest"))){
							jo.put("status","success");
							jo.put("text","File(s) are moved!");
						}
						else{
							jo.put("status","failed");
							jo.put("text","Error moving file!");
						}
						break;
					case "copy":
						String newName;
						MyFile f4=new MyFile(request.getParameter("src"));
						if((newName=f4.copyTo(request.getParameter("dest")))!=""){
							jo.put("status","success");
							if(f4.isFile())
								jo.put("newrow","<tr id='"+newName+"'><td>"+ newName +"</td><td>"+ f4.getSizeText() +"</td><td>File</td><td>"+ f4.getDateText() +"</td></tr>");
							else
								jo.put("newrow","<tr id='"+newName+"'><td>"+ newName +"</td><td> - </td><td>Folder</td><td>"+ f4.getDateText() +"</td></tr>");
							jo.put("newname",newName);
							jo.put("text","File(s) are copied!");
						}
						else{
							jo.put("status","failed");
							jo.put("text","Error coping file!");
						}
						break;
					case "info":
						MyFile f5=new MyFile(request.getParameter("file"));
						jo.put("owner",f5.getOwnerName());
						jo.put("size",f5.getSizeText());
						jo.put("date",f5.getDateText());
						String path=f5.getPath();
						path=path.substring(path.indexOf('/'));
						jo.put("path",path);
						jo.put("type",((f5.isFile())?"File":"Folder"));
						jo.put("shared",f5.isShared());
						if(f5.isShared()){
							JSONObject sha = new JSONObject(); 
							ArrayList<String> ja=new ArrayList<String>();
							ArrayList<SharingInfo> shinfo=f5.getSharingInfo();
							for(int i=0;i<shinfo.size();i++) {
								SharingInfo info=shinfo.get(i);
								sha.put("user",info.user);
								sha.put("permission",((info.permission==MyFile.READ_ONLY)?"Read only":"Read / write"));
								sha.put("shared_on",info.shared_on.toString());
								ja.add(sha.toJSONString());
							}
							jo.put("sharing_info",ja.toString());
						}
						break;
					case "is_exist":
						if(new File(request.getParameter("file")).exists())
							jo.put("result","true");
						else
							jo.put("result","false");
						break;
					case "share":
						boolean r=true;
						try{
							JSONArray users = (JSONArray)new JSONParser().parse(request.getParameter("users"));
							// int i=0;
							String usr="";
							for(Object tag:users){
								 JSONObject t=(JSONObject)tag;
								 if(!FileShare.shareTo(request.getParameter("file"),(String)t.get("tag"),Integer.parseInt(request.getParameter("permission")))){
									 r=false;
									 usr+=t.get("tag")+",";
								 }
								//  jo.put(i,t.get("tag")); 
								//  i++;
							}
							if(!r){
								if(usr.lastIndexOf(',')==(usr.length()-1))
									jo.put("text","Can't share file to "+usr.substring(0,usr.length()-1)+" !");
								else
									jo.put("text","Can't share file to "+usr+"!");
							}
							// jo.put("u1",users.get(0).tag);
							// jo.put("size",users.size());
						}
						catch(ParseException e){
							r=false;
							jo.put("text","Sorry can't share file!");
						}
						if(r){
							jo.put("status","success");
							jo.put("text","File shared successfully!");
						}
						else{
							jo.put("status","failed");
						}
							
						break;
					case "star":
						if(FileShare.addToStared(request.getParameter("file"))){
							jo.put("status","success");
							jo.put("text","File added to stared!");
						}
						else{
							jo.put("status","failed");
							jo.put("text","Failed to star the file!");
						}
						break;

				}
				//response.getWriter().println(fname);
			}
		}
		if(jsonResult)
			out.print(jo.toJSONString());
	}

}
