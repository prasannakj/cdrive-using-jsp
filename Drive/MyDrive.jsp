<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<%@ include file="header.html" %>
<%@ page import="com.johnroid.*" %>
<%@ page import="java.io.*,java.text.SimpleDateFormat,java.util.*" %>


<%! MyFile f;User user; String FULL_NAME,EMAIL;boolean IS_LOGGED_IN=false;%>

	<%
   // session = request.getSession(false);
    if ((request.getSession(false) == null)||(session.getAttribute("user")==null)){
			response.sendRedirect("Login.jsp");
		}
		else{
			user=(User)session.getAttribute("user");
			f=new MyFile(user.getWorkSpace());
      //if(f.exists())
      IS_LOGGED_IN=user.isSignedIn();

      FULL_NAME=user.getFullName();
      EMAIL=user.getMailid();
      request.setAttribute("files", f.listFiles());
			//out.println(f.getAbsolutePath());
		}
	%>

<style>
      header, main, footer {
        padding-left: 300px;
      }

      .container-fluid{
        padding-left: 10px;
        padding-right: 10px
      }

      .modal-small { width: 500px !important ;}

      
      tr { cursor: default; }
      .highlight { background: #e3f2fd; }
      
      /* #toast-container {
        top: auto !important;
        right: auto !important;
        bottom: 10%;
        left:7%;
      } */

      @media only screen and (max-width : 992px) {
        header, main, footer {
          padding-left: 0;
        }
      }
    </style>
</head>
<body>

<div>
<header>
    <nav class="navbar-fixed nav-extended"> 
      <div class="nav-wrapper blue container-fluid">
        <a href="#" data-target="slide-out" class="sidenav-trigger"><i class="material-icons">menu</i></a>
        <a href="#!" class="brand-logo"><i class="material-icons">dns</i>Drive</a>
        <ul class="right">
          <li><a href="#" id="btn_back" class="tooltipped" data-position="bottom" data-tooltip="Go back"><i class="material-icons">arrow_back</i></a></li>
          <li><a href="#" id="btn_forward" class="tooltipped" data-position="bottom" data-tooltip="Go forward"><i class="material-icons">arrow_forward</i></a></li>
          <li><a href="#" class="tooltipped" data-position="bottom" data-tooltip="Search"><i class="material-icons">search</i></a></li>
        </ul>
      </div>
      <div class="nav-wrapper container-fluid grey lighten-5 indigo-text">
        <div  id="second_nav" class="hide">
        <%-- <div class="nav-title"></div> --%>
          <div class="left"><div class="hide" id="curdir"><%= ((IS_LOGGED_IN)?user.getWorkSpace():"") %></div><b>1</b> File(s) Selected</div>

          <ul class="right">
            <li><a href="#" id="btn_view" class="tooltipped" data-position="bottom" data-tooltip="View"><i class="material-icons indigo-text">visibility</i></a></li>
            <li><a href="#modal_rename" class="tooltipped modal-trigger" data-position="bottom" data-tooltip="Rename"><i class="material-icons indigo-text">edit</i></a></li>
            <li><a href="#" id="btn_remove" class="tooltipped" data-position="bottom" data-tooltip="Remove"><i class="material-icons indigo-text">delete</i></a></li>
            <%-- <li><a href="#" class="tooltipped" data-position="bottom" data-tooltip="Move to"><i class="fas fa-cut indigo-text"></i></a></li>
            <li><a href="#" class="tooltipped" data-position="bottom" data-tooltip="Copy to"><i class="fas fa-copy indigo-text"></i></a></li> --%>
            <li><a href="#" id="share" class="tooltipped" data-position="bottom" data-tooltip="Share"><i class="fas fa-user-plus indigo-text"></i></a></li>
            <li><a href="#" id="btn_star" class="tooltipped" data-position="bottom" data-tooltip="Star"><i class="far fa-star indigo-text"></i></a></li>
            <li><a href="#" id="btn_info" class="tooltipped" data-position="bottom" data-tooltip="View details"><i class="fas fa-info-circle indigo-text"></i></a></li>
            <%-- <li><a class="dropdown-trigger" href="#!" data-target="dropdown1"><i class="material-icons indigo-text">more_vert</i></a></li> --%>
          </ul>
        </div>
    </div>
    </nav>

    <%-- <ul id="dropdown1" class="dropdown-content">
      <li><a href="#!">one</a></li>
      <li><a href="#!">two</a></li>
      <li class="divider"></li>
      <li><a href="#!">three</a></li>
    </ul> --%>
    <div class="fixed-action-btn">
  <a class="btn-floating btn-large red tooltipped" data-position="left" data-tooltip="More">
    <i class="fas fa-ellipsis-v"></i>
  </a>
  <ul>
    <li><a id="btn_download" class="btn-floating blue tooltipped disabled ccd" data-position="left" data-tooltip="Download"><i class="fas fa-download"></i></a></li>
    <li><a id="btn_paste" class="btn-floating blue tooltipped disabled" data-position="left" data-tooltip="Paste"><i class="fas fa-paste"></i></a></li>
    <li><a id="btn_copy" class="btn-floating blue tooltipped disabled ccd" data-position="left" data-tooltip="Copy"><i class="far fa-clone"></i></a></li>
    <li><a id="btn_cut" class="btn-floating blue tooltipped disabled ccd" data-position="left" data-tooltip="Cut"><i class="fas fa-cut"></i></a></li>
  </ul>
</div>

    </header>
    <ul id="slide-out" class="sidenav sidenav-fixed">
      <li><div class="user-view">
        <div class="background blue darken-2">
          
        </div>
        <a href="#"><img class="circle" src="assets/images/avatar-person.svg"></a>
        <a href="#"><span class="white-text name"><%= FULL_NAME %></span></a>
        <a href="#"><span class="white-text email"><%= EMAIL %></span></a>
      </div></li>
      <li><div class="divider"></div></li>
      <li><a class="subheader">New File / Folder</a></li>
      <li><a class="waves-effect modal-trigger" href="#modal_filename"><i class="material-icons">note_add</i>Create new file</a></li>
      <li><a class="waves-effect modal-trigger" href="#modal_foldername"><i class="material-icons">create_new_folder</i>Create new Folder</a></li>
      <li><a class="waves-effect" href="#!"><i class="material-icons">insert_drive_file</i>Upload file</a></li>
      <li><a class="waves-effect" href="#!"><i class="material-icons">folder</i>Upload folder</a></li>
      <li><div class="divider"></div></li>
      <li><a class="subheader">Navigation</a></li>
      <ul id="btn_file_navication">
        <li id="btn_view_mydrive" class="active"><a class="waves-effect" href="#!"><i class="material-icons">dns</i>My Drive</a></li>
        <li id="btn_view_shared"><a class="waves-effect" href="#!"><i class="material-icons">folder_shared</i>Shared with me</a></li>
        <li id="btn_view_recent"><a class="waves-effect" href="#!"><i class="material-icons">access_time</i>Recent</a></li>
        <li id="btn_view_stared"><a class="waves-effect" href="#!"><i class="material-icons">star_rate</i>Starred</a></li>
        <li id="btn_view_trashed"><a class="waves-effect" href="#!"><i class="material-icons">delete</i>Trash</a></li>
      </ul>
      <li><div class="divider"></div></li>
    </ul>
  
</div>
<main>
  <div id="file_container" class="container-fluid">
    <%
        //request.setAttribute("files", f.listFiles());
       // RequestDispatcher teq=request.getRequestDispatcher("listFiles.jsp");
	     // teq.include(request, response);
       if(IS_LOGGED_IN)
          org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "listFiles.jsp", out, false);
    %>
    <%-- <%@ include file="listFiles.jsp" %> --%>
    <%-- <jsp:include page="listFiles.jsp" />   --%>
  </div>
