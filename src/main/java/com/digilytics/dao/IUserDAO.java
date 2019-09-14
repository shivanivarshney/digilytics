package com.digilytics.dao;
import com.digilytics.entity.User;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

public interface IUserDAO {

    void addUser(User user) throws DataIntegrityViolationException;

    List getAllRoles();
}
 