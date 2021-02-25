package es.com.repository;

import es.com.model.UserPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPostRepository extends JpaRepository<UserPost, Long> {
}
