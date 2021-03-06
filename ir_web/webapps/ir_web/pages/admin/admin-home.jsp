<jsp:directive.page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />


<!--  
   Copyright 2008 University of Rochester

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
-->

<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ur" uri="ur-tags"%>

<!--  document type -->
<c:import url="/inc/doctype-frag.jsp"/>

<html>
    <head>
        <title>Administration Home</title>
        <c:import url="/inc/meta-frag.jsp"/>
        
        <!-- Core + Skin CSS -->
        <ur:styleSheet href="page-resources/yui/reset-fonts-grids/reset-fonts-grids.css"/>
        <ur:styleSheet href="page-resources/yui/menu/assets/skins/sam/menu.css"/>
        <ur:styleSheet href="page-resources/css/base-ur.css"/>
        
        
        <ur:styleSheet href="page-resources/css/global.css"/>
        <ur:styleSheet href="page-resources/css/main_menu.css"/>

        <!--  css files -->
        <ur:js src="page-resources/yui/yahoo-dom-event/yahoo-dom-event.js"/>
        <ur:js src="page-resources/yui/container/container_core-min.js"/>
        <ur:js src="page-resources/yui/menu/menu-min.js"/>

        <ur:js src="page-resources/js/menu/main_menu.js"/>
    </head>
    
<body class="yui-skin-sam">
    <!--  yahoo doc 2 template creates a page 950 pixles wide -->
    <div id="doc2">  

        <!--  this is the header of the page -->
        <c:import url="/inc/header.jsp"/>
            
        <!--  this is the body regin of the page -->
        <div id="bd">

            <br/>
            <br/>
    
            <c:if test="${!empty repository }">
                <h3>The system has been initialized</h3>
                <br/>
            </c:if>
    
             <c:if test="${empty repository}">
                 <h3>The system has not yet been initialized"</h3> 
    
                 <!--  Form to initalize the system -->
                 <s:form method="post">
                     <s:token name="initializeSystem.token" />
                     <s:textfield label="%{getText('repositoryPath')}" name="fileLocation"/>    
                     <s:textfield label="%{getText('repositoryName')}" name="repositoryName"/>   
                     <s:submit action="initializeSystem" value="%{getText('save')}"/>
                 </s:form>
            </c:if>
        </div>
        <!--  end the body tag --> 

        <!--  this is the footer of the page -->
        <c:import url="/inc/footer.jsp"/>
        
    </div>
    <!-- end doc -->
    </body>
</html>
   