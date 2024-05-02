package com.poluectov.rvproject.repository;

import com.poluectov.rvproject.dto.user.UserRequestTo;
import com.poluectov.rvproject.model.User;
import org.springframework.stereotype.Component;

@Component
public interface UserRepository extends ICommonRepository<User, Long> {
}
