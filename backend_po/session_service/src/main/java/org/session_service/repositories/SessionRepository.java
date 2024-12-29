package org.session_service.repositories;

import org.session_service.entities.SessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<SessionEntity, Long> {

    @Query("SELECT s FROM SessionEntity s WHERE s.sessionId = :sessionId")
    SessionEntity findSessionById(@Param("sessionId") Integer sessionId);

}
