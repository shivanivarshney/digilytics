package com.digilytics.service;

import com.digilytics.entity.User;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Interface for User Service
 */
public interface IUserService {
     HashMap<String, String> addUser(InputStream csvFile) throws IOException;
}
