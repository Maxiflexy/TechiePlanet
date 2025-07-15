CORRECT QUERIES: 1, 2, 3

Query 1: SELECT DISTINCT(salary) FROM emp ORDER BY salary DESC LIMIT 1 OFFSET 1;
--Uses DISTINCT to get unique salaries, then skips the highest with OFFSET


Query 2: SELECT MAX(salary) FROM emp WHERE salary < (SELECT MAX(salary) FROM emp);
--Finds the maximum salary that's less than the overall maximum salary


Query 3: SELECT salary FROM (SELECT DISTINCT salary FROM emp ORDER BY salary DESC LIMIT 2) AS emp ORDER BY salary LIMIT 1;
--Gets top 2 distinct salaries, then orders ascending and takes the first




INCORRECT QUERIES: 4, 5
--Query 4: Applies LIMIT before DISTINCT, so it doesn't get true distinct values
--Query 5: Returns the second record, not the second distinct salary value