package com.example.agencedevoyage.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.agencedevoyage.Entity.User;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    void insert(User user);

    @Query("SELECT * FROM user_table")
    List<User> getAllUsers();

    @Query("SELECT * FROM user_table WHERE username = :username AND password = :password LIMIT 1")
    User getUserByUsernameAndPassword(String username, String password);

    @Delete
    void delete(User user);
    @Query("SELECT * FROM user_table WHERE email = :email")
    User getUserByEmail(String email);

    @Query("UPDATE user_table SET confirmation_code = :code WHERE email = :email")
    void updateConfirmationCode(String email, String code);

    @Query("UPDATE user_table SET password = :password WHERE email = :email")
    void updatePassword(String email, String password);
}
