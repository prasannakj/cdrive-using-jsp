
<%@ page import="com.johnroid.*" %>
<%@ page import="java.io.*,java.text.SimpleDateFormat,java.util.*" %>

<table>
  <thead>
    <tr>
        <th>Name</th>
        <th>SIZE</th>
        <th>Type</th>
        <th>Last modified</th>
    </tr>
  </thead>
  <tbody>
  <%
  if(request.getAttribute("files")!=null){
    MyFile files[]=(MyFile[])request.getAttribute("files");
    boolean isempty=true;
      //SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy 'at' hh:mm a");
      String parent;
      for (MyFile file : files) {
          //String size=(file.length()>1000000)?((file.length()>1000000000)?String.format("%.04f",(file.length()*1e-9))+" GB":String.format("%.04f",(file.length()*1e-6))+" MB"):String.format("%.04f",(file.length()*0.001))+" KB";
          
          parent=file.getPath();
          parent=parent.substring(0,parent.lastIndexOf('/'));
          if (!file.isHidden()) {
              //if (file.isDirectory()) {
                  isempty=false;
                  out.println("<tr id='"+file.getName()+"' parent='"+parent+"' ><td>" +file.getName()+ "</td> "+((file.isDirectory())?"<td> - </td><td>Folder":"<td> "+ file.getSizeText() +"</td><td>File")+"</td><td>"
                      + file.getDateText() + "</td></tr>");
              /*} else {
                  isempty=false;
                  out.println("<tr id="+file.getPath()+"> <td>" +file.getName()+ "</td> <td>" +size+"</td><td>File</td><td>"
                      + df.format(new Date(file.lastModified())) + "</td></tr>");
              }*/
          }
      }
    if(isempty)
      out.println("<tr><td colspan=\"4\">Folder is empty!</td></tr>");
  }
  %>
  </tbody>
</table>