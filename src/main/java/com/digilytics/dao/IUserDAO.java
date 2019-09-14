package com.digilytics.dao;
import com.digilytics.entity.User;

import java.util.List;

public interface IUserDAO {

    void addUser(User user);

    List getAllRoles();
}
 