<details>
<summary>Tests passed (<span style="color: green;">25</span>)</summary>


  <details style="margin-left: 20px; font-size: 14px"> 
  <summary>com.teamsantos.easybarber.tests.UserTests (time: 2.055, nTests: 3, nErrors: 0, nFailures: 0)</summary>


  - ✅ **testList** (time: 0.373)
  - ✅ **addUserLocation** (time: 1.344)
  - ✅ **test** (time: 0.324)
</details>
  <details style="margin-left: 20px; font-size: 14px"> 
  <summary>com.teamsantos.easybarber.tests.ScheduleTests (time: 10.4, nTests: 5, nErrors: 0, nFailures: 0)</summary>


  - ✅ **createSchedules** (time: 4.472)
  - ✅ **listSchedules** (time: 0.986)
  - ✅ **createExceptions** (time: 1.414)
  - ✅ **listExceptions** (time: 0.865)
  - ✅ **disable** (time: 2.63)
</details>
  <details style="margin-left: 20px; font-size: 14px"> 
  <summary>com.teamsantos.easybarber.tests.EstablishmentTests (time: 9.131, nTests: 9, nErrors: 0, nFailures: 0)</summary>


  - ✅ **deleteImages** (time: 7.276)
  - ✅ **testEmployees** (time: 0.004)
  - ✅ **listClosestEstablishments** (time: 0.678)
  - ✅ **addImages** (time: 0.003)
  - ✅ **createEstablishments** (time: 0.001)
  - ✅ **listEstablishmentsByServiceType** (time: 0.316)
  - ✅ **listEstablishmentServices** (time: 0.358)
  - ✅ **testService** (time: 0.001)
  - ✅ **listEmployees** (time: 0.475)
</details>
  <details style="margin-left: 20px; font-size: 14px"> 
  <summary>com.teamsantos.easybarber.tests.AuthTests (time: 0.006, nTests: 1, nErrors: 0, nFailures: 0)</summary>


  - ✅ **test** (time: 0.001)
</details>
  <details style="margin-left: 20px; font-size: 14px"> 
  <summary>com.teamsantos.easybarber.tests.ServiceTypeTests (time: 0.326, nTests: 2, nErrors: 0, nFailures: 0)</summary>


  - ✅ **createServiceTypes** (time: 0.004)
  - ✅ **listServicesByType** (time: 0.309)
</details>
  <details style="margin-left: 20px; font-size: 14px"> 
  <summary>com.teamsantos.easybarber.tests.AppointmentTests (time: 31.432, nTests: 4, nErrors: 0, nFailures: 0)</summary>


  - ✅ **createAppointment** (time: 24.685)
  - ✅ **confirmAppointment** (time: 2.822)
  - ✅ **listAppointments** (time: 3.238)
  - ✅ **cancelAppointment** (time: 0.676)
</details>
  <details style="margin-left: 20px; font-size: 14px"> 
  <summary>com.teamsantos.easybarber.tests.HeavyDBTests (time: 0.02, nTests: 1, nErrors: 0, nFailures: 0)</summary>


  - ✅ **create** (time: 0.002)
</details>
</details>


<details>
<summary>Tests Failed (<span style="color: red;">1</span>)</summary>


  <details style="margin-left: 20px; font-size: 14px"> 
  <summary>com.teamsantos.easybarber.tests.EmployeeTests (time: 49.613, nErrors: 0, nFailures: 1)</summary>


  - ❌ listEstablishments (time: 5.963)
  ````java
  java.lang.AssertionError
	at com.teamsantos.easybarber.tests.EmployeeTests.listEstablishments(EmployeeTests.java:210)
	at java.base/java.lang.reflect.Method.invoke(Method.java:580)
	at java.base/java.util.ArrayList.forEach(ArrayList.java:1597)
	at java.base/java.util.ArrayList.forEach(ArrayList.java:1597)
  ````
</details>
</details>
