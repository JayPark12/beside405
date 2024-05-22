package com.beside.DAO;
import com.beside.Entity.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface UserDao extends Repository<UserEntity, String> {

    @Query(value = """
    SELECT * FROM USER_INFO WHERE id = :id AND password = :password"""
    , nativeQuery = true)
    UserEntity findUser(@Param("id") String id, @Param("password") String password);

}
