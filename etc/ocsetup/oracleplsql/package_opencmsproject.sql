CREATE OR REPLACE
PACKAGE OpenCmsProject IS

  FUNCTION getAllAccessibleProjects(pUserID IN NUMBER) RETURN userTypes.anyCursor;
  PROCEDURE createProject(pUserId IN NUMBER, pProjectName IN VARCHAR2, pProjectDescription IN VARCHAR2,
                         pGroupName IN VARCHAR2, pManagerGroupName IN VARCHAR2, pTaskID IN NUMBER, 
                         pProject OUT userTypes.anyCursor);
  PROCEDURE copyResourceToProject(pUserID IN NUMBER, pProjectId IN NUMBER, pResource IN VARCHAR2);
  PROCEDURE publishProject(pUserId NUMBER, pProjectId NUMBER, pOnlineProjectId NUMBER);
  FUNCTION onlineProject RETURN userTypes.anyCursor;
END ;
/
