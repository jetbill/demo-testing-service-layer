package com.application.jet.testing_service_layer.repository;

import com.application.jet.testing_service_layer.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository <Employee, Long> {

    //find employee by email using derivative method
    Optional<Employee> findByEmail(String email);

    // find employee name and lastname using JPQL and index parameters
    @Query("select e from Employee e where e.firstName = ?1 and e.lastName = ?2")
    Employee findByNameAndLastName(String firstName, String lastName);

    // find employee name and lastname using JPQL and query params
    @Query("select e from Employee e where e.firstName =:firstName and e.lastName =:lastName")
    Employee findByJPQLNamedParams(@Param("firstName") String firstName, @Param("lastName") String lastName);

    // find using native query and  index parameters
    @Query(value = "select * from employees e where e.first_name =?1 and e.last_name =?2", nativeQuery = true)
    Employee findByNativeSQL(String firstName, String lastName);
    // find using native query with params
    @Query(value = "select * from employees e where e.first_name =:firstName and e.last_name =:lastName", nativeQuery = true)
    Employee findByNativeSQLNamed(@Param("firstName") String firstName, @Param("lastName") String lastName);
}
