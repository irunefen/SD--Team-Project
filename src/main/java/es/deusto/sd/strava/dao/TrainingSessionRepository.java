package es.deusto.sd.strava.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.deusto.sd.strava.entity.TrainingSession;

public interface TrainingSessionRepository extends JpaRepository<TrainingSession, Long> {
	List<TrainingSession> findByUserId(Long userId);
}
