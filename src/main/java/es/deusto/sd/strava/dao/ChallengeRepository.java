package es.deusto.sd.strava.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import es.deusto.sd.strava.entity.Challenge;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {

}
