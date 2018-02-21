<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<html>
<c:set var="title" value="Login" />
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<!--
<body>

<%--=========================================================================== 
Here we use a table layout.
Class page corresponds to the '.page' element in included CSS document.
===========================================================================--%> 

	<table id="main-container">

<%--=========================================================================== 
This is the HEADER, containing a top menu.
header.jspf contains all necessary functionality for it.
Just included it in this JSP document.
===========================================================================--%> 

		<%-- HEADER --%>
		<%@ include file="/WEB-INF/jspf/header.jspf"%>
		<%-- HEADER --%>

<%--=========================================================================== 
This is the CONTENT, containing the main part of the page.
===========================================================================--%>  
		    <tr>
			<td class="content center">
			<%-- CONTENT --%>
			
<%--=========================================================================== 
Defines the web form.
===========================================================================--%> 
				<form id="login_form" action="controller" method="post">

<%--=========================================================================== 
Hidden field. In the query it will act as command=login.
The purpose of this to define the command name, which have to be executed 
after you submit current form. 
===========================================================================--%> 
					<input type="hidden" name="command" value="enterCommand"/>

					<fieldset >
						<legend>Login</legend>
						<input name="login"/><br/>
					</fieldset><br/>
					<fieldset>
						<legend>Password</legend>
						<input type="password" name="password"/>
					</fieldset><br/>
					
					<input type="submit" value="Login">	
					Localization:
					<select name="locale">
                       <option value="EN" ${lang != 'ru_RU' ? 'selected' : ''}>EN</option>
                       <option value="RU" ${lang == 'ru_RU' ? 'selected' : ''}>RU</option>
                    </select>							
				</form>
				
			<%-- CONTENT --%>

			</td>
		</tr>

		
		
	</table>
</body>
-->
<body>
 <div class="container-fluid">
                <div class="row-fluid" >
                   
                      
                     <div class="col-md-offset-4 col-md-4" id="box">
                      <h2>Login</h2>
                       
                            <hr>
                           

                                <form class="form-horizontal" id="login_form" action="controller" method="post">
                                  <input type="hidden" name="command" value="enterCommand"/>
                                    <fieldset>
                                        <!-- Form Name -->


                                        <!-- Text input-->

                                        <div class="form-group">

                                            <div class="col-md-12">
                                                <div class="input-group">
                                                    <span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
                                                    <input name="login" placeholder="Login" class="form-control" type="text">
                                                </div>
                                            </div>
                                        </div>


                                  
                                        <!-- Text input-->
                                        <div class="form-group">

                                            <div class="col-md-12">
                                                <div class="input-group">
                                                    <span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
                                                    <input name="password" placeholder="Password" class="form-control" type="password">
                                                </div>
                                            </div>
                                        </div>


                                    
                                   
                                        <div class="form-group">

                                            <div class="col-md-12">
                                                <input type="submit" class="btn btn-md btn-danger pull-right" value="Login">
                                            </div>
                                        </div>
                                       Localization:
					<select name="locale">
                       <option value="EN" ${lang != 'ru_RU' ? 'selected' : ''}>EN</option>
                       <option value="RU" ${lang == 'ru_RU' ? 'selected' : ''}>RU</option>
                    </select>
                                    </fieldset>
                                </form>
                    </div> 
</div>
</div>
</body>
</html>