</main>   

  <div id="modal_filename" class="modal modal-small">
    <form id="frm_new_file">
      <div class="modal-content">
        <h4>New file</h4>
        <input type="text" id="txt_file_name" placeholder="File name">
      </div>
      <div class="modal-footer">
        <a href="#!" class="modal-close waves-effect btn-flat">CANCEL</a>
        <button id="btn_new_file" class="modal-close waves-effect btn btn-primary blue">CREATE</button>
      </div>
      </form>
  </div>

  <div id="modal_foldername" class="modal modal-small">
    <form id="frm_new_folder">
      <div class="modal-content">
        <h4>New Folder</h4>
        <input type="text" id="txt_folder_name" placeholder="Folder name">
      </div>
      <div class="modal-footer">
        <a href="#!" class="modal-close waves-effect btn-flat">CANCEL</a>
        <button id="btn_new_folder" class="modal-close waves-effect btn btn-primary blue">CREATE</button>
      </div>
    </form>
  </div>


  <div id="modal_rename" class="modal modal-small">
    <form id="frm_rename">
      <div class="modal-content">
        <h4>Rename</h4>
        <input type="text" id="txt_new_name" required placeholder="New name">
      </div>
      <div class="modal-footer">
          <a href="#!" class="modal-close waves-effect btn-flat">CANCEL</a>
          <button id="btn_rename" class="modal-close waves-effect btn btn-primary blue">RENAME</button>
      </div>
    </form>
  </div>

  <div id="modal_replace_moving" class="modal modal-small">
    <form id="frm_replace_moving">
      <div class="modal-content">
        <h4>Replace existing</h4>
        <p>File name already exist. Do you want to replace it with the one you’re moving?</p>
      </div>
      <div class="modal-footer">
          <a href="#!" id="btn_cancel_replace_move" class="modal-close waves-effect btn-flat">NO</a>
          <button id="btn_confirm_replace_move" class="modal-close waves-effect btn btn-primary blue">YES</button>
      </div>
    </form>
  </div>
  

  <div id="modal_share" class="modal modal-small">
    <form id="frm_share">
      <div class="modal-content">
        <h4>Share with others</h4>
        <p>Enter the username of the people you want to share</p>
        <div class="row">
          <div class="col s9">
            <div class="chips chips-share-username"></div>
            <%-- <label for="last_name">Enter the username of the people you want to share</label> --%>
          </div>
          <div class="input-field col s3">
            <select id="share-permission" class="browser-default">
              <%-- <option value="" disabled selected>Choose your option</option> --%>
              <option value="1">Can Edit</option>
              <option value="2">Can view only</option>
            </select>
            <%-- <label>Materialize Select</label> --%>
          </div>  
        </div>
      </div>
      <div class="modal-footer">
          <a href="#!" id="btn_cancel_share" class="modal-close waves-effect btn-flat">Cancel</a>
          <button id="btn_share" class="modal-close waves-effect btn btn-primary blue">Done</button>
      </div>
    </form>
  </div>
          
  <div id="modal_file_view" class="modal">
    <form id="frm_file_view">
      <div class="modal-content">
        <h5 id="head_file_name">File View</h5>
        <textarea id="file_content" style="height:450px"></textarea>
      </div>
      <div class="modal-footer">
          <a href="#!" class="modal-close waves-effect btn-flat">CANCEL</a>
          <button id="btn_file_save" class="modal-close waves-effect btn btn-primary blue"><i class="material-icons left">save</i>SAVE</button>
      </div>
    </form>
  </div>

  <div id="modal_info" class="modal">
    <div class="modal-content">
      <h4 id="txt_info_file_name">File info</h4>
      <p id="txt_info"></p>
    </div>
    <div class="modal-footer">
      <a href="#!" class="modal-close waves-effect btn-flat">OK</a>
    </div>
  </div>

