spring-stateful-rest-security
=============================

How to secure stateful rest-like backend with spring security. (WTF? Stateful REST? Just because.)
see sk.bsmk.controllers.SecurityIntegrationTest

POST /rest/login
----------------
Only resource that is not protected with csrf token. After successful login csrf-token is returned in headers.

GET /rest/secured
-----------------
Resource accessible only with correct csrf token. 
After each request new csrf-token is generated and returned in headers. (see sk.bsmk.security.CsrfTokenGeneratorFilter)

POST /rest/logout
-----------------
Session is terminated.