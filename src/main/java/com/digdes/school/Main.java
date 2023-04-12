package com.digdes.school;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/*
insErt vAluEs 'lastName' = 'Fedorov', 'age' = 40, 'id' = 1, 'active' = true
insert values 'lastName' = 'Nechaev', 'age' = 35, 'id' = 2, 'active' = false
insert values 'lastName' = 'Petrov', 'age' = 27, 'id' = 3, 'active' = true
insert values 'lastName' = 'Корнеев', 'age' = 41, 'id' = 4, 'active' = true
insert values 'lastName' = 'Исаев', 'age' = 17, 'id' = 5, 'active' = false, 'cost' = 10.3
UPDATE VALUES 'active'=true, 'cost'=10.1 where 'id' >1.0 ANd 'active' = false
insert ValUes 'lastName' = 'Петров', 'id' = 1, 'age'=30, 'cost'=5.4, 'active'=true
insert ValUes 'lastName' = 'Иванов', 'id' = 2, 'age'=25, 'cost'=4.3, 'active'=false
InSerT values 'lastName'='Федоров', 'age'=40, 'active'=true, 'id' = 3
update VALUES 'active'=false, 'cost'=10.1 where 'id' = 3
update VALUES 'active'=false, 'cost'=10.1 where 'is' = 3
select where 'age' >= true and 'lastName' ilike '%п%'
UpDaTE vALUES 'active'=null where 'active'=false
delete where 'id'=3
 */

public class Main {
    public static void main(String[] args) {
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8))) {
            JavaSchoolStarter starter = new JavaSchoolStarter();
            String request = reader.readLine();
            while (!request.equals("exit")) {
                starter.execute(request);
                request = reader.readLine();
            }
        } catch (IOException e) {
            e.getStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}