</body>
<script>

  // document.addEventListener('DOMContentLoaded', function() {
  //   var elems = document.querySelectorAll('.sidenav');
  //   var instances = M.Sidenav.init(elems, options);
  // });

  // Initialize collapsible (uncomment the lines below if you use the dropdown variation)
  // var collapsibleElem = document.querySelector('.collapsible');
  // var collapsibleInstance = M.Collapsible.init(collapsibleElem, options);


  $(document).ready(function(){
    $('.sidenav').sidenav();
    // $('#modal_file_view').modal({dismissible:false});
    $('.modal').modal({dismissible:false,onCloseEnd:function(e){
      // console.log(e);
       $(".modal form").trigger("reset");
      }});
    $('.tooltipped').tooltip({enterDelay:500});
    $(".dropdown-trigger").dropdown({hover:true});
    $('.fixed-action-btn').floatingActionButton({hoverEnabled:false});
    $('select').formSelect();
    $('.dropdown-trigger').dropdown();

    $('.chips-share-username').chips({
        placeholder: 'Enter username',
        secondaryPlaceholder: '+ More people...'
      // autocompleteOptions: {
      //   data: {
      //     'Apple': null,
      //     'Microsoft': null,
      //     'Google': null
      //   },
      //   limit: Infinity,
      //   minLength: 1,
      // }
    });

    var selectedFiles=[];
    var fCache=[];
    var rCache=[];
    // var currRow;
    var fileClipboard={files:[],parent:"",option:"",rows:[]};
    // var clipboardOption="";
    // [0].children[0].childNodes[0].nodeValue
    // console.log($("#untitled\\ folder")[0].cells[0].innerHTML);

    // console.log(document.getElementById("untitled folder").cells[0].innerHTML);
    function tblFun() {

      /* Get all rows from your 'table' but not the first one 
      * that includes headers. */
      var rows = $('tr').not(':first');
      //console.log(rows);
      // console.log(fileClipboard.files=="");
      if(fileClipboard.files==""){
        $(".grey-text").addClass("hide");
        // console.log(rows.removeClass('grey-text'));
      }
      if(rows[0].cells[0].getAttribute("colspan")!=4){
        rows.on('click', function(e) {

          /* Get current row */
          var row = $(this);
          //console.log(e.currentTarget);
           
          rows.removeClass('highlight');
          row.addClass('highlight');
          $(".ccd").removeClass('disabled');
          $('#second_nav').removeClass("hide");
          selectedFiles[0]=e.currentTarget.id;
          
          // console.log(selectedFiles.length);

          // if ((e.ctrlKey || e.metaKey) || e.shiftKey) {
          //     /* If pressed highlight the other row that was clicked */
          //     row.addClass('highlight');
          // } else {
          //     /* Otherwise just highlight one row and clean others */
          //     rows.removeClass('highlight');
          //     row.addClass('highlight');
          // }

        });

        rows.on('dblclick',function(e){
          // console.log(e.currentTarget.id);
          if(e.currentTarget.cells[2].innerText!='Folder'){
            $('#head_file_name').html(e.currentTarget.cells[0].innerText);
            // $('#modal_file_view').modal({
            //   dismissible: false
            // });
            $('#modal_file_view').modal('open');
            // $('#file_content').text(file_open(e.currentTarget.cells[0].innerText));
             file_open(e.currentTarget.cells[0].innerText);
          }
          else{
            //console.log(rCache[rCache.length - 1]);
            //console.log($('#file_container').html());
            //console.log($('#curdir').html());
            updateFiles(e.currentTarget.cells[0].innerText);
    // console.log($("table tbody").find('td[colspan=4]'));
          }
        });
      }
      
    }

    // $("#file_content").change(function(e){
    //   console.log($("#file_content").val());
    // });

    tblFun();
    $(document).click(function(event) { 
      $target = $(event.target);
      if(!$target.closest('table').length && 
      (selectedFiles.length==1)&&
      (!$target.closest('nav').length)&&
      (!$target.closest('.modal').length)&&
      (!$target.closest('.toast').length)&&
      (!$target.closest('.fixed-action-btn').length)&&
      (!$target.closest('.modal-overlay').length)) {
        //$('#menucontainer').hide();
        $('tr').not(':first').removeClass('highlight');
        selectedFiles=[];
        $('#second_nav').addClass("hide");
          $(".ccd").addClass('disabled');
      }      
    });

    $(document).bind('selectstart dragstart', function(e) { 
        e.preventDefault(); return false; 
    });

    $('#btn_file_navication li').click(function(e){
      // alert();
      $('#btn_file_navication li').removeClass("active");
      $(this).addClass("active");
      // if(this.id=="btn_view_stared")
       console.log(e.currentTarget.id);
    });

    $('#btn_star').click(function(e){
      $.post("AjaxFileOpr",
      {
        q: "star",
        file: $('#curdir').html()+"/"+selectedFiles[0]
      },
      function(data, status){
        //alert("Data: " + data + "\nStatus: " + status);
        data=JSON.parse(data);
        if(status=="success"){
          console.log(data);
          M.toast({html: data.text, classes: 'rounded'});
        }
      });
    });

    $("#share").click(function(e){
      // alert(e);
      $("#modal_share").modal("open");
    });

    $("#btn_share").click(function(e){
      var r={"users":M.Chips.getInstance($('.chips-share-username')).chipsData,"permission":$('#share-permission').val()};
     // alert(JSON.stringify(r));
      console.log(JSON.stringify(r));
      $.post("AjaxFileOpr",
      {
        q: "share",
        file: $('#curdir').html()+"/"+selectedFiles[0],
        users:JSON.stringify(M.Chips.getInstance($('.chips-share-username')).chipsData),
        permission:$('#share-permission').val()
      },
      function(data, status){
        //alert("Data: " + data + "\nStatus: " + status);
        data=JSON.parse(data);
        if(status=="success"){
          // console.log(data);
          M.toast({html: data.text, classes: 'rounded'});
        }
      });
     
    });

    $("#btn_cut").click(function(e){
      //alert(selectedFiles[0]+" Cut!");
      // clipboardOption="cut";
      if(fileClipboard.files.length>0)
        document.getElementById(fileClipboard.files[0]).classList.remove("grey-text");
      fileClipboard={files:selectedFiles.slice(),parent:$('#curdir').html(),option:"cut",rows:[document.getElementById(selectedFiles[0])]};
      // console.log(fileClipboard.files[0]);
      $('#btn_paste').removeClass("disabled");
      document.getElementById(fileClipboard.files[0]).classList.add("grey-text");
    });

    $("#btn_copy").click(function(e){
      //alert(selectedFiles[0]+" Cut!");
      // clipboardOption="copy";
      if(fileClipboard.files.length>0)
        document.getElementById(fileClipboard.files[0]).classList.remove("grey-text");
      fileClipboard={files:selectedFiles.slice(),parent:$('#curdir').html(),option:"copy",rows:[document.getElementById(selectedFiles[0])]};
      $('#btn_paste').removeClass("disabled");
    });
          //  $('#modal_replace_moving').modal('open');

    $("#btn_paste").click(function(e){
      if(fileClipboard.option=="cut"){
        if(fileClipboard.parent==$('#curdir').html()){
          M.toast({html: 'File(s) are moved!', classes: 'rounded'});
          document.getElementById(fileClipboard.files[0]).classList.remove("grey-text");
        }
        else{
          $.post("AjaxFileOpr",
          {
            q: "move",
            src: fileClipboard.parent+"/"+fileClipboard.files[0],
            dest:$('#curdir').html()
          },
          function(data, status){
            //alert("Data: " + data + "\nStatus: " + status);
            data=JSON.parse(data);
            if(status=="success"){
              // alert(data.newrow);
              if($("table tbody tr td")[0].getAttribute("colspan")==4)
                $("table tbody tr").remove();
              $("table tbody").prepend(fileClipboard.rows[0]);
              $('tr').not(':first').removeClass('highlight');
              $(fileClipboard.rows).removeClass("grey-text");
              $(fileClipboard.rows).addClass("highlight");
              tblFun();
              M.toast({html: data.text, classes: 'rounded'});
            }
          });
        }
        fileClipboard.files=[];
        $('#btn_paste').addClass("disabled");
      }
      else if(fileClipboard.option=="copy"){
        $.post("AjaxFileOpr",
        {
          q: "copy",
          src: fileClipboard.parent+"/"+fileClipboard.files[0],
          dest:$('#curdir').html()
        },
        function(data, status){
          //alert("Data: " + data + "\nStatus: " + status);
          data=JSON.parse(data);
          if(status=="success"){
            // alert(data.newrow);
            if($("table tbody tr td")[0].getAttribute("colspan")==4)
              $("table tbody tr").remove();
            $("table tbody").prepend(data.newrow);
            $('tr').not(':first').removeClass('highlight');
            // $(fileClipboard.rows).removeClass("grey-text");
            // $(data.newrow).addClass("highlight");
            document.getElementById(data.newname).classList.add("highlight");
            selectedFiles[0]=data.newname;
            // console.log(data.newrow);
            tblFun();
            M.toast({html: data.text, classes: 'rounded'});
          }
        });
      }
    });


    $("#btn_info").click(function(e){
      // $('#curdir').html()+"/"+selectedFiles[0];
      $.post("AjaxFileOpr",
      {
        q: "info",
        file: $('#curdir').html()+"/"+selectedFiles[0]
      },
      function(data, status){
        //alert("Data: " + data + "\nStatus: " + status);
        data=JSON.parse(data);
        if(status=="success"){
          console.log(data);
          var info='<div class="row">'+
            '<div class="col s6">Type</div><div class="col s6">'+data.type+'</div>'+
            '<div class="col s6">Last Modified</div><div class="col s6">'+data.date+'</div>'+
            '<div class="col s6">Owner</div><div class="col s6">'+data.owner+'</div>'+
            '<div class="col s6">Path</div><div class="col s6">'+data.path+'</div>'+
            ((data.type=="File")?'<div class="col s6">Size</div><div class="col s6">'+data.size+'</div>':'')+
          '</div>';
          if(data.shared){
            info+='<hr>';
            var sharing_info=JSON.parse(data.sharing_info);
            console.log(sharing_info);
            sharing_info.forEach(function(val){
              info+='<div class="col s12">Shared to <b>'+val.user+'</b> on <b>'+val.shared_on+'</b> with <b>'+val.permission+'</b> permission</div>';
              // info+='<div class="col s6">Shared to</div><div class="col s6">'+val.user+'</div>';
              // info+='<div class="col s6">Shared to</div><div class="col s6">'+val.user+'</div>';
            });
            info+='<hr>'
          }
          $('#txt_info_file_name').html(selectedFiles[0]);
          $('#txt_info').html(info);
          $('#modal_info').modal('open');
          // // alert(data.newrow);
          // if($("table tbody tr td")[0].getAttribute("colspan")==4)
          //   $("table tbody tr").remove();
          // $("table tbody").prepend(data.newrow);
          // $('tr').not(':first').removeClass('highlight');
          // // $(fileClipboard.rows).removeClass("grey-text");
          // // $(data.newrow).addClass("highlight");
          // document.getElementById(data.newname).classList.add("highlight");
          // selectedFiles[0]=data.newname;
          // // console.log(data.newrow);
          // tblFun();
          // M.toast({html: data.text, classes: 'rounded'});
        }
      });
    });

    $('#btn_remove').on('click',function(e){
      var undo=false;
      var currFile=selectedFiles[0];
      var currRow=document.getElementById(currFile);
      $('#second_nav').addClass("hide");
      // $(currRow).remove();
      $(currRow).addClass("hide");
      M.toast({html: '<span>File deleted</span><button class="btn-flat toast-action" id="undoDelete">Undo</button>', classes: 'rounded',completeCallback: function(){
        if(!undo){
          //console.log(currFile);
          $.post("AjaxFileOpr",
          {
            q: "remove_file",
            files: currFile,
            parent:$('#curdir').html()
          },
          function(data, status){
            data=JSON.parse(data);
            if((status=="success")&&(data.status=="success")){
              //console.log(data);
                selectedFiles=[];
            }else{
              // $("table tbody").prepend(currRow);
              $(currRow).removeClass("hide");
              // tblFun();
              // $(currRow).addClass("highlight");
              $('#second_nav').removeClass("hide");
              M.toast({html: 'Network error! Your file not deleted', classes: 'rounded'});
            }
          });
        }
      }});
      $('#undoDelete').click(function(e){
       // alert("undo called");
        // $("table tbody").prepend(currRow);
        $(currRow).removeClass("hide");
        // tblFun();
        // $(currRow).addClass("highlight");
        $('#second_nav').removeClass("hide");
        undo=true;
        M.Toast.dismissAll();
      });
    });



    $("#btn_back").on('click',function(e){
      if(rCache.length>0){
        var arr=rCache.pop();
        //console.log(arr);
        fCache.push({parent:$('#curdir').html(),files:$('#file_container').html()});
        $('#file_container').html(arr.files);
        $('#curdir').html(arr.parent);
        tblFun();
      }
    });

    $("#btn_forward").on('click',function(e){
      if(fCache.length>0){
        var arr=fCache.pop();
        //console.log(arr);
        rCache.push({parent:$('#curdir').html(),files:$('#file_container').html()});
        $('#file_container').html(arr.files);
        $('#curdir').html(arr.parent);
        //rCache.push(arr);
        tblFun();
      }
    });

    $("#frm_new_file").on('submit',function(e){
      // console.log($("#txt_file_name").text());
      $.post("AjaxFileOpr",
      {
        q: "new_file",
        f_name: $("#txt_file_name").val(),
        parent:$('#curdir').html()
      },
      function(data, status){
        //alert("Data: " + data + "\nStatus: " + status);
        data=JSON.parse(data);
        if(status=="success"){
          // alert(data.newrow);
          if($("table tbody tr td")[0].getAttribute("colspan")==4)
            $("table tbody tr").remove();
          $("table tbody").prepend(data.newrow);
          tblFun();
          M.toast({html: data.text, classes: 'rounded'});
        }
      });
      e.preventDefault();
    });

    $("#frm_new_folder").on('submit',function(e){
      // console.log($("#txt_folder_name").val());
      $.post("AjaxFileOpr",
      {
        q: "new_folder",
        f_name: $("#txt_folder_name").val(),
        parent:$('#curdir').html()
      },
      function(data, status){
        data=JSON.parse(data);
          if(status=="success"){
            // alert(data.newrow);
            if($("table tbody tr td")[0].getAttribute("colspan")==4)
              $("table tbody tr").remove();
            $("table tbody").prepend(data.newrow);
            tblFun();
              M.toast({html: data.text, classes: 'rounded'});
          }
      });
      e.preventDefault();
    });

    $("#frm_file_view").on('submit',function(e){
      // console.log($("#file_content").val());
      $.post("AjaxFileOpr",
      {
        q: "save_file",
        filename: $("#head_file_name").text(),
        parent: $("#curdir").html(),
        text:$("#file_content").val()
      },
      function(data, status){
        data=JSON.parse(data);
        // console.log(data);
          if(status=="success"){
            //alert(data.newrow);
            M.toast({html: data.text, classes: 'rounded'});
          }
      });
      e.preventDefault();
    });

    $("#btn_view").on('click',function(e){
      if(document.getElementById(selectedFiles[0]).cells[2].innerText!="Folder"){
        $('#head_file_name').html(selectedFiles[0]);
        $('#modal_file_view').modal('open');
        file_open(selectedFiles[0]);
      }
      else{
        updateFiles(document.getElementById(selectedFiles[0]).cells[0].innerText);
      }
      //selectedFiles[0];
      // if(e.currentTarget.cells[2].innerText!='Folder'){
      //   $('#head_file_name').html(e.currentTarget.cells[0].innerText);
      //   $('#modal_file_view').modal('open');
      // }
    });


    $("#frm_rename").on('submit',function(e){
      //console.log(data);
      $.post("AjaxFileOpr",
      {
        q: "rename",
        parent: $("#curdir").html(),
        newname: $("#txt_new_name").val(),
        oldname:  selectedFiles[0]
      },
      function(data, status){
        data=JSON.parse(data);
        //console.log(data);
        if(status=="success"){
          if(data.status=="success"){
            var ele=document.getElementById(selectedFiles[0]);
            ele.cells[0].innerHTML=$("#txt_new_name").val();
            ele.id=$("#txt_new_name").val();
          }
          M.toast({html: data.text, classes: 'rounded'});
        }
      });
      e.preventDefault();
    });

    function file_open(file){
      var txt="";
      $.post("AjaxFileOpr",
      {
        q: "fileOpen",
        filename: file,
        parent: $("#curdir").html()
      },
      function(data, status){
        data=JSON.parse(data);
        //console.log(data.text);
        txt= data.text;
        if(status=="success"){
          $('#file_content').text(txt);
         }
      });
    }

    function updateFiles(dir){
      var curdir=$("#curdir").html();
      rCache.push({parent:curdir,files:$('#file_container').html()});
      fCache=[];
      $.post("AjaxFileOpr",
      {
        q: "get_files",
        folder: dir,
        parent: curdir
      },
      function(data, status){
        if(status=="success"){
          $('#file_container').html(data);
          $('#curdir').html(curdir+"/"+dir);
          //console.log($('#curdir').html());
          tblFun();
         }
      });
    }

    
  });



        
</script>
</html>